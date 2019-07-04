package org.fl.collectionAlbum.rapportHtml;

import java.util.logging.Logger;

import org.fl.collectionAlbum.concerts.ListeConcert;

public class RapportListeConcerts extends RapportHtml {

	private ListeConcert listeConcerts ;
	
	public RapportListeConcerts(ListeConcert lc,  String titre, Logger rl) {
		super(titre, rl) ;
		withHtmlLinkList(RapportStructuresAndNames.getAccueils());
		withTitleDisplayed();
		listeConcerts = lc ;
	}

	@Override
	protected void corpsRapport() {
		
		FragmentListeConcerts.buildTable(listeConcerts, rBuilder, urlOffset);		
	}

}
