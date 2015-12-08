package ast;

public class IntLiteral extends Expr{
    public int i;

    public IntLiteral(int num){
        this.type = Type.INT;
        this.i = num;
    }

    public <T> T accept(ASTVisitor<T> v){
        return v.visitIntLiteral(this);
    }
}
