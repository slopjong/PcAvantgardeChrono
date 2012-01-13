package global;

import boxmodel.annotation.Immutable;

/**
 * Enthält die Kommandos, die für das Protokoll zwischen <code>Observable</code> 
 * und den <code>Observer</code> verwendet werden können.
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
