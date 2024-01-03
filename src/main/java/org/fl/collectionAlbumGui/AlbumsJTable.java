/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

import java.util.Map;
import java.util.logging.Logger;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.Format.ContentNature;
import org.fl.collectionAlbum.RangementComparator;
import org.fl.collectionAlbum.albums.Album;

public class AlbumsJTable extends JTable {

	private static final long serialVersionUID = 1L;

	private static final Logger tLog = Control.getAlbumLog();
	
	public AlbumsJTable(AlbumsTableModel dm) {
		super(dm);
		init();
	}

	private void init() {
		
		setFillsViewportHeight(true);
		setAutoCreateRowSorter(true);
		
		Map<ContentNature, MediaFilesSearchListener> mediaFilesSearchListeners = 
				Map.of(
					ContentNature.AUDIO, new MediaFilesSearchListener(this, ContentNature.AUDIO),
					ContentNature.VIDEO, new MediaFilesSearchListener(this, ContentNature.VIDEO
				));

		Map<ContentNature, MediaFileValidationListener> mediaFilesValidationListeners = 
				Map.of(
					ContentNature.AUDIO, new MediaFileValidationListener(this, ContentNature.AUDIO),
					ContentNature.VIDEO, new MediaFileValidationListener(this, ContentNature.VIDEO
				));
		
		getColumnModel().getColumn(AlbumsTableModel.AUTEUR_COL_IDX)
			.setCellRenderer(new AuteursRenderer());
		getColumnModel().getColumn(AlbumsTableModel.MEDIA_FILES_COL_IDX)
			.setCellRenderer(new MediaFilesRenderer(mediaFilesSearchListeners, mediaFilesValidationListeners));
		getColumnModel().getColumn(AlbumsTableModel.MEDIA_FILES_COL_IDX)
			.setCellEditor(new MediaFilesCellEditor(this, mediaFilesSearchListeners, mediaFilesValidationListeners));
		getColumnModel().getColumn(AlbumsTableModel.PROBLEM_COL_IDX)
			.setCellRenderer(new AlbumProblemRenderer());
		getColumnModel().getColumn(AlbumsTableModel.TITRE_COL_IDX).setPreferredWidth(350);
		getColumnModel().getColumn(AlbumsTableModel.AUTEUR_COL_IDX).setPreferredWidth(350);
		getColumnModel().getColumn(AlbumsTableModel.MEDIA_FILES_COL_IDX).setPreferredWidth(400);
		getColumnModel().getColumn(AlbumsTableModel.PROBLEM_COL_IDX).setPreferredWidth(80);
		getColumnModel().getColumn(AlbumsTableModel.DISCOGS_COL_IDX).setPreferredWidth(120);
		
		// Row sorter
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(getModel());
		setRowSorter(sorter);
		sorter.sort();
		
		sorter.setComparator(AlbumsTableModel.AUTEUR_COL_IDX, new RangementComparator());
		sorter.setComparator(AlbumsTableModel.MEDIA_FILES_COL_IDX, new RangementComparator());
		
		// Allow single row selection only
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel(listSelectionModel);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
		
		addMouseListener(new AlbumMouseAdapter(this, Control.getOsActions()));

	}

	// Get the selected album
	public Album getSelectedAlbum() {
		
		int[] rowIdxs = getSelectedRows();
		if (rowIdxs.length == 0) {
			return null;
		} else if (rowIdxs.length > 1) {
			tLog.severe("Found several selected rows for AlbumJTable. Number of selected rows: " + rowIdxs.length);
		}
		return ((AlbumsTableModel)getModel()).getAlbumAt(convertRowIndexToModel(rowIdxs[0]));
	}
}
