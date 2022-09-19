package org.fl.collectionAlbum.albums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Format;
import org.fl.collectionAlbum.RangementComparator;

public class ListeAlbum {
	
	private List<Album> albums ;
	
	private Format formatListeAlbum ;
	
	private Logger listeAlbumLog;
	
	public ListeAlbum(Logger al) {
		
		listeAlbumLog = al ;
		formatListeAlbum = new Format(null, listeAlbumLog) ;
		albums  = new ArrayList<Album>() ;
	}
	
	public void addAlbum(Album a) {
		if (! albums.contains(a)) {
			albums.add(a) ;
			formatListeAlbum.incrementFormat(a.getFormatAlbum()) ;
		}
	}
	
	public ListeAlbum sortChronoEnregistrement() {
		AlbumEnregistrementComparator compAlbum = new AlbumEnregistrementComparator(listeAlbumLog);
		Collections.sort(albums, compAlbum) ;
		return this ;
	}
	
	public ListeAlbum sortChronoComposition() {
		AlbumCompositionComparator compAlbum = new AlbumCompositionComparator(listeAlbumLog);
		Collections.sort(albums, compAlbum) ;
		return this ;
	}
	
	public ListeAlbum sortRangementAlbum() {
		RangementComparator compAlbum = new RangementComparator(listeAlbumLog);
		Collections.sort(albums, compAlbum) ;
		return this ;
	}
	
	public Format getFormatListeAlbum() {
		return formatListeAlbum;
	}
	
	public int getNombreAlbums() {
		return albums.size() ;
	}
	
	public List<Album> getAlbums() {
		return albums;
	}

}
