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

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import org.fl.collectionAlbum.gui.adapter.MediaFileMouseAdapter;
import org.fl.collectionAlbum.mediaFile.MediaFile;

public class MediaFileJTable extends JTable {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(MediaFileJTable.class.getName());
	
	public MediaFileJTable(MediaFileTableModel mediaFileTableModel) {
		super(mediaFileTableModel);
		
		setFillsViewportHeight(true);
		
		TableRowSorter<MediaFileTableModel> sorter = new TableRowSorter<>(mediaFileTableModel);
		
		List<TableColumnParameter<MediaFile>> columnsParameters = mediaFileTableModel.getMediaColumnsParameters();
		
		for (int colIdx = 0; colIdx < columnsParameters.size();  colIdx++) {
			
			TableCellRenderer renderer = columnsParameters.get(colIdx).cellRenderer();
			if (renderer != null) {
				 getColumnModel().getColumn(colIdx).setCellRenderer(renderer);
			}
			getColumnModel().getColumn(colIdx).setPreferredWidth(columnsParameters.get(colIdx).width());
			
			Comparator<?> comparator = columnsParameters.get(colIdx).comparator();
			if (comparator != null) {
				sorter.setComparator(colIdx, comparator);
			}
		}
		
		// Allow single row selection only
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel(listSelectionModel);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		addMouseListener(new MediaFileMouseAdapter(this));
		
		setRowSorter(sorter);
	}

	public MediaFile getSelectedMediaFile() {
		int[] rowIdxs = getSelectedRows();
		if (rowIdxs.length == 0) {
			return null;
		} else if (rowIdxs.length > 1) {
			logger.severe("Found several selected rows for MediaFilesJTable. Number of selected rows: " + rowIdxs.length);
		}
		return ((MediaFileTableModel)getModel()).getMediaFileAt(convertRowIndexToModel(rowIdxs[0]));
	}
}
