package uhrfunktionen.zeitfunktionen;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


import global.Commands;
import global.DigitToHandle;
import global.ExceptionFormatter;
import global.Notify;



/** 
 * <code>Countdown</code> bietet die Funktionalität eines realen Countdowns an. 
 * Dies bedeutet, dass intern ein Zähler die Stunden und Minuten dekrementiert.
 * Die Zeit kann dabei eingestellt und der <code>Countdown</code> gestartet werden. 
 * Entsprechend kann der Countdown auch rückgesetzt werden, um die Zeit neu
 * einzustellen oder den <code>Countdown</code> abzubrechen.
 * 
 * @see Stoppuhr Stoppuhr
 * @see Uhr Uhr
 * 
 * @author Romain Schmitz
 */
public class Countdown
extends ZeitFunktionImpl
{	
	/**
	 * Dieser Timer ist nach außen hin für den Countdown ein Timer, der ihn
	 * taktet. Aus der Sichtweise des Timers selbst ist er ein TimerTask, der
	 * vom java-eigenen Timer {@link java.util.Timer} angeregt wird und den
	 * in der <code>run</code>-Methode spezifizierten Task ausführt.
	 * @author Romain Schmitz
	 *
	 */
	class Timer
	extends java.util.TimerTask
	implements java.lang.Cloneable
	{
		private boolean stopped;
		java.util.Timer timer;
		private int interval = 1000;// 1000 ms = 1 s
		
		public Timer()
		{
			this.stopped = true;
		}
		
		private void start()
		{
			if (this.stopped)
			{
				logger.info("Timer starten");
				this.stopped = false;
				
				/* 
				 * Neuen Timer erzeugen. Wenn der Countdown vorher schon lief und 
				 * vom Benutzer gestoppt wurde, so wurde der Timer mit cancel() 
				 * gestoppt. Dieser kann dann nicht mehr verwendet werden. Daher 
				 * ist eine neue Instanz von Timer notwendig. Obwohl Java mit 
				 * tausenden Timern gleichzeitig umgehen können soll (laut Java API),
				 * soll hier kein Timer laufen, wenn die Funktion Countdown vom 
				 * Benutzer nicht verwendet wird. 
				 */
			
				
				this.timer = new java.util.Timer("Countdown-Timer");
				try
				{					
					this.timer.scheduleAtFixedRate(
							// TimerTask klonen, damit immer auf einer
							// flachen Kopie (shallow copy) gearbeitet wird, 
							// weil ein bereits bearbeiteter (scheduled) TimerTask 
							// nicht mehr erneut verwendet werden darf.
							(Timer) this.clone(), 
							interval, 
							interval);
				}
				catch(CloneNotSupportedException e)
				{
					logger.severe(ExceptionFormatter.format(e));
				}
				catch(IllegalStateException e)
				{
					logger.severe(ExceptionFormatter.format(e));
				}
			}
			else
			{
				// Dieser Fall tritt nur auf, wenn der Countdown schon
				// gesetzt wurde und der Benutzer den Countdown erneut gesetzt
				// hat und der Countdow den Zustand CountdownGesetzt erneut
				// betritt.
				this.stop();
				this.start();
			}
		}
		
		
		private void stop()
		{
			if (!this.stopped)
			{
				logger.info("Timer stoppen");
				this.stopped = true;
				this.timer.cancel();
			}
			else
				// Diese Logging-Meldung mehrmals hintereinander auftreten, da
				// beim Setzen der Stunden und Minuten der Countdown immer mit
				// Reset rückgesetzt werden kann!
				logger.info("!! Timer schon gestoppt. !!");
		}
		
		public void run()
		{
			decrementSecond();
			
			if (isCountdownOver())
			{			
				this.stop();
				accusticOutput();
			}
		}
	}
	
	private Timer timer;
		
	
	public Countdown()
	{
		this.timer = new Timer();
	}
	
	/* TODO Javadoc
	 * @see uhrfunktionen.Steuerungsfunktion#mode()
	 */
	public void mode()
	{		
		switch(aktuellerZustand)
		{
			case CountdownStundenSetzen:
				
				aktuellerZustand = UhrsystemZustand.CountdownMinutenSetzen;
				enterStateOfActiveFunction();
				break;
					
			case CountdownMinutenSetzen:
				
				aktuellerZustand = UhrsystemZustand.CountdownGesetzt;
				enterStateOfActiveFunction();
				break;
				
			case CountdownGesetzt:
				
				aktuellerZustand = UhrsystemZustand.UhrZeiteinstellenStunde;
				setActiveFunction(basicUhr);
				enterStateOfActiveFunction();
				break;
				
			default: ;
					
		}
	}


	/* (non-Javadoc)
	 * @see uhrfunktionen.Steuerungsfunktion#reset()
	 */
	public void reset()
	{
		switch(aktuellerZustand)
		{
			case CountdownStundenSetzen:	
				
				this.timer.stop();
				break;
					
			case CountdownMinutenSetzen:
				
				this.timer.stop();
				break;
				
			case CountdownGesetzt:
				
				if (this.timer.stopped)
					this.timer.start();					
				else
					this.timer.stop();
				
				break;
				
			default: 
				
				logger.warning("Illegaler Zustand betreten in Countdown.reset()");
					
		}
	}


	/* (non-Javadoc)
	 * @see uhrfunktionen.Steuerungsfunktion#setMinus()
	 */
	public void setMinus()
	{
		switch(aktuellerZustand)
		{
			case CountdownStundenSetzen:
				
				this.simpleDecrementHour();
				break;
					
			case CountdownMinutenSetzen:
				
				this.simpleDecrementMinute();
				break;
				
			default: ;
					
		}
	}


	/* (non-Javadoc)
	 * @see uhrfunktionen.Steuerungsfunktion#setPlus()
	 */
	public void setPlus()
	{
		switch(aktuellerZustand)
		{
			case CountdownStundenSetzen:
				
				this.incrementHour();
				break;
					
			case CountdownMinutenSetzen:
				
				this.incrementMinute();
				break;
				
			default: ;
					
		}
	}

	
	/**
	 * Liefert ein einzelnes Zeichen zurück, welches den aktuellen Uhrmodus in der Anzeige signalisiert.
	 * 
	 * @return das Zeichen 'C', welches den aktuellen Uhrmodus in der Anzeige signalisiert.
	 */
	@Override
	public char getDisplayFunctionChar() 
	{
		return 'C';
	}
	
	
	@Override
	void enter()
	{
		// FIXME Anzeige aktualisieren.
		
		switch(aktuellerZustand)
		{
			case CountdownStundenSetzen:
				
				logger.info("Zustand 'Countdown Stunden setzen' betreten.");
				
				this.notifyObservers(
					new Notify(
							Commands.setDigit,
							DigitToHandle.HOUR
							)
					);
				
				this.notifyObservers(
					new Notify(
							Commands.setDigit,
							DigitToHandle.MINUTE
							)
					);	
				
				this.notifyObservers(
					new Notify(Commands.setDisplayChar)
					);
				
				this.hourBlink();
				break;
					
			case CountdownMinutenSetzen:

				logger.info("Zustand 'Countdown Minuten Setzen' betreten.");
				
				this.minuteBlink();
				break;
				
			case CountdownGesetzt:
				
				logger.info("Zustand 'Countdown Gesetzt' betreten.");	
				
				this.noBlink();
				this.timer.start();
				break;
				
			default: ;
					
		}
	}
	
	
	@Override
	void exit()
	{
		
	}
	
	
	/**
	 * Liefert zurück, ob der Countdown jetzt abgelaufen ist oder nicht.
	 * @return True, falls der Countdown abgelaufen ist, sonst false.
	 */
	private boolean isCountdownOver()
	{
		return this.hour == 0 && this.minute == 0 && this.second == 0;
	}
	
	
	/**
	 * TODO Javadoc
	 *
	 */
	private void resetTimeValues()
	{
		this.hour = 0;
		this.minute = 0;
		this.second = 0;
		this.hsecond = 0;
	}
	
	
	/**
	 * Benachrichtig die Observer (Anzeige, ...), dass alle Digits aktualisiert
	 * werden sollen.
	 *
	 */
	private void notifyAllDigits()
	{
		// FIXME Die Anzeige aktualisieren
	}
	
	
	/**
	 * Läßt das Stunden-Digit blinken.
	 *
	 */
	private void hourBlink()
	{
		this.notifyObservers(
			new Notify(
					Commands.setBlink, 
					DigitToHandle.HOUR)
			);		
	}
	
	
	/**
	 * Läßt das Minuten-Digit blinken.
	 *
	 */
	private void minuteBlink()
	{
		this.notifyObservers(
			new Notify(
					Commands.setBlink, 
					DigitToHandle.MINUTE)
			);		
	}
	
	
	/**
	 * Das Blinken ausschalten.
	 *
	 */
	private void noBlink()
	{
		// Erst müssen die Sekunden blinken, bevor das Blinken
		// abgeschaltet werden kann. Ein kleiner Design-Fehler beim
		// entworfenen State-Pattern für das Blinken der Digits. 
		this.notifyObservers(
			new Notify(
					Commands.setBlink, 
					DigitToHandle.SECOND)
			);
		
		this.notifyObservers(
			new Notify(
					Commands.setBlink, 
					DigitToHandle.NONE)
			);		
	}
	
	
	/**
	 * Dekrementiert die Stunden.
	 *
	 */
	private void decrementHour()
	{
		if (this.hour > 0)
		{
			this.hour--;
		
			this.notifyObservers(
				new Notify(
						Commands.setDigit, 
						DigitToHandle.HOUR)
				);
		}
	}
	
	
	/**
	 * Dekrementiert die Minuten.
	 *
	 */	
	private void decrementMinute()
	{
		if (this.minute > 0)
		{
			this.minute--;
		}
		else
		{
			if (this.hour > 0)
			{
				this.decrementHour();
				this.minute = 59;
			}
		}	
		
		this.notifyObservers(
			new Notify(
					Commands.setDigit, 
					DigitToHandle.MINUTE)
			);
	}
	
	/**
	 * Dekrementiert die Sekunden.
	 *
	 */	
	private void decrementSecond()
	{
		if (this.second > 0)
			this.second--;
		else
		{
			if (this.minute>0 || this.hour>0)
			{
				this.second = 59;
				this.decrementMinute();
			}
		}
		
		this.notifyObservers(
			new Notify(
					Commands.setDigit, 
					DigitToHandle.SECOND)
			);
	}
	
	
	private void simpleDecrementHour()
	{
		this.decrementHour();
	}
	
	
	private void simpleDecrementMinute()
	{
		if (this.minute > 0) 
		{
			this.minute--;
			
			this.notifyObservers(
				new Notify(
						Commands.setDigit, 
						DigitToHandle.MINUTE)
				);
		}
	}
	
	
	/**
	 * Erzeugt eine akkustische Ausgabe, falls der Countdown abgelaufen ist.
	 * @throws URISyntaxException 
	 *
	 */
	private void accusticOutput()
	{
		/* FIXME Tonsignalausgabe */
		logger.info("Countdown abgelaufen.");
		
		try {
			AudioClip clip =Applet.newAudioClip(new URL("http://localhost/gse/plan.wav"));
			clip.play();
			Thread.sleep(2000);
			clip =Applet.newAudioClip(new URL("http://localhost/gse/sound_monkey.wav"));
			clip.play();
			Thread.sleep(4000);
			clip =Applet.newAudioClip(new URL("http://localhost/gse/reminder.wav"));
			clip.play();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
