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
import javax.swing.table.TableRowSorter;

import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.ConcertAuteurComparator;
import org.fl.collectionAlbum.gui.renderer.AuteursRenderer;
import org.fl.collectionAlbum.gui.renderer.DateRenderer;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class ConcertsJTable extends JTable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ConcertsJTable.class.getName());
	
	public ConcertsJTable(ConcertTableModel concertTableModel) {
		
		super(concertTableModel);
		
		setFillsViewportHeight(true);
		
		setRowHeight(50);
		
		getColumnModel().getColumn(ConcertTableModel.DATE_COL_IDX).setCellRenderer(new DateRenderer());
		getColumnModel().getColumn(ConcertTableModel.ARTISTE_COL_IDX).setCellRenderer(new AuteursRenderer());
		
		getColumnModel().getColumn(ConcertTableModel.DATE_COL_IDX).setPreferredWidth(200);
		getColumnModel().getColumn(ConcertTableModel.ARTISTE_COL_IDX).setPreferredWidth(700);
		getColumnModel().getColumn(ConcertTableModel.LIEU_COL_IDX).setPreferredWidth(500);
		
		// Allow single row selection only
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel(listSelectionModel);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		// Row sorter
		TableRowSorter<ConcertTableModel> sorter = new TableRowSorter<>(concertTableModel);
		setRowSorter(sorter);
		
		sorter.setComparator(ConcertTableModel.DATE_COL_IDX, new TemporalUtils.TemporalAccessorComparator());
		sorter.setComparator(ConcertTableModel.ARTISTE_COL_IDX, new ConcertAuteurComparator());
	}
	
	public Concert getSelectedConcert() {
		
		int[] rowIdxs = getSelectedRows();
		if (rowIdxs.length == 0) {
			return null;
		} else if (rowIdxs.length > 1) {
			logger.severe("Found several selected rows for ConcertsJTable. Number of selected rows: " + rowIdxs.length);
		}
		return ((ConcertTableModel)getModel()).getConcertAt(convertRowIndexToModel(rowIdxs[0]));
	}
}
