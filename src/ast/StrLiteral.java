package ast;

public class StrLiteral extends Expr{
    public String str;

    public StrLiteral(String s){
        this.type = Type.STRING;
        this.str = s;
    }

    public <T> T accept(ASTVisitor<T> v){
        return v.visitStrLiteral(this);
    }
}
