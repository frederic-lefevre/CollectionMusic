package org.fl.collectionAlbumGui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingWorker;

public class CollectionProcessWaiter implements PropertyChangeListener {

	private StartControl[] startCtrlList;
	
	public CollectionProcessWaiter(StartControl[] stList) {
		startCtrlList = stList ;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		
		 if ("state".equals(event.getPropertyName())
                 && SwingWorker.StateValue.DONE == event.getNewValue()) {
			 for (StartControl startCtrl : startCtrlList) {
				 startCtrl.activate() ;
			 }
		 }
	}

}
