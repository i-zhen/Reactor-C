package ast;

/**
 * Created by zhenyi on 15/12/8.
 */
public class Printi extends Stmt{
    public final Expr exp;

    public Printi(Expr exp){
        this.exp = exp;
    }

    public <T> T accept(ASTVisitor<T> v) {
        return v.visitPrinti(this);
    }
}
