/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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

import java.util.logging.Logger;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;
import org.fl.collectionAlbum.mediaPath.MediaFilePathAlbumComparator;
import org.fl.collectionAlbumGui.adapter.MediaFileMouseAdapter;
import org.fl.collectionAlbumGui.renderer.AlbumsRenderer;
import org.fl.collectionAlbumGui.renderer.CollectionBooleanRenderer;

public class MediaFilesJTable extends JTable {

	private static final long serialVersionUID = 1L;

	private static final Logger tLog = Logger.getLogger(MediaFilesJTable.class.getName());
	
	public MediaFilesJTable(MediaFilesTableModel dm) {
		super(dm);
		
		setFillsViewportHeight(true);
		
		getColumnModel().getColumn(MediaFilesTableModel.ALBUMS_COL_IDX).setCellRenderer(new AlbumsRenderer());
		getColumnModel().getColumn(MediaFilesTableModel.PATH_COL_IDX).setPreferredWidth(700);
		getColumnModel().getColumn(MediaFilesTableModel.ALBUMS_COL_IDX).setPreferredWidth(700);
		getColumnModel().getColumn(MediaFilesTableModel.NB_FILES_COL_IDX).setPreferredWidth(125);
		getColumnModel().getColumn(MediaFilesTableModel.COVER_IMAGE_COL_IDX).setPreferredWidth(140);
		getColumnModel().getColumn(MediaFilesTableModel.EXTENSION_COL_IDX).setPreferredWidth(100);
		getColumnModel().getColumn(MediaFilesTableModel.COVER_IMAGE_COL_IDX).setCellRenderer(new CollectionBooleanRenderer());
				
		// Allow single row selection only
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel(listSelectionModel);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		addMouseListener(new MediaFileMouseAdapter(this, Control.getOsActionOnMediaFilePath()));
		
		// Row sorter
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(getModel());
		setRowSorter(sorter);
				
		sorter.setComparator(MediaFilesTableModel.ALBUMS_COL_IDX, new MediaFilePathAlbumComparator());
	}

	public MediaFilePath getSelectedMediaFile() {
		
		int[] rowIdxs = getSelectedRows();
		if (rowIdxs.length == 0) {
			return null;
		} else if (rowIdxs.length > 1) {
			tLog.severe("Found several selected rows for MediaFilesJTable. Number of selected rows: " + rowIdxs.length);
		}
		return ((MediaFilesTableModel)getModel()).getMediaFileAt(convertRowIndexToModel(rowIdxs[0]));
	}
}
