package global;

import boxmodel.annotation.Immutable;

/**
 * Die einzige Aufgabe dieser Klassse ist Exceptions in eine lesbare Form für den 
 * Logger zu formatieren. Daher ist der Konstruktor nach außen hin nicht sichtbar,
 * um die Methode <code>format</code> über den statischen Weg aufrufen zu
 * müssen.
 * 
 * @author Romain Schmitz
 *
 */
@Immutable
public class ExceptionFormatter 
{
	/**
	 * Der Konstruktor ist nach außen hin nicht sichtbar, damit diese Klasse
	 * nicht instanziiert werden kann. Die Klasse soll immer über den statischen
	 * Weg verwendet werden. 
	 *
	 */
	private ExceptionFormatter()
	{
		
	}
	
	
	/**
	 * Formatiert die übergebene Exception <code>e</code> in eine lesbare Form
	 * mit Zeilenumbrüchen.
	 * 
	 * @param e Eine Exception, welche für den Logger formatiert wird.
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
