package Syntax;

import Visitor.Visitor;

public class VarDeclOp extends DeclOp{
	
	public VarDeclOp(String op, Node type, Node vars) {
		super(op, type, vars);
	}
	
	public VarDeclOp(String op) {
		super(op);
	}

	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}

}
