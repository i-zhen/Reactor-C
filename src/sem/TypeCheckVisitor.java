package sem;

import ast.*;

public class TypeCheckVisitor extends BaseSemanticVisitor<Type> {

	@Override
	public Type visitBlock(Block b) {
		// To be completed...
		return null;
	}

	@Override
	public Type visitProcedure(Procedure p) {
		// To be completed...
		return null;
	}

	@Override
	public Type visitProgram(Program p) {
		// To be completed...
		return null;
	}

	@Override
	public Type visitVarDecl(VarDecl vd) {
		// To be completed...
		return null;
	}

	@Override
	public Type visitVar(Var v) {
		// To be completed...
		return null;
	}

	@Override
	public Type visitIntLiteral(IntLiteral i){
		return null;
	}

	@Override
	public Type visitStrLiteral(StrLiteral s){
		return null;
	}

	@Override
	public Type visitChrLiteral(ChrLiteral c){
		return null;
	}

	@Override
	public Type visitBinOp(BinOp b){
		return null;
	}

	@Override
	public Type visitFunCallExpr(FunCallExpr f){
		return null;
	}

	@Override
	public Type visitReturn(Return r){
		return null;
	}

	@Override
	public Type visitFunCallStmt(FunCallStmt f){
		return null;
	}

	@Override
	public Type visitWhile(While w){
		return null;
	}

	@Override
	public Type visitIf(If i){
		return null;
	}

	@Override
	public Type visitAssign(Assign a){
		return null;
	}
}
