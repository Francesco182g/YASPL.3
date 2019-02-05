package ToC;
/**
 * Connection pull DB
 * 
 * @author Francesco Garofalo
 *
 */

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

import Semantic.Info;
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
import ToC.SymbolTableToC;
import ToC.VarDeclaration;
import Visitor.Visitor;

public class ToCVisitor implements Visitor{

	//Salva il nome del file 
	private String fileName;
	//Puntatore che permette di scrivere sul file destinazione
	private FileWriter file;
	//Stringa per prendere il tipo
	private String type="no-type";
	//ArrayList di variabili, usati per diversi scopi all'interno dell'analisi semantica
	private ArrayList<VarDeclaration> tempA = new ArrayList<VarDeclaration>();
	private ArrayList<String> tempVar = new ArrayList<String>();
	//ASSIGN VAL : Qunado assegno alla variabile dichiarata un valore 
	private String assignVal = "";
	//ASSIGN OP : Qunado eseguo un operazione di assegnamento e altre operazioni
	private String assignOp = "";
	//String per salvare il tipo di operatore matematico
	private String tempMath = "";
	//String usata da CallOp per salvare il nome del metodo
	private String methodName = "";
	//String usata per salvare il nome della variabile interna alla dichiarazione della funzione
	private String functionName = "";
	//HashTable che istanzia le funzioni in modo da poterle richiamare nella CallOP
	private Hashtable<String,ArrayList<String>> funcDef = new Hashtable<String, ArrayList<String>>();
	//HashTable per settare il puntatore in DefDecl
	private Hashtable<String,String> funcNames = new Hashtable<String,String>();
	//Scooping per ReadOp
	private Stack<SymbolTableToC> stack = new Stack<>();
	//Table scope di supporto 2
	private SymbolTableToC tableScope;
	private SymbolTableToC tableScope2;


	public ToCVisitor(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public Object visit(ProgramOp node) throws Exception {
		//Gestione Scooping per ReadOp
		System.out.println("Avvio conversione a C");
		tableScope = new SymbolTableToC("HEAD");
		stack.push(tableScope);
		//Creazione File
		file = new FileWriter(fileName + ".c");
		/*
		 * Import stdio.h, stdlib.h, stdbool.h
		 */
		String text = "#include <stdio.h>\n";
		text += "#include <stdlib.h>\n";
		text += "#include <stdbool.h>\n\n";
		file.write(text);		
		List<Node> decls= ((NodeOp) node.nodeList().get(0)).nodeList();
		List<Node> statements= ((NodeOp) node.nodeList().get(1)).nodeList();
		//Visit Decls
		for(Node nodeDecls:decls) {
			nodeDecls.accept(this);
		}
		//Main()
		file.write("\n\nmain(){\n");
		//Visit Statements
		for(Node nodeS:statements) {
			nodeS.accept(this);
		}
		char c = '"';
		text = "\nsystem("+c+"pause"+c+");\n";
		text += "\nreturn 0;\n}";
		file.write(text);
		file.close();
		return null;
	}

	@Override
	public Object visit(NodeOp node) throws Exception {
		List<Node> nodes = node.nodeList();

		//Incontro nodo fittizio AssignVal e setto assignVal = "assign"
		if(node.getOp() == "AssignVal") {
			assignVal = "assign";
		}
		//Incontro nodo Statements, disattivo assign	
		if(node.getOp() == "StatementsOp") {
			assignVal = "";
		}
		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}

		}
		return null;
	}

	@Override
	public Object visit(NodeLeaf node) throws Exception {
		/*
		 * IDdecl:
		 * Salva il nome dell'id in un array temporaneo
		 * Salva il valore in una tabella dei simboli (necessaria per lo scoping)
		 */
		if(node.getOp().equals("IDdecl")) {
			VarDeclaration v = new VarDeclaration();
			v.setId(node.getValore().toString());
			tempA.add(v);
			//Gestione Scoping
			tableScope = stack.pop();
			Info i = new Info(type, "");
			tableScope.addId(node.getValore().toString(), i);
			node.setType(i.getType());
			stack.push(tableScope);	
		}

		/*
		 * IDop:
		 * assignVal=assign
		 * aggiunge a tempVar il valore del nodo
		 * 
		 * assignOp=assignOp
		 * Se la funzione non è in, scrivi il * prima della variabile
		 * else scrivi solo la variabile
		 * 
		 * assignOp=condOp
		 * Scrvi direttamente il valore su file
		 * 
		 * assignOp=readOp
		 * null
		 * 
		 * assignOp=writeOp
		 * aggiungi a tempAm l'id
		 * 
		 * else
		 * scrivi direttmente su file
		 * 
		 * 
		 * 
		 */
		if(node.getOp().equals("IDop")) {
			if(assignVal.equals("assign")) {
				tempVar.add(node.getValore().toString());
			} else if (assignOp.equals("assignOp")) {
				if(funcNames.containsKey(node.getValore().toString())) {
					if(!funcNames.get(node.getValore().toString()).equals("IN")) {
						file.write("*"+node.getValore().toString());							
					}else{
						file.write(node.getValore().toString());
					}	
				} else {
					file.write(node.getValore().toString());
				}
			} else if (assignOp.equals("condOp")) {
				file.write(node.getValore().toString());
			} else if(assignOp.equals("readOp")){
				//READ OP
			} else if(assignOp.equals("writeOp")){
				VarDeclaration ro = new VarDeclaration("ID", node.getValore().toString());
				tempA.add(ro);
				//System.out.println(node.getValore());
			} else if(assignOp.equals("callOp")){
				VarDeclaration vd = new VarDeclaration(node.getValore().toString(), type);
				tempA.add(vd);
			} else {
				file.write(node.getValore().toString());				
			}
		}

		/*
		 * ID:
		 * Lo usa la chiamata a funzione stessa. chiamata();
		 * 
		 */
		if(node.getOp().equals("ID")) {
			tempVar.add(node.getValore().toString());
		}
		/*
		 * IDdefdecl:
		 * salva il nome del metodo nella variabile methodName
		 * scrive la def.di funzione con il void prima
		 * aggiunge la dichiarazione alla SymbolTable
		 * 
		 */
		if (node.getOp().equals("IDdefdecl")) {
			methodName = node.getValore().toString();
			file.write("\nvoid " + node.getValore() + " (");
			//Creo una nuova Tabella dei simboli di scooping (per readop)
			tableScope = new SymbolTableToC("DefinizioneInterna");
			stack.push(tableScope);
		}
		/*
		 * IDpardecl:
		 * aggiunge a tempA le (pardecl)
		 * Aggiunge a funcNames le variabili e aggiunge se sono in o out
		 * aggiunge alla tabella di scope LOCALE le variabili
		 * 
		 */
		if (node.getOp().equals("IDpardecl")) {
			VarDeclaration v = new VarDeclaration();
			v.setId(node.getValore().toString());
			v.setVal(type);
			tempA.add(v);
			//funcNames
			funcNames.put(node.getValore().toString(), functionName);
			tableScope = stack.pop();
			Info i = new Info(type, "");
			tableScope.addId(node.getValore().toString(), i);
			stack.push(tableScope);
		}
		if(node.getOp() == "DOUBLE" || node.getOp() == "INT" || node.getOp() == "STRING" || node.getOp() == "CHAR" || node.getOp() == "BOOL") {
			type = node.getOp().toLowerCase();
		}
		if(node.getOp().equals("INT_CONST")){
			if(assignVal.equals("assign")) {
				tempA.get(tempA.size()-1).setVal(node.getValore().toString());	
			} else if (assignOp.equals("assignOp") || (assignOp.equals("condOp"))) {
				file.write(node.getValore().toString());
			} else if (assignOp.equals("writeOp")) {
				String castString = "STRING_CONST";
				VarDeclaration ro = new VarDeclaration(castString, node.getValore().toString());
				tempA.add(ro);
			}
		}
		if(node.getOp().equals("DOUBLE_CONST")) {
			if(assignVal.equals("assign")) {
				tempA.get(tempA.size()-1).setVal(node.getValore().toString());
			} else if (assignOp.equals("assignOp") || (assignOp.equals("condOp"))) {
				file.write(node.getValore().toString());
			} else if (assignOp.equals("writeOp")) {
				String castString = "STRING_CONST";
				VarDeclaration ro = new VarDeclaration(castString, node.getValore().toString());
				tempA.add(ro);
			}
		}
		if(node.getOp().equals("BOOL_CONST")) {
			if(assignVal.equals("assign")) {
				tempA.get(tempA.size()-1).setVal(node.getValore().toString().toLowerCase());	
			} else if (assignOp.equals("assignOp") || (assignOp.equals("condOp"))) {
				file.write(node.getValore().toString().toLowerCase());
			} else if (assignOp.equals("writeOp")) {
				String castString = "STRING_CONST";
				VarDeclaration ro = new VarDeclaration(castString, node.getValore().toString());
				tempA.add(ro);
			}
		}
		if(node.getOp().equals("CHAR_CONST")) {
			if(assignVal.equals("assign")) {
				tempA.get(tempA.size()-1).setVal(node.getValore().toString());	
			}else if (assignOp.equals("assignOp") || (assignOp.equals("condOp"))) {
				file.write(node.getValore().toString());
			} else if (assignOp.equals("writeOp")) {
				String castString = "STRING_CONST";
				VarDeclaration ro = new VarDeclaration(castString, node.getValore().toString());
				tempA.add(ro);
			}
		}
		if(node.getOp().equals("STRING_CONST")) {
			if(assignVal.equals("assign") || (assignOp.equals("condOp"))) {
				tempA.get(tempA.size()-1).setVal(node.getValore().toString());	
			} else if (assignOp.equals("assignOp")) {
				file.write(node.getValore().toString());
			} else if (assignOp.equals("writeOp")) {
				VarDeclaration ro = new VarDeclaration(node.getOp(), node.getValore().toString());
				tempA.add(ro);
			}else {

			}
		}
		if( node.getOp() == "IN" || node.getOp() == "OUT" || node.getOp() == "INOUT") {
			tempVar.add(node.getOp());
			functionName = node.getOp();
		}
		return null;
	}


	/*
	 * VarDeclOp
	 * Setta il tipo della variabile nella stringa type
	 */
	@Override
	public Object visit(VarDeclOp node) throws Exception {
		type = node.getLeafType().toLowerCase(); 
		char c = '"';
		tempA = new ArrayList<VarDeclaration>();
		List<Node> nodes = node.nodeList();
		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}
		}
		String text = "";
		if(type.equals("string")) {
			text = "char ";
		} else {
			text = type + " ";
		}
		if(tempA.size() > 1) {
			for(int i=0; i<tempA.size()-1; i++) {
				if(tempA.get(i).getVal().equals("")) {
					if(type.equals("string")) {
						text += tempA.get(i).getId() + "[50], ";
					}else {
						text += tempA.get(i).getId()+", ";
					}
				} else {
					if(type.equals("string")) {
						text += tempA.get(i).getId()+ " [50] = " +c+tempA.get(i).getVal()+c+",";
					}else if(type.equals("char")){
						text += tempA.get(i).getId()+ "  = " +"'"+tempA.get(i).getVal()+"'"+" ,";
					}else {
						text += tempA.get(i).getId()+" = "+tempA.get(i).getVal()+", ";						
					}
				}
			}
		}
		if(tempA.get(tempA.size()-1).getVal().equals("")) {
			if(type.equals("string")) {
				text += tempA.get(tempA.size()-1).getId()+ "[50];\n";
			}else {
				text += tempA.get(tempA.size()-1).getId()+";\n";
			}
		} else {
			if(type.equals("string")) {
				text += tempA.get(tempA.size()-1).getId()+"[50] = "+c+tempA.get(tempA.size()-1).getVal()+c+";\n";
			}else if(type.equals("char")){
				text += tempA.get(tempA.size()-1).getId()+" = "+"'"+tempA.get(tempA.size()-1).getVal()+"'"+";\n";
			}else {
				text += tempA.get(tempA.size()-1).getId()+" = "+tempA.get(tempA.size()-1).getVal()+";\n";				
			}
		}
		tempMath = "";
		file.write(text);
		return this.visit((NodeOp) node);
	}
	@Override
	public Object visit(VarInitValueOp node) throws Exception {
		tempVar = new ArrayList<String>();
		List<Node> nodes = node.nodeList();
		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}

		}
		if(!tempVar.isEmpty()) {
			for(int i=0; i<tempVar.size()-1; i++) {
				tempA.get(tempA.size()-1).setVal(tempA.get(tempA.size()-1).getVal() + " "+
						tempVar.get(i)+ " " +tempMath);
			}
			tempA.get(tempA.size()-1).setVal(tempA.get(tempA.size()-1).getVal() + " "+
					tempVar.get(tempVar.size()-1)+ "");
		}
		return null;
	}

	@Override
	public Object visit(ParDeclOp node) throws Exception {
		// TODO Auto-generated method stub
		List<Node> nodes = node.nodeList();
		tempA = new ArrayList<>();
		tempVar = new ArrayList<String>();
		funcNames = new Hashtable<String,String>();
		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}
		}
		if(tempA.size() == 1) {
			if(!tempVar.get(0).equals("IN")) {
				file.write(tempA.get(0).getVal().toLowerCase() + " *" + tempA.get(0).getId() + " ");
			} else {
				file.write(tempA.get(0).getVal().toLowerCase() + " " + tempA.get(0).getId() + " ");
			}
		}else {
			for (int i = 0; i < tempA.size()-1; i++) {
				if(!tempVar.get(i).equals("IN")) {
					file.write(tempA.get(i).getVal().toLowerCase() + " *" + tempA.get(i).getId() + ", ");
				} else {
					file.write(tempA.get(i).getVal().toLowerCase() + " " + tempA.get(i).getId() + ", ");
				}
			}
			if(!tempVar.get(tempVar.size()-1).equals("IN")) {
				file.write(tempA.get(tempA.size()-1).getVal().toLowerCase() + " *" + tempA.get(tempA.size()-1).getId() + "");
			} else {
				file.write(tempA.get(tempA.size()-1).getVal().toLowerCase() + " " + tempA.get(tempA.size()-1).getId() + "");
			}
		}

		funcDef.put(methodName, tempVar);
		return null;
	}

	@Override
	public Object visit(BodyOp node) throws Exception {
		functionName = "";
		file.write(")");
		file.write("\n{\n");
		List<Node> nodes = node.nodeList();
		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}
		}
		stack.pop();
		file.write("\n}");
		assignVal = "";
		functionName = "";
		funcNames = new Hashtable<String,String>();
		return null;
	}

	//MATH OP 
	@Override
	public Object visit(UnaryOp node) throws Exception {
		if(node.getOp().equals("MinusOP")) {
			file.write("-(");
		} else if(node.getOp().equals("NotOP")) {
			file.write("!(");
		}
		List<Node> nodes = node.nodeList();
		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}
		}
		file.write(")");
		return null;
	}

	//OPERAZIONE DI ASSEGNAZIONE E MATH
	@Override
	public Object visit(AssignOp node) throws Exception {
		//Attiva assignOp
		assignVal = "";
		assignOp = "assignOp";
		Node part1 = node.nodeList().get(0);
		Node part2 = node.nodeList().get(1);
		file.write("\n");
		part1.accept(this);
		file.write(" = ");
		part2.accept(this);
		file.write(";");
		file.write("\n");
		assignOp = "";
		//Disattiva assignOp
		return null;
	}

	@Override
	public Object visit(MathOp node) throws Exception {
		tempMath = node.getOp();
		switch(node.getOp().toString()) {
		case "PlusOp":
			node.nodeList().get(0).accept(this);
			if(!assignVal.equals("assign")){
				file.write(" + ");
			}else {
				tempMath = "+";
			}
			node.nodeList().get(1).accept(this);
			break;
		case "DiffOp": 
			node.nodeList().get(0).accept(this);
			if(!assignVal.equals("assign")){
				file.write(" - ");
			}else {
				tempMath = "-";
			}
			node.nodeList().get(1).accept(this);
			break;
		case "TimesOp":
			node.nodeList().get(0).accept(this);
			if(!assignVal.equals("assign")){
				file.write(" * ");
			}else {
				tempMath = "*";
			}
			node.nodeList().get(1).accept(this);
			break;
		case "DivOp": 
			node.nodeList().get(0).accept(this);
			if(!assignVal.equals("assign")){
				file.write(" / ");
			}else {
				tempMath = "/";
			}
			node.nodeList().get(1).accept(this);
			break;
		case "AndOp":
			assignOp = "condOp";
			node.nodeList().get(0).accept(this);
			file.write(" && ");
			node.nodeList().get(1).accept(this);
			assignOp = "";
			break;
		case "OrOp":
			assignOp = "condOp";
			file.write("(");
			node.nodeList().get(0).accept(this);
			file.write(" || ");
			node.nodeList().get(1).accept(this);
			file.write(")");
			assignOp = "";
			break;
		case "GTOp":
			assignOp = "condOp";
			file.write("(");
			node.nodeList().get(0).accept(this);
			file.write("");
			file.write(" > ");
			file.write("");
			node.nodeList().get(1).accept(this);
			file.write(")");
			assignOp = "";
			break;
		case "GEOp":
			assignOp = "condOp";
			file.write("(");
			node.nodeList().get(0).accept(this);
			file.write("");
			file.write(" >= ");
			file.write("");
			node.nodeList().get(1).accept(this);
			file.write(")");
			assignOp = "";
			break;
		case "LTOp":
			assignOp = "condOp";
			file.write("(");
			node.nodeList().get(0).accept(this);
			file.write("");
			file.write(" < ");
			file.write("");
			node.nodeList().get(1).accept(this);
			file.write(")");
			assignOp = "";
			break;
		case "LEOp":
			assignOp = "condOp";
			file.write("(");
			node.nodeList().get(0).accept(this);
			file.write("");
			file.write(" <= ");
			file.write("");
			node.nodeList().get(1).accept(this);
			file.write(")");
			assignOp = "";
			break;
		case "EQOp":
			assignOp = "condOp";
			file.write("(");
			node.nodeList().get(0).accept(this);
			file.write("");
			file.write(" == ");
			file.write("");
			node.nodeList().get(1).accept(this);
			file.write(")");
			assignOp = "";
			break;
		}
		return null;

	}



	//ConditionalOp

	@Override
	public Object visit(IfThenOp node) throws Exception {
		Node expression= node.nodeList().get(0);
		Node statement= node.nodeList().get(1);

		file.write("if(");
		expression.accept(this);
		file.write("){");
		statement.accept(this);
		file.write("}\n");
		return null;

	}

	@Override
	public Object visit(IfThenElseOp node) throws Exception {

		Node expression= node.nodeList().get(0);
		Node statement1= node.nodeList().get(1);
		Node statement2= node.nodeList().get(2);

		file.write("if(");
		expression.accept(this);
		file.write("){");
		statement1.accept(this);
		file.write("}else{");
		statement2.accept(this);
		file.write("}\n");

		return null;

	}

	@Override
	public Object visit(WhileOp node) throws Exception {
		Node expression= node.nodeList().get(0);
		Node statement= node.nodeList().get(1);

		file.write("while( ");
		expression.accept(this);
		file.write(" ){\n");
		statement.accept(this);
		file.write("}\n\n");

		return null;

	}

	//Use ArgsOp & IDop
	@Override
	public Object visit(CallOp node) throws Exception {
		assignOp = "callOp";
		ArrayList<String> temp = funcDef.get(node.getName());
		List<Node> nodes = node.nodeList();
		tempA = new ArrayList<>();

		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}
		}

		file.write("\n"+node.getName()+"(");
		//System.out.println(tempA);
		if(tempA.size() == 0) {
			file.write(");\n");
		}else {
			for(int i=0; i<tempA.size()-1; i++) {
				if(temp.get(i).equals("IN")){
					file.write("" + tempA.get(i).getId()+", ");
				} else {
					file.write("&" + tempA.get(i).getId()+", ");
				}
			}
			if(temp.get(temp.size()-1).equals("IN")){
				file.write("" + tempA.get(tempA.size()-1).getId()+");\n");
			} else {
				file.write("&" + tempA.get(tempA.size()-1).getId()+");\n");
			}
		}
		assignOp = "";
		return null;

	}

	@Override
	public Object visit(WriteOp node) throws Exception {
		//Setto assignOp per gestire la writeOp nelle leaf e resetto assign val per evitare conflitti
		assignOp = "writeOp";
		assignVal = "";
		char c = '"';
		tempA = new ArrayList<VarDeclaration>();
		//AntiConflitti
		tempVar = new ArrayList<String>();

		file.write("\nprintf(");
		List<Node> nodes = node.nodeList();
		for (Node n : nodes){
			assignOp = "writeOp";
			if (n!=null){
				n.accept(this);
			}
		}
		//System.out.println(tempA.size());
		//System.out.println(tempA.toString());
		try {
			String printType = "";
			if(stack.size() == 2) {
				tableScope = stack.pop();
				if(tempA.size() == 1) {
					if((tempA.get(0)).getId().equals("STRING_CONST")) {
						file.write(c+tempA.get(0).getVal()+"\\n"+c+");\n");
					} else if(((tempA.get(0)).getId().equals("ID"))){
						//VERIFICO IN BASE AL Tipo:
						Info i = new Info();
						i = tableScope.lookup(tempA.get(0).getVal());
						printType = verifyType(i);
						if(i == null) {
							tableScope2 = stack.pop();
							Info i2 = new Info();
							i2 = tableScope2.lookup(tempA.get(0).getVal());
							printType = verifyType(i2);
							stack.push(tableScope2);
							file.write(c+printType+"\\n"+c+", "+tempA.get(0).getVal()+");\n\n");
						}else {
							file.write(c+printType+"\\n"+c+", "+tempA.get(0).getVal()+");\n\n");
						}
					} else {
						file.write(""+tempA.get(0).getVal()+");\n");				
					}

					stack.push(tableScope);
				}else{
					tableScope = stack.pop();
					for(int i=0; i<tempA.size()-1;i++) {
						if(tempA.get(i).getId().equals("ID")) {
							Info info = new Info();
							info = tableScope.lookup(tempA.get(i).getVal());
							printType = verifyType(info);
							if(info == null) {
								tableScope2 = stack.pop();
								Info i2 = new Info();
								i2 = tableScope2.lookup(tempA.get(0).getVal());
								printType = verifyType(i2);
								stack.push(tableScope2);
								file.write(c+printType+"\\n"+c+", "+tempA.get(i).getVal()+", ");
							}else {
								file.write(c+printType+"\\n"+c+", "+tempA.get(i).getVal()+", ");
							}

						} else if(tempA.get(i).getId().equals("STRING_CONST")){
							file.write(c+tempA.get(i).getVal()+"\\n"+c+", ");
						} else {
							file.write(tempA.get(i).getVal()+", ");					
						}
					}

					stack.push(tableScope);

					if((tempA.get(tempA.size()-1)).getId().equals("STRING_CONST")) {
						file.write(c+tempA.get(tempA.size()-1).getVal()+"\\n"+c+"); \n");
					} else if((tempA.get(tempA.size()-1)).getId().equals("ID")){
						Info info = new Info();
						info = tableScope.lookup(tempA.get(tempA.size()-1).getVal());
						printType = verifyType(info);
						if(info == null) {
							tableScope2 = stack.pop();
							Info i2 = new Info();
							i2 = tableScope2.lookup(tempA.get(tempA.size()-1).getVal());
							printType = verifyType(i2);
							stack.push(tableScope2);
							file.write(c+printType+"\\n"+c+", "+tempA.get(tempA.size()-1).getVal()+"); \n");
						}else {
							file.write(c+printType+"\\n"+c+", "+tempA.get(tempA.size()-1).getVal()+"); \n ");
						}
						//System.out.println("ciao1");
						//file.write(""+tempA.get(tempA.size()-1).getVal()+"); \n");
					} else {
						file.write(tempA.get(tempA.size()-1).getVal()+"); \n");				
					}
					stack.push(tableScope);
				}

				//CASO STACK == 1!
			} else {
				tableScope = stack.pop();
				if(tempA.size() == 1) {
					if((tempA.get(0)).getId().equals("STRING_CONST") || (tempA.get(0)).getId().equals("CHAR_CONST")
							|| (tempA.get(0)).getId().equals("DOUBLE_CONST") || (tempA.get(0)).getId().equals("INT_CONST")
							|| (tempA.get(0)).getId().equals("BOOL_CONST")
							) {
						//System.out.println("val "+tempA.get(0).getId());
						file.write(c+tempA.get(0).getVal()+"\\n"+c+");\n");
					} else if(((tempA.get(0)).getId().equals("ID"))){
						//VERIFICO IN BASE AL Tipo:
						Info i = new Info();
						i = tableScope.lookup(tempA.get(0).getVal());
						printType = verifyType(i);
						file.write(c+printType+"\\n"+c+", "+tempA.get(0).getVal()+");\n");

					} else {
						file.write(""+tempA.get(0).getVal()+");\n");				
					}

				}else{
					for(int i=0; i<tempA.size()-1;i++) {
						if(tempA.get(i).getId().equals("ID")) {
							Info info = new Info();
							info = tableScope.lookup(tempA.get(i).getVal());
							printType = verifyType(info);
							file.write(c+printType+"\\n"+c+", "+tempA.get(i).getVal()+", ");
						} else if(tempA.get(i).getId().equals("STRING_CONST")  || tempA.get(i).getId().equals("CHAR_CONST")
								|| tempA.get(i).getId().equals("DOUBLE_CONST")  || tempA.get(i).getId().equals("INT_CONST")
								|| tempA.get(i).getId().equals("BOOL_CONST")
								){
							file.write(c+tempA.get(i).getVal()+"\\n"+c+" ");
						} else {
							file.write(tempA.get(i).getVal()+", ");					
						}
					}
					//System.out.println(tempA.size());
					if((tempA.get(tempA.size()-1)).getId().equals("STRING_CONST") || (tempA.get(tempA.size()-1)).getId().equals("CHAR_CONST")
							|| (tempA.get(tempA.size()-1)).getId().equals("INT_CONST") || (tempA.get(tempA.size()-1)).getId().equals("DOUBLE_CONST")
							|| 	(tempA.get(tempA.size()-1)).getId().equals("BOOL_CONST")
							) {
						file.write(c+tempA.get(tempA.size()-1).getVal()+"\\n"+c+"); \n");
					} else if((tempA.get(tempA.size()-1)).getId().equals("ID")){
						Info info = new Info();
						info = tableScope.lookup(tempA.get(tempA.size()-1).getVal());
						printType = verifyType(info);
						file.write(c+printType+"\\n"+c+", "+tempA.get(tempA.size()-1).getVal()+"); \n ");
					} else {
						file.write(tempA.get(tempA.size()-1).getVal()+"); \n");				
					}
				}
				stack.push(tableScope);
			}

		}catch(Exception e){
			throw new Exception("To C converter: Conversione non consentita! Cambia la tua write con meno argomenti.");
		}

		//Distingui da stringhe e =! da stringhe
		assignOp = "";
		return null;
	}

	@Override
	public Object visit(ReadOp node) throws Exception {
		assignOp = "readOp";
		tempVar = new ArrayList<String>();
		char c = '"';
		List<Node> nodes = node.nodeList();
		for (Node n : nodes){
			if (n!=null){
				n.accept(this);
			}
		}
		String scanf = "";
		//System.out.println(stack.size());
		try {
			if (stack.size() == 2) {
				tableScope = stack.pop();
				for (int i = 0; i < tempVar.size(); i++) {
					Info info = tableScope.lookup(tempVar.get(i));
					if (info == null) {
						tableScope2 = stack.pop();
						Info info1 = tableScope2.lookup(tempVar.get(i));
						scanf = verifyType(info1);
						stack.push(tableScope2);
					}else {
						scanf = verifyType(info);
					}
					file.write("scanf(" + c + scanf + c + ", &" + tempVar.get(i)+ ");\n");
				}
				stack.push(tableScope);
			}else {
				tableScope = stack.pop();
				for (int i = 0; i < tempVar.size(); i++) {
					Info info = tableScope.lookup(tempVar.get(i));
					scanf = verifyType(info);
					file.write("scanf(" + c + scanf + c + ", &" + tempVar.get(i) + ");\n");
				}
				stack.push(tableScope);
			}
		}catch(Exception e){
			throw new Exception("To C converter: \n Qualcosa è andato storto durante la conversione su READ:");
		}

		assignOp = "";
		return null;
	}
	public String verifyType(Info info) {

		switch (info.getType()) {
		case "int":
			return "%d";

		case "double":
			return "%lf";

		case "char":
			return "%c";

		case "bool":
			return "%d";

		case "string":
			return "%s";

		default:
			return "no-type";
		}
	}

	@Override
	public Object visit(DeclOp node) throws Exception {
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
	@Override
	public Object visit(CondOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}
	@Override
	public Object visit(ArgsOp node) throws Exception {
		return this.visit((NodeOp) node);
	}
	@Override
	public Object visit(VarsOp node) throws Exception {
		// TODO Auto-generated method stub
		return this.visit((NodeOp) node);
	}
	@Override
	public Object visit(Node node) {
		// TODO Auto-generated method stub
		return null;
	}
}
