package ast;

/**
 * Created by zhenyi on 15/12/8.
 */
public class ReadcExpr extends Expr {

    public ReadcExpr(){
    }

    public <T> T accept(ASTVisitor<T> v){
        return v.visitReadcExpr(this);
    }
}
