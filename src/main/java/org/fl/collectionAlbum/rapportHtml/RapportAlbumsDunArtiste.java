package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.artistes.Artiste;

public class RapportAlbumsDunArtiste extends RapportHtml {

	private final Artiste artiste ;
	
	public RapportAlbumsDunArtiste(Artiste a, File rFile, String o, Logger rl) {
		super("", rFile, null, o, rl);
		artiste = a ;
		if (! rapportExists()) {		
			withTitle(artiste.getPrenoms() + " " + artiste.getNom()) ;
			HtmlLinkList concertLink = new HtmlLinkList(Control.getAccueils(), "../../") ;
			if (artiste.getConcerts().getNombreConcerts() > 0) {
				concertLink.addLink("Concerts", artiste.getHtmlConcertFileName()) ;
			}
			withHtmlLinkList(concertLink) ;
		}
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
