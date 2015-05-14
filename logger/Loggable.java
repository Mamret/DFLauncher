package logger;

/**
 * Entry point to Logger functionality
 * Any class that wants to easily log events should implement this interface
 * and use its default methods.
 * 
 * @author Marek Murdza
 *
 */

public interface Loggable {
	default void logEvent(String message) {
		// basic logging method 
		// message is a String with description of an event
		Loggers.log(message);
	}      
	default void setLogPath(String... path) {
		// sets path to the logging file
		// folder structure is defined in Logger class
		
		Loggers.setLogPath(path); // to be deleted
	}

}
