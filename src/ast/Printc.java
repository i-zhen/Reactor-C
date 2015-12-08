package ast;

/**
 * Created by zhenyi on 15/12/8.
 */
public class Printc extends Stmt{
    public final Expr exp;

    public Printc(Expr exp){
        this.exp = exp;
    }

    public <T> T accept(ASTVisitor<T> v) {
        return v.visitPrintc(this);
    }
}
