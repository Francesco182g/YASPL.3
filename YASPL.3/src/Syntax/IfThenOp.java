package Syntax;

import Visitor.Visitor;

public class IfThenOp extends CondOp{

	public IfThenOp(String op, Node e, Node s) {
		super(op, e, s);
	}

	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}
}

