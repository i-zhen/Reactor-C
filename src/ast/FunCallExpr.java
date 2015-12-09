package ast;

import java.util.List;

public class FunCallExpr extends Expr{
    public final String name;
    public final List<Expr> args;
    public Procedure p;

    public FunCallExpr(String name, List<Expr> args){
        this.name = name;
        this.args = args;
    }

    public <T> T accept(ASTVisitor<T> v){
        return v.visitFunCallExpr(this);
    }
}
