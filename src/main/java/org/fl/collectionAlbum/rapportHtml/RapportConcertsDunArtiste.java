package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.artistes.Artiste;

public class RapportConcertsDunArtiste extends RapportHtml {

	private final Artiste artiste ;
	
	protected RapportConcertsDunArtiste(Artiste a, File rFile, String o, Logger rl) {
		super("", rFile, null, o, rl);
		artiste = a ;
		withTitle(artiste.getPrenoms() + " " + artiste.getNom()) ;
		HtmlLinkList albumLink = new HtmlLinkList(Control.getAccueils(), "../../") ;
		if (artiste.getConcerts().getNombreConcerts() > 0) {
			albumLink.addLink("Albums", artiste.getHtmlFileName()) ;						
		}
		withHtmlLinkList(albumLink) ;
		
	}

	@Override
	protected void corpsRapport() {
		
		write("<table class=\"auteurTab\">\n  <tr>\n    <td class=\"auteurTitre\"><span class=\"auteurTitre\">") ;
		write(artiste.getPrenoms()).write(" ").write(artiste.getNom()) ;
		write("</span> (").write(artiste.getDateNaissance()).write(" - ").write(artiste.getDateMort()) ;
		write(")</td>\n  </tr>\n</table>\n") ;
		FragmentListeConcerts.buildTable(artiste.getConcerts().sortChrono(), rBuilder, "../../");		
	}
}
