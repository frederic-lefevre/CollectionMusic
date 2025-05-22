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

import java.util.List;

import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class RapportConcert extends RapportMusicArtefact {

	private final Concert concert;
	
	private RapportConcert(Concert c) {
		super(c);
		concert = c;
		withTitle(TemporalUtils.formatDate(concert.getDateConcert()) + " " + concert.getLieuConcert().getLieu());
		withTitleDisplayed();
		
		HtmlLinkList concertLink = new HtmlLinkList(RapportStructuresAndNames.getAccueils());
		withHtmlLinkList(concertLink);
	}

	@Override
	protected void corpsRapport() {
		
		super.corpsRapport();
		
		List<String> titres = concert.getTitres();
		if ((titres != null) && (titres.size() > 0)) {
			write("<h3>Titres</h3>\n  <ol>\n");
			titres.forEach(titre -> write("    <li>").write(titre).write("</li>\n"));
			write("  </ol>\n");
		}
		
		concert.getTicketImages().forEach(ticketImageUri -> {
			write("<a href=\"").write(ticketImageUri.toString() ).write("\">\n");
			write("    <img class=\"ticket\" src=\"").write(ticketImageUri.toString()).write("\"/>\n</a>");
		});			
	}
	
	public static RapportConcert createRapportConcert(Concert c) {
		return new RapportConcert(c);
	}
}    	
