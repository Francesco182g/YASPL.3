package Syntax;

import Visitor.Visitor;

public class BodyOp extends DeclOp{
	
	public BodyOp(String op, Node...list) {
		super(op, list);
	}

	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}

}
