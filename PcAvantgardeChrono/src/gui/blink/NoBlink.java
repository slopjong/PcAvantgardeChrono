package gui.blink;

import global.DigitToHandle;


/**
 * <code>BlinkNone</code> stellt den nicht blinkenden Zustand 
 * der Digitalanzeige dar.
 * 
 * @see gui.blink.Blink Blink
 * 
 * @author Romain Schmitz
 * 
 */
class NoBlink
extends Blink
{
	
	/**
	 * <code>enter</code> wird aufgerufen, wenn der Zustand geändert werden soll.
	 * Diese Methode ist Teil des State-Pattern, d.h. hier werden Aktionen ausgeführt,
	 * wenn dieser Zustand betreten wird.
	 * 
	 * In dieser Klasse <code>BlinkNone</code> stoppt <code>enter</code> den Thread,
	 * welcher die Sekunden blinken läßt. 
	 *
	 */
	void enter()
	{
		logger.info("Blink-Zustand 'BlinkNone' betreten");
	}
	
	
	void exit()
	{
		
	}
	
	
	/**
	 * <code>processEvent</code> ändert den Zustand. Je nach Event-Ereignis wird
	 * der entsprechende Zustand betreten. 
	 * 
	 * Der nächste Zustand ist "Stunden blinken".
	 */
	public Blink processEvent(DigitToHandle event)
	{		
		assert (event != null) : "event darf nicht null sein!";
		
		logger.info("processEvent witch event '" + event.name() + "'");
		
		switch(event)
		{
			case HOUR:				
					hourBlink.enter();
					return (Blink) hourBlink;
					
			default: 
				
				logger.severe("Illegalen Zustand betreten");
				String msg = "Unbekanntes Event " + event;
				throw new IllegalArgumentException(msg);
		}
	}
	
	/**
	 * Tut nichts hier!!
	 */
	public void run()
	{		
		/* Do nothing */
	}
}