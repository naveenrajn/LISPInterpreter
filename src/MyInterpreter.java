import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class MyInterpreter {

	public static InbuiltFunctions inbuiltFunctions = new InbuiltFunctions();
	public static InbuiltSpecialFunctions inbuiltSpecialFunctions = new InbuiltSpecialFunctions();
	public static ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

	public static Map<String, UserDefinedFunction> userDefinedFunctions = new HashMap<String, UserDefinedFunction>();

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String filePath = "C:\\Users\\Naveenraj\\Desktop\\Lisp_Input.txt";
		Tokenizer tokenizer;

		tokenizer = new Tokenizer("(DEFUN NOTSOSILLY (A B) (COND((EQ A 0) (PLUS B 1))((EQ B 0) (NOTSOSILLY (MINUS A 1) 1))(T (NOTSOSILLY (MINUS A 1) (NOTSOSILLY A (MINUS B 1))))))(NOTSOSILLY 2 4)(NOTSOSILLY 3 5)(NOTSOSILLY 0 0)");
		new Parser(tokenizer.getTokens()).parseSExpression();
	}

}