package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
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

	private final static String styles[] = {"main","format","rapport", "artiste"} ;
	
	private final ListeArtiste auteurs ;
	
	public RapportListeArtistesConcert(ListeArtiste la, String titre, File rFile, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, rFile, idxs, o, rl);
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
			if (withAlphaBalises) {
				alphBalise(unArtiste.getNom().substring(0, 1)) ;
			}
			
			 RapportConcertsDunArtiste rapportDeSesConcerts = new RapportConcertsDunArtiste(unArtiste, unArtiste.getHtmlConcertFile(), "../../", rapportLog) ;
			 rapportDeSesConcerts.printReport(styles) ;

			write("<a href=\"").write(unArtiste.getConcertUrlHtml()).write("\">") ;

			write(unArtiste.getPrenoms()).write(" ").write(unArtiste.getNom()).write("</a></td>\n") ;
			write("    <td class=\"an\">").write(unArtiste.getDateNaissance()).write("</td>\n") ;
			write("    <td class=\"an\">").write(unArtiste.getDateMort()).write("</td>\n") ;

			write("    <td class=\"total\">").write(unArtiste.getNbConcert()).write("</td>\n") ;

			write("  </tr>\n") ;
		}
		write("</table>\n") ;
	}
}
