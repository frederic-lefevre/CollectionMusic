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
import java.util.List;

import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class RapportConcert extends RapportMusicArtefact {

	private final Concert concert ;
	
	public RapportConcert(Concert c) {
		super(c) ;
		concert = c ;
		withTitle(TemporalUtils.formatDate(concert.getDateConcert()) + " " + concert.getLieuConcert().getLieu()) ;
	}

	@Override
	protected void corpsRapport() {
		
		super.corpsRapport();
		
		List<String> titres = concert.getTitres() ;
		if ((titres != null) && (titres.size() >0)) {
			write("<h3>Titres</h3>") ;
			write("<ol>") ;
			for (String titre : titres) {
				write("<li>").write(titre).write("</li>") ;
			}
			write("</ol>") ;
		}
		
		List<String> ticketImages = concert.getTicketImages() ;
		if (ticketImages != null) {
			
			for (String ticketImage : ticketImages) {
				URI imgUri = RapportStructuresAndNames.getTicketImageAbsoluteUri(ticketImage) ;
				write("<a href=\"").write(imgUri.toString() ).write("\">\n") ;
				write("    <img class=\"ticket\" src=\"").write(imgUri.toString()).write("\"/>\n</a>") ;
			}			
		}			
	}
}    	
