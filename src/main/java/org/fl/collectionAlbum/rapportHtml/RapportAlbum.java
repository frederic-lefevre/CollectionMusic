package org.fl.collectionAlbum.rapportHtml;

import java.util.logging.Logger;

import org.fl.collectionAlbum.albums.Album;

public class RapportAlbum extends RapportMusicArtefact {

	private final Album album ;
	
	public RapportAlbum(Album a, Logger rl) {
		super(a, rl);
		album = a ;		
		withTitle(album.getTitre());	
	}

	@Override
	protected void corpsRapport() {
		super.corpsRapport();
	}
}
