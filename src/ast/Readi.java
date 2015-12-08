package ast;

/**
 * Created by zhenyi on 15/12/8.
 */
public class Readi extends Stmt {

    public Readi(){
    }

    public <T> T accept(ASTVisitor<T> v){
        return v.visitReadi(this);
    }
}
