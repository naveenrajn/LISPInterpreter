import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * LISP inbuilt functions
 * 
 * @author Naveenraj Nagarathinam(nagarathinam.1@osu.edu)
 *
 */
public class InbuiltFunctions {

	private Set<String> arithmeticFunctions;
	private Set<String> relationalFunctions;
	private Set<String> otherFunctions;
	CommonHelper commonHelper = new CommonHelper();

	public InbuiltFunctions() {
		arithmeticFunctions = new HashSet<String>();
		arithmeticFunctions.add("PLUS");
		arithmeticFunctions.add("MINUS");
		arithmeticFunctions.add("TIMES");
		arithmeticFunctions.add("QUOTIENT");
		arithmeticFunctions.add("REMAINDER");

		relationalFunctions = new HashSet<String>();
		relationalFunctions.add("LESS");
		relationalFunctions.add("GREATER");

		otherFunctions = new HashSet<String>();
		otherFunctions.add("CAR");
		otherFunctions.add("CDR");
		otherFunctions.add("CONS");
		otherFunctions.add("ATOM");
		otherFunctions.add("EQ");
		otherFunctions.add("NULL");
		otherFunctions.add("INT");
		otherFunctions.add("COND");
		otherFunctions.add("QUOTE");
		otherFunctions.add("DEFUN");
	}

	/**
	 * Applies an inbuilt function that expects two integers as argument - example PLUS, LESS
	 * 
	 * @param functionName - name of the inbuilt function
	 * @param parameterTree - SExpression root of the tree containing parameters to the function
	 * @param currentParameterList - key value pairs of program variables passed on from previous call in the stack
	 * @return - the resultant atom of applying the function on the parameters
	 * @throws Exception
	 */
	private Atom applyInbuiltBinaryIntegerFunction(String functionName, SExpression parameterTree, Map<String, SExpression> currentParameterList) throws Exception {
		if(parameterTree instanceof ComplexSExpression && ((ComplexSExpression) parameterTree).getLeftChild()!=null 
				&& ((ComplexSExpression) parameterTree).getRightChild() instanceof ComplexSExpression && 
				((ComplexSExpression) ((ComplexSExpression) parameterTree).getRightChild()).getLeftChild()!=null) {
			//continue only if two parameters are provided
			ComplexSExpression parameterExpressionTree = (ComplexSExpression) parameterTree;
			if(((ComplexSExpression) parameterExpressionTree.getRightChild()).getRightChild()==null) { 
				//continue only if exactly two parameters are provided
				SExpression parameter1 = Interpreter.expressionEvaluator.evaluateSExpression(parameterExpressionTree.getLeftChild(), currentParameterList);
				if(parameter1 instanceof Atom && ((Atom) parameter1).isNumericAtom()) {
					//continue only if the first parameter is an integer
					SExpression parameter2 = Interpreter.expressionEvaluator.evaluateSExpression(((ComplexSExpression)parameterExpressionTree.getRightChild()).getLeftChild(), currentParameterList);
					if(parameter2 instanceof Atom && ((Atom) parameter2).isNumericAtom()) {
						//continue only if the second parameter is a integer
						Atom result = null;
						Integer arithmeticResult;
						switch (functionName) {
						case "PLUS":
							arithmeticResult = Integer.parseInt(((Atom) parameter1).getValue()) + Integer.parseInt(((Atom) parameter2).getValue());
							result = new Atom(arithmeticResult.toString());
							break;
						case "MINUS":
							arithmeticResult = Integer.parseInt(((Atom) parameter1).getValue()) - Integer.parseInt(((Atom) parameter2).getValue());
							result = new Atom(arithmeticResult.toString());
							break;
						case "TIMES":
							arithmeticResult = Integer.parseInt(((Atom) parameter1).getValue()) * Integer.parseInt(((Atom) parameter2).getValue());
							result = new Atom(arithmeticResult.toString());
							break;
						case "QUOTIENT":
							arithmeticResult = Integer.parseInt(((Atom) parameter1).getValue()) / Integer.parseInt(((Atom) parameter2).getValue());
							result = new Atom(arithmeticResult.toString());
							break;
						case "REMAINDER":
							arithmeticResult = Integer.parseInt(((Atom) parameter1).getValue()) % Integer.parseInt(((Atom) parameter2).getValue());
							result = new Atom(arithmeticResult.toString());
							break;
						case "LESS":
							if(Integer.parseInt(((Atom) parameter1).getValue()) < Integer.parseInt(((Atom) parameter2).getValue())) result = new Atom("T");
							else result = new Atom("NIL");
							break;
						case "GREATER":
							if(Integer.parseInt(((Atom) parameter1).getValue()) > Integer.parseInt(((Atom) parameter2).getValue())) result = new Atom("T");
							else result = new Atom("NIL");
							break;
						}
						return result;
					} else {
						throw new Exception("ERROR: " +functionName + ": Parameter2 " + commonHelper.sExpressionToString(parameter2) + " is not a number");
					}
				} else {
					throw new Exception("ERROR: " +functionName + ": Parameter1 " + commonHelper.sExpressionToString(parameter1) + " is not a number");
				}
			} else {
				throw new Exception("ERROR: " +functionName + ": more than two parameters found");
			}
		} else {
			throw new Exception("ERROR: " +functionName + " expects two parameters");
		}
	}

	/**
	 * Applies the EQ function logic on the provided parameter tree
	 *  
	 * @param parameterTree - SExpression root of the tree containing parameters to the function
	 * @param currentParameterList - key value pairs of program variables passed on from previous call in the stack
	 * @return - the resultant atom of applying the function on the parameters
	 * @throws Exception
	 */
	private Atom isEqual(SExpression parameterTree, Map<String, SExpression> currentParameterList) throws Exception {
		if(parameterTree instanceof ComplexSExpression && ((ComplexSExpression) parameterTree).getLeftChild()!=null 
				&& ((ComplexSExpression) parameterTree).getRightChild() instanceof ComplexSExpression && 
				((ComplexSExpression) ((ComplexSExpression) parameterTree).getRightChild()).getLeftChild()!=null) {
			//continue only if two parameters are provided
			ComplexSExpression parameterExpressionTree = (ComplexSExpression) parameterTree;
			if(((ComplexSExpression) parameterExpressionTree.getRightChild()).getRightChild()==null) {
				//continue only if exactly two parameters are provided
				SExpression parameter1 = Interpreter.expressionEvaluator.evaluateSExpression(parameterExpressionTree.getLeftChild(), currentParameterList);
				if(parameter1 instanceof Atom) {
					//continue only if the first parameter is an Atom
					SExpression parameter2 = Interpreter.expressionEvaluator.evaluateSExpression(((ComplexSExpression)parameterExpressionTree.getRightChild()).getLeftChild(), currentParameterList);
					if(parameter2 instanceof Atom) {
						//continue only if the second parameter is an Atom
						if(((Atom) parameter1).isNumericAtom() && ((Atom) parameter2).isNumericAtom()) {
							if(Integer.parseInt(((Atom) parameter1).getValue()) == Integer.parseInt(((Atom) parameter2).getValue())) return new Atom("T");
						} else if(((Atom) parameter1).isLiteralAtom() && ((Atom) parameter2).isLiteralAtom()) {
							if(((Atom) parameter1).getValue().equalsIgnoreCase(((Atom) parameter2).getValue())) return new Atom("T");
						}
						return new Atom("NIL");
					} else {
						throw new Exception("ERROR: EQ: Parameter2 " + commonHelper.sExpressionToString(parameter2) + " is not an atom");
					}
				} else {
					throw new Exception("ERROR: EQ: Parameter1 " + commonHelper.sExpressionToString(parameter1) + " is not an atom");
				}
			} else {
				throw new Exception("ERROR: EQ: more than two parameters found");
			}
		} else {
			throw new Exception("ERROR: EQ expects two parameters");
		}
	}

	/**
	 * Applies the CONS function logic on the provided parameter tree
	 *  
	 * @param parameterTree - SExpression root of the tree containing parameters to the function
	 * @param currentParameterList - key value pairs of program variables passed on from previous call in the stack
	 * @return - the resultant SExpression of applying the function on the parameters
	 * @throws Exception
	 */
	private SExpression consolidateExpressions(SExpression parameterTree, Map<String, SExpression> currentParameterList) throws Exception {
		if(parameterTree instanceof ComplexSExpression && ((ComplexSExpression) parameterTree).getLeftChild()!=null 
				&& ((ComplexSExpression) parameterTree).getRightChild() instanceof ComplexSExpression && 
				((ComplexSExpression) ((ComplexSExpression) parameterTree).getRightChild()).getLeftChild()!=null) {
			//continue only if two parameters are provided
			ComplexSExpression parameterExpressionTree = (ComplexSExpression) parameterTree;
			if(((ComplexSExpression) parameterExpressionTree.getRightChild()).getRightChild()==null) {
				//continue only if exactly two parameters are provided
				SExpression parameter1 = Interpreter.expressionEvaluator.evaluateSExpression(parameterExpressionTree.getLeftChild(), currentParameterList);
				SExpression parameter2 = Interpreter.expressionEvaluator.evaluateSExpression(((ComplexSExpression)parameterExpressionTree.getRightChild()).getLeftChild(), currentParameterList);
				if(parameter1 instanceof ComplexSExpression && ((ComplexSExpression) parameter1).isList()
						&& parameter2 instanceof ComplexSExpression && ((ComplexSExpression) parameter2).isList()) {
					//both parameters are list - concatenate the list by adding the root of second list as the last element of the first list
					ComplexSExpression currentNode = (ComplexSExpression) parameter1;
					while(currentNode.getRightChild()!=null) currentNode = (ComplexSExpression) currentNode.getRightChild();
					currentNode.setRightChild(parameter2);
					return parameter1;
				} else if(parameter2 instanceof Atom && ((Atom) parameter2).getValue().equalsIgnoreCase("NIL")) {
					ComplexSExpression consolidatedExpression = new ComplexSExpression(parameter1, null);
					consolidatedExpression.setList(true);
					return consolidatedExpression;
				} else {
					//if not, consolidate with DOT
					ComplexSExpression consolidatedExpression = new ComplexSExpression(parameter1, parameter2);
					if(parameter2 instanceof ComplexSExpression && ((ComplexSExpression) parameter2).isList())
						consolidatedExpression.setList(true);
					return consolidatedExpression;
				}
			} else {
				throw new Exception("ERROR: CONS: more than two parameters found");
			}
		} else {
			throw new Exception("ERROR: CONS expects two parameters");
		}
	}

	/**
	 * Applies the CAR function logic on the provided parameter tree
	 *  
	 * @param parameterTree - SExpression root of the tree containing parameters to the function
	 * @param currentParameterList - key value pairs of program variables passed on from previous call in the stack
	 * @return - the resultant SExpression of applying the function on the parameters
	 * @throws Exception
	 */
	private SExpression car(SExpression parameterTree, Map<String, SExpression> currentParameterList) throws Exception {
		if(parameterTree instanceof ComplexSExpression && ((ComplexSExpression) parameterTree).getLeftChild()!=null) {
			if(((ComplexSExpression) parameterTree).getRightChild()==null) {
				//continue only if exactly one parameter is provided
				SExpression evaluatedParameter = Interpreter.expressionEvaluator.evaluateSExpression(((ComplexSExpression) parameterTree).getLeftChild(), currentParameterList);
				if(evaluatedParameter instanceof ComplexSExpression) {
					return ((ComplexSExpression) evaluatedParameter).getLeftChild();
				} else {
					throw new Exception("ERROR: CAR: Input is not a list: " + commonHelper.sExpressionToString(evaluatedParameter));
				}
			} else {
				throw new Exception("ERROR: CAR: more than one parameter found");
			}
		} else {
			throw new Exception("ERROR: CAR expects one parameter");
		}
	}

	/**
	 * Applies the CDR function logic on the provided parameter tree
	 *  
	 * @param parameterTree - SExpression root of the tree containing parameters to the function
	 * @param currentParameterList - key value pairs of program variables passed on from previous call in the stack
	 * @return - the resultant SExpression of applying the function on the parameters
	 * @throws Exception
	 */
	private SExpression cdr(SExpression parameterTree, Map<String, SExpression> currentParameterList) throws Exception {
		if(parameterTree instanceof ComplexSExpression && ((ComplexSExpression) parameterTree).getLeftChild()!=null) {
			if(((ComplexSExpression) parameterTree).getRightChild()==null) {
				//continue only if exactly one parameter is provided
				SExpression evaluatedParameter = Interpreter.expressionEvaluator.evaluateSExpression(((ComplexSExpression) parameterTree).getLeftChild(), currentParameterList);
				if(evaluatedParameter instanceof ComplexSExpression) {
					return ((ComplexSExpression) evaluatedParameter).getRightChild();
				} else {
					throw new Exception("ERROR: CDR: Input is not a list: " + commonHelper.sExpressionToString(evaluatedParameter));
				}
			} else {
				throw new Exception("ERROR: CDR: more than one parameter found");
			}
		} else {
			throw new Exception("ERROR: CDR expects one parameter");
		}
	}

	/**
	 * Applies an inbuilt function that expects one parameter as argument - NULL, ATOM, INT
	 * 
	 * @param functionName - name of the inbuilt function
	 * @param parameterTree - SExpression root of the tree containing parameters to the function
	 * @param currentParameterList - key value pairs of program variables passed on from previous call in the stack
	 * @return - the resultant atom of applying the function on the parameters
	 * @throws Exception
	 */
	private Atom applyInbuiltUnaryBooleanFunction(String functionName, SExpression parameterTree, Map<String, SExpression> currentParameterList) throws Exception {
		if(parameterTree instanceof ComplexSExpression && ((ComplexSExpression) parameterTree).getLeftChild()!=null) {
			if(((ComplexSExpression) parameterTree).getRightChild()==null) {
				//continue only if exactly one parameter is provided
				SExpression evaluatedParameter = Interpreter.expressionEvaluator.evaluateSExpression(((ComplexSExpression) parameterTree).getLeftChild(), currentParameterList);
				switch (functionName) {
				case "NULL":
					if(evaluatedParameter instanceof ComplexSExpression && ((ComplexSExpression) evaluatedParameter).getLeftChild() ==null)
						return new Atom("T");
					if(evaluatedParameter instanceof Atom && ((Atom) evaluatedParameter).getValue().equalsIgnoreCase("NIL"))
						return new Atom("T");
					if(evaluatedParameter == null)
						return new Atom("T");
					return new Atom("NIL");
				case "ATOM":
					if(evaluatedParameter instanceof Atom) return new Atom("T");
					return new Atom("NIL");
				case "INT":
					if(evaluatedParameter instanceof Atom && ((Atom) evaluatedParameter).isNumericAtom()) return new Atom("T");
					return new Atom("NIL");
				}
				throw new Exception("ERROR: Unsupported inbuilt function");
			} else {
				throw new Exception("ERROR: NULL: more than one parameter found");
			}
		} else {
			throw new Exception("ERROR: NULL expects one parameter");
		}
	}

	/**
	 * Calls the appropriate function based on the called function
	 *  
	 * @param functionName - name of the inbuilt function
	 * @param parameterTree - SExpression root of the tree containing parameters to the function
	 * @param currentParameterList - key value pairs of program variables passed on from previous call in the stack
	 * @return - the resultant SExpression of applying the function on the parameters
	 * @throws Exception
	 */
	public SExpression applyInbuiltFunction(String functionName, SExpression parameterTree, Map<String, SExpression> currentParameterList) throws Exception {
		if(arithmeticFunctions.contains(functionName) || relationalFunctions.contains(functionName)) {
			return applyInbuiltBinaryIntegerFunction(functionName, parameterTree, currentParameterList);
		} else {
			switch (functionName) {
			case "ATOM":
			case "NULL":
			case "INT":
				return applyInbuiltUnaryBooleanFunction(functionName, parameterTree, currentParameterList);
			case "EQ":
				return isEqual(parameterTree, currentParameterList);
			case "CONS":
				return consolidateExpressions(parameterTree, currentParameterList);
			case "CAR":
				return car(parameterTree, currentParameterList);
			case "CDR":
				return cdr(parameterTree, currentParameterList);
			case "COND":
				return new InbuiltSpecialFunctions().evaluateCondition(parameterTree, currentParameterList);
			case "DEFUN":
				return new InbuiltSpecialFunctions().defineNewFunction(parameterTree);
			case "QUOTE":
				return new InbuiltSpecialFunctions().quoteExpression(parameterTree);
			default:
				throw new Exception("ERROR: Unsupported inbuilt function"); //TODO reword or remove
			}
		}
	}

	/**
	 * Verifies if the given function name is an inbuilt function
	 * 
	 * @param functionName - name of the function called
	 * @return - true if inbuilt function, false if not
	 */
	public boolean isInbuiltFunction(String functionName) {
		if(arithmeticFunctions.contains(functionName) || relationalFunctions.contains(functionName) || otherFunctions.contains(functionName))
			return true;
		return false;
	}

}