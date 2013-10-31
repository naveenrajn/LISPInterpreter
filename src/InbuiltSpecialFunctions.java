import java.util.ArrayList;

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
						|| MyInterpreter.inbuiltFunctions.isInbuiltFunction(newFunction.getFunctionName())) {
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
		
		MyInterpreter.userDefinedFunctions.put(newFunction.getFunctionName(), newFunction);
		return new Atom(newFunction.getFunctionName());
	}
}