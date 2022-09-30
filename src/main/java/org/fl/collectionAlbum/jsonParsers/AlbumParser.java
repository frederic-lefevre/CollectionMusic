package org.fl.collectionAlbum.jsonParsers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Format;
import org.fl.collectionAlbum.Format.RangementSupportPhysique;
import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.utils.TemporalUtils;
import org.fl.util.date.FuzzyPeriod;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AlbumParser {

	public static String getAlbumTitre(JsonObject jAlbum, Logger albumLog) {
		
		String titre ;
		JsonElement jElem = jAlbum.get(JsonMusicProperties.TITRE) ;
		if (jElem == null) {
			albumLog.warning("Titre de l'album null pour l'album " + jAlbum.toString());
            titre = "" ;
        } else {
        	titre = jElem.getAsString() ;
        }
		albumLog.finest(() -> "Titre: " + titre);
		return titre ;
	}
	
	public static Format getFormatAlbum(JsonObject jAlbum, Logger albumLog) {

        Format formatAlbum;
		JsonElement jElem = jAlbum.get(JsonMusicProperties.FORMAT) ;
        if (jElem == null) {
        	albumLog.warning("Format d'album null pour l'album " + jAlbum) ;
            formatAlbum = new Format(null, albumLog) ;
        } else {
        	formatAlbum = new Format(jElem.getAsJsonObject(), albumLog) ;
        	if (formatAlbum.hasError()) {
        		albumLog.warning("Format d'album en erreur pour l'album " + jAlbum) ;
        	}
        }
        return formatAlbum;
    }
	
	public static RangementSupportPhysique getRangementAlbum(JsonObject jAlbum, Logger albumLog) {
		
		RangementSupportPhysique rangementAlbum;
		JsonElement jElem = jAlbum.get(JsonMusicProperties.RANGEMENT) ;
		if (jElem != null) {
			String jsonProp = jElem.getAsString();
			rangementAlbum = RangementSupportPhysique.getRangement(jsonProp);
			if (rangementAlbum == null) {
				albumLog.warning("Rangement d'album " + jsonProp + "inconnu pour l'album " + jAlbum) ;
			}
		} else {
			rangementAlbum = null;
		}
		return rangementAlbum;
	}
	
	public static FuzzyPeriod processPeriodEnregistrement(JsonObject albumJson, Logger albumLog) {
		FuzzyPeriod periodeEnregistrement = processPeriod(albumJson, JsonMusicProperties.ENREGISTREMENT, albumLog) ;
		if (periodeEnregistrement == null) {
			albumLog.warning(" Pas de dates d'enregistrement pour l'album " + albumJson);
        }
		return periodeEnregistrement ;
	}
	
	public static FuzzyPeriod processPeriodComposition(JsonObject albumJson, Logger albumLog) {
		return processPeriod(albumJson, JsonMusicProperties.COMPOSITION, albumLog) ;
	}
	
	private static FuzzyPeriod processPeriod(JsonObject albumJson, String periodProp, Logger albumLog) {
		
		FuzzyPeriod period = null ;
		JsonElement jElem = albumJson.get(periodProp) ;
		if (jElem != null) {
			if (!jElem.isJsonArray()) {
				albumLog.warning(periodProp + " n'est pas un JsonArray pour l'album " + albumJson);
			} else {
				JsonArray jDates = jElem.getAsJsonArray();
				if (jDates.size() != 2) {
					albumLog.warning(periodProp + " ne contient pas 2 éléments pour l'album " + albumJson);
				} else {

					try {
						String debut = jDates.get(0).getAsString();
						String fin = jDates.get(1).getAsString();

						period = new FuzzyPeriod(TemporalUtils.parseDate(debut), TemporalUtils.parseDate(fin), albumLog) ;
						
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
