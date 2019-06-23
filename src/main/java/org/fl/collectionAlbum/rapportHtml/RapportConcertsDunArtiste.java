package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.util.logging.Logger;

import org.fl.collectionAlbum.artistes.Artiste;

public class RapportConcertsDunArtiste extends RapportHtml {

	private final Artiste artiste ;
	
	protected RapportConcertsDunArtiste(Artiste a, String titre, File rFile, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, rFile, idxs, o, rl);
		artiste = a ;
	}

	@Override
	protected void corpsRapport() {
		
		write("<table class=\"auteurTab\">\n  <tr>\n    <td class=\"auteurTitre\"><span class=\"auteurTitre\">") ;
		write(artiste.getPrenoms()).write(" ").write(artiste.getNom()) ;
		write("</span> (").write(artiste.getDateNaissance()).write(" - ").write(artiste.getDateMort()) ;
		write(")</td>\n  </tr>\n</table>\n") ;
		FragmentListeConcerts.buildTable(artiste.getConcerts(), rBuilder, "../../");		
	}
}
