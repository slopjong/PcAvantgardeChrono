package uhrfunktionen;

import global.Observer;

public interface ZeitFunktion
extends Steuerungsfunktionen
{
	
	/**
	 * F�gt einen Beobachter (Observer) f�r das Horchen auf eine �nderung der Stunden hinzu.
	 * 
	 * @param obs Beobachter, welcher auf eine �nderung der Stunden wartet.
	 */
 	public void addObserver(Observer obs);
	
 	
	/**
	 * Liefert die Studentenanzahl als Ganzzahl zur�ck.
	 * @return Studenten
	 */
	public int getHour();
	
	
	/**
	 * Liefert die Minutenanzahl als Ganzzahl zur�ck.
	 * @return Minuten
	 */
	public int getMinute();
	
	
	/**
	 * Liefert die Sekundenanzahl als Ganzzahl zur�ck.
	 * @return Sekunden
	 */
	public int getSecond();
	
	
	/**
	 * Liefert die Hundertstelsekunden als Ganzzahl zur�ck.
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