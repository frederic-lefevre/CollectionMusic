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

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class ConcertTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	public static final int DATE_COL_IDX = 0;
	public static final int ARTISTE_COL_IDX = 1;
	public static final int LIEU_COL_IDX = 2;
	
	private static final String[] entetes = {"Dates", "Artistes", "Lieu"};
	
	private final List<Concert> listeConcert;
	
	public ConcertTableModel(List<Concert> listeConcert) {
		super();
		this.listeConcert = listeConcert;
	}
	
	@Override
	public int getRowCount() {
		return listeConcert.size();
	}

	@Override
	public int getColumnCount() {
		return entetes.length;
	}

	@Override
	public String getColumnName(int col) {
	    return entetes[col];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if (listeConcert.size() < rowIndex + 1) {
			// This may happen when triggering a rescan of the collection
			return null;
		} else {
			
			return switch(columnIndex) {
				case DATE_COL_IDX -> listeConcert.get(rowIndex).getDateConcert();
				case ARTISTE_COL_IDX -> listeConcert.get(rowIndex);
				case LIEU_COL_IDX -> listeConcert.get(rowIndex).getLieuConcert().getLieu();
				default -> null;
			};
		}

	}

	public Concert getConcertAt(int rowIndex) {
		return listeConcert.get(rowIndex);
	}
}
