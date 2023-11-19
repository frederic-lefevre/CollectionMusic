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

package org.fl.collectionAlbum.json;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.JsonMusicProperties;

import com.google.gson.JsonObject;

public class AbstractMediaFileParser {

	private final static Logger albumLog = Control.getAlbumLog();
	
	protected static String parseNote(JsonObject mediaFileJson) {
		return ParserHelpers.parseStringProperty(mediaFileJson, JsonMusicProperties.NOTE, false);
	}

	protected static Set<Path> parseAudioFileLocation(JsonObject mediaFileJson) {

		Set<String> locations = ParserHelpers.getArrayAttributeAsSet(mediaFileJson,  JsonMusicProperties.LOCATION);
		if (locations == null) {
			return null;
		} else {
			return locations.stream()
					.map(locationString -> {
						try {
							Path locationPath = Path.of(locationString);
							if (locationPath.isAbsolute()) {
								if (!Files.exists(locationPath)) {
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
					})
					.filter(Objects::nonNull)
					.collect(Collectors.toSet());
		}
	}
	
	protected static String parseSource(JsonObject mediaFileJson) {
		
		return ParserHelpers.parseStringProperty(mediaFileJson, JsonMusicProperties.SOURCE, true);
	}

}
