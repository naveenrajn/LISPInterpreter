import java.util.ArrayList;
import java.util.Map;

/**
 * Inbuilt functions: DEFUN, QUOTE, COND
 * 
 * @author Naveenraj Nagarathinam(nagarathinam.1@osu.edu)
 *
 */
public class InbuiltSpecialFunctions {
	
	private CommonHelper commonHelper = new CommonHelper();
	
	/**
	 * Adds a new function to the user defined functions list
	 * Accepts only function name, parameter list and function body
	 * 
	 * @param sExpression
	 * @return
	 * @throws Exception
	 */
	public SExpression defineNewFunction(SExpression sExpression) throws Exception {
		UserDefinedFunction newFunction = new UserDefinedFunction();
		
		SExpression currentExpressionRoot = sExpression;
		//Process function name - check for validity and populate the object
		if(sExpression instanceof ComplexSExpression) {
			if(((ComplexSExpression) sExpression).getLeftChild() instanceof Atom && ((Atom)((ComplexSExpression) sExpression).getLeftChild()).isLiteralAtom()) {
				newFunction.setFunctionName(((Atom)((ComplexSExpression) sExpression).getLeftChild()).getValue());
				if(newFunction.getFunctionName().equalsIgnoreCase("T") || newFunction.getFunctionName().equalsIgnoreCase("NIL")
						|| Interpreter.inbuiltFunctions.isInbuiltFunction(newFunction.getFunctionName())) {
					throw new Exception("ERROR: DEFUN: " + newFunction.getFunctionName() + " is a reserved keyword and is not allowed as a function name");
				}
			} else {
				throw new Exception("ERROR: DEFUN: " + commonHelper.sExpressionToString(((ComplexSExpression) sExpression).getLeftChild()) + " is not a valid function name");
			}
		} else {
			throw new Exception("ERROR: DEFUN missing function name");
		}
		
		//Process formals - check for validity, multiplicity and populate the object
		currentExpressionRoot = ((ComplexSExpression) currentExpressionRoot).getRightChild();
		if(currentExpressionRoot instanceof ComplexSExpression && ((ComplexSExpression) currentExpressionRoot).getLeftChild() instanceof ComplexSExpression) {
			ComplexSExpression formalsTree = (ComplexSExpression) ((ComplexSExpression) currentExpressionRoot).getLeftChild();
			if(formalsTree.isList()) {
				ArrayList<String> formalsList = new ArrayList<String>();
				while(formalsTree!=null) {
					if(formalsTree.getLeftChild() instanceof Atom) {
						String formalName = ((Atom) formalsTree.getLeftChild()).getValue();
						if(!formalName.equalsIgnoreCase("T") && !formalName.equalsIgnoreCase("NIL")) {
							if(!formalsList.contains(formalName)) {
								formalsList.add(formalName);
								formalsTree = (ComplexSExpression) formalsTree.getRightChild();
							} else {
								throw new Exception("ERROR: DEFUN: Multiple usage of the formal name " + formalName);
							}
						} else {
							throw new Exception("ERROR: DEFUN: " + formalName + " is a reserved keyword and is not allowed as a formal name");
						}
					} else {
						throw new Exception("ERROR: DEFUN: " + commonHelper.sExpressionToString(formalsTree.getLeftChild()) + " is an invalid formal name");
					}
				}
				newFunction.setParameterNames(formalsList);
			} else {
				throw new Exception("ERROR: DEFUN: Invalid formals list");
			}
		} else {
			throw new Exception("ERROR: DEFUN: Invalid formals list");
		}
		
		//Process function body
		currentExpressionRoot = ((ComplexSExpression) currentExpressionRoot).getRightChild();
		if(currentExpressionRoot instanceof ComplexSExpression && ((ComplexSExpression) currentExpressionRoot).getLeftChild() instanceof ComplexSExpression) {
			ComplexSExpression functionBodyTree = (ComplexSExpression) ((ComplexSExpression) currentExpressionRoot).getLeftChild();
			newFunction.setFunctionBody(functionBodyTree);
		} else {
			throw new Exception("ERROR: DEFUN: Function body invalid or missing");
		}
		
		//Check for end of list
		currentExpressionRoot = ((ComplexSExpression) currentExpressionRoot).getRightChild();
		if(currentExpressionRoot!=null) {
			throw new Exception("ERROR: DEFUN: Invalid definition. Definition should contain only name, formals and function body");
		}
		
		Interpreter.userDefinedFunctions.put(newFunction.getFunctionName(), newFunction);
		return new Atom(newFunction.getFunctionName());
	}
	
	/**
	 * Applies COND function logic - evaluates conditions one by one and evaluates the corresponding expression
	 * 
	 * @param parameterTree - root of the parameter tree to be processed - containing pairs of boolean expressions and SExpressions
	 * @param currentParameterList - parameter list passed on from the previous call in stack
	 * @return - result of evaluation of the expression for which the corresponding boolean expression evaluates to true
	 * @throws Exception
	 */
	public SExpression evaluateCondition(SExpression parameterTree, Map<String, SExpression> currentParameterList) throws Exception {
		if(parameterTree instanceof ComplexSExpression) {
			ComplexSExpression pendingExpressionPairs = (ComplexSExpression) parameterTree;
			while(pendingExpressionPairs!=null) {
				if(((ComplexSExpression) pendingExpressionPairs).getLeftChild() instanceof ComplexSExpression) {
					//First expression should be a function call to check for condition
					ComplexSExpression currentExpressionPair = (ComplexSExpression) ((ComplexSExpression) pendingExpressionPairs).getLeftChild();
					SExpression conditionEvaluationResult = Interpreter.expressionEvaluator.evaluateSExpression(currentExpressionPair.getLeftChild(), currentParameterList);
					if(conditionEvaluationResult instanceof Atom && !((Atom) conditionEvaluationResult).getValue().equalsIgnoreCase("NIL")) {
						//condition satisfied - evaluate the expression and return the result
						if(currentExpressionPair.getRightChild() instanceof ComplexSExpression && ((ComplexSExpression)currentExpressionPair.getRightChild()).getLeftChild()!=null) {
							if(((ComplexSExpression)currentExpressionPair.getRightChild()).getRightChild()==null) {
								return Interpreter.expressionEvaluator.evaluateSExpression(((ComplexSExpression)currentExpressionPair.getRightChild()).getLeftChild(), currentParameterList);
							} else {
								throw new Exception("ERROR: COND: More than one evaluation expression found for condition: " + commonHelper.sExpressionToString(currentExpressionPair.getLeftChild()));
							}
						} else {
							throw new Exception("ERROR: COND: Invalid evaluation expression: " + commonHelper.sExpressionToString(((ComplexSExpression)currentExpressionPair.getRightChild()).getLeftChild()));
						}
					} else {
						//condition not satisfied - move on to evaluate the next condition
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
	
	/**
	 * Returns the parameter to the QUOTE call as-is
	 * 
	 * @param parameterTree - root of the parameter tree to be returned
	 * @return - the parameter tree as-is
	 * @throws Exception
	 */
	public SExpression quoteExpression(SExpression parameterTree) throws Exception {
		if(parameterTree instanceof ComplexSExpression && ((ComplexSExpression) parameterTree).getLeftChild()!=null) {
			if(((ComplexSExpression) parameterTree).getRightChild()==null) {
				//return the first and only parameter
				return ((ComplexSExpression) parameterTree).getLeftChild();
			} else {
				throw new Exception("ERROR: QUOTE: more than one parameter found");
			}
		} else {
			throw new Exception("ERROR: QUOTE expects one parameter");
		}
	}
	
}