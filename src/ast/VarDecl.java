package ast;

public class VarDecl implements Tree {
    public final Type type;
    public final Var var;
    private boolean isglobal;
    private int index;

    public VarDecl(Type type, Var var) {
	    this.type = type;
	    this.var = var;
        this.isglobal = false;
        this.index = -1;
    }

    public boolean getGlobal(){
        return isglobal;
    }

    public void setGlobal(boolean b){
        isglobal = b;
    }

    public void setIndex(int i){
        this.index = i;
    }

    public int getIndex(){
        return this.index;
    }

    public <T> T accept(ASTVisitor<T> v) {
         return v.visitVarDecl(this);
    }
}
