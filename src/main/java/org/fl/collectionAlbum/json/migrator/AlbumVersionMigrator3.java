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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.json.ParserHelpers;
import org.fl.util.file.FilesUtils;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class AlbumVersionMigrator3 implements VersionMigrator {

	private final static Logger albumLog = Logger.getLogger(AlbumVersionMigrator3.class.getName());
			
	private static final int TARGET_VERSION = 3;
	
	private static AlbumVersionMigrator3 instance;
	
	private static final String SLEEVE_IMAGES_BASE_URI = "file:///FredericPersonnel/Loisirs/musique/PochettesAlbums/";
	
	private final URI absoluteUriBase;
	
	private AlbumVersionMigrator3() {

		try {
			absoluteUriBase = FilesUtils.uriStringToAbsolutePath(SLEEVE_IMAGES_BASE_URI).toUri();
		} catch (URISyntaxException e) {
			String exceptionMessage = "Exception processing sleeves images base URI String:" + SLEEVE_IMAGES_BASE_URI;
			albumLog.log(Level.SEVERE, exceptionMessage, e);
			throw new IllegalArgumentException(exceptionMessage);
		}
	}
	
	protected static AlbumVersionMigrator3 getInstance() {
		if (instance == null) {
			instance = new AlbumVersionMigrator3();
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
			
			String sleeveImg =  ParserHelpers.parseStringProperty(albumJson, JsonMusicProperties.SLEEVE_IMG, false);
			if (sleeveImg != null) {
				Path sleeveImgPath = Path.of(sleeveImg);
				if (! Files.exists(sleeveImgPath)) {
					albumLog.severe("Sleeve Image file does not exists: " + sleeveImg);
				}
				URI sleeveImgeUri = sleeveImgPath.toUri();
				
				if (! sleeveImgeUri.toString().startsWith(absoluteUriBase.toString())) {
					albumLog.severe("NOT MIGRATED : Sleeve Image file " + sleeveImg + " is not under the base folder " + SLEEVE_IMAGES_BASE_URI);
				} else {
					URI relativeResultUri = absoluteUriBase.relativize(sleeveImgeUri);
					
					String relativeResultUriString = relativeResultUri.toString();
					
					// Check
					try {
						Path reconstructedAbsolutePath = FilesUtils.uriStringToAbsolutePath(absoluteUriBase.toString() + relativeResultUriString);
						
						if (! Files.exists(reconstructedAbsolutePath)) {
							albumLog.severe("Reconstructed Sleeve Image file does not exists: " + Objects.toString(reconstructedAbsolutePath));
						} 
						albumJson.put(JsonMusicProperties.SLEEVE_IMG, relativeResultUriString);
							
						albumJson.put(JsonMusicProperties.JSON_VERSION, TARGET_VERSION);
						
					} catch (URISyntaxException e) {
						albumLog.log(Level.SEVERE, "Exception reconstructing sleeves images URI with relative URI String:" + relativeResultUriString, e);
					}
				}
			} else {
				// No Sleeve image for this album : just upgrade version
				albumJson.put(JsonMusicProperties.JSON_VERSION, TARGET_VERSION);
			}
			
		}
		return albumJson;
	}
}
