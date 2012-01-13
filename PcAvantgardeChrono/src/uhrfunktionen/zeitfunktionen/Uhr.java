package uhrfunktionen.zeitfunktionen;

import global.Commands;
import global.DigitToHandle;
import global.ExceptionFormatter;
import global.Notify;

/**
 * <code>Uhr</code> bietet die Funktionalit�t einer realen Uhr an. Dies bedeutet, 
 * dass intern ein Z�hler die Sekunden inkrementiert. Die Zeit kann nat�rlich 
 * neu gesetzt werden, damit nach dem Starten der Uhr die Zeit auch eingestellt 
 * werden kann.
 * 
 * 
 * @see Countdown Countdown
 * @see Stoppuhr Stoppuhr
 * 
 * @author Romain Schmitz
 *
 */
public class Uhr
extends ZeitFunktionImpl
{
	/**
	 * Dieser Timer ist nach au�en hin f�r die Uhr ein Timer, der sie
	 * taktet. Aus der Sichtweise des Timers selbst ist er ein TimerTask, der
	 * vom java-eigenen Timer {@link java.util.Timer} angeregt wird und den
	 * in der <code>run</code>-Methode spezifizierten Task ausf�hrt.
	 * @author Romain Schmitz
	 *
	 */
	class Timer
	extends java.util.TimerTask
	{		
		public Timer()
		{
			java.util.Timer timer = new java.util.Timer("Uhr-Timer");
			
			// 3 Milisekunden warten, da die Gui ein wenig Zeit braucht, um sich aufzubauen.
			// Der Taktgeber soll in dieser Zeit nicht schon takten.
			// => sonst NullPointerException
			// h�ngt mit dem SocketTimeout zusammen
			
			// delay und interval im Konstruktor deklariert, weil sie
			// ansonsten frei zug�nglich sind f�r die �u�ere Klasse
			int delay = 2100;
			int interval = 1000;
			timer.scheduleAtFixedRate(this, delay, interval);
		}
		
		public void run()
		{
			// Die Uhr an sich kann sich nur in 4 Zust�nden befinden:
			//  - Grundmode
			//  - Stunden setzen
			//  - Minuten setzen
			//  - Sekunden setzen
			// 
			// Da das Uhrsystem aber weitere Zust�nde annehmen kann, m�ssen
			// explizit die 3 Zust�nde gefiltert werden.
			switch(aktuellerZustand)
			{
				case UhrZeiteinstellenStunde: break;
				case UhrZeiteinstellenMinute: break;
				case UhrZeiteinstellenSekunde: break;
				
				default: 
					
					incrementSecond();
				
					// Da incrementSecond unter Umst�nden das Inkrementieren
					// der Stunden veranlasst hat, m�ssen die Stunden �berpr�ft
					// werden.
					checkHour();

			}
		}
	}
	
	
	/**
	 * In diesem Konstruktor wird ein Timer erzeugt, welcher erstmal 2 Sekunden
	 * wartet und die Uhr dann in einem Intervall von 1 Sekunde taktet.
	 *
	 */
	public Uhr()
	{		
		new Timer();
	}
	
	/* TODO Javadoc
	 * @see uhrfunktionen.Steuerungsfunktion#mode()
	 */
	public void mode()
	{
		switch(aktuellerZustand)
		{
			case UhrGrundmode:
		
				// Die Anzeige nicht mehr benachrichtigen, da der Timer
				// im Hintergrund weiterl�uft.
				this.isBackground = true;
				
				aktuellerZustand = UhrsystemZustand.Stoppuhr;
				setActiveFunction(stoppUhr);
				enterStateOfActiveFunction();
				break;
				
			case UhrZeiteinstellenStunde:
				
				aktuellerZustand = UhrsystemZustand.UhrZeiteinstellenMinute;
				enterStateOfActiveFunction();
				break;
				
			case UhrZeiteinstellenMinute:

				aktuellerZustand = UhrsystemZustand.UhrZeiteinstellenSekunde;
				enterStateOfActiveFunction();
				break;
				
			case UhrZeiteinstellenSekunde: 

				aktuellerZustand = UhrsystemZustand.UhrGrundmode;
				enterStateOfActiveFunction();
				break;
				
			default: /* Do nothing*/
			
		}
	}


	/* TODO Javadoc
	 * @see uhrfunktionen.Steuerungsfunktion#reset()
	 */
	public void reset()
	{
		switch(aktuellerZustand)
		{
			case UhrZeiteinstellenStunde:
				
				this.hour = 0;
				
				this.notifyObservers(
					new Notify(
							Commands.setDigit, 
							DigitToHandle.HOUR)
					);
				
				break;
			
			case UhrZeiteinstellenMinute:
				
				this.minute = 0;
				
				this.notifyObservers(
					new Notify(
							Commands.setDigit, 
							DigitToHandle.MINUTE)
					);
				
				break;
			
			case UhrZeiteinstellenSekunde:
				
				this.second = 0;
				
				this.notifyObservers(
					new Notify(
							Commands.setDigit, 
							DigitToHandle.SECOND)
					);
				
				break;
				
			default:
		}
	}


	/* TODO Javadoc
	 * @see uhrfunktionen.Steuerungsfunktion#setMinus()
	 */
	public void setMinus()
	{
		try
		{
			switch(aktuellerZustand)
			{
				case UhrGrundmode:
					
					/* Do nothing */
					break;
					
				case UhrZeiteinstellenStunde:
					
					this.decrementHour();
					break;
					
				case UhrZeiteinstellenMinute:
					
					this.decrementMinute();
					break;
					
				case UhrZeiteinstellenSekunde: 
	
					/* Do nothing */
					break;
				
				default: 
					throw new IllegalStateException("Illegaler Zustand betreten...");
			}
		}
		catch(IllegalStateException e)
		{
			logger.severe(ExceptionFormatter.format(e));
		}
	}


	/* TODO Javadoc
	 * @see uhrfunktionen.Steuerungsfunktion#setPlus()
	 */
	public void setPlus()
	{
		try
		{
			switch(aktuellerZustand)
			{
				case UhrGrundmode:
					
					/* Do nothing */
					break;
					
				case UhrZeiteinstellenStunde:
					
					this.incrementHour();
					break;
					
				case UhrZeiteinstellenMinute:
					
					this.simpleIncrementMinute();
					break;
					
				case UhrZeiteinstellenSekunde: 
	
					/* Do nothing */
					break;
				
				default: 
					throw new IllegalStateException("Illegaler Zustand betreten...");
			}
		}
		catch(IllegalStateException e)
		{
			e.printStackTrace();
		}
	}
	
	

	
	
	void enter()
	{		
		try
		{
			switch(aktuellerZustand)
			{
				case UhrGrundmode:
					
					logger.info("Zustand 'Uhr-Grundmode' betreten.");			
					
					// Nichts blinken lassen
					this.notifyObservers(
						new Notify(
								Commands.setBlink, 
								DigitToHandle.NONE)
						);
					
					break;
					
				case UhrZeiteinstellenStunde:
					
					logger.info("Zustand 'Uhr Stunden stellen' betreten.");
					
					// Nachdem die Uhr aus dem Uhrmodus Stoppuhr oder Countdown
					// zur�ckgekehrt ist, m�ssen die Digits wieder aktualisiert
					// werden.
					
					this.isBackground = false;
					// Display-Char aktualisieren
					this.notifyObservers(
							new Notify(Commands.setDisplayChar)
							);
								
					// Studen-Anzeige aktualisieren
					this.notifyObservers(
						new Notify(
								Commands.setDigit, 
								DigitToHandle.HOUR)
						);
					
					
					// Minuten-Anzeige aktualisieren
					this.notifyObservers(
						new Notify(
								Commands.setDigit, 
								DigitToHandle.MINUTE)
						);
						
					
					// Sekunden-Anzeige aktualisieren
					this.notifyObservers(
						new Notify(
								Commands.setDigit, 
								DigitToHandle.SECOND)
						);
					
					
					// Hundertstelsekunden-Anzeige aktualisieren, weil die
					// wom�glich von einer anderen Uhrfunktion ge�ndert wurde
					this.notifyObservers(
						new Notify(
								Commands.setDigit, 
								DigitToHandle.HSECOND)
						);
					
					
					// Stunden blinken lassen
					this.notifyObservers(
						new Notify(
								Commands.setBlink, 
								DigitToHandle.HOUR)
						);
					break;
					
				case UhrZeiteinstellenMinute:
					
					logger.info("Zustand 'Uhr Minuten stellen' betreten.");
					
					// Minuten blinken lassen
					this.notifyObservers(
						new Notify(
								Commands.setBlink, 
								DigitToHandle.MINUTE)
						);					
					break;
					
				case UhrZeiteinstellenSekunde: 
	
					logger.info("Zustand 'Uhr Sekunden stellen' betreten.");
					
					// Sekunden blinken lassen
					this.notifyObservers(
							new Notify(
									Commands.setBlink, 
									DigitToHandle.SECOND)
							);
					break;
				
				default: 
					throw new IllegalStateException("Illegaler Zustand betreten...");
			}
		}
		catch(IllegalStateException e)
		{
			logger.severe(ExceptionFormatter.format(e));
		}
	}
	
	
	void exit()
	{
	}
	
	
	/**
	 * Dekrementiert die Studenten.
	 *
	 */
	private void decrementHour()
	{
		if (this.hour>0)
			this.hour--;
		else
			this.hour = 23;
		
		this.notifyObservers(
			new Notify(
					Commands.setDigit, 
					DigitToHandle.HOUR)
			);
	}
	
	
	/**
	 * Dekrementiert die Minuten.
	 *
	 */
	private void decrementMinute()
	{
		if (this.minute>0)
			this.minute--;
		else
			this.minute = 59;
		
		this.notifyObservers(
			new Notify(
					Commands.setDigit, 
					DigitToHandle.MINUTE)
			);
	}
	
	
	/**
	 * Laut Spezifikation k�nnen die Sekunden nur �ber die Reset-Taste
	 * r�ckgesetzt werden. Ein Dekrementieren wird nicht erfordert.
	 * Daher tut diese Methode nichts.
	 *
	 */
	private void decrementSecond()
	{
		/* Do nothing */
	}
	
	
	/**
	 * �berpr�ft die Stunden, ob diese gr��er gleich 24 sind. Wenn ja, so werden
	 * diese auf 0 r�ckgesetzt und die Anzeige benachrichtigt.
	 * 
	 */	 
	private void checkHour()
	{
		if(this.hour >= 24)
		{
			this.hour = 0;
			
			this.notifyObservers(
				new Notify(
						Commands.setDigit, 
						DigitToHandle.HOUR)
				);	
		}	
	}
	
	
	@Override
	protected void incrementHour()
	{
		if (this.hour<23)
			super.incrementHour();
		else
		{
			this.hour = 0;
			this.notifyObservers(
				new Notify(
						Commands.setDigit,
						DigitToHandle.HOUR)
				);
		}
	}
	
	
	/**
	 * TODO Javadoc
	 */
	private void simpleIncrementMinute()
	{
		if (this.minute<59)
			this.minute++;
		else
			this.minute = 0;
			
		this.notifyObservers(
			new Notify(
					Commands.setDigit,
					DigitToHandle.MINUTE)
			);
	}
}
