package ast;

public class ChrLiteral extends Expr{
    public char c;

    public ChrLiteral(char ch){
        this.type = Type.CHAR;
        this.c = ch;
    }

    public <T> T accept(ASTVisitor<T> v){
        return v.visitChrLiteral(this);
    }
}
