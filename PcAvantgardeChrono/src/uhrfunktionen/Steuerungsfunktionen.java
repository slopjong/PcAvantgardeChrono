package uhrfunktionen;

/**
 * Dieses Interface beschreibt die "Kn�pfe" der Uhr, welche von der Uhr bet�tigt werden k�nnen.
 * F�r jede Methode gibt es ein entsprechender Button in der gui.
 * 
 * Dieses Interface wird von jeder Klasse implementiert, welche eine Uhrfunktion darstellt.
 * 
 * Uhrfunktionen k�nnen sein:
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
	 * Diese Methode wird ausgef�hrt, wenn der Button SET+ bet�tigt wurde.
	 *
	 */
	public void setPlus();
	
	
	/**
	 * Diese Methode wird ausgef�hrt, wenn der Button SET- bet�tigt wurde.
	 *
	 */
	public void setMinus();
	
	
	/**
	 * Diese Methode wird ausgef�hrt, wenn der Button MODE bet�tigt wurde.
	 *
	 */
	public void mode();
	
	
	/**
	 * Diese Methode wird ausgef�hrt, wenn der Button RESET bet�tigt wurde.
	 *
	 */
	public void reset();
	
}
