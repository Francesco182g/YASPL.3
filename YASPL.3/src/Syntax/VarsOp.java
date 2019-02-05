package Syntax;

import Visitor.Visitor;

public class VarsOp extends NodeOp {


	public VarsOp(String op, Node...n) {
		super(op, n);
	}

	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}
}
