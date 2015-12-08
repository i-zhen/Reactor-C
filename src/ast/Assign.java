package ast;

public class Assign extends Stmt{
    public final Expr exp;
    public final Var var;

    public Assign(Var var, Expr exp) {
        this.exp = exp;
        this.var = var;
    }

    public <T> T accept(ASTVisitor<T> v) {
        return v.visitAssign(this);
    }
}
