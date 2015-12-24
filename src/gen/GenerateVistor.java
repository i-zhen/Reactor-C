package gen;

import ast.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateVistor extends BaseCodeGenVisitor<Void> {
    private final static int FRAMES = ClassWriter.COMPUTE_FRAMES; //or 0
    private static ClassWriter cw = new ClassWriter(FRAMES);      //Class Begin
    private static MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                                                     "main", "([Ljava/lang/String;)V", null, null);

    public Void init(){
        cw.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC, "Main", null, "java/lang/Object", null);
        return null;
    }

    public void writeClass(ClassWriter cw){
        //Output the final bytecode to the file
        byte [] b = cw.toByteArray();
        try (FileOutputStream outputFile = new FileOutputStream("out/Main.class")) {
            outputFile.write(b);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public Void visitBlock(Block b) {

        return null;
    }

    @Override
    public Void visitProcedure(Procedure p) {

        return null;
    }

    @Override
    public Void visitProgram(Program p) {
        init();
        //insert static field

        mv.visitCode();

        //insert kernel here

        //every method must contain at least one instruction in Java ByteCode. This is the attribute
        mv.visitInsn(Opcodes.RETURN); //IMPORTANT, DO NOT FORGET
        mv.visitMaxs(1, 1);           //Compute frame and local variable here
        mv.visitEnd();

        cw.visitEnd();
        writeClass(cw);
        return null;
    }

    @Override
    public Void visitVarDecl(VarDecl vd) {

        return null;
    }

    @Override
    public Void visitVar(Var v) {

        return null;
    }

    @Override
    public Void visitBinOp(BinOp b){

        return null;
    }

    @Override
    public Void visitFunCallExpr(FunCallExpr f){

        return null;
    }

    @Override
    public Void visitReturn(Return r){

        return null;
    }

    @Override
    public Void visitFunCallStmt(FunCallStmt f){

        return null;
    }

    @Override
    public Void visitWhile(While w){

        return null;
    }

    @Override
    public Void visitIf(If i){

        return null;
    }

    @Override
    public Void visitAssign(Assign a){

        return null;
    }

    @Override
    public Void visitIntLiteral(IntLiteral i){ return null; }

    @Override
    public Void visitStrLiteral(StrLiteral s){ return null; }

    @Override
    public Void visitChrLiteral(ChrLiteral c){ return null; }

}
