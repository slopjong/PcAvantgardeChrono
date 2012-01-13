package uhrfunktionen.zeitfunktionen;

import global.Commands;
import global.DigitToHandle;
import global.Notify;

import java.util.Timer;
import java.util.TimerTask;



/**
 * <code>Uhr</code> bietet die Funktionalität einer realen Stoppuhr an. 
 * Dies bedeutet, dass intern ein Zähler die Hundertstelsekunden inkrementiert.
 * Die Stoppuhr kann nachdem sie gestartet wurde auch wieder gestoppt und 
 * rückgesetzt werden. Die Stoppuhr wird gestoppt, wenn der Modus nach Countdown
 * gewechselt wird.
 * 
 * @see Countdown Countdown
 * @see Uhr Uhr
 * 
 * @author Romain Schmitz
 */
public class Stoppuhr
extends ZeitFunktionImpl
{
	
	// Gibt an, ob die Stoppuhr gerade angehalten wurde oder nicht.
	// Diese Variable ist nicht mit threadStopped zu verwechseln!
	private boolean stopwatchStopped = true;
	
	private Timer timer;
	
	/* TODO Javadoc
	 * @see uhrfunktionen.Steuerungsfunktion#mode()
	 */
	public void mode()
	{		
		if(this.stopwatchStopped)
		{
			// Timer stoppen exit()
			this.exit();
			
			logger.info("Uhrfunktion wird auf Modus Countdown gesetzt.");
			
			aktuellerZustand = UhrsystemZustand.CountdownStundenSetzen;
			setActiveFunction(countDown);
			enterStateOfActiveFunction();
		}
	}


	/** 
	 * Mit <code>reset</code> wird die Stoppuhr wieder zurückgesetzt.
	 * Rücksetzen bedeutet, dass die Anzeige für die Studenten, Minuten
	 * und Sekunden 00 anzeigt.
	 * 
	 * @see uhrfunktionen.Steuerungsfunktion#reset()
	 */
	public void reset()
	{
		
		// Siehe Spezifikation, nur wenn Stoppuhr gestoppt ist,
		// kann sie rückgesetzt werden!
		if(this.stopwatchStopped)
		{
			logger.info("Stoppuhr rücksetzen...");
			
			this.hour = 0;
			this.minute = 0;
			this.second = 0;
			this.hsecond = 0;
			
			this.notifyObservers(
					new Notify(Commands.setDigit, DigitToHandle.HOUR)
					);
			
			this.notifyObservers(
				new Notify(Commands.setDigit, DigitToHandle.MINUTE));
			
			this.notifyObservers(
				new Notify(Commands.setDigit, DigitToHandle.SECOND));

			this.notifyObservers(
				new Notify(Commands.setDigit, DigitToHandle.HSECOND));
		}	
	}


	/** Tut nichts in diesem Uhr-Modus.
	 * 
	 * @see uhrfunktionen.Steuerungsfunktion#setMinus()
	 */
	public void setMinus()
	{
	}


	/* Startet die Stoppuhr und hält sie auch wieder an.
	 * 
	 * @see uhrfunktionen.Steuerungsfunktion#setPlus()
	 */
	public void setPlus()
	{
		stopwatchStopped = !stopwatchStopped;
		
		if(stopwatchStopped)
			logger.info("Stoppuhr gestoppt.");
		else
			logger.info("Stoppuhr gestartet.");
	}
	
	
	/**
	 * Liefert ein einzelnes Zeichen zurück, welches den aktuellen Uhrmodus in 
	 * der Anzeige signalisiert.
	 * 
	 * @return das Zeichen 'S', welches den aktuellen Uhrmodus in der Anzeige signalisiert.
	 */
	public char getDisplayFunctionChar() 
	{
		return 'S';
	}
	
	
	public void run()
	{
		if(!stopwatchStopped) 
			this.incrementHSecond();				
	}
	
	void enter()
	{
		logger.info("Uhrfunktion wird auf Modus Stoppuhr gesetzt.");
		
		this.notifyObservers(
			new Notify(Commands.setDisplayChar)
			);
		
		// Die Anzeige auf 00:00:00:00 stellen
		this.reset();
		
		
		// neuen Timer erstellen
		logger.info("Neuen Timer erzeugen");
		this.setTimer(new Timer("Stoppuhr-Timer"));
		
		TimerTask newTimerTask = new TimerTask()
			{
				public void run()
				{
					if (!stopwatchStopped)
						incrementHSecond();
				}
			}; // end of new TimerTask
		
		this.timer.scheduleAtFixedRate(newTimerTask, 0, 10);
	}
	
	
	private void setTimer(Timer timer)
	{
		this.timer = timer;
	}
	
	
	void exit()
	{
		logger.info("Timer stoppen");
		
		// Timer stoppen
		this.timer.cancel();
	}

}
