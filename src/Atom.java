
public class Atom extends SExpression {

	private String value;
	
	public Atom(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isLiteralAtom() {
		if(value.matches("^[a-zA-Z][a-zA-Z0-9]*")) return true;
		else return false;
	}

	public boolean isNumericAtom() {
		if(value.matches("^[+|-]?[0-9]+")) return true;
		else return false;
	}
	
}
