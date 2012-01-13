package uhrfunktionen.zeitfunktionen;

import global.Commands;
import global.DigitToHandle;
import global.ExceptionFormatter;
import global.Notify;
import global.Observer;
import global.synchronize.SntpClient;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import logger.LoggerFactory;
import uhrfunktionen.Observable;
import uhrfunktionen.ZeitFunktion;


/**
 * <code>ZeitFunktionImpl</code> ist Oberklasse einer jeden Uhrfunktion, welche
 * eine Zeitangabe verarbeiten. Diese Klasse ist Teil des State-Pattern, so dass
 * sie abstrakt deklariert wurde. 
 * 
 * Hier wurde allerdings das State-Pattern leicht
 * abgewandelt, da im Attribut <code>activeFunction</code> zwar die aktuell 
 * aktivierte Uhrfunktion gespeichert wird, nicht aber der Zustand, ob gerade
 * die Uhrzeit eingestellt wird. Somit kann ein Observer immer die aktuell
 * aktivierte Funktion abfragen. 
 * 
 * Da aber nicht komplett auf die Speicherung dieser
 * benötigten Zustände verzichtet werden kann, dient das Attribut <code>
 * aktuellerZustand</code>, welches einen Wert der Enumeration 
 * <code>{@link UhrsystemZustand}</code> enthält.
 * 
 * @author Romain Schmitz
 *
 */
abstract class ZeitFunktionImpl 
extends Observable
implements ZeitFunktion
{
	/**
	 * Stunden
	 */
	protected int hour = 0;
	
	/**
	 * Minuten
	 */
	protected int minute = 0;
	
	/**
	 * Sekunden
	 */
	protected int second = 0;
	
	/**
	 * Hundertstelsekunden
	 */
	protected int hsecond = 0;
	
	/**
	 * Enthält die Observer, welche auf eine Änderung der Zeitangaben reagieren sollen.
	 */
	//protected static TimeObserver anzeige = new TimeObserver();
	
	// TODO Javadoc für diese Felder angeben!
	protected static final ZeitFunktionImpl basicUhr = new Uhr();
	protected static final ZeitFunktionImpl stoppUhr = new Stoppuhr();
	protected static final ZeitFunktionImpl countDown = new Countdown();
	
	// TODO Javadoc erstellen
	private static ZeitFunktionImpl activeFunction;
	
	protected static UhrsystemZustand aktuellerZustand;
	
	private static boolean isInitialized = false;
	
	//private static Observable observerPool = new Observable();
	
	/**
	 * Beschreibt, ob die Observer benachrichtigt werden sollen oder nicht.
	 * Falls isBackground true ist, so werden die Observer benachrichtigt
	 * und so zum Beispiel die Anzeige aktualisiert. Falls isBackground false
	 * ist, so wird die Uhrfunktion zwar weiterhin getaktet, jedoch werden
	 * die Observer nicht mehr benachrichtigt.
	 */
	boolean isBackground;

	static Logger logger = LoggerFactory.getLogger();
	/**
	 * Diese Methode liefert ein einzelnes Zeichen, welches
	 * am Display angezeigt wird, um dem Benutzer den aktuellen
	 * Uhrmodus zu signalisieren
	 * 
	 * @return Zeichen, welches die aktuelle Funktion am Display signalisiert.
	 */
	public char getDisplayFunctionChar() 
	{
		return ' ';
	}
	
	public int getHour() 
	{
		return hour;
	}


	public int getMinute() 
	{
		return minute;
	}


	public int getSecond() 
	{
		return second;
	}
	
	
	public int getHSecond()
	{
		return hsecond;
	}
	

	/**
	 * Inkrementiert die Sekunden. 
	 * Anschließend werden die Observer über eine Änderung benachrichtigt. Im Gegensatz
	 * zu den anderen Inkrement-Methoden überprüft <code>incrementHour</code> nicht, ob
	 * der Wert 23 überschritten wird. Die Unterklassen, welche aber einen Wert größer
	 * als 23 nicht zulassen dürfen, müssen diese Methode überladen.
	 * 
	 * Die Prüfung wird nicht gemacht, damit andere Uhrfunktionen wie z.B. die Stoppuhr
	 * und der Countdown auch realitätsgetreue Stunden hoch- bzw. runterzählen können 
	 * 
	 * 
	 * @see ZeitFunktion#incrementHour()
	 * @see ZeitFunktion#incrementMinute()
	 * @see ZeitFunktion#incrementSecond()
	 * @see ZeitFunktion#incrementHSecond()
	 *
	 */
	protected void incrementHour()
	{
		this.hour++;
		
		assert(minute>=0) : "Es gibt keine negativen Stunden!";
		
		if (!this.isBackground)
			this.notifyObservers(
				new Notify(
						Commands.setDigit, 
						DigitToHandle.HOUR)
				);		
	}
	
	
	/**
	 * Inkrementiert die Minuten. 
	 * Anschließend werden die Observer über eine Änderung
	 * benachrichtigt. Die Methode überprüft desweiteren, ob der Wert beim nächsten Inkrement
	 * den Wert 59 übersteigen würde. Ist dies der Fall, so werden die Minuten zurückgesetzt und
	 * die Stunden inkrementiert.
	 * 
	 * @see ZeitFunktion#incrementHour()
	 * @see ZeitFunktion#incrementMinute()
	 * @see ZeitFunktion#incrementSecond()
	 * @see ZeitFunktion#incrementHSecond()
	 *
	 */
	protected void incrementMinute()
	{
		assert(minute>=0 && minute<60) : "Falscher Wert (" + minute + ") für Minute!";
		
		if(++this.minute == 60)
		{
			this.incrementHour();
			this.minute = 0;
		}
		
		if (!this.isBackground)
			this.notifyObservers(
				new Notify(
						Commands.setDigit, 
						DigitToHandle.MINUTE)
				);		
	}
	

	/**
	 * Inkrementiert die Sekunden. 
	 * Anschließend werden die Observer über eine Änderung
	 * benachrichtigt. Die Methode überprüft desweiteren, ob der Wert beim nächsten Inkrement
	 * den Wert 59 übersteigen würde. Ist dies der Fall, so werden die Sekunden zurückgesetzt und
	 * die Minuten inkrementiert.
	 * 
	 * @see ZeitFunktion#incrementHour()
	 * @see ZeitFunktion#incrementMinute()
	 * @see ZeitFunktion#incrementSecond()
	 * @see ZeitFunktion#incrementHSecond()
	 *
	 */
	protected void incrementSecond()
	{
		assert(second>=0 && second<60) : 
			"Falscher Wert ("+second+") für Sekunde!";
		
		if(++this.second == 60)
		{
			this.incrementMinute();
			this.second = 0;
		}

		if (!this.isBackground)
			this.notifyObservers(
				new Notify(
						Commands.setDigit, 
						DigitToHandle.SECOND) 
				);
	}
	
	
	/**
	 * Inkrementiert die Hundertstelsekunden. 
	 * Anschließend werden die Observer über eine Änderung
	 * benachrichtigt. Die Methode überprüft desweiteren, ob der Wert beim nächsten Inkrement
	 * den Wert 99 übersteigen würde. Ist dies der Fall, so werden die Hundertstelsekunden zurückgesetzt 
	 * und die Sekunde inkrementiert.
	 * 
	 * @see ZeitFunktion#incrementHour()
	 * @see ZeitFunktion#incrementMinute()
	 * @see ZeitFunktion#incrementSecond()
	 * @see ZeitFunktion#incrementHSecond()
	 *
	 */
	protected void incrementHSecond()
	{
		if(++this.hsecond >= 100)
		{
			this.incrementSecond();
			this.hsecond = 0;
		}
		
		assert(hsecond>=0 && hsecond<100) : "Falscher Wert (" + hsecond + " für Hundertstelsekunde!";
		
		if (!this.isBackground)
			this.notifyObservers(
				new Notify(Commands.setDigit, DigitToHandle.HSECOND));		
	}
	
	static ZeitFunktion getActiveFunction()
	{
		return activeFunction;
	}
	
	static void setActiveFunction(ZeitFunktionImpl funktion)
	{
		assert(funktion != null) : 
			"Der Methode setActiveFunction kann kein null-Objekt übergeben "+
			"werden";
		activeFunction = funktion;
	}
	
	static void enterStateOfActiveFunction()
	{
		activeFunction.enter();
	}
	
	abstract void enter();
	
	abstract void exit();
	
	static void init()
	{
		// init nur beim Systemstart aufrufen, ansonsten AssertionError werfen.
		
		assert (!isInitialized) : 
			"Beim Initialisieren ist ein Fehler aufgetreten."+
			" => aktuellerZustand nicht initial.";
		
		// TODO Das Synchronisieren sollte vielleicht regelmäßig gemacht werden.
		String server = "ntp1.theremailer.net";
		
		logger.info("Uhr mit " + server + " synchronisieren");
		
		int[] time = {0,0,0};
		
		try 
		{
			time = SntpClient.getTime(server);
		} 
		catch (IOException e) 
		{
			logger.warning("Internet-Synchronisation fehlgeschlagen");
			logger.warning(ExceptionFormatter.format(e));
			
			logger.info("Mit der Systemuhrzeit synchronisieren");
			
			// FIXME Deprecated	        
	        Date date = new Date();
	        int hours = date.getHours();
	        int minutes = date.getMinutes();
	        int seconds = date.getSeconds();
	        
			time = new int[]{hours, minutes, seconds};
			
			logger.info("Synchronisierte Zeit ist " + 
					// time[0] = Stunden
					// time[1] = Minuten
					// time[2] = Sekunden
			time[0] + ":" + time[1] + ":" + time[2]);
		}
		
		basicUhr.hour = time[0];
		basicUhr.minute = time[1];
		basicUhr.second = time[2];
		
		logger.info("Uhrfunktion wird auf Modus Uhr (Grundmode) gesetzt.");
		aktuellerZustand = UhrsystemZustand.UhrGrundmode;
		activeFunction = basicUhr;
		
		// Zustand betreten
		activeFunction.enter();
		
		isInitialized = true;
	}
	
	
	@Override
	public void addObserver(Observer observer)
	{
		super.addObserver(observer);
	}
}
