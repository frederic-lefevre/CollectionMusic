package org.fl.collectionAlbumGui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import org.fl.collectionAlbum.CollectionAlbums;

public class StartReadCollection implements ActionListener {

	private Logger albumLog ;
	private ProgressInformationPanel pip;
	private CollectionProcessWaiter collectionProcWaiter;
	private StartControl  startCtrl;
	private StartControl[]  startCtrlTab;
	
	public StartReadCollection(ProgressInformationPanel progInfoPanel, StartControl stCtrl, StartControl[] stList, Logger aLog) {

		albumLog = aLog;
		pip = progInfoPanel ;
		startCtrl = stCtrl ;
		startCtrlTab = stList ;
	}

	
	public void setCollectionProcWaiter(CollectionProcessWaiter collectionProcWaiter) {
		this.collectionProcWaiter = collectionProcWaiter;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		startCtrl.setTriggered(true) ;
		startCtrl.getStartButton().setBackground(new Color(27,224,211)) ;
		for (StartControl st : startCtrlTab ) {
			st.getStartButton().setEnabled(false) ;
		}
		CollectionAlbums ca = new CollectionAlbums(pip, albumLog) ;
		ca.addPropertyChangeListener(collectionProcWaiter);
		ca.execute() ;
	}

}
