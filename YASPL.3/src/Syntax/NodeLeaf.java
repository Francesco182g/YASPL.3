package Syntax;

import Visitor.Visitor;

public class NodeLeaf extends Node{

	private String valore;
	
	public NodeLeaf(String op, String valore, String type){
		super(op);
		this.valore = valore;
		this.setType(type);
	}
	
	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}

	public Object getValore() {
		return valore;
	}
	public void setValore(String valore) {
		this.valore = valore;
	}
	
}