package Syntax;

import Lexer.SymbolTable;
import Visitor.Visitor;

public class ScopeNode extends DeclOp{

	private SymbolTable symbolTable;
	
	public ScopeNode(String op, NodeOp n1, NodeOp n2) {
		super(op, n1, n2);
		symbolTable = null;
	}
	
	public ScopeNode(String op, NodeLeaf l, NodeOp n1, NodeOp n2, NodeOp n3) {
		super(op, l, n1, n2, n3);
		symbolTable = null;
	}
	
	public SymbolTable getSymbolTable(){
		return this.symbolTable;
	}
	
	public void setSymbolTable(SymbolTable symbolTable){
		this.symbolTable = symbolTable;
	}
	
	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}
}
