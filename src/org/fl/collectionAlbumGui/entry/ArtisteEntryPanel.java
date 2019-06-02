package org.fl.collectionAlbumGui.entry;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ArtisteEntryPanel {

	private JPanel aEntryPane ;
	
	public ArtisteEntryPanel() {

		aEntryPane = new JPanel() ;
		
		JLabel nl = new JLabel("Nom") ;
		aEntryPane.add(nl) ;
		
		JComboBox<String> comboNom = new JComboBox<String>() ;
		comboNom.setEditable(true) ;
		aEntryPane.add(comboNom) ;
	}

	public JPanel getaEntryPane() {
		return aEntryPane;
	}

}
