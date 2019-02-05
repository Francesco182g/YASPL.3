package Syntax;

import Visitor.Visitor;

public class CallOp extends SupportNode{

	public CallOp(String op, NodeLeaf l, Node e) {
		super(op, l, e);
	}
	
	public CallOp(String op, NodeLeaf l) {
		super(op, l);
	}
	
	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}	
	
}
