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

	private String inputStatement = null;
	private Map<String, String> tokenCheckPriorities;
	private ArrayList<String> tokens;

	private Tokenizer() {
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
	
	public Tokenizer(String inputStatement) {
		this();
		this.inputStatement = inputStatement.toUpperCase();
		this.tokenize();
		System.out.println(this.tokens);
	}

	private void tokenize() {
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
				System.out.println("ERROR: Invalid input found at line: " + inputStatement);
				break;
			}
			currentStatementFragment = currentStatementFragment.trim();
		}
	}
	
	public ArrayList<String> getTokens() {
		return this.tokens;
	}
}