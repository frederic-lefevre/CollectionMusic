package org.fl.collectionAlbum.rapportHtml;

import java.net.URI;
import java.util.List;

import org.fl.collectionAlbum.artistes.Artiste;

public class FragmentListeArtistesSimple {

	public static void buildTable(List<Artiste> auteurs, StringBuilder fragment) {

		fragment.append("<table>\n") ;
		for (Artiste unArtiste : auteurs) {
			fragment.append("  <tr>\n    <td class=\"auteur\">") ;
			fragment.append("<a href=\"") ;

			URI albumUri = RapportStructuresAndNames.getArtisteAlbumRapportRelativeUri(unArtiste) ;
			if (albumUri != null) {
				fragment.append(albumUri.toString()) ;
			} else {
				URI concertUri = RapportStructuresAndNames.getArtisteConcertRapportRelativeUri(unArtiste) ;
				if (concertUri != null) {
					fragment.append(concertUri.toString()) ;
				}
			}

			fragment.append("\">").append(unArtiste.getPrenoms()).append(" ").append(unArtiste.getNom()).append("</a></td>\n") ;
			fragment.append("    <td class=\"an\">").append(unArtiste.getDateNaissance()).append("</td>\n") ;
			fragment.append("    <td class=\"an\">").append(unArtiste.getDateMort()).append("</td>\n") ;
			fragment.append("  </tr>\n") ;
		}
		fragment.append("</table>\n") ;
	}
}
