package org.fl.collectionAlbum.jsonParsers;

import java.lang.reflect.Type;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.utils.TemporalUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class ConcertParser {

	public static TemporalAccessor getConcertDate(JsonObject arteFactJson, Logger cLog) {

		TemporalAccessor dateConcert = null ;
		JsonElement jElem = arteFactJson.get(JsonMusicProperties.DATE) ;
		if (jElem == null) {
			cLog.warning("Pas de date dans le concert " + arteFactJson);
		} else {
			try {
				dateConcert = TemporalUtils.parseDate(jElem.getAsString()) ;
			} catch (Exception e) {
				cLog.log(Level.SEVERE, "Erreur dans les dates du concert " + arteFactJson, e) ;
			}
		}
		return dateConcert ;
	}
	
	public static String getConcertLieu(JsonObject arteFactJson, Logger cLog) {
		
		String lieuConcert = null ;
		JsonElement jElem = arteFactJson.get(JsonMusicProperties.LIEU) ;
		if (jElem == null) {
			cLog.warning("Pas de lieu dans le concert " + arteFactJson);
		} else {
			lieuConcert = jElem.getAsString() ;
		}
		return lieuConcert ;
	}
	
	public static String getConcertUrlInfos(JsonObject arteFactJson, Logger cLog) {
		
		String urlInfos = null ;
		JsonElement jElem = arteFactJson.get(JsonMusicProperties.URL_INFOS) ;
		if (jElem != null) {
			urlInfos = jElem.getAsString() ;
		}
		return urlInfos ;
	}
	
	public static List<String> getConcertMorceaux(JsonObject arteFactJson, Logger cLog) {
		
		Type listType = new TypeToken<List<String>>() {}.getType();	
		List<String> titres = null;
		JsonElement jElem = arteFactJson.get(JsonMusicProperties.MORCEAUX) ;
		if (jElem != null) {
			if (jElem.isJsonArray()) {				
				JsonArray jTitres = jElem.getAsJsonArray() ;			
				titres = new Gson().fromJson(jTitres, listType);
			} else {
				cLog.warning(JsonMusicProperties.MORCEAUX + " n'est pas un JsonArray pour le concert " + arteFactJson) ;
			}
		}
		return titres ;
	}
	
	public static List<String> getConcertTickets(JsonObject arteFactJson, Logger cLog) {
		
		Type listType = new TypeToken<List<String>>() {}.getType();
		List<String> ticketImages = null ;
		JsonElement jElem = arteFactJson.get(JsonMusicProperties.TICKET_IMG) ;
		if (jElem == null) {
			cLog.info("Pas de ticket pour le concert " + arteFactJson) ;
		} else {
			if (jElem.isJsonArray()) {
				JsonArray jTickets = jElem.getAsJsonArray() ; 
				ticketImages =  new Gson().fromJson(jTickets,  listType);
			} else {
				cLog.warning(JsonMusicProperties.TICKET_IMG + " n'est pas un JsonArray pour le concert " + arteFactJson) ;
			}
		}  
		return ticketImages ;
	}

}
