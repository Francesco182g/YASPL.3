package Syntax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import Visitor.Visitor;

public class NodeOp extends Node {

	private List<Node> val = new ArrayList<Node>();

	/*
	 * Specialize Node (you can have more Node)
	 */
	public NodeOp(String op, Node...list) {
		super(op);
		for (Node node : list) {
			val.add(node);
		}
		//System.out.println(" " + NodeList().toString());
	}

	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}

	public void addNode(Node n){
		val.add(n);
	}

	public List<Node> nodeList(){
		return val;
	}
	
	public void reverseList() {
		Collections.reverse(val);
	}

	
	

		

}
