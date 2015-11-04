package ast;

public abstract class Expr implements Tree {

    public Type type;
    public abstract <T> T accept(ASTVisitor<T> v);
}
