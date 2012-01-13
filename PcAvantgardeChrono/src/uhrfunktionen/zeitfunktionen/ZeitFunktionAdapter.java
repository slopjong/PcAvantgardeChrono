package uhrfunktionen.zeitfunktionen;

import uhrfunktionen.ZeitFunktion;

// Modifikator auskommentiert am 10. Jan 07
// Ich glaube das überlebte noch aus früheren Zeiten
public /*abstract*/ class ZeitFunktionAdapter {
	
	public static void init()
	{
		ZeitFunktionImpl.init();
	}
	
	public static ZeitFunktion getActiveFunction()
	{
		return (ZeitFunktion) ZeitFunktionImpl.getActiveFunction();
	}
}
