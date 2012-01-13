package global;

import boxmodel.annotation.Immutable;

/**
 * 
 * Diese Klasse repräsentiert die Nachricht, welche mittels der
 * <code>notify</code>-Methode an die Beobachter gesendet wird.
 * 
 * Ein solches Objekt enthält ein Kommando. Es kann optional
 * noch Daten enthalten.
 * 
 * @author Romain Schmitz
 *
 */
@Immutable
public class Notify 
{
	
	private Commands command = null;
	private Object value = null;	
	
	public Notify(Commands command)
	{
		assert(command != null) : "Kommando darf nicht null sein!";
		
		this.command = command;
	}
	
	
	public Notify(Commands command, Object value)
	{
		assert(command != null) : "Kommando darf nicht null sein!";
		
		this.command = command;
		this.value = value;
	}
	
	
	public Commands getCommand()
	{
		return this.command;
	}
		
	
	public Object getValue()
	{
		return this.value;
	}	
	
}
