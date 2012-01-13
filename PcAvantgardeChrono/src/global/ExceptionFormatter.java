package global;

import boxmodel.annotation.Immutable;

/**
 * Die einzige Aufgabe dieser Klassse ist Exceptions in eine lesbare Form f�r den 
 * Logger zu formatieren. Daher ist der Konstruktor nach au�en hin nicht sichtbar,
 * um die Methode <code>format</code> �ber den statischen Weg aufrufen zu
 * m�ssen.
 * 
 * @author Romain Schmitz
 *
 */
@Immutable
public class ExceptionFormatter 
{
	/**
	 * Der Konstruktor ist nach au�en hin nicht sichtbar, damit diese Klasse
	 * nicht instanziiert werden kann. Die Klasse soll immer �ber den statischen
	 * Weg verwendet werden. 
	 *
	 */
	private ExceptionFormatter()
	{
		
	}
	
	
	/**
	 * Formatiert die �bergebene Exception <code>e</code> in eine lesbare Form
	 * mit Zeilenumbr�chen.
	 * 
	 * @param e Eine Exception, welche f�r den Logger formatiert wird.
	 * @return Eine formatierte Exception als Zeichenkette.
	 */
	public static String format(Exception e)
	{
		StringBuffer output = new StringBuffer();
		
		output.append(e.getMessage() + "\n\n");
		
		for (StackTraceElement element : e.getStackTrace())
			output.append("     " + element + "\n");
		
		return output.toString();
	}
}
