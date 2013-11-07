import java.util.HashMap;
import java.util.Map;

public class Interpreter {

	public static InbuiltFunctions inbuiltFunctions = new InbuiltFunctions();
	public static ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

	public static Map<String, UserDefinedFunction> userDefinedFunctions = new HashMap<String, UserDefinedFunction>();

	public static void main(String[] args) throws Exception {
		Parser parser = new Parser();
		try {
			parser.parseAndEvaluate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}