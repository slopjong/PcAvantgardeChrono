package global;

import boxmodel.annotation.Immutable;

/**
 * Enthält Eigenschaften, welche als Zustand gespeichert werden könne, um
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