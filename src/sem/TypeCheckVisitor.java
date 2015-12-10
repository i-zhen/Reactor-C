package sem;

import ast.*;

public class TypeCheckVisitor extends BaseSemanticVisitor<Type> {

	@Override
	public Type visitBlock(Block b) {
        Type retT = null;
		for(VarDecl vd : b.params)
            vd.accept(this);
        for(Stmt st : b.stmts)
            if(retT == null)
                retT = st.accept(this);
            else
                st.accept(this);
        return retT;
	}

	@Override
	public Type visitProcedure(Procedure p) {
        for(VarDecl vd : p.params)
            vd.accept(this);
        Type ret = p.block.accept(this);
        if(ret == null && p.type == Type.VOID)
		    return p.type;
        if(ret != null && ret == p.type)
            return p.type;
        error("The return type must match the type of function/main " + ret + " " + p.type);
        return p.type;
	}

	@Override
	public Type visitProgram(Program p) {
		for(VarDecl vd : p.varDecls)
            vd.accept(this);
        for(Procedure ps : p.procs)
            ps.accept(this);
        p.main.accept(this);
		return null;
	}

	@Override
	public Type visitVarDecl(VarDecl vd) {
		if (vd.type == Type.VOID)
			error("Variable cannot be VOID type");
		return null;
	}

	@Override
	public Type visitVar(Var v) {
		v.type = v.vd.type;
		return v.type;
	}

	@Override
	public Type visitIntLiteral(IntLiteral i){ return Type.INT; }

	@Override
	public Type visitStrLiteral(StrLiteral s){
		return Type.STRING;
	}

	@Override
	public Type visitChrLiteral(ChrLiteral c){
		return Type.CHAR;
	}

	@Override
	public Type visitBinOp(BinOp b){
        Type lhsT = b.lhs.accept(this);
        Type rhsT = b.rhs.accept(this);
        switch (b.op){
            case ADD: case SUB: case DIV: case MOD: case MUL:
                if(lhsT == Type.INT && rhsT == Type.INT){
                    b.type = Type.INT;
                    return Type.INT;
                } else
                    error("Type mismatch : BinOp :" + lhsT + " " + rhsT);
                break;
            default:
                if(lhsT == Type.VOID || rhsT == Type.VOID)
                    error("IMPOSSIBLE type : VOID");
                else if(lhsT == rhsT){
                    b.type = Type.INT;
                    return b.type;
                } else
                    error("Type mismatch : " + lhsT + " " + rhsT);
        }
        return null;
	}

	@Override
	public Type visitFunCallExpr(FunCallExpr f){
        if(f.p.params.size() != f.args.size())
            error("Number of args does not match : FunCallExpr");
        Boolean result = true;
        for(int pos = 0; pos < f.args.size(); pos++)
            result &= f.args.get(pos).accept(this) == f.p.params.get(pos).type;
        if(result) {
            if(f.p.type == Type.VOID){
                error("Function Expr cannot be VOID");
                return Type.VOID;
            }
            f.type = f.p.type;
            return f.type;
        }
        error("Type of args does not match : FunCallExpr");
		return null;
	}

	@Override
	public Type visitReturn(Return r){
        if (r.exp != null)
            return r.exp.accept(this);
		return null;
	}

	@Override
	public Type visitFunCallStmt(FunCallStmt f){
        if(f.p.params.size() != f.args.size()) {
            error("Number of args does not match : FunCallStmt");
            return null;
        }
        Boolean result = true;
        for(int pos = 0; pos < f.args.size(); pos++)
            result &= f.args.get(pos).accept(this) == f.p.params.get(pos).type;
        if(!result)
            error("Type of args does not match : FunCallStmt");
        return null;
	}

	@Override
	public Type visitWhile(While w){
        if(w.exp.accept(this) != Type.INT)
            error("The Type of expression of While statement must be INT");
        w.stmt.accept(this);
		return null;
	}

	@Override
	public Type visitIf(If i){
        if(i.exp.accept(this) != Type.INT)
            error("The Type of expression of If statement must be INT");
        i.ifstmt.accept(this);
        if(i.elsestmt != null)
            i.elsestmt.accept(this);
		return null;
	}

	@Override
	public Type visitAssign(Assign a){
        Type expT = a.exp.accept(this);
        a.var.accept(this);
        if(a.var.type != expT)
            error("Type mismatch : Assign :" + a.var.type.toString() + " " + expT);
		return null;
	}
}
