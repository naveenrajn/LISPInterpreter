import java.util.HashSet;
import java.util.Map;
import java.util.Set;


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
				SExpression parameter1 = MyInterpreter.expressionEvaluator.evaluateSExpression(parameterExpressionTree.getLeftChild(), currentParameterList);
				if(parameter1 instanceof Atom && ((Atom) parameter1).isNumericAtom()) { 
					//continue only if the first parameter is an integer
					SExpression parameter2 = MyInterpreter.expressionEvaluator.evaluateSExpression(((ComplexSExpression)parameterExpressionTree.getRightChild()).getLeftChild(), currentParameterList);
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
			ComplexSExpression parameterExpressionTree = (ComplexSExpression) parameterTree;
			if(((ComplexSExpression) parameterExpressionTree.getRightChild()).getRightChild()==null) {
				SExpression parameter1 = MyInterpreter.expressionEvaluator.evaluateSExpression(parameterExpressionTree.getLeftChild(), currentParameterList);
				if(parameter1 instanceof Atom) {
					SExpression parameter2 = MyInterpreter.expressionEvaluator.evaluateSExpression(((ComplexSExpression)parameterExpressionTree.getRightChild()).getLeftChild(), currentParameterList);
					if(parameter2 instanceof Atom) {
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
			ComplexSExpression parameterExpressionTree = (ComplexSExpression) parameterTree;
			if(((ComplexSExpression) parameterExpressionTree.getRightChild()).getRightChild()==null) {
				SExpression parameter1 = MyInterpreter.expressionEvaluator.evaluateSExpression(parameterExpressionTree.getLeftChild(), currentParameterList);
				SExpression parameter2 = MyInterpreter.expressionEvaluator.evaluateSExpression(((ComplexSExpression)parameterExpressionTree.getRightChild()).getLeftChild(), currentParameterList);
				if(parameter1 instanceof ComplexSExpression && ((ComplexSExpression) parameter1).isList()
						&& parameter2 instanceof ComplexSExpression && ((ComplexSExpression) parameter2).isList()) {
					ComplexSExpression currentNode = (ComplexSExpression) parameter1;
					while(currentNode.getRightChild()!=null) currentNode = (ComplexSExpression) currentNode.getRightChild();
					currentNode.setRightChild(parameter2);
					return parameter1;
				} else {
					//Consolidate with DOT
					ComplexSExpression consolidatedExpression = new ComplexSExpression(parameter1, parameter2);
					if(parameter2 instanceof ComplexSExpression && ((ComplexSExpression) parameter2).isList()) consolidatedExpression.setList(true);
					return consolidatedExpression;
				}
			} else {
				throw new Exception("ERROR: CONS: more than two parameters found");
			}
		} else {
			throw new Exception("ERROR: CONS expects two parameters");
		}
	}
	
	private SExpression quoteExpression(SExpression parameterTree) throws Exception {
		if(parameterTree instanceof ComplexSExpression && ((ComplexSExpression) parameterTree).getLeftChild()!=null) {
			if(((ComplexSExpression) parameterTree).getRightChild()==null) {
				return ((ComplexSExpression) parameterTree).getLeftChild();
			} else {
				throw new Exception("ERROR: QUOTE: more than one parameter found");
			}
		} else {
			throw new Exception("ERROR: QUOTE expects one parameter");
		}
	}

	private SExpression car(SExpression parameterTree, Map<String, SExpression> currentParameterList) throws Exception {
		if(parameterTree instanceof ComplexSExpression && ((ComplexSExpression) parameterTree).getLeftChild()!=null) {
			if(((ComplexSExpression) parameterTree).getRightChild()==null) {
				SExpression evaluatedParameter = MyInterpreter.expressionEvaluator.evaluateSExpression(((ComplexSExpression) parameterTree).getLeftChild(), currentParameterList);
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
	
	private SExpression cdr(SExpression parameterTree, Map<String, SExpression> currentParameterList) throws Exception {
		if(parameterTree instanceof ComplexSExpression && ((ComplexSExpression) parameterTree).getLeftChild()!=null) {
			if(((ComplexSExpression) parameterTree).getRightChild()==null) {
				SExpression evaluatedParameter = MyInterpreter.expressionEvaluator.evaluateSExpression(((ComplexSExpression) parameterTree).getLeftChild(), currentParameterList);
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

	public boolean isInbuiltFunction(String functionName) {
		if(arithmeticFunctions.contains(functionName) || relationalFunctions.contains(functionName) || otherFunctions.contains(functionName))
			return true;
		return false;
	}
	
	private SExpression evaluateCondition(SExpression parameterTree, Map<String, SExpression> currentParameterList) throws Exception {
		// TODO Auto-generated method stub
		if(parameterTree instanceof ComplexSExpression) {
			ComplexSExpression pendingExpressionPairs = (ComplexSExpression) parameterTree;
			while(pendingExpressionPairs!=null) {
				if(((ComplexSExpression) pendingExpressionPairs).getLeftChild() instanceof ComplexSExpression) {
					ComplexSExpression currentExpressionPair = (ComplexSExpression) ((ComplexSExpression) pendingExpressionPairs).getLeftChild();
					SExpression conditionEvaluationResult = MyInterpreter.expressionEvaluator.evaluateSExpression(currentExpressionPair.getLeftChild(), currentParameterList);
					if(conditionEvaluationResult instanceof Atom && !((Atom) conditionEvaluationResult).getValue().equalsIgnoreCase("NIL")) {
						if(currentExpressionPair.getRightChild() instanceof ComplexSExpression && ((ComplexSExpression)currentExpressionPair.getRightChild()).getLeftChild()!=null) {
							if(((ComplexSExpression)currentExpressionPair.getRightChild()).getRightChild()==null) {
								return MyInterpreter.expressionEvaluator.evaluateSExpression(((ComplexSExpression)currentExpressionPair.getRightChild()).getLeftChild(), currentParameterList);
							} else {
								throw new Exception("ERROR: COND: More than one evaluation expression found for condition: " + commonHelper.sExpressionToString(currentExpressionPair.getLeftChild()));
							}
						} else {
							throw new Exception("ERROR: COND: Invalid evaluation expression: " + commonHelper.sExpressionToString(((ComplexSExpression)currentExpressionPair.getRightChild()).getLeftChild()));
						}
					} else {
						//TODO what if it returns a list or an atom other that 'T' or 'NIL'?
						pendingExpressionPairs = (ComplexSExpression) pendingExpressionPairs.getRightChild();
					}
				} else {
					throw new Exception("ERROR: COND: Invalid parameters. Expression pairs exected in format (BooleanExpression Expression)");
				}
			}
			throw new Exception("ERROR: COND: None of the expressions evaluated to true");
		} else {
			throw new Exception("ERROR: COND: At least one expression pair of the format (BooleanExpression Expression) is expected");
		}
	}
	
	public SExpression applyInbuiltFunction(String functionName, SExpression parameterTree, Map<String, SExpression> currentParameterList) throws Exception {
		if(arithmeticFunctions.contains(functionName) || relationalFunctions.contains(functionName)) {
			return applyInbuiltBinaryIntegerFunction(functionName, parameterTree, currentParameterList);
		} else {
			switch (functionName) {
			case "ATOM":
				if(parameterTree instanceof Atom) return new Atom("T");
				else return new Atom("NIL");
			case "NULL":
				if(parameterTree==null || (parameterTree instanceof ComplexSExpression && ((ComplexSExpression)parameterTree).getLeftChild()==null)) return new Atom("T");
				else return new Atom("NIL");
			case "INT":
				if(parameterTree instanceof Atom && ((Atom) parameterTree).isNumericAtom()) return new Atom("T");
				else return new Atom("NIL");
			case "EQ":
				return isEqual(parameterTree, currentParameterList);
			case "CONS":
				return consolidateExpressions(parameterTree, currentParameterList);
			case "DEFUN":
				return MyInterpreter.inbuiltSpecialFunctions.defineNewFunction(parameterTree);
			case "QUOTE":
				return quoteExpression(parameterTree);
			case "CAR":
				return car(parameterTree, currentParameterList);
			case "CDR":
				return cdr(parameterTree, currentParameterList);
			case "COND":
				return evaluateCondition(parameterTree, currentParameterList);
			default:
				throw new Exception("ERROR: Unsupported inbuilt function"); //TODO reword or remove
			}
		}
	}

}