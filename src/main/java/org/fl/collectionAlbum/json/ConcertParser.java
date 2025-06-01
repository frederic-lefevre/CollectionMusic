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

package org.fl.collectionAlbum.json;

import java.net.URI;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.json.MusicArtefactParser.CheckPathOption;
import org.fl.collectionAlbum.utils.TemporalUtils;

import com.fasterxml.jackson.databind.JsonNode;

public class ConcertParser {

	private final static Logger albumLog = Logger.getLogger(ConcertParser.class.getName());
	
	public static TemporalAccessor getConcertDate(JsonNode arteFactJson) {

		TemporalAccessor dateConcert = null ;
		JsonNode jElem = arteFactJson.get(JsonMusicProperties.DATE) ;
		if (jElem == null) {
			albumLog.warning("Pas de date dans le concert " + arteFactJson);
		} else {
			try {
				dateConcert = TemporalUtils.parseDate(jElem.asText()) ;
			} catch (Exception e) {
				albumLog.log(Level.SEVERE, "Erreur dans les dates du concert " + arteFactJson, e) ;
			}
		}
		return dateConcert ;
	}
	
	public static String getConcertLieu(JsonNode arteFactJson) {		
		return ParserHelpers.parseStringProperty(arteFactJson, JsonMusicProperties.LIEU, true);
	}
	
	public static List<String> getConcertMorceaux(JsonNode arteFactJson) {	
		return ParserHelpers.getArrayAttribute(arteFactJson, JsonMusicProperties.MORCEAUX);
	}
	
	public static List<URI> getConcertTickets(JsonNode arteFactJson) {
		return MusicArtefactParser.getUrisList(arteFactJson, JsonMusicProperties.TICKET_IMG, Control.getConcertTicketImgUri(), CheckPathOption.EXISTS, CheckPathOption.IS_IMAGE_FILE);
	}
}
