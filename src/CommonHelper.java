
/**
 * Helper functions to traverse and print the SExpression tree
 * 
 * @author Naveenraj Nagarathinam(nagarathinam.1@osu.edu)
 *
 */
public class CommonHelper {

	/**
	 * Prints the SExpression to standard output
	 * 
	 * @param sExpression - root of the SExpression tree to be printed
	 */
	public void printResult(SExpression sExpression) {
		System.out.println(sExpressionToString(sExpression));
	}

	/**
	 * Decodes the given SExpression to string in list or DOT notation as appropriate
	 * 
	 * @param sExpression - root of the SExpression tree to be printed
	 * @return - converted SExpression as String
	 */
	public String sExpressionToString(SExpression sExpression) {
		StringBuilder sExpressionString = new StringBuilder("");
		if(sExpression instanceof Atom) {
			if(((Atom) sExpression).isNumericAtom()) sExpressionString.append(Integer.parseInt(((Atom) sExpression).getValue())); 
			else sExpressionString.append(((Atom) sExpression).getValue());
		} else if(sExpression instanceof ComplexSExpression) {
			ComplexSExpression currentExpression = (ComplexSExpression) sExpression;
			if(currentExpression.isList()) {
				if(currentExpression.getLeftChild()==null && currentExpression.getRightChild()==null) {
					sExpressionString.append("NIL");
				} else {
					sExpressionString.append("(");
					boolean firstElement = true;
					while(currentExpression!=null) {
						if(!firstElement) sExpressionString.append(" ");
						firstElement = false;
						sExpressionString.append(sExpressionToString(currentExpression.getLeftChild()));
						currentExpression = (ComplexSExpression) currentExpression.getRightChild();
					}
					sExpressionString.append(")");
				}
			} else {
				sExpressionString.append("(");
				sExpressionString.append(sExpressionToString(currentExpression.getLeftChild()));
				sExpressionString.append(" . ");
				sExpressionString.append(sExpressionToString(currentExpression.getRightChild()));
				sExpressionString.append(")");
			}
		} else if(sExpression == null) {
			sExpressionString.append("NIL");
		}
		return sExpressionString.toString();
	}

}