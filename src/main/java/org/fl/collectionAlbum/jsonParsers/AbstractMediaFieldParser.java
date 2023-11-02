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

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.JsonMusicProperties;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AbstractMediaFieldParser {

	private final static Logger albumLog = Control.getAlbumLog();
	
	protected static String parseNote(JsonObject mediaFileJson) {
		return parseOptionalStringProperty(mediaFileJson, JsonMusicProperties.NOTE);
	}

	protected static Path parseAudioFileLocation(JsonObject mediaFileJson) {
		
		String location = parseOptionalStringProperty(mediaFileJson, JsonMusicProperties.LOCATION);
		
	if (location != null) {
		try {
			Path locationPath = Path.of(location);
			if (locationPath.isAbsolute()) {
				if (! Files.exists(locationPath)) {
					albumLog.warning("Media file location does not exists: " + mediaFileJson);
				}
				return locationPath;
			} else {
				albumLog.severe("Media file location is not absolute: " + mediaFileJson);
				return null;
			}
		} catch (Exception e) {
			albumLog.log(Level.SEVERE, "Invalid media file location: " + mediaFileJson, e);
			return null;
		}
	} else {
		return null;
	}
		
	}
	
	protected static String parseSource(JsonObject mediaFileJson) {
		
		return Optional.ofNullable(mediaFileJson.get(JsonMusicProperties.SOURCE))
				.map(JsonElement::getAsString)
				.orElseGet(() -> {
					albumLog.severe("Json MediaFile null source parameter: " + mediaFileJson);
					return null;
				});
	}
	
	private static String parseOptionalStringProperty(JsonObject mediaFileJson, String property) {
		
		return Optional.ofNullable(mediaFileJson.get(property))
				.map(JsonElement::getAsString)
				.orElse(null);
	}
}
