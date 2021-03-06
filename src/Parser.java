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

	Tokenizer tokenizer = new Tokenizer();

	String nextTokenToBeProcessed = null;

	private SExpression sExpressionTree;
	private Stack<ComplexSExpression> treeTops;
	private boolean isDottedExpression;

	private CommonHelper helper = new CommonHelper();

	/**
	 * Triggers parsing the tokens - evaluate if a complete expression is found; then continue with the rest of the input
	 * 
	 * @throws Exception
	 */
	public void parseAndEvaluate() throws Exception {
		while(true) {
			sExpressionTree = null;
			treeTops = new Stack<ComplexSExpression>();
			try {
				completeProcessingCurrentToken();
				if(nextTokenToBeProcessed == null) break;
				parseE();
				helper.printResult(new ExpressionEvaluator().evaluateSExpression(sExpressionTree, new HashMap<String, SExpression>()));
			} catch(NullPointerException e) {
				throw new Exception("ERROR: Syntax error. S-expression incomplete");
			}
		}
	}

	/**
	 * Looks for the non-terminal 'E' of the LL(1) grammar in the input pending to be processed
	 * <E> ::= atom or <E> ::= ( <X>
	 * 
	 * @throws Exception
	 */
	private void parseE() throws Exception {
		if(nextTokenToBeProcessed.matches("^\\(")) {
			//Matches the production "<E> ::= ( <X>"
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
			completeProcessingCurrentToken();
			parseX();
		} else {
			//Matches the production "<E> ::= atom"
			Atom atom = matchAtom();
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

	/**
	 * Looks for the non-terminal 'X' of the LL(1) grammar in the input pending to be processed
	 * <X> ::= <E> <Y> or <X> ::= )
	 * 
	 * @throws Exception
	 */
	private void parseX() throws Exception {
		if(nextTokenToBeProcessed.matches("^\\)")) {
			//Matches the production "<X> ::= )"
			match(")");
			backTrackAndComputeListFlag();
		} else {
			//Matches the production "<X> ::= <E> <Y>"
			parseE();
			completeProcessingCurrentToken();
			parseY();
		}
	}

	/**
	 * Looks for the non-terminal 'Y' of the LL(1) grammar in the input pending to be processed
	 * <Y> ::= . <E> ) or <Y> ::= <R> )
	 * 
	 * @throws Exception
	 */
	private void parseY() throws Exception {
		if(nextTokenToBeProcessed.matches("^\\.")) {
			//Matches the production "<Y> ::= . <E> )"
			match(".");
			this.isDottedExpression = true;
			completeProcessingCurrentToken();
			parseE();
			completeProcessingCurrentToken();
			match(")");
			backTrackAndComputeListFlag();
		} else {
			//Matches the production "<Y> ::= <R> )"
			parseR();
			//completeProcessingCurrentToken();
			match(")");
			backTrackAndComputeListFlag();
		}
	}

	/**
	 * Looks for the non-terminal 'R' of the LL(1) grammar in the input pending to be processed
	 * <R> ::= ε or <R> ::= <E> <R>
	 * 
	 * @throws Exception
	 */
	private void parseR() throws Exception {
		try {
			//Matches the production "<R> ::= <E> <R>"
			parseE();
			completeProcessingCurrentToken();
			parseR();
		} catch(Exception e) {
			//Matches the production "<R> ::= ε"
			//The grammar is looking for <E> but found others, which means that <R> may be an empty string or there is an actual syntax errors
			//If it turns out there is an actual syntax error, it will be caught in the next step of the previous call in the stack i.e. in parseY()
			if(!e.getMessage().startsWith("Syntax error")) throw e;
		}
	}

	/**
	 * Checks if the next token to be processed is an atom
	 * 
	 * @return Atom instance of the matched atom
	 * @throws Exception
	 */
	private Atom matchAtom() throws Exception {
		if(!nextTokenToBeProcessed.matches("^[a-zA-Z][a-zA-Z0-9]*") && !nextTokenToBeProcessed.matches("^[+|-]?[0-9]+")) {
			throw new Exception("Syntax error. Expected atom; found " + nextTokenToBeProcessed);
		}
		Atom matchedAtom = new Atom(nextTokenToBeProcessed);
		//completeProcessingCurrentToken();
		return matchedAtom;
	}

	/**
	 * Checks if the next token to be processed matches the provided terminal
	 * 
	 * @param terminal - terminal to be matched to the token
	 * @throws Exception
	 */
	private void match(String terminal) throws Exception {
		if(!terminal.equalsIgnoreCase(nextTokenToBeProcessed)) /*completeProcessingCurrentToken();
		else*/ throw new Exception("Syntax error. Expected " + terminal + "; found " + nextTokenToBeProcessed);
	}

	/**
	 * A sub expression is complete - backtrack to the parent of the current sub expression so that the stack contains the node at the top to which rest of the expression can be placed as child
	 * Simultaneously computes the isList flag in all the nodes in the backtracking path
	 * 
	 */
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

	/**
	 * Updates the next token to be processed by getting the next token from tokenizer
	 * 
	 * @throws Exception
	 */
	private void completeProcessingCurrentToken() throws Exception {
		nextTokenToBeProcessed = tokenizer.getNextToken();
	}

}