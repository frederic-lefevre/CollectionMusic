package org.fl.collectionAlbum.rapportHtml;

import java.net.URI;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.ListeConcert;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class FragmentListeConcerts {

	// HTML fragment
	private final static String table1 = "<div class=\"mhc\">\n  <table>\n  <tr>\n    <td class=\"an\">Date</td>\n    <td class=\"auteur\">Artistes</td>\n    <td class=\"album\">Lieu</td>\n  </tr>\n  </table>\n</div>\n  <table>\n  <tr class=\"head\">\n    <td class=\"an\">Date</td>\n    <td class=\"auteur\">Artistes</td>\n    <td class=\"album\">Lieu</td>\n  </tr>\n" ;
	
	public static void buildTable(ListeConcert listeConcerts,StringBuilder fragment, String urlOffset) {
		
		fragment.append(table1) ;
		for (Concert unConcert : listeConcerts.getConcerts()) {
			String dateConcert = TemporalUtils.formatDate(unConcert.getDateConcert()) ;
			
			fragment.append("  <tr>\n    <td class=\"an\">") ;
			URI aPath = RapportStructuresAndNames.getConcertRapportPath(unConcert) ;
			if (aPath != null) {
				fragment.append("<a href=\"").append(aPath.toString()).append("\">") ;
			}
			fragment.append(dateConcert) ;
			if (unConcert.additionnalInfo()) {
				fragment.append("</a>") ;
			}
			fragment.append("</td>\n    <td class=\"auteur\">\n") ;
			
			if (unConcert.getAuteurs() != null) {
				for (Artiste unArtiste : unConcert.getAuteurs()) {
					appendLinkConcertArtiste(unArtiste, fragment, urlOffset) ;
				}
			}
			fragment.append("    </td>\n    <td class=\"album\">").append(unConcert.getLieuConcert()).append("\n") ;
			
			if (unConcert.getChefsOrchestre() != null) {
				for (Artiste unChef : unConcert.getChefsOrchestre()) {
					fragment.append("      <li>Direction: ") ;
					appendLinkConcertArtiste(unChef, fragment, urlOffset) ;
					fragment.append("</li>\n") ;
				}
			}
			
			if (unConcert.getInterpretes() != null) {
				for (Artiste unInterprete : unConcert.getInterpretes()) {
					fragment.append("      <li>Interpr&egrave;te: ") ;
					appendLinkConcertArtiste(unInterprete, fragment, urlOffset) ;
					fragment.append("</li>\n") ;
				}
			}
			
			if (unConcert.getEnsembles() != null) {
				for (Artiste unGroupe : unConcert.getEnsembles()) {
					fragment.append("      <li>Ensemble: ") ;
					appendLinkConcertArtiste(unGroupe, fragment, urlOffset) ;
					fragment.append("</li>\n") ;
				}
			}
			fragment.append("    </td>\n  </tr>\n") ;
		}
		fragment.append("</table>\n") ;
	}

	private static void appendLinkConcertArtiste(Artiste unArtiste, StringBuilder fragment,  String urlOffset) {
		URI concertUri = RapportStructuresAndNames.getArtisteConcertRapportRelativePath(unArtiste) ;
		if (concertUri != null) {
			fragment.append("      <a href=\"").append(urlOffset).append(concertUri.toString()).append("\">").append(unArtiste.getPrenoms()).append(" ").append(unArtiste.getNom()).append("</a><br/>\n") ;

		}
	}
}
