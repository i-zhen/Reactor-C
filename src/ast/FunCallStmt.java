package ast;

import java.util.List;

public class FunCallStmt extends Stmt{
    public final String name;
    public final List<Expr> args;
    public Procedure p;

    public FunCallStmt(String name, List<Expr> args){
        this.name = name;
        this.args = args;
    }

    public <T> T accept(ASTVisitor<T> v){
        return v.visitFunCallStmt(this);
    }
}
