package Syntax;

import Visitor.Visitor;

public class ProgramOp extends ScopeNode{

	public ProgramOp(String op, NodeOp decls, NodeOp statements) {
		super(op, decls, statements);
	}

	public Object accept(Visitor v) throws Exception{
		return v.visit(this);
	}
}
