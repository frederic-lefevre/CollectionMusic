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

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.gui.CollectionMenuItems;
import org.fl.collectionAlbum.gui.DetailedAlbumAndDiscogsInfoPane;
import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.gui.listener.MediaFilePathCommandListener;
import org.fl.collectionAlbum.gui.listener.AlbumCustomActionListener.CustomAlbumAction;
import org.fl.collectionAlbum.gui.listener.MediaFilePathActionListener;
import org.fl.collectionAlbum.gui.table.AlbumTableColumns;
import org.fl.collectionAlbum.gui.table.AlbumsScrollJTablePane;
import org.fl.collectionAlbum.gui.table.MediaFileJTable;
import org.fl.collectionAlbum.gui.table.MediaFilePathsJTable;
import org.fl.collectionAlbum.gui.table.MediaFileTableModel;
import org.fl.collectionAlbum.mediaFile.MediaFile;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;
import org.fl.collectionAlbum.osAction.OsAction;

public class MediaFilePathMouseAdapter extends MouseAdapter {

	private final MediaFilePathsJTable mediaFilePathsTable;
	private final JPopupMenu localJPopupMenu;
	private final CollectionMenuItems<MediaFilePath> mediaFilePathMenuItems;
	private final GenerationPane generationPane;
	
	public MediaFilePathMouseAdapter(MediaFilePathsJTable mediaFilePathsTable, List<OsAction<MediaFilePath>> osActions, GenerationPane generationPane) {
		super();
		
		this.mediaFilePathsTable = mediaFilePathsTable;
		this.localJPopupMenu = new JPopupMenu();
		this.mediaFilePathMenuItems = new CollectionMenuItems<>();
		this.generationPane = generationPane;
		
		JMenuItem showMediaFiles = new JMenuItem("Afficher la liste des fichiers media");
		showMediaFiles.addActionListener(new MediaFilePathActionListener(mediaFilePathsTable));
		localJPopupMenu.add(showMediaFiles);
		
		osActions.forEach(osAction -> 
			mediaFilePathMenuItems.addMenuItem(
				osAction.actionTitle(),
				new MediaFilePathCommandListener(mediaFilePathsTable, osAction),
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
		
		MediaFilePath mediaFilePath = mediaFilePathsTable.getSelectedMediaFilePath();
		if (mediaFilePath != null) {
			
			Set<Album> albums = mediaFilePath.getAlbumSet();
			if ((mediaFilePathsTable.isAlbumsColumnSelected()) && !albums.isEmpty()) {
				if (albums.size() == 1) {
					Album album = albums.iterator().next();
					JOptionPane.showMessageDialog(null, 
							new DetailedAlbumAndDiscogsInfoPane(album),
							CustomAlbumAction.DETAILED_INFO_DISPLAY.getActionTitle(), 
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					
					AlbumsScrollJTablePane albumsScrollJTablePane = 
							new AlbumsScrollJTablePane(albums.stream().toList(), AlbumTableColumns.REGULAR_COLUMNS, generationPane);
					JOptionPane.showMessageDialog(null, albumsScrollJTablePane, "Albums correspondants", JOptionPane.PLAIN_MESSAGE);
				}
			} else {
				displayMediaFileList(mediaFilePath);
			}
		}
	}
	
	public static void displayMediaFileList(MediaFilePath mediaFilePath) {
		
		List<MediaFile> mediaFileList = mediaFilePath.getMediaFiles();
		if ((mediaFileList != null) && !mediaFileList.isEmpty()) {
			MediaFileTableModel mediaFileTableModel = new MediaFileTableModel(mediaFileList, mediaFilePath.getContentNature());
			MediaFileJTable mediaFileJTable = new MediaFileJTable(mediaFileTableModel);
			
			// Scroll pane to contain the media path table
			JScrollPane mediaFileScrollTable = new JScrollPane(mediaFileJTable);
			mediaFileScrollTable.setPreferredSize(Control.getMainSubPaneDimension());
			
			JOptionPane.showMessageDialog(null, mediaFileScrollTable, "Fichiers media correspondants", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private void enableMenuItems() {
		mediaFilePathMenuItems.enableMenuItems(mediaFilePathsTable.getSelectedMediaFilePath());
	}
}
