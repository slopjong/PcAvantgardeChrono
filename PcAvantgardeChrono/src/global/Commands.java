package global;

import boxmodel.annotation.Immutable;

/**
 * Enth�lt die Kommandos, die f�r das Protokoll zwischen <code>Observable</code> 
 * und den <code>Observer</code> verwendet werden k�nnen.
 * 
 * @author Romain Schmitz
 *
 */

@Immutable
public enum Commands {
	setDigit, 
	setBlink, 
	setDisplayChar,
	disableDigit
}
