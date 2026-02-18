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
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import org.fl.collectionAlbum.albums.Album;

public abstract class AbstractAlbumsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	protected static final int TITRE_COL_IDX = 0;
	protected static final int AUTEUR_COL_IDX = 1;
	protected static final int FORMAT_COL_IDX = 2;
	protected static final int MEDIA_FILES_COL_IDX = 3;
	protected static final int PROBLEM_COL_IDX = 4;
	protected static final int DISCOGS_COL_IDX = 5;
	protected static final int POIDS_COL_IDX = 6;
	protected static final int ENREGISTREMENT_COL_IDX = 7;
	protected static final int COMPOSITION_COL_IDX = 8;
	
	private static final String[] entetes = {"Titres", "Auteurs", "Formats", "Fichiers media", "Problème", "Discogs release", "Poids", "Enregistrement", "Composition"};

	@Override
	public int getColumnCount() {
		return entetes.length;
	}
	
	@Override
	public String getColumnName(int col) {
	    return entetes[col];
	}
	
	@Override
	public int getRowCount() {
		return getAlbumsList().size();
	}
	
    @Override
    public Class<?> getColumnClass(int columnIndex) {

    	return switch(columnIndex){
			case MEDIA_FILES_COL_IDX, AUTEUR_COL_IDX, ENREGISTREMENT_COL_IDX, COMPOSITION_COL_IDX -> Album.class;
			default ->  (getAlbumsList().isEmpty() ? Object.class :  getValueAt(0, columnIndex).getClass());
    	};
    }
    
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if (getAlbumsList().size() < rowIndex + 1) {
			// This may happen when triggering a rescan of the collection
			return null;
		} else {
			return switch(columnIndex){
				case TITRE_COL_IDX -> getAlbumsList().get(rowIndex).getTitre();
				case AUTEUR_COL_IDX -> getAlbumsList().get(rowIndex);
				case FORMAT_COL_IDX -> getAlbumsList().get(rowIndex)
					.getFormatAlbum()
					.getSupportsPhysiques().stream()
					.map(f -> f.getNom())
					.collect(Collectors.joining(","));
				case MEDIA_FILES_COL_IDX -> getAlbumsList().get(rowIndex);
				case PROBLEM_COL_IDX -> getAlbumsList().get(rowIndex).hasProblem();
				case DISCOGS_COL_IDX -> Optional.ofNullable(getAlbumsList().get(rowIndex).getDiscogsLink()).orElse("");
				case POIDS_COL_IDX -> getAlbumsList().get(rowIndex).getFormatAlbum().getPoids();
				case ENREGISTREMENT_COL_IDX ->  getAlbumsList().get(rowIndex);
				case COMPOSITION_COL_IDX ->  getAlbumsList().get(rowIndex);
				default -> null;
			};
		}
	}

	public Album getAlbumAt(int rowIndex) {
		return getAlbumsList().get(rowIndex);
	}
	
	protected abstract List<Album> getAlbumsList();
}
