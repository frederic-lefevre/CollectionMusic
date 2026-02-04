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
	public static final int NB_CONCERTS_COL_IDX = 3;
	public static final int NB_ALBUMS_COL_IDX = 4;
	
	private static final long serialVersionUID = 1L;

	private static final String[] entetes = {"Noms", "Naissance", "Décès", "Concerts", "Albums"};
	
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
	
	// AbstractTableModel.getColumnClass is overridden because it interprets numbers as string
	// So they are left aligned instead of right aligned
	@Override
	public Class<?> getColumnClass(int columnIndex) {

		return switch(columnIndex){
			case NOM_COL_IDX, NAISSANCE_COL_IDX,DECES_COL_IDX -> Artiste.class;
			case NB_ALBUMS_COL_IDX, NB_CONCERTS_COL_IDX -> Number.class;
			default -> Object.class;
		};
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (artistesList.size() < rowIndex + 1) {
			// This may happen when triggering a rescan of the collection
			return null;
		} else {
			return switch(columnIndex){
				case NOM_COL_IDX -> artistesList.get(rowIndex);
				case NAISSANCE_COL_IDX -> artistesList.get(rowIndex);  // Returning a TemporalAccessor makes the sorting wrong: null value are put at the beginning
				case DECES_COL_IDX -> artistesList.get(rowIndex);
				case NB_CONCERTS_COL_IDX -> artistesList.get(rowIndex).getNbConcert();
				case NB_ALBUMS_COL_IDX -> artistesList.get(rowIndex).getNbAlbum();
				default -> null;
			};
		}
	}

}
