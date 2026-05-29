/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.format.AlbumVideoFiles;
import org.fl.collectionAlbum.format.VideoFileType;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ObjectNode;

public class VideoFilePathJsonParser extends AbstractMediaFileParser {

	private static final Logger rapportLog = Logger.getLogger(VideoFilePathJsonParser.class.getName());
	
	public VideoFilePathJsonParser() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public AlbumVideoFiles parseMediaFile(ObjectNode videoFilePathJson) {
		
		if (videoFilePathJson != null) {
		
			VideoFileType type = Optional.ofNullable(videoFilePathJson.get(JsonMusicProperties.TYPE))
					.map(JsonNode::asString)
					.map(s -> findType(s))
					.orElseGet(() -> {
						rapportLog.severe("Json VideoFile null type parameter: " + videoFilePathJson);
						return null;
					});
			
			String source = parseSource(videoFilePathJson);
			String note = parseNote(videoFilePathJson);
			Set<MediaFilePath> videoFileLocations = parseMediaFileLocation(videoFilePathJson, ContentNature.VIDEO);
			
			Integer width = Optional.ofNullable(videoFilePathJson.get(JsonMusicProperties.VIDEO_WIDTH))
					.map(JsonNode::asInt)
					.orElseGet(() -> {
						rapportLog.severe("Json VideoFile null width parameter" + videoFilePathJson);
						return null;
					});
			
			Integer height = Optional.ofNullable(videoFilePathJson.get(JsonMusicProperties.VIDEO_HEIGHT))
					.map(JsonNode::asInt)
					.orElseGet(() -> {
						rapportLog.severe("Json VideoFile null height parameter" + videoFilePathJson);
						return null;
					});
			
			if ((type == null) || (source == null) || (width == null) || (height == null)) {
				return null;
			} else {
				return new AlbumVideoFiles(videoFilePathJson, type, source, width, height, note, videoFileLocations);
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
