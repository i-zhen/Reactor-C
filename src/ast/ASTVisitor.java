package ast;

public interface ASTVisitor<T> {
    public T visitBlock(Block b);
    public T visitProcedure(Procedure p);
    public T visitProgram(Program p);
    public T visitVarDecl(VarDecl vd);
    public T visitVar(Var v);

    // to complete ... (should have one visit method for each concrete AST node class)
}
