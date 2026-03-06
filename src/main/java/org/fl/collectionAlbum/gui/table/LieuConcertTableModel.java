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

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.fl.collectionAlbum.concerts.LieuConcert;
import org.fl.collectionAlbum.gui.UpdatableElement;

public class LieuConcertTableModel extends AbstractTableModel implements UpdatableElement {

	private static final long serialVersionUID = 1L;

	public static final int LIEU_COL_IDX = 0;
	public static final int NUMBER_COL_IDX = 1;
	
	private static final String[] entetes = {"Lieu", "Nombre"};
	
	private final List<LieuConcert> lieuxConcerts;
	
	public LieuConcertTableModel(List<LieuConcert> lieuxConcerts) {
		super();
		this.lieuxConcerts = lieuxConcerts;
	}
	
	@Override
	public int getRowCount() {
		return lieuxConcerts.size();
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
		
		if (lieuxConcerts.size() < rowIndex + 1) {
			// This may happen when triggering a rescan of the collection
			return null;
		} else {
			
			return switch(columnIndex) {
				case LIEU_COL_IDX -> lieuxConcerts.get(rowIndex).getLieu();
				case NUMBER_COL_IDX -> lieuxConcerts.get(rowIndex).getNombreConcert();
				default -> null;
			};
		}
	}

	@Override
	public void updateElement() {
		fireTableDataChanged();
	}
	
	public LieuConcert getLieuConcertAt(int rowIndex) {
		return lieuxConcerts.get(rowIndex);
	}

}
