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

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import org.fl.collectionAlbum.disocgs.DiscogsAlbumRelease;

public class DisocgsReleaseTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	public final static int ID_COL_IDX = 0;
	public final static int ARTISTS_COL_IDX = 1;
	public final static int TITLE_COL_IDX = 2;
	public final static int FORMAT_COL_IDX = 3;
	public final static int ALBUM_LINK_COL_IDX = 4;
	public final static int FORMAT_MATCH_COL_IDX = 5;
			
	private final static String[] entetes = {"Id", "Auteurs", "Titre de l'album", "Formats", "Lié à un album", "Format Ok"};
	
	private final List<DiscogsAlbumRelease> discogsAlbumReleases;
	
	public DisocgsReleaseTableModel(List<DiscogsAlbumRelease> discogsAlbumReleases) {
		super();
		this.discogsAlbumReleases = discogsAlbumReleases;
	}

	@Override
	public int getRowCount() {
		return discogsAlbumReleases.size();
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
			case ID_COL_IDX -> discogsAlbumReleases.get(rowIndex).getInventoryCsvAlbum().getReleaseId();
			case ARTISTS_COL_IDX -> discogsAlbumReleases.get(rowIndex).getInventoryCsvAlbum().getArtists().stream().collect(Collectors.joining(","));
			case TITLE_COL_IDX -> discogsAlbumReleases.get(rowIndex).getInventoryCsvAlbum().getTitle();
			case FORMAT_COL_IDX -> discogsAlbumReleases.get(rowIndex).getInventoryCsvAlbum().getFormats().stream().collect(Collectors.joining(","));
			case ALBUM_LINK_COL_IDX -> discogsAlbumReleases.get(rowIndex).isLinkedToAlbum();
			case FORMAT_MATCH_COL_IDX -> discogsAlbumReleases.get(rowIndex).formatCompatibility();
			default -> null;
		};

	}

	public DiscogsAlbumRelease getDiscogsReleaseAt(int rowIndex) {
		return discogsAlbumReleases.get(rowIndex);
	}
}
