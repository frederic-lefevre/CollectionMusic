package org.fl.collectionAlbum.jsonParsers;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.VideoFile;
import org.fl.collectionAlbum.VideoFileType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class VideoFileParser {

	public static VideoFile parseVideoFile(JsonObject videoFileJson, Logger fl) {
		
		if (videoFileJson != null) {
		
			VideoFileType type = Optional.ofNullable(videoFileJson.get(JsonMusicProperties.TYPE))
					.map(JsonElement::getAsString)
					.map(s -> findType(s))
					.orElseGet(() -> {
						fl.severe("Json VideoFile null type parameter: " + videoFileJson);
						return null;
					});
			
			String source = AbstractMediaFieldParser.parseSource(videoFileJson, fl);
			String note = AbstractMediaFieldParser.parseNote(videoFileJson);
			
			Integer width = Optional.ofNullable(videoFileJson.get(JsonMusicProperties.VIDEO_WIDTH))
					.map(JsonElement::getAsInt)
					.orElseGet(() -> {
						fl.severe("Json VideoFile null width parameter" + videoFileJson);
						return null;
					});
			
			Integer height = Optional.ofNullable(videoFileJson.get(JsonMusicProperties.VIDEO_HEIGHT))
					.map(JsonElement::getAsInt)
					.orElseGet(() -> {
						fl.severe("Json VideoFile null height parameter" + videoFileJson);
						return null;
					});
			
			if ((type == null) || (source == null) || (width == null) || (height == null)) {
				return null;
			} else {
				return new VideoFile(type, source, width, height, note);
			}
		} else {
			fl.severe("Json VideoFile null parameter");
			return null;
		}
	}

	private static VideoFileType findType(String type) {
		
		return Arrays.stream(VideoFileType.values())
				.filter(t -> t.name().equals(type))
				.findFirst()
				.orElse(null);
	}
}
