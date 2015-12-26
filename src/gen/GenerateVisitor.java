package gen;

import ast.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * https://en.wikipedia.org/wiki/Java_bytecode_instruction_listings
 * http://www.egtry.com/java/bytecode/asm/field
 * http://asm.ow2.org/asm40/javadoc/user/index.html
 */

public class GenerateVisitor extends BaseCodeGenVisitor<Void> {
    private ClassWriter   cw;
    private MethodVisitor mv;

    private boolean       GlobalFlag;
    private boolean       Init;
    private int           localVariablCount;
    private Label         label;

    public GenerateVisitor() {
        this.cw                = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        this.GlobalFlag        = false;
        this.Init              = false;
        this.localVariablCount = 0;

    }

    private void newScope(){
        localVariablCount = 0;
    }

    private int getCount(){
        return localVariablCount++;
    }

    public String getType(Type t){
        switch (t) {
            case INT :   return "I";
            case CHAR:   return "C";
            case VOID:   return "V";
            case STRING: return "Ljava/lang/String;";
            default:
                error("Impossible type");
                return null;
        }
    }

    public void writeClass(ClassWriter cw){
        //Output the final bytecode to the file
        byte [] b = cw.toByteArray();
        try (FileOutputStream outputFile = new FileOutputStream("out/Main.class")) {
            outputFile.write(b);
        } catch (IOException ioe) {
            error("Cannot write to file");
            ioe.printStackTrace();
        }
    }

    @Override
    public Void visitBlock(Block b) {
        Init = true;
        for(VarDecl vd : b.params)
            vd.accept(this);
        Init = false;
        for(Stmt st : b.stmts)
            st.accept(this);
        return null;
    }

    @Override
    public Void visitProcedure(Procedure p) {
        newScope();
        String type = "(";
        for(VarDecl vd : p.params) {
            type += getType(vd.type);
            vd.accept(this);
        }
        type = type + ")" + getType(p.type);
        p.setCallType(type);

        mv = cw.visitMethod(Opcodes.ACC_STATIC, p.name, type, null, null);
        mv.visitCode();

        p.block.accept(this);

        if(getType(p.type).equals("V"))
            mv.visitInsn(Opcodes.RETURN);
        else{
            mv.visitIntInsn(Opcodes.BIPUSH, 0);
            mv.visitInsn(Opcodes.IRETURN);
        }
        mv.visitMaxs(100, 100);
        mv.visitEnd();
        return null;
    }

    @Override
    public Void visitProgram(Program p) {
        cw.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC, "Main", null, "java/lang/Object", null);
        //generate global variable declaration
        GlobalFlag = true;
        for(VarDecl vd : p.varDecls)
            vd.accept(this);
        GlobalFlag = false;
        //generate procedure declarations but leave the body empty for now
        for(Procedure pc : p.procs)
            pc.accept(this);
        //generate body of main()
        mv = cw.visitMethod(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,  //access
                "main",                                   //name
                "([Ljava/lang/String;)V",                 //desc
                null,                                     //signature
                null                                      //exceptions
        );
        mv.visitCode();
        newScope();
        p.main.block.accept(this);
        //every method must contain at least one instruction in Java ByteCode. This is the attribute
        mv.visitInsn(Opcodes.RETURN); //IMPORTANT, DO NOT FORGET
        mv.visitMaxs(100, 100);           //Compute frame and local variable here
        mv.visitEnd();
        cw.visitEnd();
        writeClass(cw);
        return null;
    }

    public Void visitVarDeclGlobal(VarDecl vd){
        cw.visitField(
                Opcodes.ACC_STATIC,  //access
                vd.var.name,         //name
                getType(vd.type),    //desc
                null,                //signature
                0                    //value
        ).visitEnd();
        vd.setGlobal(true);
        return null;
    }

    public Void visitVarDeclLocal(VarDecl vd){
        //New Frame(scope) has been created, involves new local variable index
        vd.setIndex(getCount());             //set new local variable index
        if (Init) {
            mv.visitIntInsn(Opcodes.BIPUSH, 0);  //initialize
            mv.visitVarInsn(Opcodes.ISTORE, vd.getIndex());
        }
        return null;
    }

    @Override
    public Void visitVarDecl(VarDecl vd) {
        if(GlobalFlag)
            visitVarDeclGlobal(vd);
        else
            visitVarDeclLocal(vd);
        return null;
    }

    @Override
    public Void visitVar(Var v) {
        //find the value from local/global variable
        switch (v.type) {
            case INT:
                if(!v.vd.getGlobal())
                    /*
                    mv.visitIntInsn(Opcodes.ILOAD, v.vd.getIndex());
                    mv.visitIntInsn(Opcodes.BIPUSH, 16);
                    mv.visitInsn(Opcodes.ISHL);
                    mv.visitIntInsn(Opcodes.ILOAD, v.vd.getIndex() + 1);
                    mv.visitInsn(Opcodes.IOR);
                    */
                    mv.visitVarInsn(Opcodes.ILOAD, v.vd.getIndex());
                else
                    mv.visitFieldInsn(Opcodes.GETSTATIC,
                            "Main",  // classname
                            v.name,  //static field name
                            "I"      // the type of field.
                    );
                break;
            case CHAR:
                if(!v.vd.getGlobal())
                    mv.visitVarInsn(Opcodes.ILOAD, v.vd.getIndex());
                else
                    mv.visitFieldInsn(
                            Opcodes.GETSTATIC,
                            "Main",
                            v.name,
                            "C"
                    );
                break;
            default:
                error("Impossible variable type");
        }
        return null;
    }

    @Override
    public Void visitFunCallExpr(FunCallExpr f){
        for(Expr e : f.args)
            e.accept(this);
        switch (f.name){
            case "read_i" :
                mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "IO",
                        f.name,
                        "()I"
                );
                break;
            case "read_c" :
                mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "IO",
                        f.name,
                        "()C"
                );
                break;
            default:
                mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "Main",
                        f.name,
                        f.p.getCallType()
                );
        }
        return null;
    }

    @Override
    public Void visitFunCallStmt(FunCallStmt f){
        for(Expr e : f.args)
            e.accept(this);
        switch (f.name){
            case "print_i" :
                mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "IO",
                        f.name,
                        "(I)V"
                );
                break;
            case "print_c" :
                mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "IO",
                        f.name,
                        "(C)V"
                );
                break;
            case "print_s" :
                mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "IO",
                        f.name,
                        "(Ljava/lang/String;)V"
                );
                break;
            case "read_i" :
                mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "IO",
                        f.name,
                        "()I"
                );
                mv.visitInsn(Opcodes.POP);
                break;
            case "read_c" :
                mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "IO",
                        f.name,
                        "()C"
                );
                mv.visitInsn(Opcodes.POP);
                break;
            default:
                mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "Main",
                        f.name,
                        f.p.getCallType()
                );
                if (!f.p.getCallType().contains("V")) // if the return value is not void
                    mv.visitInsn(Opcodes.POP);        // just pop it
        }
        return null;
    }

    @Override
    public Void visitBinOp(BinOp b){
        b.lhs.accept(this);
        b.rhs.accept(this);
        switch (b.op){
            case ADD:
                mv.visitInsn(Opcodes.IADD); break;
            case SUB:
                mv.visitInsn(Opcodes.ISUB); break;
            case MUL:
                mv.visitInsn(Opcodes.IMUL); break;
            case DIV:
                mv.visitInsn(Opcodes.IDIV); break;
            case MOD:
                mv.visitInsn(Opcodes.IREM); break;
            case EQ :
                mv.visitJumpInsn(Opcodes.IF_ICMPNE, label); break;
            case LE :
                mv.visitJumpInsn(Opcodes.IF_ICMPGT, label); break;
            case LT :
                mv.visitJumpInsn(Opcodes.IF_ICMPGE, label); break;
            case GT :
                mv.visitJumpInsn(Opcodes.IF_ICMPLE, label); break;
            case GE :
                mv.visitJumpInsn(Opcodes.IF_ICMPLT, label); break;
            case NE :
                mv.visitJumpInsn(Opcodes.IF_ICMPEQ, label); break;
        }
        switch (b.op) {  //should only execute once
            case EQ: case LE: case LT: case GT: case GE: case NE:
                numericalRepresentation();
        }
        return null;
    }

    private void numericalRepresentation(){
        Label jump = new Label();
        mv.visitIntInsn(Opcodes.BIPUSH, 1);
        mv.visitJumpInsn(Opcodes.GOTO, jump);
        mv.visitLabel(label);
        mv.visitIntInsn(Opcodes.BIPUSH, 0);
        mv.visitLabel(jump);
    }

    @Override
    public Void visitWhile(While w){
        Label loop = new Label();
        Label end = new Label();
        label = new Label();

        mv.visitLabel(loop);
        w.exp.accept(this);
        mv.visitJumpInsn(Opcodes.IFEQ, end);
        w.stmt.accept(this);
        mv.visitJumpInsn(Opcodes.GOTO, loop);

        mv.visitLabel(end);
        return null;
    }

    @Override
    public Void visitIf(If i){
        Label jump = new Label();
        label = new Label();

        i.exp.accept(this);
        mv.visitJumpInsn(Opcodes.IFEQ, jump);

        i.ifstmt.accept(this);
        if(i.elsestmt != null) {
            Label end = new Label();
            mv.visitJumpInsn(Opcodes.GOTO, end);
            mv.visitLabel(jump);
            i.elsestmt.accept(this);
            mv.visitLabel(end);
        } else {
            mv.visitLabel(jump);
        }
        return null;
    }

    @Override
    public Void visitAssign(Assign a){
        a.exp.accept(this);
        switch (a.var.type){
            case INT:
                if(!a.var.vd.getGlobal())
                    mv.visitVarInsn(Opcodes.ISTORE, a.var.vd.getIndex());
                else
                    mv.visitFieldInsn(
                            Opcodes.PUTSTATIC,
                            "Main",
                            a.var.name,
                            "I"
                    );
                break;
            case CHAR:
                if(!a.var.vd.getGlobal())
                    mv.visitVarInsn(Opcodes.ISTORE, a.var.vd.getIndex());
                else
                    mv.visitFieldInsn(
                            Opcodes.PUTSTATIC,
                            "Main",
                            a.var.name,
                            "C"
                    );
                break;
        }
        return null;
    }

    @Override
    public Void visitReturn(Return r){
        if(r.exp != null) {
            r.exp.accept(this);
            mv.visitInsn(Opcodes.IRETURN);
        } else {
            mv.visitInsn(Opcodes.RETURN);
        }
        return null;
    }

    @Override
    public Void visitIntLiteral(IntLiteral i){
        //mv.visitIntInsn(Opcodes.SIPUSH, i.i & 0xFFFF);
        //mv.visitIntInsn(Opcodes.SIPUSH, i.i >> 16);
        if(i.i > 32767 || i.i < -32768) {
            mv.visitLdcInsn(i.i);
        } else {
            mv.visitIntInsn(Opcodes.SIPUSH, i.i);
        }
        return null;
    }

    @Override
    public Void visitStrLiteral(StrLiteral s){
        mv.visitLdcInsn(s.str);
        return null;
    }

    @Override
    public Void visitChrLiteral(ChrLiteral c){
        mv.visitIntInsn(Opcodes.SIPUSH, c.c);
        return null;
    }
}