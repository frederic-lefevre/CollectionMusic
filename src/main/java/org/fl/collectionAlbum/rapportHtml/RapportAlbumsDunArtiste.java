/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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
import org.fl.collectionAlbum.format.Format;

public class RapportAlbumsDunArtiste extends RapportHtml {

	private static final String ALBUMS = " - Albums" ;
	
	private final Artiste artiste ;
	
	private static final boolean DONT_APPEND_AUDIO_FILE = false;
	
	public RapportAlbumsDunArtiste(Artiste a, String offset) {
		super("", null);
		withOffset(offset) ;
		artiste = a ;
		
		withTitle(artiste.getPrenoms() + " " + artiste.getNom() + ALBUMS) ;
		HtmlLinkList concertLink = new HtmlLinkList(RapportStructuresAndNames.getAccueils()) ;
	
		if (artiste.getNbConcert() > 0) {
			URI concertUri = RapportStructuresAndNames.getArtisteConcertRapportRelativeUri(artiste) ;
			concertLink.addLink("Concerts", concertUri.toString()) ;	
		}
		withHtmlLinkList(concertLink) ;
	}
	
	@Override
	protected void corpsRapport() {
		
		write("<table class=\"auteurTab\">\n  <tr>\n    <td rowspan=\"2\" class=\"auteurTitre\"><span class=\"auteurTitre\">");
		write(artiste.getPrenoms());
		write(" ");
		write(artiste.getNom());
		write("</span> (");
		write(artiste.getDateNaissance());
		write(" - ");
		write(artiste.getDateMort());
		write(")<br/>Nombre d'albums: ");
		write(artiste.getAlbums().getNombreAlbums());
		write("</td>\n");
		Format.enteteFormat(rBuilder, "total", 1, DONT_APPEND_AUDIO_FILE);
		write("  </tr>\n  <tr>\n");
		artiste.getAlbumsFormat().rowFormat(rBuilder, "artotal", DONT_APPEND_AUDIO_FILE);
		write("  </tr>\n</table>\n");
		FragmentListeAlbums.buildTable(artiste.getAlbums(), rBuilder, "../../", balises);		
	}
}
