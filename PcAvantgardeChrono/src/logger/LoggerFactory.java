package logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Romain Schmitz
 *
 */
public class LoggerFactory 
{
	private static Logger logger = null;
	
	
	private static void createLogger()
	{
		// Neuer Logger wird erzeugt.
		logger = Logger.getLogger("PcAvantgardeChrono.Logger");
		
		// Sende die Logs nicht an den Parent-Logger.
		logger.setUseParentHandlers(false);
		
		ConsoleHandler handler = new ConsoleHandler(); 
		handler.setFormatter(new LoggerFormatter());
	
		logger.addHandler(handler);
		
		// Log-Level setzen. Nur Nachrichten höher oder gleich dem gesetzten
		// Level passieren den Level-Check und werden angezeigt.
		logger.setLevel(Level.FINEST);

		logger.info("Logger initialisiert.");
	}
	
	
	/**
	 * Liefert den Logger des Uhrsystems.
	 * 
	 * @return Den Logger des Uhrsystems.
	 */
	public static Logger getLogger()
	{
		if (logger == null)
			createLogger();
		
		return logger;
	}
	
}
