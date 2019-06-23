package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.util.logging.Logger;

import org.fl.collectionAlbum.albums.Album;

public class RapportAlbum extends RapportHtml {

	private final Album album ;
	
	public RapportAlbum(Album a, String titre, File rFile, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, rFile, idxs, o, rl);
		album = a ;
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
