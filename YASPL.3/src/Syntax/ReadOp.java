package Syntax;

import Visitor.Visitor;

public class ReadOp extends SupportNode{
	
	public ReadOp(String op, Node vars) {
		super(op, vars);
	}
	
	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}
}
