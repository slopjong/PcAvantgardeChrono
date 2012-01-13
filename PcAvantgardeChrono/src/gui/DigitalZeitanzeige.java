package gui;

import global.DigitToHandle;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.Observable;
import global.Observer;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;

import logger.LoggerFactory;
import uhrfunktionen.Funktion;
import uhrfunktionen.ZeitFunktion;
import global.Notify;
import gui.blink.Blink;


/**
 * 
 * @author Romain Schmitz
 */
public class DigitalZeitanzeige
extends JPanel
implements Observer
{
	/**
	 * Unique ID zum Serialisieren
	 */
	private static final long serialVersionUID = -7133747403197649356L;
	
	private Logger logger = LoggerFactory.getLogger();
	
	/**
	 * 
	 */
	private JLabel lhour = new JLabel("00");
	private JLabel lminute = new JLabel("00");	
	private JLabel lsecond = new JLabel("00");
	private JLabel lhsecond = new JLabel("00");
	private JLabel lDisplayChar = new JLabel("");

	/**
	 * Speichert den aktuellen Blinkzustand. Initial ist der Zustand 
	 * nicht blinkend.
	 * 
	 * Es können folgende Zustände angenommen werden:
	 * 
	 * 	<ul>
	 * 		<li>nicht blinkend</li>
	 * 		<li>Stunden blinken</li>
	 * 		<li>Minuten blinken</li>
	 * 		<li>Sekunden blinken</li>
	 * 	</ul>
	 */
	private Blink aktuellerBlinkZustand = Blink.init(lhour, lminute, lsecond, lhsecond);

	/**
	 * Erzeugt die Digitalanzeige zur Darstellung der Zeit. Dabei 
	 * @param funktion
	 */
	public DigitalZeitanzeige()
	{
		// Schriftart ändern, dies ist aber rein optional.
		// Dient nur zur Verschönerung.
		this.lhour.setFont(new Font("Andale Mono",Font.BOLD,30));
		this.lminute.setFont(new Font("Andale Mono",Font.BOLD,30));
		this.lsecond.setFont(new Font("Andale Mono",Font.BOLD,30));
		this.lhsecond.setFont(new Font("Andale Mono",Font.BOLD,30));
		this.lDisplayChar.setFont(new Font("Andale Mono",Font.BOLD,30));
		
		// LayoutManager setzen
        this.setLayout(new GridLayout(1,8));
        
		// Hier werden die Digits dem Panel hinzugefügt 
        this.add(this.lDisplayChar);
        this.add(this.lhour);
		this.add(this.factoryDoppelpunkt());
		this.add(this.lminute);
		this.add(this.factoryDoppelpunkt());
		this.add(this.lsecond);
		this.add(this.factoryDoppelpunkt());
		this.add(this.lhsecond);
		
		logger.info("Digitalanzeige als Observer anmelden...");
		
		Funktion.getActiveTimeFunction().addObserver(this);	
	}
	
	
	/**
	 * Erzeugt ein neues JLabel mit dem Text ":". Diese Methode wurde erstellt, 
	 * um an einer einzigen Stelle das Trennzeichen zwischen den Stunden und 
	 * Minuten bzw. zwischen den Minuten und Sekunden anzugeben. Außerdem braucht 
	 * nur an dieser Stelle der Text formatiert zu werden.
	 * 
	 * @return ein JLabel mit ":" als Text
	 */
	private JLabel factoryDoppelpunkt()
	{
		JLabel label = new JLabel(":");
		label.setFont(new Font("Andale Mono",Font.BOLD,30));
		return label;
	}
	
	
	/**
	 * <cod>setBlink</code> läßt ein Digit aufblinken. Dazu wird der gewünschte 
	 * Zustand als Parameter übergeben. Diese Methode läßt allerdings nur eine 
	 * bestimmte Reihenfolge zu.
	 * 
	 * Die Reihenfolge lautet "Stunden blinken -> Minuten blinken -> Sekunden blinken -> Nichts blinkt -> Stunden blinken -> ... "
	 * 
	 * 
	 * Mögliche Werte für event sind:
	 * 
	 * <ul>
	 * <li>HOUR</li>
	 * <li>MINUTE</li>
	 * <li>SECOND</li>
	 * <li>HSECOND</li>
	 * <li>NONE</li>
	 * </ul>
	 * 
	 * @see DigitToHandle
	 * 
	 */
	private void setBlink(DigitToHandle digit)
	{
		assert (this.aktuellerBlinkZustand != null) : 
			"aktuellerBlinkZustand darf nie null sein!";
		
		//aktuellerBlinkZustand
		this.aktuellerBlinkZustand = this.aktuellerBlinkZustand.processEvent(digit);
	}
	
	
	/**
	 * Aktualisiert die Anzeige der Stunden, der Minuten oder der Sekunden.
	 * Es wird jeweils nur ein einziges Digit gleichzeitig aktualisiert.
	 * Dazu wird das Objekt <code>obj</code> ausgewertet. In diesem steckt
	 * die benötigte Information, welches Digit aktualisiert werden soll.
	 * 
	 */
	public void update(Notify notify)
	{
		logger.fine("Digitalanzeige wird benachrichtigt...");
		
		assert (notify != null) : "Parameter obj darf nicht NULL sein!";
		
		// Stimmt jetzt nicht mehr, weil jetzt über das Interface
		// zugegriffen wird.
		//assert (obj.getClass().getName().equals("global.Notify")) :
		//	"Parameter obj muss vom statischen Datentyp Notify sein!";
		
		switch(notify.getCommand())
		{
			case setDisplayChar:
					
					logger.info("Display Character setzen...");
					char c = Funktion.getActiveTimeFunction().getDisplayFunctionChar();
					this.lDisplayChar.setText(Character.toString(c));
					break;
					
			case setBlink:
				{ // geschweifte Klammern, wegen mehrfachem Deklarieren von digit
				
					DigitToHandle digit = (DigitToHandle) notify.getValue();
					assert (digit != null) : 
						"Mit dem Kommando setBlink muß ein Datum vom Datentyp " +
						"DigitToHandle übertragen werden. Dieses Datum darf " +
						"nicht null sein!";
					
					//logger.info("Ein Digit blinken lassen...");
					
					this.setBlink(digit);
					break;
				}	
			
			case setDigit:
				{ // geschweifte Klammern, wegen mehrfachem Deklarieren von digit
				
					DigitToHandle digit = (DigitToHandle) notify.getValue();
					
					// FIXME Sollte diese Zusicherung nicht schon vor der Zuweisung
					// stehen. Wird, wenn notify.getValue() null zurückliefert,
					// eine NullPointerException geworfen?
					assert (digit != null) : "Digit darf nicht null sein!";
					
					// FIXME Hier müsste eigentlich einen niedrigeren Level verwendet
					// werden, da dies sehr oft geloggt wird und in der Regel
					// abgeschaltet werden soll. Allerdings funktioniert es nicht
					// mit fine() oder niedriger, wieso nicht?
					logger.info("Digit " + digit.name() + " aktualisieren");
					
					ZeitFunktion activeTimeFunction = Funktion.getActiveTimeFunction();
					
					switch(digit)
					{
						case HOUR:
							
							int hour = activeTimeFunction.getHour();
							this.lhour.setText((hour<10 ? "0" : "") + hour);
							break;
							
						case MINUTE:
							
							int minute = activeTimeFunction.getMinute();
							this.lminute.setText((minute<10 ? "0" : "") + minute);
							break;
							
						case SECOND:
							
							int second = activeTimeFunction.getSecond();
							this.lsecond.setText((second<10 ? "0" : "") + second);
							break;
							
						case HSECOND:
							
							int hsecond = activeTimeFunction.getHSecond();
							this.lhsecond.setText((hsecond<10 ? "0" : "") + hsecond);
							break;
					}
					
					break;					
				}// end case
				
			case disableDigit:
				
					break;
		}
	}
}
