package org.fl.collectionAlbum;

import java.util.Optional;
import java.util.logging.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AudioFile {

	private final String type;
	private final String source;
	private final int bitDepth;
	private final double samplingRate;
	
	private static final String DEFAULT_TYPE = "FLAC";
	private static final String DEFAULT_SOURCE = "CD";
	private static final int DEFAULT_BIT_DEPTH = 16;
	private static final double DEFAULT_SAMPLING_RATE = 44.1;
	
	public AudioFile(JsonObject audioFileJson, Logger fl) {
		
		if (audioFileJson != null) {
			type = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.TYPE))
					.map(JsonElement::getAsString)
					.orElse(DEFAULT_TYPE);
			source = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.SOURCE))
					.map(JsonElement::getAsString)
					.orElse(DEFAULT_SOURCE);
			bitDepth = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.BIT_DEPTH))
					.map(JsonElement::getAsInt)
					.orElse(DEFAULT_BIT_DEPTH);
			samplingRate = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.SAMPLING_RATE))
					.map(JsonElement::getAsDouble)
					.orElse(DEFAULT_SAMPLING_RATE);
		} else {
			fl.severe("Constructor AudioFile null parameter");
			type = null;
			source = null;
			bitDepth = 0;
			samplingRate = 0;
		}
	}

	public String getType() {
		return type;
	}

	public String getSource() {
		return source;
	}

	public int getBitDepth() {
		return bitDepth;
	}

	public double getSamplingRate() {
		return samplingRate;
	}
}
