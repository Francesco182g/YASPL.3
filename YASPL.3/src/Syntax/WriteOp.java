package Syntax;

import Visitor.Visitor;

public class WriteOp extends NodeOp{

		public WriteOp(String op, Node outValues) {
			super(op, outValues);
		}

		public Object accept(Visitor v) throws Exception{
			return v.visit(this);
		}
}
