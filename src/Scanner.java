import java.io.EOFException;

/**
 * Scans stdin and returns input lines to process as necessary
 * 
 * @author Naveenraj Nagarathinam(nagarathinam.1@osu.edu)
 *
 */
public class Scanner {

	java.util.Scanner scanner;
	int count=0;

	public Scanner() {
		scanner = new java.util.Scanner(System.in);
	}

	/**
	 * Gets the next line of input from stdin
	 * 
	 * @return - next line of input from stdin
	 * @throws EOFException
	 */
	public String getNextLine() throws EOFException {
		/*if(count==0) {
			count++;
			return "(defun isprime (x)	(cond ((EQ (REMAINDER x 2) 0) NIL)		((EQ (REMAINDER x 3) 0) NIL)		((EQ (REMAINDER x 4) 0) NIL)		((EQ (REMAINDER x 5) 0) NIL)		(t t)	))(defun consprime (list)		(cond ((ISPRIME (car list)) (cons (car list) (consprime (cdr list))))		(t (consprime list))		)	)(consprime (QUOTE (9 11 13 2)))";
		} else {
			throw new EOFException();
		}*/
		if(scanner.hasNext()) {
			return scanner.nextLine();
		} else {
			throw new EOFException();
		}
	}

}
