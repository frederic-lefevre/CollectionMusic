package org.fl.collectionAlbum.rapportHtml;

import java.util.logging.Logger;

import org.fl.collectionAlbum.albums.ListeAlbum;

public class RapportListeAlbums extends RapportHtml {

	private final ListeAlbum listeAlbums ;
	
	public RapportListeAlbums(ListeAlbum la, String titre, HtmlLinkList idxs, Logger rl) {
		super(titre, idxs, rl);
		withTitleDisplayed() ;
		listeAlbums = la ;
	}

	@Override
	protected void corpsRapport() {
	
		FragmentListeAlbums.buildTable(listeAlbums, rBuilder, urlOffset) ;
	}	

}
