package Syntax;

import Visitor.Visitor;

public class SupportNode extends DeclOp{
		
		public SupportNode(String op, NodeLeaf l, Node n) {
			super(op, l, n);
		}
		
		public SupportNode(String op, Node...n) {
			super(op, n);
		}

		public Object accept(Visitor v) throws Exception{
			return v.visit(this);
		}
}
