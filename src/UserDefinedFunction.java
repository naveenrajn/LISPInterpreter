import java.util.ArrayList;


public class UserDefinedFunction {

	private String functionName;
	private ArrayList<String> parameterNames;
	private SExpression functionBody;
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public ArrayList<String> getParameterNames() {
		return parameterNames;
	}
	public void setParameterNames(ArrayList<String> parameterList) {
		this.parameterNames = parameterList;
	}
	public SExpression getFunctionBody() {
		return functionBody;
	}
	public void setFunctionBody(SExpression functionBody) {
		this.functionBody = functionBody;
	}
	
}
