package main;

import gui.Gui;

import java.util.logging.Logger;

import logger.LoggerFactory;
import uhrfunktionen.Funktion;

/**
 * 
 * Diese Klasse ist der Loader des Systems. Diese Klasse enthält daher die 
 * main-Methode, welche für das Starten des Systems aufgerufen wird.
 * 
 * @author Romain Schmitz
 *
 */
public class Uhrsystem
extends Thread
{

	/**
	 * Diese Methode startet das Uhrsystem, indem die Uhrfunktionen 
	 * initialisiert und die Gui erstellt werden.
	 * 
	 * @param args An das Programm übergebene Parameter
	 */
	public static void main(String[] args)
	{		
		Logger logger = LoggerFactory.getLogger();
		
		logger.info("Funktion initialisieren...");
		// Funktion initialisieren
		Funktion.init();

		logger.info("Gui initialisieren...");
		// Gui initialisieren
		new Gui();
	}

}
