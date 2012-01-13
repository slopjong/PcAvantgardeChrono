package global;

import boxmodel.annotation.Immutable;

/**
 * Enth�lt Eigenschaften, welche als Zustand gespeichert werden k�nne, um
 * eine entsprechende Stelle der Digitalanzeige blinken zu lassen.
 *  
 * @author Romain Schmitz
 *
 */
@Immutable
public enum DigitToHandle
{
	HOUR,
	MINUTE,
	SECOND,
	HSECOND,
	NONE;
}