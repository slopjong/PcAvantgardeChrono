package gui.blink;

import global.DigitToHandle;
import global.ExceptionFormatter;

class MinuteBlink
extends Blink
{
	private Thread thread;
	
	/**
	 * <code>enter</code> wird aufgerufen, wenn der Zustand geändert werden soll.
	 * Diese Methode ist Teil des State-Pattern, d.h. hier werden Aktionen ausgeführt,
	 * wenn dieser Zustand betreten wird.
	 * 
	 * In dieser Klasse <code>BlinkMinute</code> stoppt <code>enter</code> den Thread,
	 * welcher die Stunden blinken läßt. Womöglich sind die Minuten nicht sichtbar,
	 * weil der Thread zu einem ungünstigen Zeitpunkt gestoppt wurde. Daher wird das
	 * Label wieder als sichtbar markiert. Anschließend wird der Thread gestartet,
	 * welcher die Minuten blinken läßt.
	 *
	 */
	void enter()
	{
		logger.info("Blink-Zustand 'BlinkMinute' betreten");
		logger.info("Neuer Thread für Minuten-Blinken erzeugen und starten");
		
		lhour.setVisible(true);
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	
	void exit()
	{
		logger.info("Thread stoppen");
		
		this.thread.stop();
		lminute.setVisible(true);
	}
	
	
	/**
	 * <code>processEvent</code> ändert den Zustand. Je nach Event-Ereignis wird
	 * der entsprechende Zustand betreten. 
	 * 
	 * Der nächste Zustand ist "Sekunden blinken".
	 */
	public Blink processEvent(DigitToHandle event)
	{		
		switch(event)
		{
			case SECOND:
					
					this.exit();
					secondBlink.enter();
					return (Blink) secondBlink;
					
			default: 
				
				logger.severe("Illegalen Zustand betreten");
				String msg = "Unbekannter Event" + event;
				throw new IllegalArgumentException(msg);
		}
	}
	
	
	/**
	 * Diese von Thread vererbte Methode läßt das Minuten-Digit blinken.
	 */
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.sleep(this.intervall);
				lminute.setVisible(false);
				Thread.sleep(this.intervall);
				lminute.setVisible(true);				
			}
			catch(InterruptedException e)
			{
				logger.warning(ExceptionFormatter.format(e));
			}
		}
	}
	
}