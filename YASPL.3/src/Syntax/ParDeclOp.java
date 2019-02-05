package Syntax;

import Visitor.Visitor;

public class ParDeclOp extends DeclOp{
	
	public ParDeclOp(String op, Node...list) {
		super(op, list);
	}

	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}
}
