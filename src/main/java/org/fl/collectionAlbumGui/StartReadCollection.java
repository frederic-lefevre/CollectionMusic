/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.fl.collectionAlbumGui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.fl.collectionAlbum.CollectionAlbums;

public class StartReadCollection implements ActionListener {

	private final AlbumsTableModel albumsTableModel;
	private final ProgressInformationPanel pip;
	private CollectionProcessWaiter collectionProcWaiter;
	private final StartControl startCtrl;
	private final StartControl[] startCtrlTab;

	public StartReadCollection(AlbumsTableModel albumsTableModel, ProgressInformationPanel progInfoPanel,
			StartControl stCtrl, StartControl[] stList) {

		this.albumsTableModel = albumsTableModel;
		pip = progInfoPanel;
		startCtrl = stCtrl;
		startCtrlTab = stList;
	}

	public void setCollectionProcWaiter(CollectionProcessWaiter collectionProcWaiter) {
		this.collectionProcWaiter = collectionProcWaiter;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		startCtrl.setTriggered(true);
		startCtrl.getStartButton().setBackground(new Color(27, 224, 211));
		for (StartControl st : startCtrlTab) {
			st.getStartButton().setEnabled(false);
		}
		CollectionAlbums ca = new CollectionAlbums(albumsTableModel, pip);
		ca.addPropertyChangeListener(collectionProcWaiter);
		ca.execute();
	}
}
