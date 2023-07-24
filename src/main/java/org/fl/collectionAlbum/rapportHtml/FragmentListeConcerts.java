/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.fl.collectionAlbum.rapportHtml;

import java.net.URI;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.LieuConcert;
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
			URI aPath = RapportStructuresAndNames.getConcertRapportRelativeUri(unConcert) ;
			if (aPath != null) {
				fragment.append("<a href=\"").append(urlOffset).append(aPath.toString()).append("\">").append(dateConcert).append("</a>") ;
			} else {
				fragment.append(dateConcert) ;
			}
			
			fragment.append("</td>\n    <td class=\"auteur\">\n") ;
			
			if (unConcert.getAuteurs() != null) {
				for (Artiste unArtiste : unConcert.getAuteurs()) {
					appendLinkConcertArtiste(unArtiste, fragment, urlOffset) ;
				}
			}
			FragmentIntervenants.printIntervenant(unConcert, fragment, urlOffset) ;
			
			fragment.append("    </td>\n    <td class=\"album\">") ;
			
			LieuConcert lieuConcert = unConcert.getLieuConcert() ;
			URI lieuPath = RapportStructuresAndNames.getLieuRapportRelativeUri(lieuConcert) ;			
			if (lieuPath != null) {
				fragment.append("<a href=\"").append(urlOffset).append(lieuPath.toString()).append("\">").append(lieuConcert.getLieu()).append("</a>") ;
			} else {
				fragment.append(lieuConcert.getLieu()) ;
			}
			
			fragment.append("\n    </td>\n  </tr>\n") ;
		}
		fragment.append("</table>\n") ;
	}

	private static void appendLinkConcertArtiste(Artiste unArtiste, StringBuilder fragment,  String urlOffset) {
		URI concertUri = RapportStructuresAndNames.getArtisteConcertRapportRelativeUri(unArtiste) ;
		if (concertUri != null) {
			fragment.append("      <a href=\"").append(urlOffset).append(concertUri.toString()).append("\">").append(unArtiste.getPrenoms()).append(" ").append(unArtiste.getNom()).append("</a><br/>\n") ;

		}
	}
}
