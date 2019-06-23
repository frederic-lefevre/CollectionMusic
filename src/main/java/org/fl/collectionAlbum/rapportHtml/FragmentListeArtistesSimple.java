package org.fl.collectionAlbum.rapportHtml;

import java.util.List;

import org.fl.collectionAlbum.artistes.Artiste;

public class FragmentListeArtistesSimple {

	public static void buildTable(List<Artiste> auteurs, StringBuilder fragment) {

		fragment.append("<table>\n") ;
		for (Artiste unArtiste : auteurs) {
			fragment.append("  <tr>\n    <td class=\"auteur\">") ;
			fragment.append("<a href=\"") ;

			if (unArtiste.getNbAlbum() > 0){
				fragment.append(unArtiste.getUrlHtml()) ;
			} else {
				fragment.append(unArtiste.getConcertUrlHtml()) ;
			}

			fragment.append("\">").append(unArtiste.getPrenoms()).append(" ").append(unArtiste.getNom()).append("</a></td>\n") ;
			fragment.append("    <td class=\"an\">").append(unArtiste.getDateNaissance()).append("</td>\n") ;
			fragment.append("    <td class=\"an\">").append(unArtiste.getDateMort()).append("</td>\n") ;
			fragment.append("  </tr>\n") ;
		}
		fragment.append("</table>\n") ;
	}
}
