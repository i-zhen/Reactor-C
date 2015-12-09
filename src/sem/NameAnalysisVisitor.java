package sem;

import ast.*;

public class NameAnalysisVisitor extends BaseSemanticVisitor<Void> {
	Scope scope;
    NameAnalysisVisitor(Scope scope){ this.scope = scope; }

	@Override
	public Void visitBlock(Block b) {
        Scope oldScope = scope;
        scope = new Scope(scope);
        for(VarDecl vd : b.params)
            vd.accept(this);
        for(Stmt st : b.stmts)
            st.accept(this);
        scope = oldScope;
		return null;
	}

	@Override
	public Void visitProcedure(Procedure p) {
        Symbol s = scope.lookupCurrent(p.name);
        if(s != null)
            error("The function has been declared");
        else
            scope.put(new ProcSymbol(p));
        for(VarDecl vd : p.params)
            scope.put(new VarSymbol(vd));
        p.block.accept(this);
		return null;
	}

	@Override
	public Void visitProgram(Program p) {
        for(VarDecl vd : p.varDecls)
            vd.accept(this);
        for(Procedure ps : p.procs)
            ps.accept(this);
        p.main.accept(this);
		return null;
	}

	@Override
	public Void visitVarDecl(VarDecl vd) {
        Symbol s = scope.lookupCurrent(vd.var.name);
        if(s != null)
            error("The variable has been declared");
        else
            scope.put(new VarSymbol(vd));
		return null;
	}

	@Override
	public Void visitVar(Var v) {
		Symbol vs = scope.lookup(v.name);
        if(vs == null)
            error("Using variable without declaration");
        else if (!vs.isVar())
            error("Not a legal variable name");
        else
            v.vd = ((VarSymbol) vs).vd;     //link the variable and the declaration
		return null;
	}

	@Override
	public Void visitBinOp(BinOp b){
        b.lhs.accept(this);
        b.rhs.accept(this);
		return null;
	}

	@Override
	public Void visitFunCallExpr(FunCallExpr f){
        Symbol vs = scope.lookup(f.name);
        if(vs == null)
            error("Using function without declaration");
        else if (!vs.isProc())
            error("Not a legal function name");
        else
            f.p = ((ProcSymbol) vs).p;     //link the function and the declaration
		return null;
	}

	@Override
	public Void visitReturn(Return r){
        if (r.exp != null)
            return r.exp.accept(this);
		return null;
	}

	@Override
	public Void visitFunCallStmt(FunCallStmt f){
        Symbol vs = scope.lookup(f.name);
        if(vs == null)
            error("Using function without declaration");
        else if (!vs.isProc())
            error("Not a legal function name");
        else
            f.p = ((ProcSymbol) vs).p;     //link the function and the declaration
        return null;
	}

	@Override
	public Void visitWhile(While w){
        w.exp.accept(this);
        w.stmt.accept(this);
		return null;
	}

	@Override
	public Void visitIf(If i){
        i.exp.accept(this);
        i.ifstmt.accept(this);
        if(i.elsestmt != null)
            i.elsestmt.accept(this);
		return null;
	}

	@Override
	public Void visitAssign(Assign a){
		a.var.accept(this);
        a.exp.accept(this);
        return null;
	}

	@Override
	public Void visitPrinti(Printi p){
		p.exp.accept(this);
        return null;
	}

	@Override
	public Void visitPrintc(Printc p){
        p.exp.accept(this);
        return null;
	}

	@Override
	public Void visitReadcExpr(ReadcExpr r){ return null; }

	@Override
	public Void visitReadiExpr(ReadiExpr r) { return null; }

    @Override
    public Void visitIntLiteral(IntLiteral i){ return null; }

    @Override
    public Void visitStrLiteral(StrLiteral s){ return null; }

    @Override
    public Void visitChrLiteral(ChrLiteral c){ return null; }

    @Override
    public Void visitReadc(Readc r){ return null; }

    @Override
    public Void visitReadi(Readi r){ return null; }

    @Override
    public Void visitPrints(Prints p){ return null; }
}
