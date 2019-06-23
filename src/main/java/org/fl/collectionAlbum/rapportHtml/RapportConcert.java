package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.concerts.Concert;

public class RapportConcert extends RapportHtml {

	private final Concert concert ;
	
	public RapportConcert(Concert c, String titre, File rFile, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, rFile, idxs, o, rl) ;
		concert = c ;
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
			try {
				for (int i=0; i < ticketImages.size(); i++) {
					String imgUrl = Control.getConcertTicketImgPath() + ticketImages.get(i) ;
					// check that the file exists
					if (! (new File(new URI(imgUrl))).exists()) {
						rapportLog.warning("Le fichier ticket image suivant n'existe pas: " + imgUrl + " Date du concert: " + concert.getDateConcert().toString()) ;
					}
					write("<a href=\"").write(imgUrl ).write("\">") ;
					write("<img class=\"ticket\" src=\"").write(imgUrl).write("\"/></a>") ;
				}
			} catch (URISyntaxException e) {
				rapportLog.log(Level.SEVERE, "Exception en traitant les images de concert", e) ;
			}
		}			
	}
}    	
