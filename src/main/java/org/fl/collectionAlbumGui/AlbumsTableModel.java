package org.fl.collectionAlbumGui;

import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;

public class AlbumsTableModel extends AbstractTableModel {

	public final static int TITRE_COL_IDX = 0;
	public final static int AUTEUR_COL_IDX = 1;
	
	private static final long serialVersionUID = 1L;

	private final static String AUTEURS_SEPARATOR = ", ";
	
	private final static String[] entetes = {"Titres", "Auteurs"};
	
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
