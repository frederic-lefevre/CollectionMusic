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

package org.fl.collectionAlbum.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.table.GenreJTable;
import org.fl.collectionAlbum.gui.table.GenreTableModel;
import org.fl.collectionAlbum.mediaPath.MediaFileInventory;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;

public class MediaFileGenreTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	private final List<UpdatableElement> updatableElements;
	
	public MediaFileGenreTabbedPane() {
		
		super();
		setTabPlacement(JTabbedPane.LEFT);

		updatableElements = new ArrayList<>();
		
		Stream.of(ContentNature.values()).forEachOrdered(contentNature -> {

			MediaFileInventory mediaFileInventory = MediaFilesInventories.getMediaFileInventory(contentNature);

			GenreTableModel genreTableModel = new GenreTableModel(
					mediaFileInventory.getMediaFileGenres().getGenresParameterList(), 
					GenreTableModel.REGULAR_COLUMN);
			updatableElements.add(genreTableModel);

			GenreJTable genreTable = new GenreJTable(genreTableModel, contentNature);
			JScrollPane genreScrollTable = new JScrollPane(genreTable);
			genreScrollTable.setPreferredSize(Control.getMainSubPaneDimension());
			
			addTab(contentNature.getNom(), genreScrollTable);
		});
	}
	
	public List<UpdatableElement> getUpdatableElements() {
		return updatableElements;
	}
}
