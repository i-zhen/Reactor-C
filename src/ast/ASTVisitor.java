package ast;

public interface ASTVisitor<T> {
    public T visitBlock(Block b);
    public T visitProcedure(Procedure p);
    public T visitProgram(Program p);
    public T visitVarDecl(VarDecl vd);
    public T visitVar(Var v);
    public T visitIntLiteral(IntLiteral i);
    public T visitStrLiteral(StrLiteral s);
    public T visitChrLiteral(ChrLiteral c);
    public T visitBinOp(BinOp b);
    public T visitFunCallExpr(FunCallExpr f);
    public T visitReturn(Return r);
    public T visitFunCallStmt(FunCallStmt f);
    public T visitWhile(While w);
    public T visitIf(If i);
    public T visitAssign(Assign a);
    // to complete ... (should have one visit method for each concrete AST node class)
}
