package Semantic;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/*
 * nameTable = nome della tabella dei simboli
 * HashTable<nome della variabile, tipo della variabile>
 */
public class SymbolTableScope {
	private String nameTable;
	private Hashtable<String, Info> symScopeTable = new Hashtable<>();

	public SymbolTableScope(String nameTable) {
		super();
		this.nameTable = nameTable;
		symScopeTable = new Hashtable<String, Info>();
	}

	public void addId(String nameVar, Info info) throws Exception{
		if (!symScopeTable.containsKey(nameVar)){
			symScopeTable.put(nameVar, info);
		}
		else{
			throw new Exception("SEMANTIC ERROR: Variabile già dichiarata: " +nameVar);
		}
	}

	public Info lookup(String nameVar) {
		Info i = null;
		if (symScopeTable.containsKey(nameVar)) {
			i = symScopeTable.get(nameVar);
			
		} else {
		System.out.println("Not Found: " + nameVar);
		i = new Info("null","null");
		}
		return i;
	}

	public String getNameTable() {
		return nameTable;
	}
	public void setNameTable(String nameTable) {
		this.nameTable = nameTable;
	}
	public Hashtable<String, Info> getSymScopeTable() {
		return symScopeTable;
	}
	
	/*
	 * Metodo per testare se stampa la hashmap
	 */
	public void printSymbolTable() {
		System.out.println("\n NOME TABELLA: " + this.getNameTable() );
		Set<?> set = symScopeTable.entrySet();
		Iterator<?> iterator = set.iterator();
		while(iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry)iterator.next();
			System.out.println("\n");
			System.out.print("Nome: "+ mentry.getKey() + "");
			Info info = (Info) mentry.getValue();
			System.out.print(" Tipo:  "+info.getType() + " ");
			System.out.print("Context: "+info.getContext() + "\n");
		}

	}
	
	/*
	 * Metodo per rimuovere un elemento dalla symboltable
	 */
	public void removeElement(String elemento) {
		symScopeTable.remove(elemento);
		}
	
}

