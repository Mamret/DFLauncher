package logger;

/** Exception specific to logger classes.
 *  Used to better pinpoint errors and bugs.
 *  
 * @author Mamret
 *
 */

public class LoggerException extends RuntimeException {
	
	private static final long serialVersionUID = -4943856138333761315L;
	/* This exception is used to narrow down logger specific bugs
	 * 
	 */
	
	public LoggerException(Throwable cause) {
		super(cause);
		
	}
	

}
