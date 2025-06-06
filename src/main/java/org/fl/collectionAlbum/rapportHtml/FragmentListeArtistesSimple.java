/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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
import java.util.List;

import org.fl.collectionAlbum.artistes.Artiste;

public class FragmentListeArtistesSimple {

	public static void buildTable(List<Artiste> auteurs, StringBuilder fragment) {

		fragment.append("<table>\n");
		for (Artiste unArtiste : auteurs) {
			fragment.append("  <tr>\n    <td class=\"auteur\">");
			fragment.append("<a href=\"");

			URI albumUri = RapportStructuresAndNames.getArtisteAlbumRapportRelativeUri(unArtiste);
			if (albumUri != null) {
				fragment.append(albumUri.toString());
			} else {
				URI concertUri = RapportStructuresAndNames.getArtisteConcertRapportRelativeUri(unArtiste);
				if (concertUri != null) {
					fragment.append(concertUri.toString());
				}
			}

			fragment.append("\">").append(unArtiste.getPrenoms()).append(" ").append(unArtiste.getNom())
					.append("</a></td>\n");
			fragment.append("    <td class=\"an\">").append(unArtiste.getDateNaissance()).append("</td>\n");
			fragment.append("    <td class=\"an\">").append(unArtiste.getDateMort()).append("</td>\n");
			fragment.append("  </tr>\n");
		}
		fragment.append("</table>\n");
	}
}
