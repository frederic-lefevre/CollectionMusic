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

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.fl.collectionAlbum.artistes.Artiste;

public class ArtistesTableModel extends AbstractTableModel {

	public static final int NOM_COL_IDX = 0;
	public static final int NAISSANCE_COL_IDX = 1;
	public static final int DECES_COL_IDX = 2;
	public static final int NB_ALBUMS_COL_IDX = 3;
	public static final int NB_CONCERTS_COL_IDX = 4;
	
	private static final long serialVersionUID = 1L;

	private static final String[] entetes = {"Noms", "Naissance", "Décès", "Albums", "Concerts"};
	
	private final List<Artiste> artistesList;
	
	public ArtistesTableModel(List<Artiste> artistesList) {
		super();
		this.artistesList = artistesList;
	}
	
	@Override
	public int getRowCount() {
		return artistesList.size();
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
		if (artistesList.size() < rowIndex + 1) {
			// This may happen when triggering a rescan of the collection
			return null;
		} else {
			return switch(columnIndex){
				case NOM_COL_IDX -> artistesList.get(rowIndex);
				case NAISSANCE_COL_IDX -> artistesList.get(rowIndex).getNaissance();
				case DECES_COL_IDX -> artistesList.get(rowIndex).getMort();
				case NB_ALBUMS_COL_IDX -> artistesList.get(rowIndex).getNbAlbum();
				case NB_CONCERTS_COL_IDX -> artistesList.get(rowIndex).getNbConcert();
				default -> null;
			};
		}
	}

}
