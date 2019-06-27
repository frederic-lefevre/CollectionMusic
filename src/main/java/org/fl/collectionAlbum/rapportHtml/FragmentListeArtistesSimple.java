package org.fl.collectionAlbum.rapportHtml;

import java.nio.file.Path;
import java.util.List;

import org.fl.collectionAlbum.artistes.Artiste;

public class FragmentListeArtistesSimple {

	public static void buildTable(List<Artiste> auteurs, StringBuilder fragment) {

		fragment.append("<table>\n") ;
		for (Artiste unArtiste : auteurs) {
			fragment.append("  <tr>\n    <td class=\"auteur\">") ;
			fragment.append("<a href=\"") ;

			Path albumPath = RapportStructuresAndNames.getArtisteAlbumRapportRelativePath(unArtiste) ;
			if (albumPath != null) {
				fragment.append(albumPath.toString()) ;
			} else {
				Path concertPath = RapportStructuresAndNames.getArtisteConcertRapportRelativePath(unArtiste) ;
				if (concertPath != null) {
					fragment.append(concertPath.toString()) ;
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
