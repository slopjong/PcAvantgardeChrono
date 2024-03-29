package gui.blink;

import global.DigitToHandle;

import java.util.logging.Logger;

import javax.swing.JLabel;

import logger.LoggerFactory;


/**
 * <code>Blink</code> ist eine abstrakte Klasse, welche als "Schablone" f�r einen 
 * bestimmten Zustand dient. Diese Klasse zusammen mit den Subklassen realisieren 
 * das State-Pattern. Speziell an dieser Klasse ist, dass sie Thread spezialisiert. 
 * Ein Zustand bedeutet in diesem konkreten Kontext, dass eine Anzeige blinken
 * soll oder eben nicht blinken soll. Dazu werden in den Subklassen entsprechend 
 * die Threads gestartet und wieder gestoppt. 
 * 
 * Der initiale Zustand ist "nicht blinkend".
 * 
 * @author Romain Schmitz
 *
 */
public abstract class Blink
implements Runnable
{
	/**
	 * Speichert das Zeitintervall, welches f�r das Blinken verwendet wird.
	 */
	int intervall = 500;
	
	
	/**
	 * Speichert den Zustand "Stunden blinken"
	 */
	static HourBlink hourBlink = new HourBlink();
	
	
	/**
	 * Speichert den Zustand "Minuten blinken"
	 */
	static MinuteBlink minuteBlink = new MinuteBlink();
	
	
	/**
	 * Speichert den Zustand "Sekunden blinken"
	 */
	static SecondBlink secondBlink = new SecondBlink();
	
	
	/**
	 * Speichert den Zustand "Nichts blinkt"
	 */
	static NoBlink noBlink = new NoBlink();

	
	static Logger logger = LoggerFactory.getLogger();
	
	static JLabel lhour = null;
	static JLabel lminute = null;
	static JLabel lsecond = null;
	static JLabel lhsecond = null;
	
	
	/**
	 * Diese statische Methode initialisiert den blindenden Zustand der Anzeige.
	 * Initial wird der Zustand "Nichts blinkt" zur�ckgeliefert.
	 * <code>init</code> soll nur w�hrend dem Erstellen der Digitalanzeige aufgerufen
	 * werden. D.h. sie soll nur einmal aufgerufen werden n�mlich beim Initialisieren.
	 * 
	 * @param hour das Stunden-Label
	 * @param minute das Minuten-Label
	 * @param second das Sekunden-Label
	 * @param hsecond das Hundertstelsekunden-Label
	 * 
	 * @return den initialen Zustand "Nichts blinkt"
	 */
	public static Blink init(JLabel hour, JLabel minute, JLabel second, JLabel hsecond)
	{
		logger.info("Blink-Zustand der Digitalanzeige initialisieren");
		
		lhour = hour;
		lminute = minute;
		lsecond = second;
		lhsecond = hsecond;
		
		noBlink.enter();
		return (Blink) noBlink;
	}
	
	
	/**
	 * Spezifiziert die <code>enter</code>-Methode, welche von den Subklassen 
	 * �berschrieben werden mu�. Diese Methode wird genau dann aufgerufen, wenn 
	 * ein neuer Zustand betreten wird.
	 *
	 */
	abstract void enter();

	
	/**
	 * Spezifiziert die <code>exit</code>-Methode, welche von den Subklassen 
	 * �berschrieben werden mu�. Diese Methode wird aufgerufen, wenn ein Zustand
	 * "beendet" wird.
	 *
	 */
	abstract void exit();
	
	
	/**
	 * Spezifiziert die <code>processEvent</code>-Methode, welche aufgerufen wird,
	 * wenn sich der Zustand der Digitalanzeige �ndern soll.
	 * 
	 * @param event
	 * 
	 */
	public abstract Blink processEvent(DigitToHandle event);
	
}