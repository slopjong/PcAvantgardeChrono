package global.synchronize;

import global.ExceptionFormatter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.logging.Logger;

import logger.LoggerFactory;


/**
 * NtpClient - an NTP client for Java.  This program connects to an NTP server
 * and prints the response to the console.
 * 
 * The local clock offset calculation is implemented according to the SNTP
 * algorithm specified in RFC 2030.  
 * 
 * Note that on windows platforms, the curent time-of-day timestamp is limited
 * to an resolution of 10ms and adversely affects the accuracy of the results.
 * 
 * 
 * This code is copyright (c) Adam Buckley 2004
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.  A HTML version of the GNU General Public License can be
 * seen at http://www.gnu.org/licenses/gpl.html
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
 * more details.
 *  
 * @author Adam Buckley
 */
public class SntpClient
{
	public static int[] getTime(String serverName) 
	throws IOException
	{
		assert (serverName != "") : 
			"Der SntpClient muﬂ ein Parameter mit der Serveradresse erhalten!";
		
		Logger logger = LoggerFactory.getLogger();
		
		// Send request
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(2000);
		InetAddress address = InetAddress.getByName(serverName);
		byte[] buf = new NtpMessage().toByteArray();
		DatagramPacket packet =
			new DatagramPacket(buf, buf.length, address, 123);
		
		// Set the transmit timestamp *just* before sending the packet
		// ToDo: Does this actually improve performance or not?
		NtpMessage.encodeTimestamp(packet.getData(), 40,
			(System.currentTimeMillis()/1000.0) + 2208988800.0);
		
		socket.send(packet);
		
		
		// Get response
		logger.info("NTP-Request an " + serverName +" gesendet, auf Antwort warten...");
		packet = new DatagramPacket(buf, buf.length);
		
		try
		{
			socket.receive(packet);
		}
		catch(SocketTimeoutException argKarg)
		{
			logger.warning("Internet-Synchronisation fehlgeschlagen");

		}
		
		
		// Process response
		NtpMessage msg = new NtpMessage(packet.getData());		
		socket.close();

		return msg.toInt();
	}
}
