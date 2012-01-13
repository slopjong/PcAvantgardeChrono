package uhrfunktionen;

import global.Commands;
import global.DigitToHandle;
import global.Notify;
import global.Notify;
import global.Observer;

import java.util.ArrayList;
import java.util.logging.Logger;

import logger.LoggerFactory;



public class Observable
{
	/**
	 * Speichert alle Observer. Dieses Attribut ist als Klassenattribut
	 * deklariert, weil <code>ZeitFunktionImpl</code> diese Klasse spezialisiert
	 * und somit auch jede Uhrfunktion. So können alle die gleiche Liste
	 * verwenden. Allerdings ist Vorsicht geboten, wenn später noch Datumfunktionen
	 * implementiert werden. Dann sollte diese Klasse instanziiert werden und die
	 * Referenz in den Klassen <code>ZeitFunktionImpl</code> und in weiteren
	 * gespeichert werden. (Jeweils eine Referenz pro ZeitFunktionImpl,
	 * DatumFunktionImpl usw.) 
	 */
	private static ArrayList<Observer>  list = new ArrayList<Observer>();
	
	private Logger logger = LoggerFactory.getLogger();
	
	
	/**
	 * Fügt <code>observer</code> in die interne Liste hinzu.
	 * @param observer Observer für Zeit-, Datum- oder sonstigen Änderungen.
	 */
	public void addObserver(Observer observer)
	{
		list.add(observer);
		
		// Die Observer unmittelbar nach der Anmeldung
		// benachrichtigen, damit die auf dem aktuellsten Stand sind.
		observer.update( 
			new Notify(
				Commands.setDigit, 
				DigitToHandle.HOUR)
			);
		
		observer.update( 
				new Notify(
					Commands.setDigit, 
					DigitToHandle.MINUTE)
				);
		
		observer.update(
				new Notify(
					Commands.setDigit, 
					DigitToHandle.SECOND)
				);
	}
	
	
	/**
	 * Entfernt <code>observer</code> aus der internen Liste. Dieser wird
	 * danach über keine Änderung mehr benachrichtigt.
	 * 
	 * @param observer Observer für Zeit-, Datum- oder sonstigen Änderungen.
	 */
	public void removeObserver(Observer observer)
	{
		// TODO Nachschauen, ob auch wirklich genau das Objekt entfernt wird,
		// welches als Parameter übergeben wurde. Ich hoffe ja...
		// ...solange kein Observer entfernt wird, spielt dies keine Rolle :-)
		list.remove(observer);
	}
	
	
	/**
	 * Benachrichtigt alle Observer aus der internen Liste. Über ein
	 * <code>Notify</code>-Objekt, wird dem Observer entsprechend mitgeteilt,
	 * was geändert wurde. Diese Mitteilungen basieren auf einem Protokoll.
	 * In solch einem Objekt werden ein Kommando und gegebenenfalls weitere
	 * Steuer- oder Dateninformationen.
	 * 
	 * @see Commands
	 * @see Notify
	 * @see DigitToHandle
	 * 
	 * @param notify Ein Steuerungsobjekt
	 */
	public void notifyObservers(Notify notify)
	{
		if (!list.isEmpty())
		{
			for(Observer observer : list)
				observer.update(notify);
		}
		else
			logger.info("Noch keine Observer angemeldet");
	}
}
