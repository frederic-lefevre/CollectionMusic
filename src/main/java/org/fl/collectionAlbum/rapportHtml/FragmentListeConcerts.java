package org.fl.collectionAlbum.rapportHtml;

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
			if (unConcert.additionnalInfo()) {
				fragment.append("<a href=\"").append(unConcert.getFullPathHtmlFileName()).append("\">") ;
			}
			fragment.append(dateConcert) ;
			if (unConcert.additionnalInfo()) {
				fragment.append("</a>") ;
			}
			fragment.append("</td>\n    <td class=\"auteur\">\n") ;
			
			if (unConcert.getAuteurs() != null) {
				for (Artiste unArtiste : unConcert.getAuteurs()) {
					fragment.append("      <a href=\"").append(urlOffset).append(unArtiste.getConcertUrlHtml()).append("\">").append(unArtiste.getPrenoms()).append(" ").append(unArtiste.getNom()).append("</a><br/>\n") ;
				}
			}
			fragment.append("    </td>\n    <td class=\"album\">").append(unConcert.getLieuConcert()).append("\n") ;
			
			if (unConcert.getChefsOrchestre() != null) {
				for (Artiste unChef : unConcert.getChefsOrchestre()) {
					fragment.append("      <li>Direction: <a href=\"").append(urlOffset).append(unChef.getConcertUrlHtml()).append("\">").append(unChef.getPrenoms()).append(" ").append(unChef.getNom()).append("</a></li>\n") ;
				}
			}
			
			if (unConcert.getInterpretes() != null) {
				for (Artiste unInterprete : unConcert.getInterpretes()) {
					fragment.append("      <li>Interpr&egrave;te: <a href=\"").append(urlOffset).append(unInterprete.getConcertUrlHtml()).append("\">").append(unInterprete.getPrenoms()).append(" ").append(unInterprete.getNom()).append("</a></li>\n") ;
				}
			}
			
			if (unConcert.getEnsembles() != null) {
				for (Artiste unGroupe : unConcert.getEnsembles()) {
					fragment.append("      <li>Ensemble: <a href=\"").append(urlOffset).append(unGroupe.getConcertUrlHtml()).append("\">").append(unGroupe.getPrenoms()).append(" ").append(unGroupe.getNom()).append("</a></li>\n") ;
				}
			}
			fragment.append("    </td>\n  </tr>\n") ;
		}
		fragment.append("</table>\n") ;
	}
}
