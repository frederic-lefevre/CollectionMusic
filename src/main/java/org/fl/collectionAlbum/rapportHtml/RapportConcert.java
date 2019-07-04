package org.fl.collectionAlbum.rapportHtml;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class RapportConcert extends RapportHtml {

	private final Concert concert ;
	
	public RapportConcert(Concert c, Logger rl) {
		super("", rl) ;
		concert = c ;
		withTitle(TemporalUtils.formatDate(concert.getDateConcert()) + " " + concert.getLieuConcert().getLieu()) ;
	}

	@Override
	protected void corpsRapport() {
		
		List<String> notes = concert.getNotes() ;
		if (notes != null) {
			for (String note : notes) {
				write("<p>").write(note).write("</p>") ;
			}
		}
		
		List<String> titres = concert.getTitres() ;
		if ((titres != null) && (titres.size() >0)) {
			write("<h3>Titres</h3>") ;
			write("<ol>") ;
			for (String titre : titres) {
				write("<li>").write(titre).write("</li>") ;
			}
			write("</ol>") ;
		}
		
		String urlInfos = concert.getUrlInfos() ;
		if (urlInfos != null) {
			write("<h3><a href=\"").write(urlInfos).write("\">Informations</a></h3>") ;
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
