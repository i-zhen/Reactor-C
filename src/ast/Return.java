package ast;

public class Return extends Stmt {
    public final Expr exp;

    public Return(){
        this.exp = null;
    }
    public Return(Expr exp){
        this.exp = exp;
    }

    public <T> T accept(ASTVisitor<T> v){
        return v.visitReturn(this);
    }
}
