package Semantic;

public class Info {
	private String type;
	private String context;


	public Info(String type, String context) {
		super();
		this.type = type;
		this.context = context;
	}

	@Override
	public String toString() {
		return "Info [type=" + type + ", context=" + context + "]";
	}

	public Info() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

}
