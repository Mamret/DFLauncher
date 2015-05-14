package logger;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class containing only static methods
 * No possibility to instance this class
 * Some of the methods are protected to assure automation
 * of logging
 * 
 * @author Marek Murdza
 *
 */

public class Loggers {
	
	// logPath holds the location of a log file
	// consoleOutput is self-explanatory
	
	private static Path logPath = null;
	private static boolean consoleOutput = true;
	
	private Loggers() {
		// no instances of this class
	}
	
	
	static String log(String message) {
		// logging message into the file or printing in the console
		// depending on whether consoleOutput is true
		// returns message just in case
		
		Logger.getInstance().log(message, consoleOutput);
		return message;
	}
	
	static void setLogPath(String... path) {
		// default method to set the location of log file
		// designed to be used only once in the program
		// to avoid duplicate log files. 
		// Throws exception when path has already been set.
		// Call with no argument will set path to current working directory
		// path is resolved from the given strings. Each string is
		// separate folder with the first being the top one.
		// it is possible to abuse the method with strings containing
		// system specific separators. 
		// TO-DO: Check for validity of names.
		
		if(logPath != null) {
			throw new IllegalArgumentException("Path exists error");   // change exception to logger specific
		} else if(path.length != 0){
			Path tempPath = Paths.get("");
			for(String s: path) {
				logPath = tempPath.resolve(Paths.get(s));
				tempPath = logPath;
			}
			consoleOutput = false;
			// setting path will always switch off console logging
		} else {
			// setting current directory as path
			setLogPath("");
		}
	}
	
	public static void useConsole() {
		// Switches on console logging
		// Always turns off file logging
		
		logPath = null;
		consoleOutput = true;
	}
	
	static Path getLogPath() {
		return logPath;
	}
	
	public static boolean isPathSet() {
		if(logPath == null) {
			return false;
		} else {
			return true;
		}
	}
    
    public static boolean isConsoleOutput() {
    	return consoleOutput;
    }
    
    public static void logException(Throwable e) {
    	// method to log java exceptions messages
    	// converts exception message to Logger format
    	
    	if(Logger.getInstance().logFileExists()) {
    		log(e.toString());
   			StackTraceElement[] exceptionMessage = e.getStackTrace();
   			for(StackTraceElement ste: exceptionMessage) {
   				log(ste.toString());
   			}
    	}
    	e.printStackTrace();
    }
}
