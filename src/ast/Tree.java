package ast;

public interface Tree {
    public <T> T accept(ASTVisitor<T> v);
}
