import java.awt.EventQueue;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JTable;

public class HL_Server_Query {

	private JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
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
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public HL_Server_Query() throws UnknownHostException, IOException {
		String result;
		String[] parts;
		String ia;
		String port;
		Integer  anzPlayer;
		Integer merkerPlayer;
	
		// GUI bauen
		initialize();
		
		
		// Server abfragen
        QueryClient client;
        client = new QueryClient();
        
        //ToDo Schleife alle 5 Sekunden
        // while (true) {
	        // durchlaufe Tabelle, besorge IP:Port und frage Server ab
	        for (int i=0; i <   table.getRowCount(); i++) {
	        	result = (String) table.getValueAt(i, 1);
	        	merkerPlayer = Integer.parseInt( table.getValueAt(i, 2).toString() );
	        	
	        	parts = result.split(":");
	        	ia = parts[0];
	    		port = parts[1];
	    		anzPlayer = client.getPlayer (client.sendReceiveQuery(ia,port));
	    		System.out.println("Anzahl Spieler: " + anzPlayer);  
	    		
	    		// mit in Tabelle gespeichertem Player-Wert vergleichen
	    		if (merkerPlayer < anzPlayer) {
	    			// Unterschied? Dann Signalton

	    			
	    			
	    			// Tabelle mit aktueller Anzahl Spieler updaten
	    			table.setValueAt(anzPlayer.toString(), i,2);
	    		}
	        }
        try {
			 Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		
		
		//ToDo Aus Datei einlesen
		String[][] rowData = {
			      { "Cologne HL", "85.14.232.25:27015","0","0"  }, { "~ls", "178.161.179.214:27016","0","0" },
//			      { "Spanien", "217" }, {"Türkei", "215"} ,{ "England", "214" },
//			      { "Frankreich", "190" }, {"Griechenland", "185" },
	//		      { "Deutschland", "180" }, {"Portugal", "170" }
			    };

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
