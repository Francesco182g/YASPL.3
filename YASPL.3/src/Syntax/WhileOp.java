package Syntax;

import Visitor.Visitor;

public class WhileOp extends CondOp{
	
	public WhileOp(String op, Node e, Node s) {
		super(op, e, s);
		
	}
	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}
}
