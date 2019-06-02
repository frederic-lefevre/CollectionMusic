package org.fl.collectionAlbumGui;

import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class GenerationPane {

	private JPanel genPane ;
	private StartControl startButton ;
	private StartControl genButton ;
	
	private final static String rText = "Lecture des fichiers albums et concerts" ;
	private final static String gText = "Génération du nouveau site collection" ;
	private final static String iText = "Arrêt";
	private final static String sText = "Aucune collection lue" ;
	private final static String s1Text = "Aucun site généré" ;
	
	public GenerationPane(Logger albumLog) {
		
		
		genPane = new JPanel() ;
		genPane.setLayout(new BoxLayout(genPane, BoxLayout.Y_AXIS));
		
		startButton = new StartControl(rText, iText, sText) ;
		genPane.add(startButton.getProcCtrl()) ;
		
		
		genButton = new StartControl(gText, iText, s1Text) ;
		genButton.deactivate() ;
		genPane.add(genButton.getProcCtrl()) ;
		
		StartControl[] stCtrl = new StartControl[]{startButton, genButton} ;
		
		StartReadCollection sm = new StartReadCollection(startButton.getPip(), startButton, stCtrl, albumLog) ;
		sm.setCollectionProcWaiter(new CollectionProcessWaiter(stCtrl)) ;
		startButton.getStartButton().addActionListener(sm) ;
		
		StartGenerationSite sg = new StartGenerationSite(genButton.getPip(), genButton, stCtrl, albumLog) ;
		sg.setCollectionProcWaiter(new CollectionProcessWaiter(stCtrl)) ;
		genButton.getStartButton().addActionListener(sg) ;
	} 

	public JPanel getGenPane() {
		return genPane;
	}

}
