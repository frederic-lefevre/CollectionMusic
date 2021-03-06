package org.fl.collectionAlbum.rapportHtml;

import java.net.URI;
import java.util.logging.Logger;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;

public class RapportListeArtistesConcert extends RapportHtml {

	// HTML fragments
	private final static String F1 = "<div class=\"mhc\">\n  <table>\n  <tr>\n    <td class=\"auteur\">Auteurs</td>\n" +
									 "    <td class=\"an\">Naissance</td>\n    <td class=\"an\">Mort</td>\n" ;
	private final static String F2 = "    <td class=\"total\">Nombre<br/>concerts</td>\n" ;
	private final static String F3 = "  </tr>\n  </table>\n</div>\n<table>\n  <tr class=\"head\">\n    <td class=\"auteur\">Auteurs</td>\n" +
									 "    <td class=\"an\">Naissance</td>\n    <td class=\"an\">Mort</td>\n" ;

	private final ListeArtiste auteurs ;
	
	public RapportListeArtistesConcert(ListeArtiste la, String titre, Logger rl) {
		super(titre, rl);
		withHtmlLinkList(RapportStructuresAndNames.getAccueils());
		withTitleDisplayed();
		auteurs = la ;
	}

	@Override
	protected void corpsRapport() {

		write(F1) ;
		write(F2) ;
		write(F3) ;
		write("    <td class=\"total\">Nombre<br/>concerts</td>\n") ;
		write("  </tr>\n") ;		

		for (Artiste unArtiste : auteurs.getArtistes()) {
			write("  <tr>\n    <td class=\"auteur\">") ;
			if (balises != null) {
				balises.addCheckBaliseString(rBuilder, unArtiste.getNom()) ;
			}
			
			URI concertUri = RapportStructuresAndNames.getArtisteConcertRapportRelativePath(unArtiste) ;
			if (concertUri != null) {
				write("<a href=\"").write(concertUri.toString()). write("\">") ;
			}

			write(unArtiste.getPrenoms()).write(" ").write(unArtiste.getNom()).write("</a></td>\n") ;
			write("    <td class=\"an\">").write(unArtiste.getDateNaissance()).write("</td>\n") ;
			write("    <td class=\"an\">").write(unArtiste.getDateMort()).write("</td>\n") ;

			write("    <td class=\"total\">").write(unArtiste.getNbConcert()).write("</td>\n") ;

			write("  </tr>\n") ;
		}
		write("</table>\n") ;
	}
}
