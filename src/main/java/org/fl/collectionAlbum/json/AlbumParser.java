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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.format.Format.RangementSupportPhysique;
import org.fl.collectionAlbum.utils.FuzzyPeriod;
import org.fl.collectionAlbum.utils.TemporalUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class AlbumParser {

	private final static Logger albumLog =  Logger.getLogger(AlbumParser.class.getName());
	
	public static String getAlbumTitre(JsonNode jAlbum) {
		
		String titre =  ParserHelpers.parseStringProperty(jAlbum, JsonMusicProperties.TITRE, true);
		albumLog.finest(() -> "Titre: " + titre);
		return titre ;
	}
	
	public static Path getAlbumSleevePath(JsonNode jAlbum) {
		
		String sleeveImg =  ParserHelpers.parseStringProperty(jAlbum, JsonMusicProperties.SLEEVE_IMG, false);
		if (sleeveImg == null) {
			return null;
		} else {
			Path sleevePath = Path.of(sleeveImg);
			if (sleevePath.isAbsolute()) {
				if (Files.exists(sleevePath)) {
					return sleevePath;
				} else {
					albumLog.warning("Le path de la pochette n'existe pas pour l'album " + jAlbum);
					return null;					
				}
			} else {
				albumLog.warning("Le path de la pochette n'est pas absolu pour l'album " + jAlbum);
				return null;
			}
		}
	}
	
	public static Format getFormatAlbum(ObjectNode jAlbum) {

        Format formatAlbum;
        JsonNode jElem = jAlbum.get(JsonMusicProperties.FORMAT) ;
        if (jElem == null) {
        	albumLog.warning("Format d'album null pour l'album " + jAlbum) ;
            formatAlbum = new Format(null) ;
        } else {
        	formatAlbum = new Format((ObjectNode)jElem) ;
        	if (formatAlbum.hasError()) {
        		albumLog.warning("Format d'album en erreur pour l'album " + jAlbum) ;
        	}
        }
        return formatAlbum;
    }
	
	public static RangementSupportPhysique getRangementAlbum(JsonNode jAlbum) {
		
		RangementSupportPhysique rangementAlbum;
		JsonNode jElem = jAlbum.get(JsonMusicProperties.RANGEMENT) ;
		if (jElem != null) {
			String jsonProp = jElem.asText();
			rangementAlbum = RangementSupportPhysique.getRangement(jsonProp);
			if (rangementAlbum == null) {
				albumLog.warning("Rangement d'album " + jsonProp + "inconnu pour l'album " + jAlbum) ;
			}
		} else {
			rangementAlbum = null;
		}
		return rangementAlbum;
	}
	
	public static FuzzyPeriod processPeriodEnregistrement(JsonNode albumJson) {
		FuzzyPeriod periodeEnregistrement = processPeriod(albumJson, JsonMusicProperties.ENREGISTREMENT) ;
		if (periodeEnregistrement == null) {
			albumLog.warning(" Pas de dates d'enregistrement pour l'album " + albumJson);
        }
		return periodeEnregistrement ;
	}
	
	public static FuzzyPeriod processPeriodComposition(JsonNode albumJson) {
		return processPeriod(albumJson, JsonMusicProperties.COMPOSITION) ;
	}
	
	private static FuzzyPeriod processPeriod(JsonNode albumJson, String periodProp) {
		
		FuzzyPeriod period = null ;
		JsonNode jDates = albumJson.get(periodProp) ;
		if (jDates != null) {
			if (!jDates.isArray()) {
				albumLog.warning(periodProp + " n'est pas un JsonArray pour l'album " + albumJson);
			} else {
				if (jDates.size() != 2) {
					albumLog.warning(periodProp + " ne contient pas 2 éléments pour l'album " + albumJson);
				} else {

					try {
						String debut = jDates.get(0).asText();
						String fin = jDates.get(1).asText();

						period = new FuzzyPeriod(TemporalUtils.parseDate(debut), TemporalUtils.parseDate(fin)) ;
						
						if (! period.isValid()) {
							albumLog.warning(periodProp + " : Erreur dans les dates de l'album " + albumJson);
						}
					} catch (Exception e) {
						albumLog.log(Level.SEVERE, periodProp + " : Erreur dans les dates de " + albumJson, e);
					}					
				}
			}
		}
		return period ;
	}
}
