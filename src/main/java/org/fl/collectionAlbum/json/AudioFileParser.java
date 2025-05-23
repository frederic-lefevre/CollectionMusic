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

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.format.AbstractAudioFile;
import org.fl.collectionAlbum.format.AudioFileType;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.format.LosslessAudioFile;
import org.fl.collectionAlbum.format.LossyAudioFile;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AudioFileParser extends AbstractMediaFileParser {

	private final static Logger albumLog = Logger.getLogger(AudioFileParser.class.getName());
	
	public AudioFileParser() {
		super();
	}

	private static AudioFileType findType(String type) {
		
		return Arrays.stream(AudioFileType.values())
				.filter(t -> t.name().equals(type))
				.findFirst()
				.orElse(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public AbstractAudioFile parseMediaFile(ObjectNode audioFileJson) {

		if (audioFileJson != null) {
			
			AudioFileType type = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.TYPE))
					.map(JsonNode::asText)
					.map(s -> findType(s))
					.orElseGet(() -> {
						albumLog.severe("Json AudioFile null type parameter: " + audioFileJson);
						return null;
					});
			
			Double samplingRate = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.SAMPLING_RATE))
					.map(JsonNode::asDouble)
					.orElseGet(() -> {
						albumLog.severe("Json AudioFile null samplingRate parameter" + audioFileJson);
						return null;
					});
			
			String source = parseSource(audioFileJson);
			String note = parseNote(audioFileJson);
			Set<MediaFilePath> audioFileLocations = parseMediaFileLocation(audioFileJson, ContentNature.AUDIO);
			
			if ((type == null) || (source == null) || (samplingRate == null)) {
				
				return null;
				
			} else if (type.isLossLess()) {
				
				Integer bitDepth = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.BIT_DEPTH))
						.map(JsonNode::asInt)
						.orElseGet(() -> {
							albumLog.severe("Json AudioFile null bitDepth parameter" + audioFileJson);
							return null;
						});
				
				if (bitDepth == null) {
					return null;
				} else {
					return new LosslessAudioFile(audioFileJson, type, source, bitDepth, samplingRate, note, audioFileLocations);
				}
				
			} else {
				
				Double bitRate = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.BIT_RATE))
						.map(JsonNode::asDouble)
						.orElseGet(() -> {
							albumLog.severe("Json AudioFile null bitRate parameter" + audioFileJson);
							return null;
						});
				
				if (bitRate == null) {
					return null;
				} else {
					return new LossyAudioFile(audioFileJson, type, source, bitRate, samplingRate, note, audioFileLocations);
				}
			}
		} else {
			albumLog.severe("Json AudioFile null parameter");
			return null;
		}
	}
}
