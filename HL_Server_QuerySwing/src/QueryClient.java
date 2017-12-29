import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class QueryClient {
	private DatagramSocket socket;
    private InetAddress address;   
 
    private byte[] buf;
 
    public QueryClient() throws SocketException {
        socket = new DatagramSocket();     
        
    }
 
    
    public String sendReceiveQuery(String IP, String port) throws UnknownHostException, IOException {
    	
    	address = InetAddress.getByName(IP);   
        byte[] receiveData = new byte[1024];
        
    	// QUERY-String zusammenbauen
    	// Data 	Type 	Value
    	// Header 	4 x Hex 0xFF + byte 	'T' (0x54)
    	// Payload 	string 	"Source Engine Query + 0x00 (!!!!!)
        // => Abschluss mit Hex 00 ist nicht dokumentiert 
        // => (durch Vergleich mit Serverbrowser festgestellt
        // => Viele Server benötigen fehlendes Hex 00 am Ende nicht
        // siehe: https://developer.valvesoftware.com/wiki/Server_queries#Simple_Response_Format
    	char data[]= {(char)0xFF,(char)0xFF,(char)0xFF,(char)0xFF, (char)0x54};
    	String msg = new String (data);
    	msg = msg + "Source Engine Query" + (char)0x00;
        buf = msg.getBytes();

        DatagramPacket packet 
          = new DatagramPacket(buf, buf.length, address, Integer.parseInt(port));
        
        socket.send(packet);
        
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        //timeout + Exception if the socket does not receive anything in 1 second
        socket.setSoTimeout(1000);
        try {
        	socket.receive(receivePacket);
        }
        catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String received = new String(
        		receivePacket.getData(), 0, receivePacket.getLength());
    
        return received;
    }
 

    //
    // Aktuelle Playerzahl ist 6. Element
    // => 5 mal Hex 00 zählen zur richtigen Positionierung
    //
    public int getPlayer (String Srvmsg) {
    	int anzPlayer = 0;
    	int count = 0;
 	
    	for (int i = 0; i < Srvmsg.length(); i++) {
    		
    		if (count == 5) {
    			anzPlayer = (int) Srvmsg.charAt(i);	
    			// System.out.println("i:<"+ i + "> Wert:<"+Srvmsg.charAt(i) +">");
    			return anzPlayer;   // Hier dann raus aus der Schleife
    		}
    		
    		if (Srvmsg.charAt(i) == (char) 0x00) 
    			count++;
    	}
    	   	    	
    	return anzPlayer;
    }
    
    
    public void PlaySound () throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
    	String audioFilePath = "E:/wee3.wav";
    	File audioFile = new File(audioFilePath);
    	AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
    	AudioFormat format = audioStream.getFormat();
    	 
    	DataLine.Info info = new DataLine.Info(Clip.class, format);
    	Clip audioClip = (Clip) AudioSystem.getLine(info);
    	audioClip.open(audioStream);
    	audioClip.start();
    	
        Thread.sleep(1000);
         
        audioClip.close();
        
	}
    
    
    public void close() {
        socket.close();
    }
}