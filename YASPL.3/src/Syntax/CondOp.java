package Syntax;

import Visitor.Visitor;

public class CondOp extends NodeOp{

	public CondOp(String op, Node... list) {
		super(op, list);
	}

	public String getFirstNode(){
		return this.nodeList().get(0).getType();
	}
	
	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}
}
