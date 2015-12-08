package sem;

import ast.*;

public class NameAnalysisVisitor extends BaseSemanticVisitor<Void> {

	@Override
	public Void visitBlock(Block b) {
		// To be completed...
		return null;
	}

	@Override
	public Void visitProcedure(Procedure p) {
		// To be completed...
		return null;
	}

	@Override
	public Void visitProgram(Program p) {
		// To be completed...
		return null;
	}

	@Override
	public Void visitVarDecl(VarDecl vd) {
		// To be completed...
		return null;
	}

	@Override
	public Void visitVar(Var v) {
		// To be completed...
		return null;
	}

	@Override
	public Void visitIntLiteral(IntLiteral i){
		return null;
	}

	@Override
	public Void visitStrLiteral(StrLiteral s){
		return null;
	}

	@Override
	public Void visitChrLiteral(ChrLiteral c){
		return null;
	}

	@Override
	public Void visitBinOp(BinOp b){
		return null;
	}

	@Override
	public Void visitFunCallExpr(FunCallExpr f){
		return null;
	}

	@Override
	public Void visitReturn(Return r){
		return null;
	}

	@Override
	public Void visitFunCallStmt(FunCallStmt f){
		return null;
	}

	@Override
	public Void visitWhile(While w){
		return null;
	}

	@Override
	public Void visitIf(If i){
		return null;
	}

	@Override
	public Void visitAssign(Assign a){
		return null;
	}

	@Override
	public Void visitReadc(Readc r){
		return null;
	}

	@Override
	public Void visitReadi(Readi r){
		return null;
	}

	@Override
	public Void visitPrints(Prints p){
		return null;
	}

	@Override
	public Void visitPrinti(Printi p){
		return null;
	}

	@Override
	public Void visitPrintc(Printc p){
		return null;
	}

	@Override
	public Void visitReadcExpr(ReadcExpr r){
		return null;
	}

	@Override
	public Void visitReadiExpr(ReadiExpr r){
		return null;
	}
}
