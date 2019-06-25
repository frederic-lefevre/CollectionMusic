package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.ListeConcert;

public class RapportListeConcerts extends RapportHtml {

	private ListeConcert listeConcerts ;
	
	public RapportListeConcerts(ListeConcert lc,  String titre, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, idxs, o, rl) ;
		withTitleDisplayed();
		listeConcerts = lc ;
		
		// Generer les rapports des concerts, si ils n'existent pas déjà
		 String styles[] = {RapportHtml.albumStyle} ;
		 for (Concert concert : listeConcerts.getConcerts()) {
	    	if (concert.additionnalInfo()) {		    				
	        	File htmlFile = new File(Control.getAbsoluteConcertDir() + concert.getArtefactHtmlName()) ;	        	
	        	if (! htmlFile.exists()) {
	        		RapportConcert rapportConcert = new RapportConcert(concert,  "../", rapportLog) ;
	        		rapportConcert.printReport(htmlFile, styles) ;
	        	}
	    	}
		 }
	}

	@Override
	protected void corpsRapport() {
		
		FragmentListeConcerts.buildTable(listeConcerts, rBuilder, urlOffset);		
	}

}
