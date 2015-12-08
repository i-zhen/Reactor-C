package ast;


public class While extends Stmt {
    public final Expr exp;
    public final Stmt stmt;

    public While(Expr exp, Stmt stmt){
        this.stmt = stmt;
        this.exp = exp;
    }

    public <T> T accept(ASTVisitor<T> v) {
        return v.visitWhile(this);
    }
}
