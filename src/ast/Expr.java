package ast;

public abstract class Expr implements ASTNode {
    public abstract <T> T accept(ASTVisitor<T> v);
}
