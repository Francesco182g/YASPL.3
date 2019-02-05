package Syntax;

import java.util.ArrayList;
import java.util.List;

import Visitor.Visitor;

public class DeclOp extends NodeOp{
	
	public DeclOp(String op, NodeLeaf l, Node n) {
		super(op, l, n);
	}
	
	public DeclOp(String op, Node...l) {
		super(op, l);
	}

	public NodeLeaf getLeaf(){
		return ((NodeLeaf) this.nodeList().get(0));
	}
	
	public String getName(){
		return this.getLeaf().getValore().toString();
	}
	
	public Node getNode(){
		if(this.nodeList().size()>1) {
			return this.nodeList().get(1);
		}else {
			return null;
		}
		
	}
	
	public String getLeafType() {
		return ((NodeLeaf) this.nodeList().get(0)).getType();
	}
	
	public String getNodeType() {
		return this.nodeList().get(1).getType();
	}
	
	public String getNodeName() {
		return this.nodeList().get(1).getOp();
	}
	
	public void getAllnodes() {
		System.out.println(this.getLeaf());
	}
	
	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}
	
}
