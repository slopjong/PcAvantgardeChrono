package uhrfunktionen.zeitfunktionen;

import java.util.Observable;
import java.util.Observer;

class TimeObserver
extends Observable
{
	public void addObserver(Observer observer)
	{
		super.addObserver(observer);
	}
	
	protected void setChanged()
	{
		super.setChanged();
	}
	
	
	/**
	 * Benachrichtigt den Observer. Es wird keine <code>NullPointerException</code>
	 * geworfen, falls es keinen Observer gibt.
	 */
	public void notifyObservers(Object obj)
	{
		setChanged();
		super.notifyObservers(obj);
	}	
	
}