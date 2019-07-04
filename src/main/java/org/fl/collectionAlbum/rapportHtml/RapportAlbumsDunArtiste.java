package org.fl.collectionAlbum.rapportHtml;

import java.net.URI;
import java.util.logging.Logger;

import org.fl.collectionAlbum.artistes.Artiste;

public class RapportAlbumsDunArtiste extends RapportHtml {

	private static final String ALBUMS = " - Albums" ;
	
	private final Artiste artiste ;
	
	public RapportAlbumsDunArtiste(Artiste a, String offset, Logger rl) {
		super("", rl);
		withOffset(offset) ;
		artiste = a ;
		
		withTitle(artiste.getPrenoms() + " " + artiste.getNom() + ALBUMS) ;
		HtmlLinkList concertLink = new HtmlLinkList(RapportStructuresAndNames.getAccueils()) ;
	
		if (artiste.getNbConcert() > 0) {
			URI concertUri = RapportStructuresAndNames.getArtisteConcertRapportRelativePath(artiste) ;
			concertLink.addLink("Concerts", concertUri.toString()) ;	
		}
		withHtmlLinkList(concertLink) ;
	}
	
	@Override
	protected void corpsRapport() {
		
		write("<table class=\"auteurTab\">\n  <tr>\n    <td rowspan=\"2\" class=\"auteurTitre\"><span class=\"auteurTitre\">") ;
		write(artiste.getPrenoms()).write(" ").write(artiste.getNom()) ;
		write("</span> (").write(artiste.getDateNaissance()).write(" - ").write(artiste.getDateMort()).write(")</td>\n") ;
		artiste.getAlbumsFormat().enteteFormat(rBuilder, "total", 1) ;
		write("  </tr>\n  <tr>\n") ;
		artiste.getAlbumsFormat().rowFormat(rBuilder, "artotal") ;
		write("  </tr>\n</table>\n") ;
		FragmentListeAlbums.buildTable(artiste.getAlbums(), rBuilder, "../../");		
	}
}
