import java.io.EOFException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses the input and splits them into tokens
 * 
 * @author Naveenraj Nagarathinam(nagarathinam.1@osu.edu)
 *
 */
public class Tokenizer {
	
	Scanner scanner = new Scanner();

	private Map<String, String> tokenCheckPriorities;
	private ArrayList<String> tokens = new ArrayList<String>();
	private int lookAheadIndex = -1;

	public Tokenizer() {
		this.tokenCheckPriorities = new LinkedHashMap<String, String>();
		this.tokenCheckPriorities.put("^\\(", "^\\("); //open parenthesis
		this.tokenCheckPriorities.put("^\\)", "^\\)"); //close parenthesis
		this.tokenCheckPriorities.put("^\\.\\s", "^\\.\\s"); //DOT symbol
		this.tokenCheckPriorities.put("^[+|-]?[0-9]+[\\s|\\(|\\)]", "^[+|-]?[0-9]+"); //numeric atom
		this.tokenCheckPriorities.put("^[a-zA-Z][a-zA-Z0-9]*[\\s|\\(|\\)]", "^[a-zA-Z][a-zA-Z0-9]*"); //literal atom
		this.tokenCheckPriorities.put("^[+|-]?[0-9]+[\\s|\\(|\\)]*$", "^[+|-]?[0-9]+"); //numeric atom at end of line
		this.tokenCheckPriorities.put("^[a-zA-Z][a-zA-Z0-9]*[\\s|\\(|\\)]*$", "^[a-zA-Z][a-zA-Z0-9]*"); //literal atom at end of line
		this.tokens = new ArrayList<String>();
	}
	
	public ArrayList<String> getTokens() {
		return this.tokens;
	}
	
	/**
	 * Parse the input string and splits it into tokens. Rejects invalid input
	 * 
	 * @param inputStatement
	 * @throws Exception
	 */
	private void tokenize(String inputStatement) throws Exception {
		String currentStatementFragment = new String(inputStatement);
		currentStatementFragment = currentStatementFragment.trim();
		while(!currentStatementFragment.equalsIgnoreCase("")) {
			boolean matchFound = false;
			for(String tokenRegex : this.tokenCheckPriorities.keySet()) {
				Pattern patternToLookFor = Pattern.compile(tokenRegex);
				Matcher lookupMatcher = patternToLookFor.matcher(currentStatementFragment);
				if(lookupMatcher.lookingAt()) {
					matchFound = true;
					Pattern patternToExtract = Pattern.compile(this.tokenCheckPriorities.get(tokenRegex));
					Matcher extractMatcher = patternToExtract.matcher(currentStatementFragment);
					extractMatcher.lookingAt();
					tokens.add(extractMatcher.group().trim());
					currentStatementFragment = extractMatcher.replaceFirst("");
					break;
				}
			}
			if(!matchFound) {
				throw new Exception("ERROR: Invalid input found at line: " + inputStatement);
			}
			currentStatementFragment = currentStatementFragment.trim();
		}
	}
	
	/**
	 * Returns the next unprocessed token. If end of token list is reached, it gets the next input line from scanner, tokenizes and returns the first token
	 * 
	 * @return - next token to be processed
	 * @throws Exception
	 */
	public String getNextToken() throws Exception {
		while(tokens.isEmpty() || lookAheadIndex>=tokens.size()) {
			String inputLine;
			try {
				inputLine = scanner.getNextLine();
			} catch(EOFException e) {
				return null;
			}
			if(inputLine.equalsIgnoreCase("")) continue;
			tokens = new ArrayList<String>();
			tokenize(inputLine.toUpperCase());
			lookAheadIndex=0;
		}
		return tokens.get(lookAheadIndex++);
	}
	
}