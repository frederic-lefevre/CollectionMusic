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

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.gui.UpdatableElement;

public abstract class AbstractAlbumsTableModel extends AbstractTableModel implements UpdatableElement {

	private static final long serialVersionUID = 1L;

	private final List<AlbumTableColumn> albumTableColumns;
	
	AbstractAlbumsTableModel(List<AlbumTableColumn> albumTableColumns) {
		super();
		this.albumTableColumns = albumTableColumns;
	}
	
	public List<AlbumTableColumn> getAlbumTableColumns() {
		return albumTableColumns;
	}

	@Override
	public int getColumnCount() {
		return albumTableColumns.size();
	}
	
	@Override
	public String getColumnName(int col) {
	    return albumTableColumns.get(col).getName();
	}
	
	@Override
	public int getRowCount() {
		return getAlbumsList().size();
	}
	
    @Override
    public Class<?> getColumnClass(int columnIndex) {
    	return albumTableColumns.get(columnIndex).getValueClass();
    }
    
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if (getAlbumsList().size() < rowIndex + 1) {
			// This may happen when triggering a rescan of the collection
			return null;
		} else {
			return albumTableColumns.get(columnIndex).getValueGetter().apply(getAlbumsList().get(rowIndex));
		}
	}

	public Album getAlbumAt(int rowIndex) {
		return getAlbumsList().get(rowIndex);
	}
	
	@Override
	public void updateElement() {
		fireTableDataChanged();
	}
	
	protected abstract List<Album> getAlbumsList();
	
	public static final List<AlbumTableColumn> REGULAR_COLUMN_LIST = List.of(
			AlbumTableColumn.TITRE, 
			AlbumTableColumn.AUTEURS, 
			AlbumTableColumn.FORMAT, 
			AlbumTableColumn.DISCOGS,
			AlbumTableColumn.POIDS,
			AlbumTableColumn.ENREGISTREMENT,
			AlbumTableColumn.COMPOSITION);
	
	public static final List<AlbumTableColumn> AUGMENTED_COLUMN_LIST = List.of(
			AlbumTableColumn.TITRE, 
			AlbumTableColumn.AUTEURS, 
			AlbumTableColumn.FORMAT,
			AlbumTableColumn.MEDIA_FILES, 
			AlbumTableColumn.PROBLEM,
			AlbumTableColumn.DISCOGS,
			AlbumTableColumn.POIDS,
			AlbumTableColumn.ENREGISTREMENT,
			AlbumTableColumn.COMPOSITION);
}
