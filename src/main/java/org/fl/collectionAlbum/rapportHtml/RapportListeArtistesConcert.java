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

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;

public class RapportListeArtistesConcert extends RapportHtml {

	// HTML fragments
	private static final String F1 = "<div class=\"mhc\">\n  <table>\n  <tr>\n    <td class=\"auteur\">Auteurs</td>\n" +
									 "    <td class=\"an\">Naissance</td>\n    <td class=\"an\">Mort</td>\n";
	private static final String F2 = "    <td class=\"total\">Nombre<br/>concerts</td>\n";
	private static final String F3 = "  </tr>\n  </table>\n</div>\n<table>\n  <tr class=\"head\">\n    <td class=\"auteur\">Auteurs</td>\n" +
									 "    <td class=\"an\">Naissance</td>\n    <td class=\"an\">Mort</td>\n";

	private final ListeArtiste auteurs;
	
	public RapportListeArtistesConcert(ListeArtiste la, String titre, LinkType linkType) {
		super(titre, linkType);
		withHtmlLinkList(RapportStructuresAndNames.getAccueils());
		withTitleDisplayed();
		auteurs = la;
	}

	@Override
	protected void corpsRapport() {

		write(F1);
		write(F2);
		write(F3);
		write("    <td class=\"total\">Nombre<br/>concerts</td>\n");
		write("  </tr>\n");

		for (Artiste unArtiste : auteurs.getArtistes()) {
			write("  <tr>\n    <td class=\"auteur\">");
			if (balises != null) {				
				if (balises.getBalisesType() == Balises.BalisesType.ALPHA) {
					balises.addCheckBaliseString(rBuilder, unArtiste.getNom());
				} else if (balises.getBalisesType() == Balises.BalisesType.TEMPORAL) {
					balises.addCheckBaliseTemporal(rBuilder, unArtiste.getNaissance());
				} else {
					rapportLog.severe("Inappropriate Balises Type: " + balises.getBalisesType());
				}
			}

			URI concertUri = RapportStructuresAndNames.getArtisteConcertRapportRelativeUri(unArtiste);
			if (concertUri != null) {
				write("<a href=\"").write(concertUri.toString()).write("\">");
			}

			write(unArtiste.getPrenoms()).write(" ").write(unArtiste.getNom()).write("</a></td>\n");
			write("    <td class=\"an\">").write(unArtiste.getDateNaissance()).write("</td>\n");
			write("    <td class=\"an\">").write(unArtiste.getDateMort()).write("</td>\n");

			write("    <td class=\"total\">").write(unArtiste.getNbConcert()).write("</td>\n");

			write("  </tr>\n");
		}
		write("</table>\n");
	}
}
