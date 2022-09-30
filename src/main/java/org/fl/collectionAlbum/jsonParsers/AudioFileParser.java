package org.fl.collectionAlbum.jsonParsers;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

import org.fl.collectionAlbum.AbstractAudioFile;
import org.fl.collectionAlbum.AudioFileType;
import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.LosslessAudioFile;
import org.fl.collectionAlbum.LossyAudioFile;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AudioFileParser {

	public static AbstractAudioFile parseAudioFile(JsonObject audioFileJson, Logger fl) {
		
		if (audioFileJson != null) {
			
			AudioFileType type = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.TYPE))
					.map(JsonElement::getAsString)
					.map(s -> findType(s))
					.orElseGet(() -> {
						fl.severe("Json AudioFile null type parameter: " + audioFileJson);
						return null;
					});

			String source = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.SOURCE))
					.map(JsonElement::getAsString)
					.orElseGet(() -> {
						fl.severe("Json AudioFile null source parameter" + audioFileJson);
						return null;
					});
			
			Double samplingRate = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.SAMPLING_RATE))
					.map(JsonElement::getAsDouble)
					.orElseGet(() -> {
						fl.severe("Json AudioFile null samplingRate parameter" + audioFileJson);
						return null;
					});
			
			String note = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.NOTE))
					.map(JsonElement::getAsString)
					.orElse(null);
			
			if ((type == null) || (source == null) || (samplingRate == null)) {
				
				return null;
				
			} else if (type.isLossLess()) {
				
				Integer bitDepth = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.BIT_DEPTH))
						.map(JsonElement::getAsInt)
						.orElseGet(() -> {
							fl.severe("Json AudioFile null bitDepth parameter" + audioFileJson);
							return null;
						});
				
				if (bitDepth == null) {
					return null;
				} else {
					return new LosslessAudioFile(type, source, bitDepth, samplingRate, note);
				}
				
			} else {
				
				Double bitRate = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.BIT_RATE))
						.map(JsonElement::getAsDouble)
						.orElseGet(() -> {
							fl.severe("Json AudioFile null bitRate parameter" + audioFileJson);
							return null;
						});
				
				if (bitRate == null) {
					return null;
				} else {
					return new LossyAudioFile(type, source, bitRate, samplingRate, note);
				}
			}
		} else {
			fl.severe("Json AudioFile null parameter");
			return null;
		}
	}

	private static AudioFileType findType(String type) {
		
		return Arrays.stream(AudioFileType.values())
				.filter(t -> t.name().equals(type))
				.findFirst()
				.orElse(null);
	}
}
