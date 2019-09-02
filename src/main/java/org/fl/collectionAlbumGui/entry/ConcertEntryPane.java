package org.fl.collectionAlbumGui.entry;

import javax.swing.JPanel;

public class ConcertEntryPane extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public ConcertEntryPane() {
		
		super() ;
		
		ArtisteEntryPanel aep = new ArtisteEntryPanel() ;
		add(aep.getaEntryPane()) ;
	}

}
