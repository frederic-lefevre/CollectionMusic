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

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.fl.collectionAlbum.OsAction;
import org.fl.collectionAlbum.albums.Album;

public class AlbumMouseAdapter extends MouseAdapter {

	private final AlbumsJTable albumsJTable;
	private JPopupMenu localJPopupMenu;
	
	private List<JMenuItem> anyMenuItems;
	
	public AlbumMouseAdapter(AlbumsJTable ajt, List<OsAction> osActions) {
		
		super();
		this.albumsJTable = ajt;
		localJPopupMenu = new JPopupMenu();
		
		anyMenuItems = new ArrayList<JMenuItem>();
		
		osActions.forEach(osAction -> {
			anyMenuItems.add(addMenuItem(osAction.getActionTitle(), new AlbumCommandListener(albumsJTable, osAction.getActionCommand())));
		});
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		if (evt.isPopupTrigger()) {
			enableMenuItems();
			localJPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		if (evt.isPopupTrigger()) {
			enableMenuItems();
			localJPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}

	private void enableMenuItems() {

		Album album = albumsJTable.getSelectedAlbum();
		if (album != null) {
			anyMenuItems.forEach(menuItem -> menuItem.setEnabled(true));
		}
	}
	
	private JMenuItem addMenuItem(String title, ActionListener act) {
		JMenuItem localJMenuItem = new JMenuItem(title);
	     localJMenuItem.addActionListener(act);
	     localJPopupMenu.add(localJMenuItem);
	     return localJMenuItem ;
	}
}
