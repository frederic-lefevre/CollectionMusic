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

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;

import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.gui.renderer.CollectionNumberRenderer;
import org.fl.collectionAlbum.utils.CollectionUtils;

public class LieuConcertJTable extends JTable {

	private static final long serialVersionUID = 1L;

	private static final CollectionUtils.IntegerComparator INTEGER_COMPARATOR = new CollectionUtils.IntegerComparator();
	
	public LieuConcertJTable(LieuConcertTableModel lieuConcertTableModel, GenerationPane generationPane) {
		super(lieuConcertTableModel);
		
		setFillsViewportHeight(true);
		
		setRowHeight(30);
		
		getColumnModel().getColumn(LieuConcertTableModel.NUMBER_COL_IDX).setCellRenderer(new CollectionNumberRenderer());
		
		getColumnModel().getColumn(LieuConcertTableModel.LIEU_COL_IDX).setPreferredWidth(600);
		getColumnModel().getColumn(LieuConcertTableModel.NUMBER_COL_IDX).setPreferredWidth(200);
		
		// Allow single row selection only
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel(listSelectionModel);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		// Row sorter
		TableRowSorter<LieuConcertTableModel> sorter = new TableRowSorter<>(lieuConcertTableModel);
		sorter.setComparator(LieuConcertTableModel.NUMBER_COL_IDX, INTEGER_COMPARATOR);
		setRowSorter(sorter);
	}
}
