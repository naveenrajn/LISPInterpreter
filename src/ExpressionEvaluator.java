import java.util.HashMap;
import java.util.Map;

/**
 * Evaluates the given SExpression, calls necessary functions as appropriate
 * 
 * @author Naveenraj Nagarathinam(nagarathinam.1@osu.edu)
 *
 */
public class ExpressionEvaluator {
	
	CommonHelper helper = new CommonHelper();
	
	/**
	 * Evaluates the given SExpression. This considers the left child of the root as the function name and the right child as its parameters
	 * 
	 * @param sExpression - root of the SExpression tree
	 * @param parameterList - key value pairs of program variables passed on from previous call in the stack
	 * @return - returns the resultant SExpression of applying the function(left child) with parameters(right child)
	 * @throws Exception
	 */
	public SExpression evaluateSExpression(SExpression sExpression, Map<String, SExpression> parameterList) throws Exception {
		if(sExpression instanceof Atom) {
			Atom atomExpression = (Atom) sExpression;
			if(atomExpression.getValue().equalsIgnoreCase("T") || atomExpression.getValue().equalsIgnoreCase("NIL") || atomExpression.isNumericAtom())
				return sExpression;
			if(parameterList.containsKey(atomExpression.getValue()))
				return parameterList.get(atomExpression.getValue());
			else
				throw new Exception("ERROR: Variable " + atomExpression.getValue() + " is not defined");
		} else {
			ComplexSExpression currentSExpression = (ComplexSExpression) sExpression;
			if(currentSExpression.getLeftChild()==null) // Input is an empty list. Return the empty list
				return sExpression;
			if(currentSExpression.getLeftChild() instanceof Atom) {
				String functionName = ((Atom) currentSExpression.getLeftChild()).getValue();
				return applyFunction(functionName, currentSExpression.getRightChild(), parameterList);
			} else {
				throw new Exception("ERROR: " + helper.sExpressionToString(currentSExpression.getLeftChild()) + " is not a valid function name");
			}
		}
	}
	
	/**
	 * 
	 * Triggers execution of the intended function
	 * If the called function is an inbuilt function, the inbuilt function is called with the passed on variable list
	 * If the called function is a user defined function, adds the parameters to the variable list and triggers the user defined function
	 * 
	 * @param functionName - name of the called function
	 * @param parameterTree - parameters to the function to be used for computation
	 * @param currentParameterList - variable list passed on from the previous call in the stack
	 * @return - the resultant SExpression of applying the function
	 * @throws Exception
	 */
	private SExpression applyFunction(String functionName, SExpression parameterTree, Map<String, SExpression> currentParameterList) throws Exception {
		if(Interpreter.inbuiltFunctions.isInbuiltFunction(functionName)) {
			return Interpreter.inbuiltFunctions.applyInbuiltFunction(functionName, parameterTree, currentParameterList);
		} else {
			if(Interpreter.userDefinedFunctions.containsKey(functionName)) {
				UserDefinedFunction calledFunction = Interpreter.userDefinedFunctions.get(functionName);
				Map<String, SExpression> newParameterList = addParameters(calledFunction, parameterTree, currentParameterList);
				return evaluateSExpression(calledFunction.getFunctionBody(), newParameterList);
			} else {
				throw new Exception("ERROR: Undefined function " + functionName);
			}
		}
	}
	
	/**
	 * Creates a new parameter list to be used by the nested function
	 * The list contains - values from the current parameter list, added with parameters to the new function call
	 * If recursive call or the nested function uses same parameter name, existing value will be replaced
	 * 
	 * @param calledFunction - name of the called function
	 * @param parameterTree - parameters to the function to be used for computation
	 * @param currentParameterList - variable list passed from the previous call in stack
	 * @return - new key value map containing passed-on as well as new parameters
	 * @throws Exception
	 */
	private Map<String, SExpression> addParameters(UserDefinedFunction calledFunction, SExpression parameterTree, Map<String, SExpression> currentParameterList) throws Exception {
		Map<String, SExpression> newParameterList = new HashMap<>(currentParameterList);
		int expectedParameterCount = calledFunction.getParameterNames().size();
		while(expectedParameterCount>0 && parameterTree instanceof ComplexSExpression) {
			ComplexSExpression currentNode = (ComplexSExpression) parameterTree;
			newParameterList.put(calledFunction.getParameterNames().get(calledFunction.getParameterNames().size()-expectedParameterCount), evaluateSExpression(currentNode.getLeftChild(), currentParameterList));
			parameterTree = currentNode.getRightChild();
			expectedParameterCount--;
		}
		if(expectedParameterCount>0) {
			throw new Exception("ERROR: Missing parameters. Function " + calledFunction.getFunctionName() + " expects " + calledFunction.getParameterNames().size() + " parameters");
		}
		if(parameterTree!=null) {
			throw new Exception("ERROR: Too many parameters. Function " + calledFunction.getFunctionName() + " expects only " + calledFunction.getParameterNames().size() + " parameters");
		}
		return newParameterList;
	}
	
}