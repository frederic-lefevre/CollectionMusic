package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.util.logging.Logger;

import org.fl.collectionAlbum.albums.ListeAlbum;

public class RapportListeAlbums extends RapportHtml {

	private final ListeAlbum listeAlbums ;
	
	public RapportListeAlbums(ListeAlbum la, String titre, File rFile, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, rFile, idxs, o, rl);
		withTitleDisplayed() ;
		listeAlbums = la ;
	}

	@Override
	protected void corpsRapport() {
	
		FragmentListeAlbums.buildTable(listeAlbums, rBuilder, urlOffset) ;
	}
	

}
