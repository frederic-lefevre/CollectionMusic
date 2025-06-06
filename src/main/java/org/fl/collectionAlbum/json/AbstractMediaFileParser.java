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

import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.format.AbstractMediaFile;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class AbstractMediaFileParser {

	private final static Logger albumLog = Logger.getLogger(AbstractMediaFileParser.class.getName());
	
	public AbstractMediaFileParser() {
		super();
	}
	
	protected String parseNote(JsonNode mediaFileJson) {
		return ParserHelpers.parseStringProperty(mediaFileJson, JsonMusicProperties.NOTE, false);
	}

	protected Set<MediaFilePath> parseMediaFileLocation(JsonNode mediaFileJson, ContentNature contentNature) {

		Set<String> locations = ParserHelpers.getArrayAttributeAsSet(mediaFileJson,  JsonMusicProperties.LOCATION);
		if (locations == null) {
			return null;
		} else {
			return locations.stream()
					.map(locationString -> {
						try {
							Path locationPath = Path.of(Control.getMediaFileRootPath(contentNature).toString(), locationString);
							if (locationPath.isAbsolute()) {
								return MediaFilesInventories.getMediaFileInventory(contentNature).validateMediaFilePath(locationPath);
							} else {
								albumLog.severe("Reconstructed media file location is not absolute: " + Objects.toString(locationPath) + "\nMedia file locations: " + mediaFileJson);
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
	
	protected String parseSource(JsonNode mediaFileJson) {		
		return ParserHelpers.parseStringProperty(mediaFileJson, JsonMusicProperties.SOURCE, true);
	}

	public abstract <T extends AbstractMediaFile> T parseMediaFile(ObjectNode mediaFileJson);
}
