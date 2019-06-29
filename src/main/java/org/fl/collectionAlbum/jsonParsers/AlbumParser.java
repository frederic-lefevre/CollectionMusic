package org.fl.collectionAlbum.jsonParsers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.utils.TemporalUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.lge.fl.util.date.FuzzyPeriod;

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
		if (albumLog.isLoggable(Level.FINEST)) {
			albumLog.finest("Titre: " + titre);
		}
		return titre ;
	}
	
	public static FuzzyPeriod processPeriodEnregistrement(JsonObject albumJson, Logger albumLog) {
		return processPeriod(albumJson, JsonMusicProperties.ENREGISTREMENT, albumLog) ;
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
