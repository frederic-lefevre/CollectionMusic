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

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;

public class AlbumsTableModel extends AbstractTableModel {

	public final static int TITRE_COL_IDX = 0;
	public final static int AUTEUR_COL_IDX = 1;
	public final static int MEDIA_FILES_COL_IDX = 2;
	
	private static final long serialVersionUID = 1L;

	private final static String AUTEURS_SEPARATOR = ", ";
	
	private final static String[] entetes = {"Titres", "Auteurs", "Media files"};
	
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
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch(columnIndex){
		case TITRE_COL_IDX:
			return getAlbumsList().getAlbums().get(rowIndex).getTitre();
		case AUTEUR_COL_IDX:
			return getAlbumsList().getAlbums().get(rowIndex)
					.getAuteurs().stream()
					.map(auteur -> auteur.getNomComplet())
					.collect(Collectors.joining(AUTEURS_SEPARATOR));
		case MEDIA_FILES_COL_IDX:
			return getAlbumsList().getAlbums().get(rowIndex);
		default:
			return null;
		}
	}

	public Album getAlbumAt(int rowIndex) {
		return getAlbumsList().getAlbums().get(rowIndex);
	}
	
	private ListeAlbum getAlbumsList() {
		return albumsContainer.getCollectionAlbumsMusiques();
	}
}