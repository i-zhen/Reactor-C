package ast;


public class If extends Stmt {
    public final Expr exp;
    public final Stmt ifstmt;
    public final Stmt elsestmt;

    public If(Expr exp, Stmt ifstmt){
        this.elsestmt = null;
        this.ifstmt = ifstmt;
        this.exp = exp;
    }

    public If(Expr exp, Stmt ifstmt, Stmt elsestmt){
        this.elsestmt = elsestmt;
        this.ifstmt = ifstmt;
        this.exp = exp;
    }

    public <T> T accept(ASTVisitor<T> v) {
        return v.visitIf(this);
    }
}
