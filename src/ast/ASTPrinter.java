package ast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ASTPrinter implements ASTVisitor<Void> {

    private PrintWriter writer;

    public ASTPrinter(File outputFile) {
        try {
            this.writer = new PrintWriter(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Void visitBlock(Block b) {
        writer.print("Block(");
        // to complete
        writer.print("(");
        return null;
    }

    public Void visitProcedure(Procedure p) {
        writer.print("Procedure(");
        writer.print(p.type);
        writer.print(","+p.name);
        for (VarDecl vd : p.params) {
            writer.print(",");
            vd.accept(this);
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
        return null;
    }

    public Void visitVarDecl(VarDecl vd){
        writer.print("VarDecl(");
        vd.var.accept(this);
        writer.print(","+vd.type);
        writer.print(")");
        return null;
    }

    public Void visitVar(Var v) {
        writer.print("Var(");
        writer.print(v.name);
        writer.print(")");
        return null;
    }
    

    // to complete
    
}
