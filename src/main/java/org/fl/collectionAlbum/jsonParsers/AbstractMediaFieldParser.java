package org.fl.collectionAlbum.jsonParsers;

import java.util.Optional;
import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AbstractMediaFieldParser {

	protected static String parseNote(JsonObject mediaFileJson) {
		
		return Optional.ofNullable(mediaFileJson.get(JsonMusicProperties.NOTE))
				.map(JsonElement::getAsString)
				.orElse(null);
	}

	protected static String parseSource(JsonObject mediaFileJson, Logger logger) {
		
		return Optional.ofNullable(mediaFileJson.get(JsonMusicProperties.SOURCE))
				.map(JsonElement::getAsString)
				.orElseGet(() -> {
					logger.severe("Json MediaFile null source parameter" + mediaFileJson);
					return null;
				});
	}
}
