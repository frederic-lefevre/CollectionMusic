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

import java.util.Optional;

import javax.swing.table.AbstractTableModel;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;

public class AlbumsTableModel extends AbstractTableModel {

	public final static int TITRE_COL_IDX = 0;
	public final static int AUTEUR_COL_IDX = 1;
	public final static int MEDIA_FILES_COL_IDX = 2;
	public final static int PROBLEM_COL_IDX = 3;
	public final static int DISCOGS_COL_IDX = 4;
	
	private static final long serialVersionUID = 1L;
	
	private final static String[] entetes = {"Titres", "Auteurs", "Chemins des fichiers media", "Problème", "Discogs release"};
	
	private final CollectionAlbumContainer albumsContainer;
	
	public AlbumsTableModel(CollectionAlbumContainer albumsContainer) {
		super();
		this.albumsContainer = albumsContainer;
	}

	@Override
	public int getRowCount() {
		return getAlbumsList().getNombreAlbums();
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
    public Class<?> getColumnClass(int columnIndex) {
        if (getAlbumsList().getAlbums().isEmpty()) {
            return Object.class;
        } else {
        	return switch(columnIndex){
				case MEDIA_FILES_COL_IDX, AUTEUR_COL_IDX -> Album.class;
				default ->  getValueAt(0, columnIndex).getClass();
        	};
        }
    }
    
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if (getAlbumsList().getNombreAlbums() < rowIndex + 1) {
			// This may happen when triggering a rescan of the collection
			return null;
		} else {
			return switch(columnIndex){
				case TITRE_COL_IDX -> getAlbumsList().getAlbums().get(rowIndex).getTitre();
				case AUTEUR_COL_IDX ->getAlbumsList().getAlbums().get(rowIndex);
				case MEDIA_FILES_COL_IDX -> getAlbumsList().getAlbums().get(rowIndex);
				case PROBLEM_COL_IDX -> getAlbumsList().getAlbums().get(rowIndex).hasProblem();
				case DISCOGS_COL_IDX -> Optional.ofNullable(getAlbumsList().getAlbums().get(rowIndex).getDiscogsLink()).orElse("");
				default -> null;
			};
		}
	}

	public Album getAlbumAt(int rowIndex) {
		return getAlbumsList().getAlbums().get(rowIndex);
	}
	
	private ListeAlbum getAlbumsList() {
		return albumsContainer.getCollectionAlbumsMusiques();
	}
}
