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

package org.fl.collectionAlbum.json.migrator;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.json.ParserHelpers;
import org.fl.util.file.FilesUtils;
import org.fl.util.json.JsonUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AlbumVersionMigrator4 implements VersionMigrator {

	private final static Logger albumLog = Logger.getLogger(AlbumVersionMigrator4.class.getName());
	
	private static final int TARGET_VERSION = 4;

	private static final String AUDIO_FILES_ROOT_URI = "file:///E:/Musique";
	private static final String VIDEO_FILES_ROOT_URI = "file:///G:/Video/Musique";
	
	private static AlbumVersionMigrator4 instance;
	
	private final URI audioAbsoluteUriBase;
	private final URI videoAbsoluteUriBase;
	private boolean migrationComplete;
	
	private AlbumVersionMigrator4() {
		
		try {
			audioAbsoluteUriBase = FilesUtils.uriStringToAbsolutePath(AUDIO_FILES_ROOT_URI).toUri();
		} catch (Exception e) {
			String exceptionMessage = "Exception processiong audio files root uri: " + AUDIO_FILES_ROOT_URI;
			albumLog.log(Level.SEVERE, exceptionMessage, e);
			throw new IllegalArgumentException(exceptionMessage);
		}
		
		try {
			videoAbsoluteUriBase = FilesUtils.uriStringToAbsolutePath(VIDEO_FILES_ROOT_URI).toUri();
		} catch (URISyntaxException e) {
			String exceptionMessage = "Exception processiong video files root uri: " + AUDIO_FILES_ROOT_URI;
			albumLog.log(Level.SEVERE, exceptionMessage, e);
			throw new IllegalArgumentException(exceptionMessage);
		}
	}
	
	protected static AlbumVersionMigrator4 getInstance() {
		
		if (instance == null) {
			instance = new AlbumVersionMigrator4();
		}
		return instance;
	}

	@Override
	public int targetVersion() {
		return TARGET_VERSION;
	}
	
	@Override
	public ObjectNode migrate(ObjectNode albumJson) {
		
		if (checkVersion(albumJson)) {
			ObjectNode formatJson = (ObjectNode)albumJson.get(JsonMusicProperties.FORMAT);
			if (formatJson == null) {
				try {
					albumLog.severe("Format d'album null pour l'album " + JsonUtils.jsonPrettyPrint(albumJson));
				} catch (JsonProcessingException e) {
					albumLog.log(Level.SEVERE, "Exception en loggant l'erreur de migration (format) et imprimant le json", e);
				}
			} else {
				
				migrationComplete = true;
				migrateMediaFiles(formatJson, JsonMusicProperties.AUDIO_FILE, audioAbsoluteUriBase, albumJson);
				migrateMediaFiles(formatJson, JsonMusicProperties.VIDEO_FILE, videoAbsoluteUriBase, albumJson);
				
				if (migrationComplete) {
					albumJson.put(JsonMusicProperties.JSON_VERSION, TARGET_VERSION);
				}
			}
		}
		return albumJson;
	}
	
	private void migrateMediaFiles(ObjectNode formatJson, String mediaFileProperty, URI baseMediaFileUri, ObjectNode albumJson)  {
		
		JsonNode jsonMediaFiles = formatJson.get(mediaFileProperty);
		if (jsonMediaFiles != null) {
			jsonMediaFiles.forEach(jsonMediaFile -> {
					
				Set<String> locations = ParserHelpers.getArrayAttributeAsSet(jsonMediaFile,  JsonMusicProperties.LOCATION);
				if (locations == null) {
					try {
						albumLog.severe("Media files location missing for " + JsonUtils.jsonPrettyPrint(albumJson));
					} catch (JsonProcessingException e) {
						albumLog.log(Level.SEVERE, "Exception en loggant l'erreur de migration (locations) et imprimant le json", e);
					}
				} else {
					
					ArrayNode locationArray = JsonNodeFactory.instance.arrayNode();
					locations.stream()
						.map(location -> migrateMediaLocationPath(location, baseMediaFileUri))
						.filter(Objects::nonNull)					
						.forEach(migratedLocation -> locationArray.add(migratedLocation));

					((ObjectNode) jsonMediaFile).set(JsonMusicProperties.LOCATION, locationArray);
				}
			});
		}
	}
	
	private String migrateMediaLocationPath(String absoluteLocationPathString, URI baseMediaFileUri) {
		
		Path absoluteLocationPath = Path.of(absoluteLocationPathString);
		if (! Files.exists(absoluteLocationPath)) {
			albumLog.severe("The media file path does not exists: " + absoluteLocationPathString);
		}
		
		URI absoluteLocationUri = absoluteLocationPath.toUri();
		if (! absoluteLocationUri.toString().startsWith(baseMediaFileUri.toString())) {
			albumLog.severe("ONE PATH NOT MIGRATED *** The media file " + absoluteLocationPathString + " is not under the base folder " + baseMediaFileUri);
			migrationComplete = false;
			return absoluteLocationPathString;
		} else {
			URI relativeResultUri = baseMediaFileUri.relativize(absoluteLocationUri);
			
			// getPath() decodes the URI
			String relativeResultUriString = relativeResultUri.getPath();
			
			// Check
			try {
				Path reconstructedAbsolutePath = reconstructAbolutePath(baseMediaFileUri, relativeResultUriString);
				
				if (! Files.exists(reconstructedAbsolutePath)) {
					albumLog.severe("Reconstructed media file path does not exists: " + Objects.toString(reconstructedAbsolutePath));
				}
			} catch (URISyntaxException e) {
				albumLog.log(Level.SEVERE, "Exception reconstructing media file path with relative URI String:" + relativeResultUriString, e);
			}
			return relativeResultUriString;
		}
	}
	
	private Path reconstructAbolutePath(URI baseMediaFileUri, String relativeResultUriString) throws URISyntaxException {
		
		return Path.of(
				FilesUtils.uriStringToAbsolutePath(baseMediaFileUri.toString()).toString(), 
				relativeResultUriString);
	}

}
