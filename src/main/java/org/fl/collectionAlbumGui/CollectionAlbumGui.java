package org.fl.collectionAlbumGui;

import java.awt.EventQueue;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.rapportHtml.RapportStructuresAndNames;
import org.fl.collectionAlbumGui.entry.AlbumEntryPane;
import org.fl.collectionAlbumGui.entry.ConcertEntryPane;

import com.ibm.lge.fl.util.swing.ApplicationInfoPane;

public class CollectionAlbumGui  extends JFrame {

	private static final long serialVersionUID = 8726429353709418534L;

	private static final String DEFAULT_PROP_FILE = "file:///C:/FredericPersonnel/musique/RapportCollection/albumCollection.properties" ;
	
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
	
	private JTabbedPane			collectionTabs ;	
	private ApplicationInfoPane appInfoPane ;
	
	public CollectionAlbumGui() {
		
		// init logger and parameters
		Control.initControl(DEFAULT_PROP_FILE) ;
		Logger albumLog = Control.getAlbumLog() ;
		RapportStructuresAndNames.init() ;
   		
   		// init main window
   		setBounds(50, 50, 1500, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Collection d'album") ;
		
		// init panel de lecture et génération de site
		GenerationPane gPane   = new GenerationPane(albumLog) ;
		AlbumEntryPane aPane   = new AlbumEntryPane() ;
		ConcertEntryPane cPane = new ConcertEntryPane() ;
		appInfoPane		 	   = new ApplicationInfoPane(Control.getMusicRunningContext()) ;
		
		collectionTabs = new JTabbedPane() ;
		collectionTabs.addTab("Génération",     gPane.getGenPane()) ;
		collectionTabs.addTab("Entrée album",   aPane.getaEntryPane()) ;
		collectionTabs.addTab("Entrée concert", cPane.getcEntryPane()) ;
		collectionTabs.addTab("Informations",   appInfoPane) ;
		
		collectionTabs.addChangeListener(new CollectionTabChangeListener());
		
		collectionTabs.setSelectedIndex(0) ;
		getContentPane().add(collectionTabs) ;
		
		pack() ;		
	}

	private class CollectionTabChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent arg0) {
			
			if (collectionTabs.getSelectedComponent().equals(appInfoPane)) {
				appInfoPane.setInfos();
			}			
		}
	}
}