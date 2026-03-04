/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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

package org.fl.collectionAlbum.gui.adapter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.gui.CollectionMenuItems;
import org.fl.collectionAlbum.gui.DetailedAlbumAndDiscogsInfoPane;
import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.gui.listener.MediaFileCommandListener;
import org.fl.collectionAlbum.gui.listener.OsActionListener;
import org.fl.collectionAlbum.gui.listener.AlbumCustomActionListener.CustomAlbumAction;
import org.fl.collectionAlbum.gui.table.AlbumsScrollJTablePane;
import org.fl.collectionAlbum.gui.table.MediaFilesJTable;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;
import org.fl.collectionAlbum.osAction.OsAction;

public class MediaFileMouseAdapter extends MouseAdapter {

	private final MediaFilesJTable mediaFileTable;
	private final JPopupMenu localJPopupMenu;
	private final CollectionMenuItems<MediaFilePath> mediaFileMenuItems;
	private final GenerationPane generationPane;
	
	public MediaFileMouseAdapter(MediaFilesJTable mediaFileTable, List<OsAction<MediaFilePath>> osActions, GenerationPane generationPane) {
		super();
		
		this.mediaFileTable = mediaFileTable;
		localJPopupMenu = new JPopupMenu();
		mediaFileMenuItems = new CollectionMenuItems<>();
		this.generationPane = generationPane;
		
		osActions.forEach(osAction -> 
			mediaFileMenuItems.addMenuItem(
				osAction.actionTitle(),
				new MediaFileCommandListener(mediaFileTable, osAction),
				osAction.commandParameter().getActionValidityPredicate(),
				localJPopupMenu
			)
		);
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		actionOnMousePressedOrReleased(evt);
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		actionOnMousePressedOrReleased(evt);
	}
	
	@Override
	public void mouseClicked(MouseEvent evt) {
		actionOnMouseClicked(evt);
	}
	
	private void actionOnMousePressedOrReleased(MouseEvent evt) {
		if (evt.isPopupTrigger()) {
			enableMenuItems();
			localJPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		} 
	}
	
	private void actionOnMouseClicked(MouseEvent evt) {
		if (SwingUtilities.isLeftMouseButton(evt) && (evt.getClickCount() > 1)) {
			doubleClickAction();
		}
	}
	
	private void doubleClickAction() {
		
		MediaFilePath mediaFilePath = mediaFileTable.getSelectedMediaFile();
		if (mediaFilePath != null) {
			
			Set<Album> albums = mediaFilePath.getAlbumSet();
			if ((mediaFileTable.isAlbumsColumnSelected()) && !albums.isEmpty()) {
				if (albums.size() == 1) {
					Album album = albums.iterator().next();
					JOptionPane.showMessageDialog(null, 
							new DetailedAlbumAndDiscogsInfoPane(album),
							CustomAlbumAction.DETAILED_INFO_DISPLAY.getActionTitle(), 
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					
					AlbumsScrollJTablePane albumsScrollJTablePane = new AlbumsScrollJTablePane(albums.stream().toList(), generationPane);
					JOptionPane.showMessageDialog(null, albumsScrollJTablePane, "Albums correspondants", JOptionPane.PLAIN_MESSAGE);
				}
			} else {
				(new OsActionListener<>(mediaFilePath.getPath().toString(), Control.getDisplayFolderAction())).actionPerformed(null);
			}
		}
	}
	
	private void enableMenuItems() {
		mediaFileMenuItems.enableMenuItems(mediaFileTable.getSelectedMediaFile());
	}
}
