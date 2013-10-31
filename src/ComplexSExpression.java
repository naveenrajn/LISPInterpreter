
public class ComplexSExpression extends SExpression {

	private SExpression leftChild;
	private SExpression rightChild;
	private boolean isList;
	
	public ComplexSExpression() {
		
	}
	
	public ComplexSExpression(SExpression leftChild, SExpression rightChild) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
	
	public SExpression getLeftChild() {
		return leftChild;
	}
	public void setLeftChild(SExpression leftChild) {
		this.leftChild = leftChild;
	}
	public SExpression getRightChild() {
		return rightChild;
	}
	public void setRightChild(SExpression rightChild) {
		this.rightChild = rightChild;
	}
	public boolean isList() {
		return isList;
	}
	public void setList(boolean isList) {
		this.isList = isList;
	}
	
}
