package org.fl.collectionAlbum.rapportHtml;

import java.util.logging.Logger;

public class RapportLieuxConcerts  extends RapportHtml {

	protected RapportLieuxConcerts(String titre, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, idxs, o, rl);
		withTitleDisplayed() ;
	}

	@Override
	protected void corpsRapport() {
		
		write("<h1>VIDE</h1>") ;
	}

}
