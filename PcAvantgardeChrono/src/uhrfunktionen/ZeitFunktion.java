package uhrfunktionen;

import global.Observer;

public interface ZeitFunktion
extends Steuerungsfunktionen
{
	
	/**
	 * Fügt einen Beobachter (Observer) für das Horchen auf eine Änderung der Stunden hinzu.
	 * 
	 * @param obs Beobachter, welcher auf eine Änderung der Stunden wartet.
	 */
 	public void addObserver(Observer obs);
	
 	
	/**
	 * Liefert die Studentenanzahl als Ganzzahl zurück.
	 * @return Studenten
	 */
	public int getHour();
	
	
	/**
	 * Liefert die Minutenanzahl als Ganzzahl zurück.
	 * @return Minuten
	 */
	public int getMinute();
	
	
	/**
	 * Liefert die Sekundenanzahl als Ganzzahl zurück.
	 * @return Sekunden
	 */
	public int getSecond();
	
	
	/**
	 * Liefert die Hundertstelsekunden als Ganzzahl zurück.
	 * @return Hundertstelsekunden
	 */
	public int getHSecond();

	
	/**
	 * Diese Methode liefert ein einzelnes Zeichen, welches
	 * am Display angezeigt wird, um dem Benutzer den aktuellen
	 * Modus zu signalisieren
	 * @return Zeichen, welches die aktuelle Funktion am Display signalisiert.
	 */
	public char getDisplayFunctionChar(); 
	
}