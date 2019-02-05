package Visitor;

import java.util.ArrayList;

/* Visitatore che mi restituisce l'XML */

import org.apache.commons.lang3.StringEscapeUtils;
import Syntax.*;

public class XMLVisitor implements Visitor{
	//private ArrayList test = new ArrayList<>();

	public Object visit(Node n){
		return null;
	}
	
	public Object visit(NodeOp n) throws Exception {
		String toPrint = "";
		toPrint += "<"+n.getOp()+">\n";
		for (Node node : n.nodeList()){
			if (node!=null){
				toPrint += node.accept(this)+"";
			}
			else 
				toPrint += "<null/>\n";
		}
		toPrint += "</"+n.getOp()+">\n";
		return toPrint;
	}

	public Object visit(NodeLeaf n) {
		String toPrint = "";
		toPrint += "<" + n.getOp();
		if (n.getValore() != null) {
			toPrint += " attr='" + StringEscapeUtils.escapeXml(n.getValore().toString()) + "'"; //converto la stringa nella sua rappresentazione XML
		}
		toPrint += "/>\n";
		return toPrint;
	}

	@Override
	public Object visit(ProgramOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(DeclOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(SupportNode node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(ScopeNode node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(MathOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(UnaryOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(CondOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(IfThenOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(IfThenElseOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(WhileOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(CallOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(AssignOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(WriteOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(ReadOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(VarDeclOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(ParDeclOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(ArgsOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(VarInitValueOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(BodyOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(VarsOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}


}
