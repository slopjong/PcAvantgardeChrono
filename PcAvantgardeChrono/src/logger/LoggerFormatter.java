package logger;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


/**
 * Der <code>LoggerFormatter</code> formatiert die Log-Meldungen in eine
 * lesbare Form. Solche Log-Meldungen haben folgende Gestalt:
 * 
 * ...
 * Sat Jan 13 20:20:42 CET 2007 INFO logger.LoggerFactory.createLogger: Logger initialisiert
 * Sat Jan 13 20:20:42 CET 2007 INFO main.Uhrsystem.main: Funktion initialisieren...
 * Sat Jan 13 20:20:42 CET 2007 INFO main.Uhrsystem.main: Gui initialisieren...
 *
 * @author Cappellaro, Hess, Schmitz
 *
 */


class LoggerFormatter 
extends Formatter
{

	/**
	 * Methode, die für alle Logeintraege aufgerufen wird um das 
	 * Standardausgabeformat zu ändern.
	 *
	 * @param rec Angabe der zu loggenden Nachricht.
	 * @return Log Eintrag im oben angegebenen Format.
	 */
    public String format(LogRecord rec) 
    {
    	//kein Stringbuilder wegen sync
        StringBuffer buf = new StringBuffer(1000);
        
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        
        buf.append(hours + ":" + minutes + " ");
        buf.append(rec.getLevel());
//        buf.append(" ");
//        buf.append(rec.getSourceClassName());
//        buf.append(".");
//        buf.append(rec.getSourceMethodName() + "()");
        buf.append(": ");
        buf.append(formatMessage(rec));
        buf.append('\n');
        return buf.toString();
    }

    /**
     * Diese Methode generiert den Header und Ã¼berschreibt die
     * in Formatter definierte Methode getHead, um mögliche Seiteneffekte
     * zu vermeiden.
     *
     * @param h Übergabe des Handlers, der die Ausgabe handelt.
     */
    public String getHead(Handler h) {
        return "";
    }

     /**
     * Diese Methode generiert den Tail und Ã¼berschreibt die
     * in Formatter definierte Methode getTail, um mÃ¶gliche Seiteneffekte
     * zu vermeiden.
     *
     * @param h Übergabe des Handlers, der die Ausgabe handelt.
     */
    public String getTail(Handler h) {
        return "";
    }
}

