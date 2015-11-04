package ast;

import java.util.List;

public class Program implements Tree {

    public final List<VarDecl> varDecls;
    public final List<Procedure> procs;
    public final Procedure main;

    public Program(List<VarDecl> varDecls, List<Procedure> procs, Procedure main) {
	    this.varDecls = varDecls;
	    this.procs = procs;
	    this.main = main;
    }

    public <T> T accept(ASTVisitor<T> v) {
	return v.visitProgram(this);
    }
}
