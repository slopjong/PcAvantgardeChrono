package uhrfunktionen;
import uhrfunktionen.zeitfunktionen.ZeitFunktionAdapter;


/**
 * TODO: Javadoc aktualisieren
 * Diese abstrakte Klasse ist Oberklasse für die (Grund-)Ausstattungen der
 * Uhr. Sie ist Teil des Controllers bezogen auf das MVC-Prinzip. Sie wurde
 * abstrakt definniert, um die Methoden aus der Schnittstellendefinition des
 * Interfaces Steuerungsfunktionen nicht implementieren zu müssen. Nicht
 * abstrakte Unterklassen müssen diese entsprechend implementieren. Aus dieser
 * kann mittels eines Getters auf die aktive Funktion der Uhr zugegriffen werden.
 * Andere Systemkomponenten stehen nur mit dieser Klasse als Repräsentant für
 * die aktive Uhrfunktion im Zusammenhang.
 * 
 * @author Romain Schmitz
 * 
 */
public class Funktion
{	
	public static void init()
	{
		ZeitFunktionAdapter.init();
	}
	
	public static ZeitFunktion getActiveTimeFunction()
	{
		return ZeitFunktionAdapter.getActiveFunction();
	}
	
}
