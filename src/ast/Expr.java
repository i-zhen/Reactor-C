package ast;

public abstract class Expr implements Tree {
    public abstract <T> T accept(ASTVisitor<T> v);
}
