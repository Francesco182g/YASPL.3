package Semantic;

import java.util.ArrayList;

public class MethodDefinition {
	private String nameMethod;
	private ArrayList<String> tipi;
	
	public MethodDefinition() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MethodDefinition(String nameMethod, ArrayList<String> tipi) {
		super();
		this.nameMethod = nameMethod;
		this.tipi = new ArrayList<String>();
	}
	public String getNameMethod() {
		return nameMethod;
	}
	public void setNameMethod(String nameMethod) {
		this.nameMethod = nameMethod;
	}
	public ArrayList<String> getTipi() {
		return tipi;
	}
	public void setTipi(ArrayList<String> tipi) {
		this.tipi = tipi;
	}
	@Override
	public String toString() {
		return "MethodDefinition [nameMethod=" + nameMethod + ", tipi=" + tipi + "]";
	}
	
	
}
