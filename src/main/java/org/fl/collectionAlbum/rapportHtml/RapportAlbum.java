package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.util.logging.Logger;

import org.fl.collectionAlbum.albums.Album;

public class RapportAlbum extends RapportHtml {

	private final Album album ;
	
	public RapportAlbum(Album a, File rFile, String o, Logger rl) {
		super("", rFile, null, o, rl);
		album = a ;
		withTitle(album.getTitre());
		
	}

	@Override
	protected void corpsRapport() {

		if (album.getNotes() != null) {
			for (String note : album.getNotes()) {
				write("<p>").write(note).write("</p>") ;
			}
		}	
	}
}
