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

package org.fl.collectionAlbum.json.migrator;

import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.json.ParserHelpers;
import org.fl.util.json.JsonUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AlbumVersionMigrator2 implements VersionMigrator {

	private final static Logger albumLog = Logger.getLogger(AlbumVersionMigrator2.class.getName());
	
	private static final int TARGET_VERSION = 2;
	
	private static AlbumVersionMigrator2 instance;
	
	private AlbumVersionMigrator2() {
	}

	protected static AlbumVersionMigrator2 getInstance() {
		
		if (instance == null) {
			instance = new AlbumVersionMigrator2();
		}
		return instance;
	}
	
	@Override
	public int targetVersion() {
		return TARGET_VERSION;
	}

	@Override
	public JsonObject migrate(JsonObject albumJson) {

		if (checkVersion(albumJson)) {
			JsonElement jElem = albumJson.get(JsonMusicProperties.FORMAT);
			if (jElem == null) {
				albumLog.severe("Format d'album null pour l'album " + JsonUtils.jsonPrettyPrint(albumJson));
			} else {
				
				migrateMediaFiles(jElem, JsonMusicProperties.AUDIO_FILE);
				migrateMediaFiles(jElem, JsonMusicProperties.VIDEO_FILE);
				
				albumJson.addProperty(JsonMusicProperties.JSON_VERSION, TARGET_VERSION);
			}
		}
		return albumJson;
	}
	
	private void migrateMediaFiles(JsonElement formatJson, String mediaFileProperty)  {
		
		JsonArray jsonAudioFiles = formatJson.getAsJsonObject().getAsJsonArray(mediaFileProperty);
		if (jsonAudioFiles != null) {
			jsonAudioFiles.forEach(jsonAudioFile -> {

				JsonObject audioFileJsonObject = jsonAudioFile.getAsJsonObject();						
				String location = ParserHelpers.parseStringProperty(audioFileJsonObject, JsonMusicProperties.LOCATION, false);
				if (location != null) {
					JsonArray locationArray = new JsonArray();
					locationArray.add(location);
					audioFileJsonObject.add(JsonMusicProperties.LOCATION, locationArray);
				}
			});
		}
	}
}
