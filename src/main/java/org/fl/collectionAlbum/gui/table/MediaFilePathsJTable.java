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

package org.fl.collectionAlbum.gui.table;

import java.util.logging.Logger;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.gui.adapter.MediaFilePathMouseAdapter;
import org.fl.collectionAlbum.gui.renderer.AlbumsRenderer;
import org.fl.collectionAlbum.gui.renderer.CollectionBooleanRenderer;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;
import org.fl.collectionAlbum.mediaPath.MediaFilePathAlbumComparator;
import org.fl.collectionAlbum.utils.CollectionUtils;

public class MediaFilePathsJTable extends JTable {

	private static final long serialVersionUID = 1L;

	private static final Logger tLog = Logger.getLogger(MediaFilePathsJTable.class.getName());
	
	private static final MediaFilePathAlbumComparator MEDIA_FILE_PATH_ALBUM_COMPARATOR = new MediaFilePathAlbumComparator();
	private static final CollectionUtils.LongComparator LONG_COMPARATOR = new CollectionUtils.LongComparator();
	
	public MediaFilePathsJTable(MediaFilePathsTableModel mediaFilePathsTableModel, GenerationPane generationPane) {
		super(mediaFilePathsTableModel);
		
		setFillsViewportHeight(true);
		
		getColumnModel().getColumn(MediaFilePathsTableModel.ALBUMS_COL_IDX).setCellRenderer(new AlbumsRenderer());
		getColumnModel().getColumn(MediaFilePathsTableModel.PATH_COL_IDX).setPreferredWidth(700);
		getColumnModel().getColumn(MediaFilePathsTableModel.ALBUMS_COL_IDX).setPreferredWidth(700);
		getColumnModel().getColumn(MediaFilePathsTableModel.NB_FILES_COL_IDX).setPreferredWidth(125);
		getColumnModel().getColumn(MediaFilePathsTableModel.COVER_IMAGE_COL_IDX).setPreferredWidth(140);
		getColumnModel().getColumn(MediaFilePathsTableModel.EXTENSION_COL_IDX).setPreferredWidth(100);
		getColumnModel().getColumn(MediaFilePathsTableModel.COVER_IMAGE_COL_IDX).setCellRenderer(new CollectionBooleanRenderer());
				
		// Allow single row selection only
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel(listSelectionModel);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		addMouseListener(new MediaFilePathMouseAdapter(this, Control.getOsActionOnMediaFilePath(), generationPane));
		
		// Row sorter
		TableRowSorter<MediaFilePathsTableModel> sorter = new TableRowSorter<>(mediaFilePathsTableModel);
		sorter.setComparator(MediaFilePathsTableModel.ALBUMS_COL_IDX, MEDIA_FILE_PATH_ALBUM_COMPARATOR);
		sorter.setComparator(MediaFilePathsTableModel.NB_FILES_COL_IDX, LONG_COMPARATOR);
		setRowSorter(sorter);
				
	}

	public MediaFilePath getSelectedMediaFilePath() {
		
		int[] rowIdxs = getSelectedRows();
		if (rowIdxs.length == 0) {
			return null;
		} else if (rowIdxs.length > 1) {
			tLog.severe("Found several selected rows for MediaFilesJTable. Number of selected rows: " + rowIdxs.length);
		}
		return ((MediaFilePathsTableModel)getModel()).getMediaFilePathAt(convertRowIndexToModel(rowIdxs[0]));
	}
	
	public boolean isAlbumsColumnSelected() {
		return  isColumnSelected(MediaFilePathsTableModel.ALBUMS_COL_IDX);
	}
}
