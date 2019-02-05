package ToC;

public class VarDeclaration {
	private String id;
	private String val;
	
	public VarDeclaration() {
		super();
		val = "";
		// TODO Auto-generated constructor stub
	}
	public VarDeclaration(String id, String val) {
		super();
		this.id = id;
		this.val = val;
	}
	@Override
	public String toString() {
		return "VarDeclaration [id=" + id + ", val=" + val + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	
	
}
