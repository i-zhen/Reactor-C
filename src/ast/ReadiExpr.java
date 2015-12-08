package ast;

/**
 * Created by zhenyi on 15/12/8.
 */
public class ReadiExpr extends Expr {

    public ReadiExpr(){
    }

    public <T> T accept(ASTVisitor<T> v){
        return v.visitReadiExpr(this);
    }
}
