/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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
			
			Double samplingRate = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.SAMPLING_RATE))
					.map(JsonElement::getAsDouble)
					.orElseGet(() -> {
						fl.severe("Json AudioFile null samplingRate parameter" + audioFileJson);
						return null;
					});
			
			String source = AbstractMediaFieldParser.parseSource(audioFileJson);
			String note = AbstractMediaFieldParser.parseNote(audioFileJson);
			
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
