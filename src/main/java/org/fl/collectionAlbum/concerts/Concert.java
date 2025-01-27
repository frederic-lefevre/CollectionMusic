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

package org.fl.collectionAlbum.concerts;

import java.nio.file.Path;
import java.time.temporal.TemporalAccessor;
import java.util.List;

import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.json.ConcertParser;

import com.google.gson.JsonObject;

public class Concert extends MusicArtefact {

    private TemporalAccessor dateConcert;   
    private LieuConcert	lieuConcert;     
    private List<String> titres;    
    private List<String> ticketImages;
    
	public Concert(JsonObject concertJson, List<ListeArtiste> knownArtistes, LieuxDesConcerts lieuxDesConcerts,
			Path jsonFilePath) {

		super(concertJson, knownArtistes, jsonFilePath);

		dateConcert = ConcertParser.getConcertDate(concertJson);
		lieuConcert = lieuxDesConcerts.addLieuDunConcert(ConcertParser.getConcertLieu(concertJson));
		titres = ConcertParser.getConcertMorceaux(concertJson);
		ticketImages = ConcertParser.getConcertTickets(concertJson);
	}

	@Override
	public boolean hasAdditionnalInfo() {

		boolean res = false;
		if ((ticketImages != null) && (ticketImages.size() > 0)) {
			res = true;
		} else {
			res = super.hasAdditionnalInfo();
		}
		return res;
	}

	public LieuConcert getLieuConcert() {
		return lieuConcert;
	}

	public TemporalAccessor getDateConcert() {
		return dateConcert;
	}

	public List<String> getTitres() {
		return titres;
	}

	public List<String> getTicketImages() {
		return ticketImages;
	}
	
}