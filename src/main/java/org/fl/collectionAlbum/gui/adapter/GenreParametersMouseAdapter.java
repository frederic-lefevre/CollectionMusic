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

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.table.GenreJTable;
import org.fl.collectionAlbum.gui.table.MediaFileJTable;
import org.fl.collectionAlbum.gui.table.MediaFileTableColumns;
import org.fl.collectionAlbum.gui.table.MediaFileTableModel;
import org.fl.collectionAlbum.mediaFile.MediaFile;
import org.fl.collectionAlbum.mediaFile.MediaFileGenres.GenreParameters;

public class GenreParametersMouseAdapter extends MouseAdapter {

	private final GenreJTable genreTable;
	private final ContentNature contentNature;
	
	public GenreParametersMouseAdapter(GenreJTable genreTable, ContentNature contentNature) {
		super();
		
		this.genreTable = genreTable;
		this.contentNature = contentNature;
	}
	
	@Override
	public void mouseClicked(MouseEvent evt) {
		
		if (SwingUtilities.isLeftMouseButton(evt) && (evt.getClickCount() > 1)) {
			
			GenreParameters  selectedGenreParameters = genreTable.getSelectedGenreParameters();
			if (selectedGenreParameters != null) {
				
				List<MediaFile> mediaFileList = selectedGenreParameters.mediaFiles();
				if ((mediaFileList != null) && !mediaFileList.isEmpty()) {
					
					MediaFileTableModel mediaFileTableModel = 
							new MediaFileTableModel(mediaFileList, MediaFileTableColumns.mediaColumnsParameters(contentNature));
					
					MediaFileJTable mediaFileJTable = new MediaFileJTable(mediaFileTableModel);
					
					// Scroll pane to contain the media path table
					JScrollPane mediaFileScrollTable = new JScrollPane(mediaFileJTable);
					mediaFileScrollTable.setPreferredSize(Control.getMainSubPaneDimension());
					
					JOptionPane.showMessageDialog(null, mediaFileScrollTable, "Fichiers media " + selectedGenreParameters.genre(), JOptionPane.PLAIN_MESSAGE);
				}
			}
		}
	}
}
