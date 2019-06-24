package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Format;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;

public class RapportListeArtistesAlbum extends RapportHtml {

	// HTML fragments
	private final static String F1 = "<div class=\"mhc\">\n  <table>\n  <tr>\n    <td class=\"auteur\">Auteurs</td>\n" +
									 "    <td class=\"an\">Naissance</td>\n    <td class=\"an\">Mort</td>\n" ;
	private final static String F3 = "  </tr>\n  </table>\n</div>\n<table>\n  <tr class=\"head\">\n    <td class=\"auteur\">Auteurs</td>\n" +
									 "    <td class=\"an\">Naissance</td>\n    <td class=\"an\">Mort</td>\n" ;

	String styles[] = {"main","format","rapport", "artiste"} ;
	
	private final ListeArtiste auteurs ;
	
	public RapportListeArtistesAlbum(ListeArtiste la, String titre, File rFile, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, rFile, idxs, o, rl);
		auteurs = la ;
		withTitleDisplayed();
	}

	@Override
	protected void corpsRapport() {

		Format entete = new Format(null, rapportLog) ;

		write(F1) ;
		entete.enteteFormat(rBuilder, "total", 1) ;
		write(F3) ;
		entete.enteteFormat(rBuilder, "total", 1) ;
		write("  </tr>\n") ;

		for (Artiste unArtiste : auteurs.getArtistes()) {
			write("  <tr>\n    <td class=\"auteur\">") ;
			if (withAlphaBalises) {
				alphBalise(unArtiste.getNom().substring(0, 1)) ;
			}
			
			RapportAlbumsDunArtiste rapportDeSesAlbums = new RapportAlbumsDunArtiste(unArtiste, unArtiste.getHtmlFile(), "../../", rapportLog) ;
			rapportDeSesAlbums.printReport(styles) ;

			write("<a href=\"").write(unArtiste.getUrlHtml()). write("\">") ;

			write(unArtiste.getPrenoms()).write(" ").write(unArtiste.getNom()).write("</a></td>\n") ;
			write("    <td class=\"an\">").write(unArtiste.getDateNaissance()).write("</td>\n") ;
			write("    <td class=\"an\">").write(unArtiste.getDateMort()).write("</td>\n") ;

			unArtiste.getAlbumsFormat().rowFormat(rBuilder, "total") ;

			write("  </tr>\n") ;
		}
		write("</table>\n") ;
	}
}
