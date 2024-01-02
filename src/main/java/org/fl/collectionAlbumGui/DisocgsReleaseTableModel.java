/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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

import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.discogsInterface.inventory.InventoryCsvAlbum;

public class DisocgsReleaseTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	public final static int ID_COL_IDX = 0;
	public final static int ARTISTS_COL_IDX = 1;
	public final static int TITLE_COL_IDX = 2;
	public final static int FORMAT_COL_IDX = 3;
	
	private final static String[] entetes = {"Id", "Auteurs", "Titre de l'album", "Formats"};
	
	public DisocgsReleaseTableModel() {
		super();
	}

	@Override
	public int getRowCount() {
		return DiscogsInventory.getDiscogsInventory().size();
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
    public boolean isCellEditable(int row, int col) {
        return true;
    }
    
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		return switch(columnIndex){
			case ID_COL_IDX -> DiscogsInventory.getDiscogsInventory().get(rowIndex).getReleaseId();
			case ARTISTS_COL_IDX -> DiscogsInventory.getDiscogsInventory().get(rowIndex).getArtists().stream().collect(Collectors.joining(","));
			case TITLE_COL_IDX -> DiscogsInventory.getDiscogsInventory().get(rowIndex).getTitle();
			case FORMAT_COL_IDX -> DiscogsInventory.getDiscogsInventory().get(rowIndex).getFormats().stream().collect(Collectors.joining(","));
			default -> null;
		};

	}

	public InventoryCsvAlbum getDiscogsReleaseAt(int rowIndex) {
		return DiscogsInventory.getDiscogsInventory().get(rowIndex);
	}
}
