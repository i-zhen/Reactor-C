package ast;

public class Block extends Stmt {
    public <T> T accept(ASTVisitor<T> v) {
	    return v.visitBlock(this);
    }
    // to complete ...
}
