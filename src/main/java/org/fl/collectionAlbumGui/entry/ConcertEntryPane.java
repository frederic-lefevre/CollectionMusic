package org.fl.collectionAlbumGui.entry;

import javax.swing.JPanel;

public class ConcertEntryPane {

	private JPanel cEntryPane ;
	
public ConcertEntryPane() {
		
		cEntryPane = new JPanel() ;
		
		ArtisteEntryPanel aep = new ArtisteEntryPanel() ;
		cEntryPane.add(aep.getaEntryPane()) ;
	}

	public JPanel getcEntryPane() {
		return cEntryPane;
	}

}
