package Syntax;

import Visitor.Visitor;

public class AssignOp extends SupportNode{

	public AssignOp(String op, NodeLeaf name, Node e) {
		super(op, name, e);
	}

	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}
	
	public Node getFirstOp(){
		return this.nodeList().get(0);
	}
	
	public Node getSecondOp(){
		return this.nodeList().get(1);
	}

}
