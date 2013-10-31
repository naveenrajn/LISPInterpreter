public class CommonHelper {

	/**
	 * Prints the SExpression to the console
	 * @param sExpression
	 */
	public void printResult(SExpression sExpression) {
		System.out.println(sExpressionToString(sExpression));
	}

	public String sExpressionToString(SExpression sExpression) {
		StringBuilder sExpressionString = new StringBuilder("");
		if(sExpression instanceof Atom) {
			sExpressionString.append(((Atom) sExpression).getValue());
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