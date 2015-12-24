package gen;


/**
 *
 * A base class providing basic error accumulation.
 */
public abstract class BaseCodeGenVisitor<T> implements CodeGenVisitor<T> {
	private int errors;

	public BaseCodeGenVisitor() {
		errors = 0;
	}
	
	public int getErrorCount() {
		return errors;
	}
	
	protected void error(String message) {
		System.err.println("Code generating error: " + message);
		errors++;
	}
}
