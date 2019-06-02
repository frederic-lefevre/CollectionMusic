package org.fl.collectionAlbumGui;

import java.awt.EventQueue;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbumGui.entry.AlbumEntryPane;
import org.fl.collectionAlbumGui.entry.ConcertEntryPane;

public class CollectionAlbumGui  extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8726429353709418534L;

	private static final String DEFAULT_PROP_FILE = "file:///C:/FredericPersonnel/musique/RapportCollection/albumCollection.properties" ;
	
	// Logger
	private Logger albumLog ;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CollectionAlbumGui window = new CollectionAlbumGui();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public CollectionAlbumGui() {
		
		// init logger and parameters
		Control.initControl(DEFAULT_PROP_FILE) ;
   		albumLog = Control.getAlbumLog() ;
   		
   		// init main window
   		setBounds(50, 50, 1500, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Collection d'album") ;
		
		// init panel de lecture et génération de site
		GenerationPane gPane = new GenerationPane(albumLog) ;
		AlbumEntryPane aPane = new AlbumEntryPane() ;
		ConcertEntryPane cPane = new ConcertEntryPane() ;
		
		JTabbedPane operationTab = new JTabbedPane() ;
		operationTab.addTab("Génération", gPane.getGenPane()) ;
		operationTab.addTab("Entrée album", aPane.getaEntryPane()) ;
		operationTab.addTab("Entrée concert", cPane.getcEntryPane()) ;
		
		getContentPane().add(operationTab) ;
		pack() ;
		
	}

}