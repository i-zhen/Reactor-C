package sem;

import ast.Procedure;
import ast.VarDecl;

public abstract class Symbol {
	public String name;

	public Symbol(String name) {
		this.name = name;
	}
    public boolean isVar(){ return false;}
    public boolean isProc(){ return false;}


}

class ProcSymbol extends Symbol{
    Procedure p;

    public ProcSymbol(Procedure p){
        super(p.name);
        this.p = p;
    }
    @Override
    public boolean isProc() {return true;}
}

class VarSymbol extends Symbol{
    VarDecl vd;

    public VarSymbol(VarDecl vd){
        super(vd.var.name);
        this.vd = vd;
    }
    @Override
    public boolean isVar() {return true;}
}