package org.fl.collectionAlbum.rapportHtml;

import java.util.logging.Logger;

import org.fl.collectionAlbum.concerts.LieuConcert;

public class RapportConcertDunLieu  extends RapportHtml{

	private LieuConcert lieuConcert ;
	
	protected RapportConcertDunLieu(LieuConcert lc, String o, Logger rl) {
		super("", null, o, rl);
		lieuConcert = lc ;
		withTitle(lieuConcert.getLieu()) ;
		withTitleDisplayed();		
	}

	@Override
	protected void corpsRapport() {
		
		FragmentListeConcerts.buildTable(lieuConcert.getConcerts(),  rBuilder, "../");	
	}

}
