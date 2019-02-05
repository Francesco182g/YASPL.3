package Syntax;

import Visitor.Visitor;

public class VarInitValueOp extends DeclOp{

	public VarInitValueOp(String op, Node...e) {
		super(op, e);
	}

	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}
}
