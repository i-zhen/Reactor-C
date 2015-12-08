package ast;

/**
 * Created by zhenyi on 15/12/8.
 */
public class Readc extends Stmt {

    public Readc(){
    }

    public <T> T accept(ASTVisitor<T> v){
        return v.visitReadc(this);
    }
}
