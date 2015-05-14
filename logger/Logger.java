package logger;

import java.text.DateFormat;
import static java.text.DateFormat.*;
import java.util.Date;
import java.io.*;
import java.nio.file.*;

/**
 * Heart of the Logger API. There can be only one instance of the Logger,
 * thus constructors are private. Logger instance is accesed and created via getInstance() method.
 * The method itself as well as all the other methods are private or package private.
 * The only way to use Logger is via Loggers utility class.
 
 * @author Mamret
 *
 */

class Logger {
	/* Path and File members hold the locations of files and folders in
	 * file system. The actual files are created when getInstance() method is 
	 * first called unless Loggers.isConsoleOutput() returns true.
	 * The folder structure is as follows:
	 *     - [LOG_PATH]			                     - top folder, by default it is application folder
	 *            - [LOG_DIR]                        - \[LOG_PATH]\logs\           folder with log
	 *                 - [BAK_DIR]                   - \[LOG_PATH]\logs\backup     folder with copies of old logs
	 *                       - ddmmyy_hhmm_log.tx    - \[LOG_PATH]\logs\backup\1234_5678_log.txt
	 *                                                 old logs file with date stamp of creating at the front
	 *                                                 they are copies of the log.txt file found when 
	 *                                                 getInstance() method is called for the first time.
	 *                 - logFile                     - \[LOG_PATH]\logs\log.txt
	 *                                                 new log file created after calling getInstance().
	 *                 
	 *               
	 */
	
	private static File logFile;
	private static Logger logger = null;
	
	private static Path LOG_PATH;
	private static Path LOG_DIR;
	private static Path BAK_DIR;
	
	private Logger() {
		LOG_PATH = Loggers.getLogPath() != null?Loggers.getLogPath(): Paths.get("");
		// when Loggers.getLogPath() is null it means the console output is set. However this check is 
		// here just in case. Maybe changed for exception later. If the case is null then paths are set as 
		// after calling Loggers.setLogPath() with no argument. Note that Loggers.getLogPath() is still null.
		LOG_DIR = LOG_PATH.resolve(Paths.get("logs"));
		BAK_DIR = LOG_DIR.resolve(Paths.get("backup"));
		logFile = LOG_DIR.resolve(Paths.get("log.txt")).toFile();
		// Files are created only when Loggers.consoleOutput is set to false AND files don't exist.
		if(!Loggers.isConsoleOutput()) {
			if(!BAK_DIR.toFile().exists()) BAK_DIR.toFile().mkdirs();
			createLogFile();
		}			
	}
	
	static Logger getInstance() {
		// only one instance possible
		if(logger == null) logger = new Logger();
		return logger;
	}
	
	
	private String log(String message) {
		// private method that does actual logging to file
		// returns the message given as parameter
		try(PrintWriter output = new PrintWriter(new FileWriter(logFile, true))) {
			output.println(message);
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		return message;
	}
	
	String log(String message, final boolean consoleOutput) {
		// if consoleOutput is true message is send to system console
		// otherwise private log() method is called
		// Date stamp is added in front of the message so the end format is: 
		// dd/mm/yy hh:mm: message
		
		message = DateFormat.getDateTimeInstance(SHORT, SHORT).format(new Date()) + ": " + message;
		if(consoleOutput) {
			System.out.println(message);
		} else {
			log(message);
		}
		return message;
	}
	
	private void createLogFile() {
		// creates files on the disk drive
		// if there is existing log.txt file it is copied to backup folder.
		// Its name is modified with the date stamp at the front
		try {
			if(logFile.exists()) {
				String name = 
						DateFormat.getDateTimeInstance(SHORT, SHORT).format(new Date()).replace("/", "").replace(":", "").
						replace(" ", "_") +	"_log.txt";
				Path oldLog = BAK_DIR.resolve(Paths.get(name));
				Files.copy(logFile.toPath(), oldLog);
				logFile.delete();
				createLogFile();
			} else {
				logFile.createNewFile();
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	boolean logFileExists() {
		return logFile.exists();
	} 
}
