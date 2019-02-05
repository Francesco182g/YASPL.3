package Syntax;

import Visitor.Visitor;

public class UnaryOp extends NodeOp{
	
	public UnaryOp(String op, Node node) {
		super(op, node);
	}
	
	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}
	
	public String getNodeType(){
		return this.nodeList().get(0).getType();
	}

}
