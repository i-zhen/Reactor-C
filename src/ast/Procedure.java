package ast;

import java.util.List;

public class Procedure implements Tree {
    public final Type type;
    public final String name;
    public final List<VarDecl> params;
    public final Block block;
    private String callType;

    public Procedure(Type type, String name, List<VarDecl> params, Block block) {
	    this.type = type;
	    this.name = name;
	    this.params = params;
	    this.block = block;
        this.callType = "()V";
    }

    public void setCallType(String s){
        this.callType = s;
    }

    public String getCallType(){
        return this.callType;
    }

    public <T> T accept(ASTVisitor<T> v) {
        return v.visitProcedure(this);
    }
}
