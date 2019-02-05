package Visitor;

import Syntax.Node;
import Syntax.NodeLeaf;
import Syntax.NodeOp;
import Syntax.*;

public interface Visitor {

	Object visit(Node node);
	Object visit(NodeOp node) throws Exception;
	Object visit(NodeLeaf node) throws Exception;

	Object visit(ProgramOp node) throws Exception;
	Object visit(DeclOp node) throws Exception;
	Object visit(SupportNode node) throws Exception;
	Object visit(ScopeNode node) throws Exception;
	Object visit(MathOp node) throws Exception;
	Object visit(UnaryOp node) throws Exception;
	Object visit(CondOp node) throws Exception;
	Object visit(IfThenOp node) throws Exception;
	Object visit(IfThenElseOp node) throws Exception;
	Object visit(WhileOp node) throws Exception;
	Object visit(CallOp node) throws Exception;
	Object visit(AssignOp node) throws Exception;
	Object visit(WriteOp node) throws Exception;
	Object visit(ReadOp node) throws Exception;
	Object visit(VarDeclOp node) throws Exception;
	Object visit(ParDeclOp node) throws Exception;
	Object visit(ArgsOp node) throws Exception;
	Object visit(VarInitValueOp node) throws Exception;
	Object visit(BodyOp node) throws Exception;
	Object visit(VarsOp node) throws Exception;
}
