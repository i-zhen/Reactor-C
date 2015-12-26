package sem;

import ast.*;

import java.util.ArrayList;
import java.util.List;


public class NameAnalysisVisitor extends BaseSemanticVisitor<Void> {
	Scope scope;
    NameAnalysisVisitor(Scope scope){ this.scope = scope; }

    private void init(){
        //init internal functions such as read and print
        List<VarDecl> vdReadChr = new ArrayList<>();
        List<VarDecl> vdReadInt = new ArrayList<>();
        List<VarDecl> vdPrintChr = new ArrayList<>();
        vdPrintChr.add(new VarDecl(Type.CHAR, new Var("paraPrintChr")));
        List<VarDecl> vdPrintInt = new ArrayList<>();
        vdPrintInt.add(new VarDecl(Type.INT, new Var("paraPrintInt")));
        List<VarDecl> vdPrintStr = new ArrayList<>();
        vdPrintStr.add(new VarDecl(Type.STRING,new Var("paraPrintInt")));
        scope.put(new ProcSymbol(new Procedure(Type.INT, "read_i", vdReadInt, null)));
        scope.put(new ProcSymbol(new Procedure(Type.CHAR, "read_c", vdReadChr, null)));
        scope.put(new ProcSymbol(new Procedure(Type.VOID, "print_i", vdPrintInt, null)));
        scope.put(new ProcSymbol(new Procedure(Type.VOID, "print_c", vdPrintChr, null)));
        scope.put(new ProcSymbol(new Procedure(Type.VOID, "print_s", vdPrintStr, null)));
    }

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
        Scope oldScope = scope;
        scope = new Scope(scope);
        for(VarDecl vd : p.params)
            vd.accept(this);
        Block b = p.block;
        for(VarDecl vd : b.params)
            vd.accept(this);
        for(Stmt st : b.stmts)
            st.accept(this);
        scope = oldScope;
		return null;
	}

	@Override
	public Void visitProgram(Program p) {
        init();
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
            error("Using function(Expr) without declaration");
        else if (!vs.isProc())
            error("Not a legal function(Expr) name");
        else
            f.p = ((ProcSymbol) vs).p;     //link the function and the declaration
        for(Expr v : f.args)
            v.accept(this);
		return null;
	}

	@Override
	public Void visitReturn(Return r){
        if (r.exp != null)
            r.exp.accept(this);
		return null;
	}

	@Override
	public Void visitFunCallStmt(FunCallStmt f){
        Symbol vs = scope.lookup(f.name);
        if(vs == null)
            error("Using function(Stmt) without declaration");
        else if (!vs.isProc())
            error("Not a legal function(Stmt) name");
        else
            f.p = ((ProcSymbol) vs).p;     //link the function and the declaration
        for(Expr v : f.args)
            v.accept(this);
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
    public Void visitIntLiteral(IntLiteral i){ return null; }

    @Override
    public Void visitStrLiteral(StrLiteral s){ return null; }

    @Override
    public Void visitChrLiteral(ChrLiteral c){ return null; }
}
