package ast;

public class BinOp extends Expr{
    public final Expr lhs;
    public final Expr rhs;
    public final Op op;

    public BinOp(Expr lhs, Op op, Expr rhs){
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }
    public <T> T accept(ASTVisitor<T> v){
        return v.visitBinOp(this);
    }
}
