import java.awt.EventQueue;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JTable;

public class HL_Server_Query {

	private JFrame frame;
	private static JTable table;

	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws InterruptedException 
	 * @throws LineUnavailableException 
	 * @throws UnsupportedAudioFileException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {
		String result;
		String[] parts;
		String ia;
		String port;
		Integer  anzPlayer;
		Integer merkerPlayer;
		
		HL_Server_Query window = new HL_Server_Query();
		window.frame.setVisible(true);
		
		
/*		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HL_Server_Query window = new HL_Server_Query();
					
					// Hier wird GUI erst zur Anzeige gebracht.
					// Folgeverarbeitung dann ab hier
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
		
		
		// Ab hier Verarbeitung!?
		// Server abfragen
        QueryClient client;
        client = new QueryClient();
        
        //ToDo Schleife alle 5 Sekunden
        while (true) {
	        // durchlaufe Tabelle, besorge IP:Port und frage Server ab
	        for (int i=0; i <   table.getRowCount(); i++) {
	        	result = (String) table.getValueAt(i, 1);
	        	merkerPlayer = Integer.parseInt( table.getValueAt(i, 2).toString() );
	        	
	        	parts = result.split(":");
	        	ia = parts[0];
	    		port = parts[1];
	    		anzPlayer = client.getPlayer (client.sendReceiveQuery(ia,port));
//	    		System.out.println("Anzahl Spieler: " + anzPlayer);  
	    		
	    		// mit in Tabelle gespeichertem Player-Wert vergleichen
	    		if (merkerPlayer != anzPlayer) {  			
	    			
	    			// Tabelle mit aktueller Anzahl Spieler updaten
	    			table.setValueAt(anzPlayer.toString(), i,2);
	    			
	    			// Unterschied? Dann Signalton
	    			client.PlaySound();
	    			
	    		}
	        }
	        Thread.sleep(2000);
        }
		
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public HL_Server_Query() throws UnknownHostException, IOException {
	
		// GUI bauen
		initialize();
	}
		
		
	

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	private void initialize() throws UnknownHostException, IOException {
				
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 210);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblHalfLifeServer = new JLabel("Half Life Server Watch");
		lblHalfLifeServer.setHorizontalAlignment(SwingConstants.CENTER);
		lblHalfLifeServer.setFont(new Font("Tahoma", Font.BOLD, 20));
		frame.getContentPane().add(lblHalfLifeServer, BorderLayout.NORTH);
		
		
		//TODO Aus Datei einlesen
		String filename = "E:/servers.txt";
		FileReader fr = new FileReader (filename);
		BufferedReader br = new BufferedReader (fr);
		
		
		// Datei hat 3 Einträge, mit ; getrennt
		// Servername;IP:Port;checken J/N
		
		List<String> lines = new ArrayList<String>();
		String line = null;
		
		while ((line = br.readLine()) != null) {
			// gelesenen String spliten und zu rowData hinzufügen
			//System.out.println( "Aus Datei gelesen:" + line);
			lines.add(line);
		}
		
        br.close();
		
		// Daten aus ArrayList nach roWData formatiert übergeben
        String[][] rowData = new String[lines.size()][4];
        
        int i = 0;
        
        for (String s : lines ) {
        	String[] tmp = s.split(";");
        	rowData[i][0] = tmp[0];
        	rowData[i][1] = tmp[1];
        	rowData[i][2] = "0";
        	rowData[i][3] = "0";
        	i++;
        }
        
        
//		String[][] rowData = {
//			      { "Cologne HL", "85.14.232.25:27015","0","0"  }, { "~ls", "178.161.179.214:27016","0","0" },
//			      { "peet", "37.200.99.45:27020","0","0" }, 
// 			    };

			    String[] columnNames =  {
			      "Servername", "IP:Port","Anzahl Player", "checken?"
			    };
		
		table = new JTable(rowData, columnNames);
		frame.add(new JScrollPane(table));
		table.setShowGrid( false );
		table.setShowVerticalLines( true );
		table.getColumnModel().getColumn( 2 ).setPreferredWidth( 30 );
		
		// Center Alignment
		TableColumn col = table.getColumnModel().getColumn(2);
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();  
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);
        col.setCellRenderer(dtcr);
        col = table.getColumnModel().getColumn(3);
        col.setCellRenderer(dtcr);
        
	}
      
}
