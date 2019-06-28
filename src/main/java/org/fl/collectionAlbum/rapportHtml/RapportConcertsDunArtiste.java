package org.fl.collectionAlbum.rapportHtml;

import java.net.URI;
import java.util.logging.Logger;

import org.fl.collectionAlbum.artistes.Artiste;

public class RapportConcertsDunArtiste extends RapportHtml {

	private final Artiste artiste ;
	
	protected RapportConcertsDunArtiste(Artiste a, String o, Logger rl) {
		super("", null, o, rl);
		artiste = a ;
		withTitle(artiste.getPrenoms() + " " + artiste.getNom()) ;
		HtmlLinkList albumLink = new HtmlLinkList(RapportStructuresAndNames.getAccueils(), "../../") ;
		
		URI albumUri = RapportStructuresAndNames.getArtisteAlbumRapportRelativePath(artiste) ;
		if (albumUri != null) {
			albumLink.addLink("Concerts", albumUri.toString()) ;	
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
