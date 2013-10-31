import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Parses the given set of tokens
 * looks for the syntactic compliance by matching with the specified LL(1) grammar
 * incrementally builds the SExpression tree and triggers evaluation
 * 
 * @author Naveenraj Nagarathinam(nagarathinam.1@osu.edu)
 *
 */
public class Parser {
	private ArrayList<String> tokens;
	private int lookAheadIndex = 0;

	private SExpression sExpressionTree;
	private Stack<ComplexSExpression> treeTops;
	private boolean isDottedExpression;

	private CommonHelper helper = new CommonHelper();

	public Parser(ArrayList<String> tokens) {
		this.tokens = tokens;
	}

	/**
	 * Triggers parsing with the set of tokens
	 * 
	 * @throws Exception
	 */
	public void parseSExpression() throws Exception {
		try {
			while(lookAheadIndex<tokens.size()) {
				sExpressionTree = null;
				treeTops = new Stack<ComplexSExpression>();
				parseE();
				helper.printResult(sExpressionTree);
				helper.printResult(new ExpressionEvaluator().evaluateSExpression(sExpressionTree, new HashMap<String, SExpression>()));
			}
		} catch(IndexOutOfBoundsException e) {
			throw new Exception("Syntax error. S-expression incomplete");
		} catch (Exception e) {
			throw e;
		}
	}

	private void parseE() throws Exception {
		if(tokens.get(lookAheadIndex).matches("^\\(")) {
			//Matches the production "E=(X"
			match("(");
			ComplexSExpression complexSExpression = new ComplexSExpression();
			if(treeTops.isEmpty()) sExpressionTree = complexSExpression;
			else if (treeTops.peek().getLeftChild()==null) treeTops.peek().setLeftChild(complexSExpression);
			else {
				if(isDottedExpression) {
					treeTops.peek().setRightChild(complexSExpression);
					isDottedExpression = false;
				} else {
					treeTops.peek().setList(true);
					treeTops.peek().setRightChild(complexSExpression);
					treeTops.push(complexSExpression);
					complexSExpression = new ComplexSExpression();
					treeTops.peek().setLeftChild(complexSExpression);
				}
			}
			treeTops.push(complexSExpression);
			parseX();
		} else {
			//Matches the production "E=atom"
			matchAtom();
			Atom atom = new Atom(tokens.get(lookAheadIndex-1));
			if(treeTops.isEmpty()) sExpressionTree = atom; 
			else if(treeTops.peek().getLeftChild()==null) treeTops.peek().setLeftChild(atom);
			else {
				if(isDottedExpression) {
					if(!atom.getValue().equalsIgnoreCase("NIL")) {
						treeTops.peek().setRightChild(atom);
						treeTops.peek().setList(false);
					} else {
						treeTops.peek().setList(true);
					}
					isDottedExpression = false;
				}
				else {
					ComplexSExpression complexSExpression = new ComplexSExpression(atom, null);
					treeTops.peek().setRightChild(complexSExpression);
					treeTops.peek().setList(true);
					treeTops.push(complexSExpression);
				}
			}

		}
	}

	private void parseX() throws Exception {
		if(tokens.get(lookAheadIndex).matches("^\\)")) {
			//Matches the production "X=)"
			match(")");
			backTrackAndComputeListFlag();
		} else {
			//Matches the production "X=EY"
			parseE();
			parseY();
		}
	}

	private void parseY() throws Exception {
		if(tokens.get(lookAheadIndex).matches("^\\.")) {
			//Matches the production "Y=.E)"
			match(".");
			this.isDottedExpression = true;
			parseE();
			match(")");
			backTrackAndComputeListFlag();
		} else {
			//Matches the production "Y=R)"
			parseR();
			match(")");
			backTrackAndComputeListFlag();
		}
	}

	private void parseR() throws Exception {
		try {
			//Matches the production "R=ER"
			parseE();
			parseR();
		} catch(Exception e) {
			//Matches the production "R=empty"
			//The grammar is looking for <E> but found others, which means that <R> may be an empty string or there is an actual syntax errors
			//If it turns out there is an actual syntax error, it will be caught in the next step of the previous call in the stack i.e. in parseY()
			if(!e.getMessage().startsWith("Syntax error")) throw e;
		}
	}

	private void matchAtom() throws Exception {
		if(!tokens.get(lookAheadIndex).matches("^[a-zA-Z][a-zA-Z0-9]*") && !tokens.get(lookAheadIndex).matches("^[+|-]?[0-9]+")) {
			throw new Exception("Syntax error. Expected atom; found " + tokens.get(lookAheadIndex));
		}
		lookAheadIndex++;
	}

	private void match(String terminal) throws Exception {
		if(terminal.equalsIgnoreCase(tokens.get(lookAheadIndex))) lookAheadIndex++;
		else throw new Exception("Syntax error. Expected " + terminal + "; found " + tokens.get(lookAheadIndex));
	}

	private void backTrackAndComputeListFlag() {
		if(!treeTops.isEmpty()) {
			ComplexSExpression completedExpression = treeTops.pop();
			if(completedExpression.getRightChild()==null) {
				completedExpression.setList(true);
				while(!treeTops.isEmpty() && treeTops.peek().isList()) treeTops.pop();
			} else if(completedExpression.getRightChild() instanceof ComplexSExpression && ((ComplexSExpression) completedExpression.getRightChild()).isList()) {
				completedExpression.setList(true);
			}
		}
	}

}