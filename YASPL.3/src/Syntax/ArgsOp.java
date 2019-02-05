package Syntax;

import Visitor.Visitor;

public class ArgsOp extends DeclOp {
	
	public ArgsOp(String op, Node...n) {
		super(op, n);
	}

	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}
	
}
