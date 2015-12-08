package ast;

import java.util.List;

public class Block extends Stmt {
    public final List<Stmt> stmts;
    public final List<VarDecl> params;

    public Block(List<VarDecl> params, List<Stmt> stmts){
        this.stmts = stmts;
        this.params = params;
    }

    public <T> T accept(ASTVisitor<T> v) {
        return v.visitBlock(this);
    }
}
