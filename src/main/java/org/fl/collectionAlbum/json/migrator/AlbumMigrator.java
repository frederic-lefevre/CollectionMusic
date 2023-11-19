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

package org.fl.collectionAlbum.json.migrator;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.util.json.JsonUtils;

import com.google.gson.JsonObject;

public class AlbumMigrator {

	private final static Logger albumLog = Control.getAlbumLog();
	
	private static AlbumMigrator instance;
	
	protected final List<VersionMigrator> versionMigrators;
	
	private AlbumMigrator() {
		
		versionMigrators = List.of(
				AlbumVersionMigrator1.getInstance(),
				AlbumVersionMigrator2.getInstance()
				);
	}

	public static AlbumMigrator getMigrator() {
		if (instance == null) {
			instance = new AlbumMigrator();
		}
		return instance;
	}
	
	public JsonObject migrate(JsonObject album, Path jsonFilePath) {
		
		if (album == null) {
			return null;
		}
		
		try {
			versionMigrators.forEach(versionMigrator -> {
				if (versionMigrator.checkVersion(album)) {
					versionMigrator.migrate(album);
				}
			});
		} catch (Exception e) {
			albumLog.log(Level.SEVERE, "Exception dans la migration de l'album " + JsonUtils.jsonPrettyPrint(album), e);
		}

		writeJson(album, jsonFilePath);
		
		return album;	
	}
	
	private void writeJson(JsonObject album, Path jsonFilePath) {
		
		try (BufferedWriter buff = Files.newBufferedWriter(jsonFilePath, Control.getCharset())) {
			
			buff.write(JsonUtils.jsonPrettyPrint(album)) ;
			albumLog.fine(() -> "Ecriture du fichier json: " + jsonFilePath);

		} catch (Exception e) {			
			albumLog.log(Level.SEVERE,"Erreur dans l'écriture du fichier json" + jsonFilePath, e) ;
		}
	}
}
