package ast;

/**
 * Created by zhenyi on 15/12/8.
 */
public class Prints extends Stmt{
    public final StrLiteral str;

    public Prints(StrLiteral str){
        this.str = str;
    }

    public <T> T accept(ASTVisitor<T> v) {
        return v.visitPrints(this);
    }
}
