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

package org.fl.collectionAlbum.gui;

import java.util.logging.Logger;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumRelease;
import org.fl.collectionAlbum.gui.adapter.DiscogsInventoryMouseAdapter;
import org.fl.collectionAlbum.gui.renderer.CollectionBooleanRenderer;
import org.fl.collectionAlbum.gui.renderer.FormatCompatibilityRenderer;

public class DiscogsReleaseJTable extends JTable {

	private static final long serialVersionUID = 1L;

	private static final Logger tLog = Logger.getLogger(DiscogsReleaseJTable.class.getName());
	
	public DiscogsReleaseJTable(DisocgsReleaseTableModel dm, CollectionAlbumContainer albumsContainer, GenerationPane generationPane) {
		super(dm);
		
		setFillsViewportHeight(true);
		
		getColumnModel().getColumn(DisocgsReleaseTableModel.ALBUM_LINK_COL_IDX)
			.setCellRenderer(new CollectionBooleanRenderer());
		getColumnModel().getColumn(DisocgsReleaseTableModel.FORMAT_MATCH_COL_IDX)
			.setCellRenderer(new FormatCompatibilityRenderer());
		getColumnModel().getColumn(DisocgsReleaseTableModel.ID_COL_IDX).setPreferredWidth(70);
		getColumnModel().getColumn(DisocgsReleaseTableModel.ARTISTS_COL_IDX).setPreferredWidth(720);
		getColumnModel().getColumn(DisocgsReleaseTableModel.TITLE_COL_IDX).setPreferredWidth(580);
		getColumnModel().getColumn(DisocgsReleaseTableModel.FORMAT_COL_IDX).setPreferredWidth(200);
		getColumnModel().getColumn(DisocgsReleaseTableModel.ALBUM_LINK_COL_IDX).setPreferredWidth(100);
		getColumnModel().getColumn(DisocgsReleaseTableModel.FORMAT_MATCH_COL_IDX).setPreferredWidth(100);
		
		// Allow single row selection only
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel(listSelectionModel);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		addMouseListener(new DiscogsInventoryMouseAdapter(this, Control.getOsActionOnDiscogsRelease(), albumsContainer, generationPane));
		
		// Row sorter
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(getModel());
		setRowSorter(sorter);
	}

	public DiscogsAlbumRelease getSelectedDisocgsRelease(){
		
		int[] rowIdxs = getSelectedRows();
		if (rowIdxs.length == 0) {
			return null;
		} else if (rowIdxs.length > 1) {
			tLog.severe("Found several selected rows for MediaFilesJTable. Number of selected rows: " + rowIdxs.length);
		}
		return ((DisocgsReleaseTableModel)getModel()).getDiscogsReleaseAt(convertRowIndexToModel(rowIdxs[0]));
	}
}
