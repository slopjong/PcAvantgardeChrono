package gui.blink;

import global.DigitToHandle;
import global.ExceptionFormatter;

class HourBlink
extends Blink
{
	// Aktiver Thread zum Blinken
	private Thread thread;
	
	
	/**
	 * <code>enter</code> wird aufgerufen, wenn der Zustand ge�ndert werden soll.
	 * Diese Methode ist Teil des State-Pattern, d.h. hier werden Aktionen ausgef�hrt,
	 * wenn dieser Zustand betreten wird.
	 * 
	 * In dieser Klasse <code>BlinkHour</code> wird der Thread gestartet,
	 * welcher die Stunden blinken l��t.
	 *
	 */
	void enter()
	{		
		logger.info("Blink-Zustand 'BlinkHour' betreten");
		logger.info("Neuer Thread f�r Stunden-Blinken erzeugen und starten");
		
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	
	void exit()
	{		
		logger.info("Thread stoppen");
		
		// Ist zwar unsch�n, aber Thread-Safe-Problem spielt hier keine Rolle.
		// Blinken ist eine ungef�hrliche und nebens�chliche Funktionalit�t.
		this.thread.stop();
	}
	
	/**
	 * <code>processEvent</code> �ndert den Zustand. Je nach Event-Ereignis wird
	 * der entsprechende Zustand betreten.
	 * 
	 * Der n�chste Zustand ist "Minuten blinken".
	 */
	@Override
	public Blink processEvent(DigitToHandle event)
	{		
		switch(event)
		{
			case MINUTE:
					this.exit();
					minuteBlink.enter();
					return (Blink) minuteBlink;
					
			default: 
				
				logger.severe("Illegalen Zustand betreten");
				String msg = "Unbekannter Event" + event;
				throw new IllegalArgumentException(msg);
		}
	}
	
	
	/**
	 * TODO Javadoc aktualisieren
	 * Diese Methode l��t das Stunden-Digit blinken.
	 */
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.sleep(this.intervall);
				lhour.setVisible(false);
				Thread.sleep(this.intervall);
				lhour.setVisible(true);				
			}
			catch(InterruptedException e)
			{
				logger.warning(ExceptionFormatter.format(e));
			}
		}
	}
}