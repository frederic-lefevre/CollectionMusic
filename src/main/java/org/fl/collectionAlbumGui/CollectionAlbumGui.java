package org.fl.collectionAlbumGui;

import java.awt.EventQueue;
import java.util.logging.Logger;

import javax.swing.JFrame;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.rapportHtml.RapportStructuresAndNames;
import org.fl.util.swing.ApplicationTabbedPane;

public class CollectionAlbumGui  extends JFrame {

	public static final String MUSIQUE_DIRECTORY_URI = "file:///C:/FredericPersonnel/Loisirs/musique/" ;
	
	private static final long serialVersionUID = 8726429353709418534L;

	public static final String DEFAULT_PROP_FILE = MUSIQUE_DIRECTORY_URI + "RapportCollection/albumCollection.properties" ;
	
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
		Logger albumLog = Control.getAlbumLog() ;
		RapportStructuresAndNames.init() ;
   		
   		// init main window
   		setBounds(50, 50, 1500, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Collection d'album") ;
		
		ApplicationTabbedPane collectionTabs = new ApplicationTabbedPane(Control.getMusicRunningContext()) ;
		
		// init panel de lecture et génération de site
		GenerationPane gPane   = new GenerationPane(albumLog) ;
		
		collectionTabs.add(gPane, "Génération", 0) ;
		
		collectionTabs.setSelectedIndex(0) ;
		getContentPane().add(collectionTabs) ;
		
	}

}