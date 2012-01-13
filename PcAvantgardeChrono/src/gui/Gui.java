package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import uhrfunktionen.Funktion;
import uhrfunktionen.ZeitFunktion;

/**
 * Diese Klasse stellt die eigentliche Gui dar. Die Gui wird im Loader 
 * ({@link main.Uhrsystem}) erzeugt. Die Gui selbst enthält sowohl die 
 * Digitalanzeige ({@link DigitalZeitanzeige}) für die Zeit als auch die Buttons
 * für <code>mode</code>, <code>set+</code>, <code>set-</code> und <code>reset</code>.
 * 
 * @author Romain Schmitz
 *
 */
public class Gui
extends JFrame
{	
	/**
	 * Unique ID zum Serialisieren
	 */
	private static final long serialVersionUID = -3964125189576502585L;


	/**
	 * Ruft die Methode createGui intern auf mittels der Methode invokeLater aus
	 * der <code>SwingUtilities</code>-Klasse.
	 * 
	 * @param activeTimeFunction Aktuell aktive Zeitfunktion
	 */
	public Gui()
	{				
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Siehe hierzu http://java.sun.com/docs/books/tutorial/uiswing/start/compile.html
				// das Beispiel HelloWorldSwing.java
				createGui();
			}
		});
	}
	
	
	/**
	 * Erzeugt die Gui.
	 *
	 */
	private void createGui()
	{
        // Standard-LookAndFeel setzen
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Fenster erzeugen
        JFrame frame = new JFrame("PcAvantgardeChrono");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layoutmanager festlegen
        frame.setLayout(new FlowLayout());
        
        // Digitalanzeige hinzufügen
        frame.getContentPane().add(new DigitalZeitanzeige());
        
        // mode-Button erzeugen
        JButton mode = new JButton("mode");
        mode.addActionListener(
				new ActionListener(){
						public void actionPerformed(ActionEvent e){
							Funktion.getActiveTimeFunction().mode(); }
					}
				);
        frame.getContentPane().add(mode);
        
        // reset-Button erzeugen
        JButton reset = new JButton("reset");
        reset.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Funktion.getActiveTimeFunction().reset(); }
				}
			);        
        frame.getContentPane().add(reset);
        
        // set+ Button erzeugen
        JButton setPlus = new JButton("set+");
        setPlus.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Funktion.getActiveTimeFunction().setPlus(); }
				}
			);
        frame.getContentPane().add(setPlus);
        
        // set- Button erzeugen
        JButton setMinus = new JButton("set-");
        setMinus.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Funktion.getActiveTimeFunction().setMinus(); }
				}
			);
        frame.getContentPane().add(setMinus);
              
        // Fenster anzeigen
        frame.pack();
        frame.setVisible(true);
	}
}
