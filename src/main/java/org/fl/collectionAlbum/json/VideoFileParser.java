/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.format.VideoFile;
import org.fl.collectionAlbum.format.VideoFileType;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class VideoFileParser extends AbstractMediaFileParser {

	private final static Logger rapportLog = Control.getAlbumLog();
	
	public VideoFileParser() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public VideoFile parseMediaFile(JsonObject videoFileJson) {
		
		if (videoFileJson != null) {
		
			VideoFileType type = Optional.ofNullable(videoFileJson.get(JsonMusicProperties.TYPE))
					.map(JsonElement::getAsString)
					.map(s -> findType(s))
					.orElseGet(() -> {
						rapportLog.severe("Json VideoFile null type parameter: " + videoFileJson);
						return null;
					});
			
			String source = parseSource(videoFileJson);
			String note = parseNote(videoFileJson);
			Set<MediaFilePath> videoFileLocations = parseMediaFileLocation(videoFileJson, ContentNature.VIDEO);
			
			Integer width = Optional.ofNullable(videoFileJson.get(JsonMusicProperties.VIDEO_WIDTH))
					.map(JsonElement::getAsInt)
					.orElseGet(() -> {
						rapportLog.severe("Json VideoFile null width parameter" + videoFileJson);
						return null;
					});
			
			Integer height = Optional.ofNullable(videoFileJson.get(JsonMusicProperties.VIDEO_HEIGHT))
					.map(JsonElement::getAsInt)
					.orElseGet(() -> {
						rapportLog.severe("Json VideoFile null height parameter" + videoFileJson);
						return null;
					});
			
			if ((type == null) || (source == null) || (width == null) || (height == null)) {
				return null;
			} else {
				return new VideoFile(videoFileJson, type, source, width, height, note, videoFileLocations);
			}
		} else {
			rapportLog.severe("Json VideoFile null parameter");
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
