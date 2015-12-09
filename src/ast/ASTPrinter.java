package ast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.SyncFailedException;

public class ASTPrinter implements ASTVisitor<Void> {

    private PrintWriter writer;

    public ASTPrinter(PrintWriter writer) {
            this.writer = writer;
    }

    public Void visitBlock(Block b) {
        writer.print("Block(");
        for(VarDecl vd : b.params){
            vd.accept(this);
            writer.print(",");
        }
        for(int i = 0; i < b.stmts.size() - 1; i++) {
            b.stmts.get(i).accept(this);
            writer.print(",");
        }
        if(b.stmts.size() > 0)
            b.stmts.get(b.stmts.size() - 1).accept(this);
        writer.print(")");
        return null;
    }

    public Void visitProcedure(Procedure p) {
        writer.print("Procedure(");
        writer.print(p.type);
        writer.print(","+p.name+",");
        for (VarDecl vd : p.params) {            
            vd.accept(this);
            writer.print(",");
        }
        p.block.accept(this);
        writer.print(")");
        return null;
    }

    public Void visitProgram(Program p) {
        writer.print("Program(");
        for (VarDecl vd : p.varDecls) {
            vd.accept(this);
            writer.print(",");
        }
        for (Procedure proc : p.procs) {
            proc.accept(this);
            writer.print(",");
        }
        p.main.accept(this);
        writer.print(")");
	    writer.flush();
        return null;
    }

    public Void visitVarDecl(VarDecl vd){
        writer.print("VarDecl(");
        writer.print(vd.type+",");
        vd.var.accept(this);
        writer.print(")");
        return null;
    }

    public Void visitVar(Var v) {
        writer.print("Var(");
        writer.print(v.name);
        writer.print(")");
        return null;
    }

    public Void visitIntLiteral(IntLiteral i){
        writer.print("IntLiteral(");
        writer.print(i.i);
        writer.print(")");
        return null;
    }

    public Void visitStrLiteral(StrLiteral s){
        writer.print("StrLiteral(");
        writer.print(s.str);
        writer.print(")");
        return null;
    }

    public Void visitChrLiteral(ChrLiteral c){
        writer.print("ChrLiteral(");
        writer.print(c.c);
        writer.print(")");
        return null;
    }

    public Void visitBinOp(BinOp b){
        writer.print("BinOp(");
        b.lhs.accept(this);
        writer.print("," + b.op + ",");
        b.rhs.accept(this);
        writer.print(")");
        return null;
    }

    public Void visitFunCallExpr(FunCallExpr f){
        writer.print("FunCallExpr(");
        writer.print(f.name);
        if (!f.args.isEmpty()) writer.print(",");
        for(int i = 0; i < f.args.size() - 1; i++) {
            f.args.get(i).accept(this);
            writer.print(",");
        }
        if(f.args.size() > 0)
            f.args.get(f.args.size() - 1).accept(this);
        writer.print(")");
        return null;
    }

    public Void visitReturn(Return r){
        writer.print("Return(");
        if(r.exp != null) writer.print(r.exp);
        writer.print(")");
        return null;
    }

    public Void visitFunCallStmt(FunCallStmt f){
        writer.print("FunCallStmt(");
        writer.print(f.name);
        if (!f.args.isEmpty()) writer.print(",");
        for(int i = 0; i < f.args.size() - 1; i++) {
            f.args.get(i).accept(this);
            writer.print(",");
        }
        if(f.args.size() > 0)
            f.args.get(f.args.size() - 1).accept(this);
        writer.print(")");
        return null;
    }

    public Void visitWhile(While w){
        writer.print("While(");
        w.exp.accept(this);
        writer.print(",");
        w.stmt.accept(this);
        writer.print(")");
        return null;
    }

    public Void visitIf(If i){
        writer.print("If(");
        i.exp.accept(this);
        writer.print(",");
        i.ifstmt.accept(this);
        if(i.elsestmt != null){
            writer.print(",");
            i.elsestmt.accept(this);
        }
        writer.print(")");
        return null;
    }

    public Void visitAssign(Assign a){
        writer.print("Assign(");
        a.var.accept(this);
        writer.print(",");
        a.exp.accept(this);
        writer.print(")");
        return null;
    }
}
