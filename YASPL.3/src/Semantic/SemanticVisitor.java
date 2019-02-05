package Semantic;
/**
 * Analisi Semantica
 * 
 * @author Francesco Garofalo
 *
 */


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

import Syntax.ArgsOp;
import Syntax.AssignOp;
import Syntax.BodyOp;
import Syntax.CallOp;
import Syntax.CondOp;
import Syntax.DeclOp;
import Syntax.IfThenElseOp;
import Syntax.IfThenOp;
import Syntax.MathOp;
import Syntax.Node;
import Syntax.NodeLeaf;
import Syntax.NodeOp;
import Syntax.ParDeclOp;
import Syntax.ProgramOp;
import Syntax.ReadOp;
import Syntax.ScopeNode;
import Syntax.SupportNode;
import Syntax.UnaryOp;
import Syntax.VarDeclOp;
import Syntax.VarInitValueOp;
import Syntax.VarsOp;
import Syntax.WhileOp;
import Syntax.WriteOp;
import Visitor.Visitor;

public class SemanticVisitor implements Visitor{

	/*
	 * Stack: Utilizza lo stack per inserire le tabelle dei simboli associate agli scope
	 */
	private Stack<SymbolTableScope> stack = new Stack<>();

	/*
	 * Salva al suo interno tutte le definizioni dei metodi con relativi metodi.
	 */
	private Hashtable<String,ArrayList<String>> methodDef = new Hashtable<String, ArrayList<String>>();

	/*
	 * Oggetto che al suo interno ha una Hashtable<Nome della variabile, Info della var associata>
	 */
	private SymbolTableScope tableScope;
	private SymbolTableScope tableScope2;

	/*
	 * ArrayList temporaneo per la gestione dei method definition
	 */
	private ArrayList<String> tempAm = new ArrayList<String>();

	private String nameMethod;

	//Prende il tipo usato
	private String type = "Type";

	private String assignVal = "";
	private String function = "Decl";

	@Override
	public Object visit(Node node) {
		return null;
	}

	@Override
	public Object visit(NodeOp node) throws Exception {
		List<Node> nodes = node.nodeList();

		if(node.getOp() == "DefDeclsOp") {
			assignVal = "method";
			tempAm = new ArrayList<String>();
		}
		if(node.getOp() == "AssignVal") {
			assignVal = "assign";
		}
		if(node.getOp() == "CompStatOp") {
			//System.out.println("STATEMTENS OP PRESENTE");
		}
		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}
		}
		return null;
	}


	@Override
	public Object visit(ProgramOp node) throws Exception {
		System.out.println("Analisi Semantica...");
		tableScope = new SymbolTableScope("HEAD");
		stack.push(tableScope);
		List<Node> nodes = node.nodeList();
		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}
		}
		return null;
	}

	@Override
	public Object visit(DeclOp node) throws Exception {
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(VarDeclOp node) throws Exception {
		type = node.getLeaf().getType()+"_CONST";
		//System.out.println(node.getLeaf().getType());
		return this.visit((NodeOp) node);
	}



	@Override
	public Object visit(NodeLeaf node) throws Exception {
		if(node.getOp() == "ID") {
			tableScope = stack.pop();
			Info i = tableScope.lookup(node.getValore().toString());
			node.setType(i.getType());
			stack.push(tableScope);
			tempAm.add(i.getType());
		}
		//Metodo che verifica se l'id chiamato è presente e ne setta il tipo
		if(node.getOp() == "IDop") {
			if(stack.size() == 2) {
				tableScope = stack.pop();
				Info i = tableScope.lookup(node.getValore().toString());
				if(i.getType().equals("null")) {
					tableScope2 = stack.pop();
					i = tableScope2.lookup(node.getValore().toString());
					node.setType(i.getType());
					tempAm.add(i.getType());
					stack.push(tableScope2);
					stack.push(tableScope);

				} else {
	
					node.setType(i.getType());
					stack.push(tableScope);
					tempAm.add(i.getType());
				}
			} else {
				
				tableScope = stack.pop();
				Info i = tableScope.lookup(node.getValore().toString());
				if(i.getType().equals("null")) {
					throw new Exception("SEMANTIC ERROR: Variabile non dichiarata");
				}
				node.setType(i.getType());				
				stack.push(tableScope);
				tempAm.add(i.getType());
			}
		}
		if(node.getOp() == "IDdecl") {
			tableScope = stack.pop();
			Info i = new Info(type, function);
			tableScope.addId(node.getValore().toString(), i);
			function = "Decl";
			node.setType(i.getType());
			stack.push(tableScope);		
		}
		if(node.getOp() == "IN" || node.getOp() == "OUT" || node.getOp() == "INOUT") {
			function = node.getOp();
		}
		if(node.getOp() == "DOUBLE" || node.getOp() == "INT" || node.getOp() == "STRING" || node.getOp() == "CHAR" || node.getOp() == "BOOL") {
			type = node.getOp()+"_CONST";
			//aggiunge i tipi
			tempAm.add(type);
		}
		if(node.getOp() == "IDdefdecl") {
			if(assignVal.equals("method")) {
				tableScope = stack.pop();
				Info i = new Info("METHOD", "");
				tableScope.addId(node.getValore().toString(), i);
				nameMethod = node.getValore().toString();
				stack.push(tableScope);
				node.setType(type);
				tableScope = new SymbolTableScope("DefinizioneInterna");
				stack.push(tableScope);
				tempAm.add("");
				methodDef.put(nameMethod, tempAm);
				tempAm = new ArrayList<String>();
			}
		}
		if(node.getOp() == "IDpardecl") {
			tableScope = stack.pop();
			Info i = new Info(type, function);
			tableScope.addId(node.getValore().toString(), i);
			stack.push(tableScope);
			node.setType(type);
			function = "Decl";
		}
		if(node.getOp() == "INT_CONST") {
			if(assignVal.equals("assign")) {
				TypeCheck.checkInt(type, node.getValore().toString());
			}
		}
		if(node.getOp() == "DOUBLE_CONST") {
			if(assignVal.equals("assign")) {
				TypeCheck.checkDouble(type, node.getValore().toString());
			}
		}
		if(node.getOp() == "BOOL_CONST") {
			if(assignVal.equals("assign")) {
				TypeCheck.checkBool(type, node.getValore().toString());
			}
		}
		if(node.getOp() == "CHAR_CONST") {
			if(assignVal.equals("assign")) {
				TypeCheck.checkChar(type, node.getValore().toString());
			}	
		}
		if(node.getOp() == "STRING_CONST") {
			if(assignVal.equals("assign")) {
				TypeCheck.checkString(type, node.getValore().toString());
			}
		}
		assignVal = "";
		return null;
	}

	@Override
	public Object visit(ParDeclOp node) throws Exception {
		List<Node> nodes = node.nodeList();
		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}
		}
		methodDef.replace(nameMethod, tempAm);
		tempAm = new ArrayList<String>();
		return null;
	}

	@Override
	public Object visit(BodyOp node) throws Exception {	

		List<Node> nodes = node.nodeList();
		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}
		}
		stack.pop();
		return null;
	}

	@Override
	public Object visit(UnaryOp node) throws Exception {
		if(node.getOp().equals("MinusOP")) {
			List<Node> nodes = node.nodeList();			
			for (Node n : nodes){
				if (n!=null){
					n.accept(this);
				}
			}		
			if((node.getNodeType().equals("DOUBLE_CONST")) || (node.getNodeType().equals("INT_CONST")) ) {
				node.setType(node.getNodeType());
			}else {
				throw new Exception("Type Mismatch: MINUS ONLY AT INT OR DOUBLE: "+node.getNodeType());
			}
			node.setType(node.getNodeType());
		}
		if(node.getOp().equals("NotOP")) {
			List<Node> nodes = node.nodeList();			
			for (Node n : nodes){
				if (n!=null){
					n.accept(this);
				}
			}
			if((node.getNodeType().equals("BOOL_CONST"))) {
				node.setType(node.getNodeType());
			}else {
				throw new Exception("Type Mismatch: NOT ONLY AT BOOL: "+node.getNodeType());
			}
			node.setType(node.getNodeType());
		}
		return null;
	}

	@Override
	public Object visit(MathOp node) throws Exception {
		node.getFirstOp().accept(this);
		node.getSecondOp().accept(this);
		String type1 = node.getFirstOp().getType();
		String type2 = node.getSecondOp().getType();
		String op = node.getOp();
		if(!op.equals("AndOp") || (!op.equals("OrOp"))) {

			if((op.equals("AndOp")) || (op.equals("OrOp")) ) {
				if(type1.equals("BOOL_CONST") && type2.equals("BOOL_CONST")) {
					node.setType(type1);
				} else {
					throw new Exception("Type Mismatch: " + type1+ " " +op+ " " +type2);	
				}
			}

			/*
			 * Type System per
			 * >, <, >=, <=, ==
			 * Consentito solo se i due operatori sono Uguali, Int e dobule, double e int
			 * Errore se uno dei opertori sono diversi tra loro eccetto int e double
			 */
			if((op.equals("GTOp")) || (op.equals("GEOp")) || (op.equals("LTOp")) || (op.equals("LEOp")) || (op.equals("EQOp"))) {
				if((type1.equals("INT_CONST")) && (type2.equals("DOUBLE_CONST"))) {
					node.setType("BOOL_CONST");
				} else if((type1.equals("DOUBLE_CONST")) && (type2.equals("INT_CONST"))) {
					node.setType("BOOL_CONST");
				} else if(type1.equals(type2)) {
					if(type1.equals("STRING_CONST") || type.equals("CHAR_CONST")){
						throw new Exception("Type Mismatch: " + type1+ " " +op+ " " +type2);	
					}else {
					}
					node.setType("BOOL_CONST");
				}
				else {
					throw new Exception("Type Mismatch: " + type1+ " " +op+ " " +type2);	
				}
			}

			/*
			 * Type System per 
			 * SOTTRAZIONI, MOLTIPLICAZIONI, DIVISIONI
			 * Consentito solo se i due operatori sono interi o double
			 * Errore se uno dei due operatori o entrambi sono stringhe, char, bool
			 */

			if(op.equals("DiffOp") || op.equals("TimesOp") || op.equals("DivOp")) {

				if((type1.equals("STRING_CONST")) || (type1.equals("CHAR_CONST")) ||
						(type2.equals("STRING_CONST")) || (type2.equals("CHAR_CONST")) ||
						(type2.equals("BOOL_CONST")) || (type2.equals("BOOL_CONST"))
						)
				{
					throw new Exception("Type Mismatch: " + type1+ " " +op+ " " +type2);	

				} else {

					if(type1.equals("INT_CONST") && (type2.equals("DOUBLE_CONST"))) {
						node.setType("DOUBLE_CONST");
					}else if(type2.equals("INT_CONST") && (type1.equals("DOUBLE_CONST"))) {
						node.setType("DOUBLE_CONST");
					}else {
						if(type1.equals(type2)) {
							node.setType(type1);
						}
						else {
							throw new Exception("Type Mismatch: " + type1+ " " +op+ " " +type2);			
						}
					}	
				}

			}

			/*
			 * Type System per 
			 * ADDIZIONI
			 * Consentito solo se i due operatori sono interi o double
			 * Consentito solo se i due operatori sono ENTRAMBI string
			 * Errore se uno dei due operatori o entrambi sono char o bool
			 */
			if (op.equals("PlusOp")) {
				if((type1.equals("CHAR_CONST")) || (type1.equals("CHAR_CONST")) ||
						(type1.equals("BOOL_CONST")) || (type2.equals("BOOL_CONST")))
				{
					throw new Exception("Type Mismatch");
				}
				else {
					if (type1.equals("INT_CONST") && (type2.equals("DOUBLE_CONST"))) {
						node.setType("DOUBLE_CONST");
					}else if(type1.equals("DOUBLE_CONST") && (type2.equals("INT_CONST"))) {
						node.setType("DOUBLE_CONST");	
					}else if(type1.equals(type2)) {
						node.setType(type1);
					}else {
						throw new Exception("Type Mismatch: " + type1+ " " +op+ " " +type2);			
					}
				}
			} 
		}else {
			//Niente
			node.setType(type1);
		}

		return null;
	}

	/*
	 * CondOp()
	 * Verifica tutti i nodi condizioni affinché il tipo sia BOOLEANO
	 * Errore se il tipo è diverso da BOOL_CONST
	 */
	@Override
	public Object visit(CondOp node) throws Exception {
		List<Node> nodes = node.nodeList();
		for (Node n : nodes){
			if (n!=null){
				n.accept(this); 
			}
		}
		if(node.getFirstNode().equals("BOOL_CONST")) {
			node.setType("BOOL_CONST");
		} else {
			throw new Exception("Type Mismatch: CONDOP ONLY BOOLEAN: "+node.getFirstNode());
		}	
		return null;
	}

	//Verifica a CondOp()
	@Override
	public Object visit(IfThenOp node) throws Exception {		
		return this.visit((CondOp) node);
	}

	//Verifica a CondOp()
	@Override
	public Object visit(IfThenElseOp node) throws Exception {
		return this.visit((CondOp) node);
	}

	//Verifica a CondOp()
	@Override
	public Object visit(WhileOp node) throws Exception {
		return this.visit((CondOp) node);
	}


	@Override
	public Object visit(CallOp node) throws Exception {
		ArrayList<String> par = new ArrayList<String>();
		par = methodDef.get(node.getName());

		List<Node> nodes = node.nodeList();

		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}
		}

		if(nodes.size() == 1) {
			if(!methodDef.containsKey(node.getName())) {
				throw new Exception("Call Not Found");
			}
		}else if(tempAm.equals(par)) {
			//OK
		}else {
			throw new Exception("Call Mismatch");
		}

		return null;
	}

	@Override
	public Object visit(ArgsOp node) throws Exception {
		tempAm = new ArrayList<String>();
		List<Node> nodes = node.nodeList();

		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}
		}

		return null;
	}

	@Override
	public Object visit(AssignOp node) throws Exception {
		if(stack.size() == 2) {
			tableScope = stack.pop();
			tableScope2 = stack.pop();
			stack.push(tableScope2);
			stack.push(tableScope);
		} else {
			tableScope = stack.pop();
			stack.push(tableScope);
		}
		Info i = tableScope.lookup(node.getName());
		if(i.getType().equals("null")) {
			i = tableScope2.lookup(node.getName());
		}
		node.getFirstOp().setType(i.getType());
		node.getSecondOp().accept(this);

		String firstOp = node.getFirstOp().getType();
		String secondOp = node.getSecondOp().getType();

		if(firstOp.equals("DOUBLE_CONST") && secondOp.equals("INT_CONST")) {

		}else if(firstOp.equals(secondOp)) {

		}else {
			throw new Exception("Type Mismatch: "+firstOp+secondOp +"var: "+node.getName());
		}




		return null;
	}

	@Override
	public Object visit(WriteOp node) throws Exception {

		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(ReadOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(VarInitValueOp node) throws Exception {
		return this.visit((NodeOp) node);
	}

	@Override
	public Object visit(VarsOp node) throws Exception {
		tempAm = new ArrayList<String>();
		List<Node> nodes = node.nodeList();
		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}
		}

		String verifica = tempAm.get(0).toString();		
		for (String x : tempAm) {
			if (x.equals(verifica)) {

			}else {
				throw new Exception("Type not equals");
			}
		}

		return null;
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


}
