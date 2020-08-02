package org.fl.collectionAlbum.rapportHtml;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class RapportConcert extends RapportMusicArtefact {

	private final Concert concert ;
	
	public RapportConcert(Concert c, Logger rl) {
		super(c, rl) ;
		concert = c ;
		withTitle(TemporalUtils.formatDate(concert.getDateConcert()) + " " + concert.getLieuConcert().getLieu()) ;
	}

	@Override
	protected void corpsRapport() {
		
		super.corpsRapport();
		
		List<String> titres = concert.getTitres() ;
		if ((titres != null) && (titres.size() >0)) {
			write("<h3>Titres</h3>") ;
			write("<ol>") ;
			for (String titre : titres) {
				write("<li>").write(titre).write("</li>") ;
			}
			write("</ol>") ;
		}
		
		List<String> ticketImages = concert.getTicketImages() ;
		if (ticketImages != null) {
			
			for (int i=0; i < ticketImages.size(); i++) {
				URI imgUri = RapportStructuresAndNames.getTicketImageAbsoluteUri(ticketImages.get(i)) ;
				write("<a href=\"").write(imgUri.toString() ).write("\">\n") ;
				write("    <img class=\"ticket\" src=\"").write(imgUri.toString()).write("\"/>\n</a>") ;
			}			
		}			
	}
}    	
