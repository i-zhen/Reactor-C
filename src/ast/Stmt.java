package ast;

public abstract class Stmt implements Tree {
    public abstract <T> T accept(ASTVisitor<T> v);
}
