package uhrfunktionen;

/**
 * Dieses Interface beschreibt die "Knöpfe" der Uhr, welche von der Uhr betätigt werden können.
 * Für jede Methode gibt es ein entsprechender Button in der gui.
 * 
 * Dieses Interface wird von jeder Klasse implementiert, welche eine Uhrfunktion darstellt.
 * 
 * Uhrfunktionen können sein:
 * 
 * <ul>
 * <li>Uhr</li>
 * <li>Stoppuhr</li>
 * <li>Countdown</li>
 * <li>...</li>
 * </ul>
 * 
 * 
 * @author Romain Schmitz
 *
 */
public interface Steuerungsfunktionen
{

	/**
	 * Diese Methode wird ausgeführt, wenn der Button SET+ betätigt wurde.
	 *
	 */
	public void setPlus();
	
	
	/**
	 * Diese Methode wird ausgeführt, wenn der Button SET- betätigt wurde.
	 *
	 */
	public void setMinus();
	
	
	/**
	 * Diese Methode wird ausgeführt, wenn der Button MODE betätigt wurde.
	 *
	 */
	public void mode();
	
	
	/**
	 * Diese Methode wird ausgeführt, wenn der Button RESET betätigt wurde.
	 *
	 */
	public void reset();
	
}
