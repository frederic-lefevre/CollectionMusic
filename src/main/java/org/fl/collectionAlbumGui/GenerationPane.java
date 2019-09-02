package org.fl.collectionAlbumGui;

import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class GenerationPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private final static String rText  = "Lecture des fichiers albums et concerts" ;
	private final static String gText  = "Génération du nouveau site collection" ;
	private final static String iText  = "Arrêt";
	private final static String sText  = "Aucune collection lue" ;
	private final static String s1Text = "Aucun site généré" ;
	
	public GenerationPane(Logger albumLog) {
			
		super() ;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		StartControl startButton = new StartControl(rText, iText, sText) ;
		add(startButton.getProcCtrl()) ;
				
		StartControl genButton = new StartControl(gText, iText, s1Text) ;
		genButton.deactivate() ;
		add(genButton.getProcCtrl()) ;
		
		StartControl[] stCtrl = new StartControl[]{startButton, genButton} ;
		
		StartReadCollection sm = new StartReadCollection(startButton.getPip(), startButton, stCtrl, albumLog) ;
		sm.setCollectionProcWaiter(new CollectionProcessWaiter(stCtrl)) ;
		startButton.getStartButton().addActionListener(sm) ;
		
		StartGenerationSite sg = new StartGenerationSite(genButton.getPip(), genButton, stCtrl, albumLog) ;
		sg.setCollectionProcWaiter(new CollectionProcessWaiter(stCtrl)) ;
		genButton.getStartButton().addActionListener(sg) ;
	} 

}
