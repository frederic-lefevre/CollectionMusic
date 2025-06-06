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

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.json.MusicArtefactParser;
import org.fl.util.json.JsonUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MusicArtefactMigrator {

	private final static Logger albumLog = Logger.getLogger(MusicArtefactMigrator.class.getName());
	
	private static MusicArtefactMigrator instance;
	
	private final List<VersionMigrator> albumVersionMigrators;
	private final int highestVersion;
	
	private MusicArtefactMigrator() {
		
		albumVersionMigrators = List.of(
				AlbumVersionMigrator1.getInstance(),
				AlbumVersionMigrator2.getInstance(),
				AlbumVersionMigrator3.getInstance(),
				AlbumVersionMigrator4.getInstance()
				);
		
		highestVersion = albumVersionMigrators.getLast().targetVersion();
	}

	public static MusicArtefactMigrator getMigrator() {
		if (instance == null) {
			instance = new MusicArtefactMigrator();
		}
		return instance;
	}
	
	public ObjectNode migrateAlbum(ObjectNode album, Path jsonFilePath) {
		
		return migrateMusicArtefact(album, jsonFilePath, albumVersionMigrators);
	}
	
	private ObjectNode migrateMusicArtefact(ObjectNode artefactJson, Path jsonFilePath, List<VersionMigrator> migrators) {
		
		boolean modifiedByMigration = false;
		
		if (artefactJson == null) {
			return null;
		}
		
		try {
			
			if (MusicArtefactParser.getVersion(artefactJson) > highestVersion) {
				albumLog.severe("The album json has an unexpected (too high) json version. Highest version expected: " + highestVersion + "\nalbum json:\n" + JsonUtils.jsonPrettyPrint(artefactJson));
			}
			
			long nbMigrationDone = migrators.stream()
				.filter(versionMigrator -> versionMigrator.checkVersion(artefactJson))
				.map(versionMigrator -> versionMigrator.migrate(artefactJson))
				.count();

			if (nbMigrationDone > 0) {
				albumLog.warning(nbMigrationDone + " migrations done for the json album " + jsonFilePath);
				modifiedByMigration = true;
			}
		} catch (Exception e) {
			try {
				albumLog.log(Level.SEVERE, "Exception dans la migration de l'artefact " + JsonUtils.jsonPrettyPrint(artefactJson), e);
			} catch (JsonProcessingException e1) {
				albumLog.log(Level.SEVERE, 
						"Exception dans la migration de l'artefact, puis dans le logging de l'erreur (json erroné) pour le fichier " + Objects.toString(jsonFilePath), e);
			}
		}

		if (modifiedByMigration) {
			writeJson(artefactJson, jsonFilePath);
		}
		
		return artefactJson;	
	}
	
	private void writeJson(JsonNode musicArtefact, Path jsonFilePath) {
		
		try (BufferedWriter buff = Files.newBufferedWriter(jsonFilePath, Control.getCharset())) {
			
			buff.write(JsonUtils.jsonPrettyPrint(musicArtefact)) ;
			albumLog.fine(() -> "Ecriture du fichier json: " + jsonFilePath);

		} catch (Exception e) {			
			albumLog.log(Level.SEVERE,"Erreur dans l'écriture du fichier json" + jsonFilePath, e) ;
		}
	}

	public int getHighestVersion() {
		return highestVersion;
	}
}
