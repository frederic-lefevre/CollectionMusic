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

import static org.assertj.core.api.Assertions.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.json.MusicArtefactParser;
import org.fl.util.FilterCounter;
import org.fl.util.FilterCounter.LogRecordCounter;
import org.fl.util.file.FilesUtils;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

class MusicArtefactMigratorTest {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private static final String albumStr1 = """
			{ 
			  "titre": "Portrait in jazz\",
			  "format": {  "cd": 1,
				"audioFiles" : [{
				    "bitDepth": 16 , 
				    "samplingRate" : 44.1, 
				    "source" : "MOFI Fidelity Sound Lab", 
				    "type" : "FLAC" }]
			     }, 
			  "auteurCompositeurs": [ 
			    {  
			      "nom": "Evans",
			      "prenom": "Bill", 
			      "naissance": "1929-08-16",
			      "mort": "1980-09-15"  
			    }                                 
			  ],    								
			  "enregistrement": [ "1959-12-28",  "1959-12-28" ],  
			  "liensUrl":  [ "http://somwhere" ] 
			 } 
			""" ;
	
	@Test
	void shouldMigrateAlbum() throws JsonMappingException, JsonProcessingException, URISyntaxException {
		
		LogRecordCounter migratorFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.migrator.MusicArtefactMigrator"));
		
		MusicArtefactMigrator migrator = MusicArtefactMigrator.getMigrator();
		
		ObjectNode albumJson = (ObjectNode)mapper.readTree(albumStr1);
		
		int initialVersion = MusicArtefactParser.getVersion(albumJson);
		assertThat(initialVersion).isZero();
		
		assertThat(albumJson.get(JsonMusicProperties.JSON_VERSION)).isNull();
		
		Path jsonFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/PortraitInJazz2.json");
		
		ObjectNode migratedAlbum = migrator.migrateAlbum(albumJson, jsonFilePath);
		
		assertThat(migratedAlbum.get(JsonMusicProperties.JSON_VERSION).asInt())
			.isEqualTo(albumJson.get(JsonMusicProperties.JSON_VERSION).asInt())
			.isEqualTo(MusicArtefactParser.getVersion(migratedAlbum))
			.isEqualTo(MusicArtefactParser.getVersion(albumJson))
			.isEqualTo(migrator.getHighestVersion());
		
		assertThat(migratorFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(migratorFilterCounter.getLogRecordCount(Level.WARNING)).isEqualTo(1);
	}
	
	private static final String albumStr2 = """
{
  "titre" : "Speak No Evil",
  "format" : {
    "cd" : 1,
    "audioFiles" : [ {
      "bitDepth" : 16,
      "samplingRate" : 44.1,
      "source" : "CD",
      "type" : "FLAC",
      "location" : [ "E:\\\\Musique\\\\s\\\\Wayne Shorter\\\\Speak No Evil" ]
    } ]
  },
  "auteurCompositeurs" : [ {
    "nom" : "Shorter",
    "prenom" : "Wayne",
    "naissance" : "1933-08-25",
    "mort" : "2023-03-02"
  } ],
  "enregistrement" : [ "1964-12-24", "1964-12-24" ],
  "jsonVersion" : 3,
  "discogs" : "11052375"
}	
			""";
	
	@Test
	void shouldMigrateAlbum2() throws JsonMappingException, JsonProcessingException, URISyntaxException {
		
		LogRecordCounter migratorFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.migrator.MusicArtefactMigrator"));
		
		MusicArtefactMigrator migrator = MusicArtefactMigrator.getMigrator();
		
		ObjectNode albumJson = (ObjectNode)mapper.readTree(albumStr2);
		
		int initialVersion = MusicArtefactParser.getVersion(albumJson);
		assertThat(initialVersion).isEqualTo(3);
		
		assertThat(albumJson.get(JsonMusicProperties.JSON_VERSION)).isNotNull();
		assertThat(albumJson.get(JsonMusicProperties.JSON_VERSION).asInt()).isEqualTo(3);
		
		Path jsonFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/SpeakNoEvil.json");
		
		ObjectNode migratedAlbum = migrator.migrateAlbum(albumJson, jsonFilePath);
		
		assertThat(migratedAlbum.get(JsonMusicProperties.JSON_VERSION).asInt())
			.isEqualTo(albumJson.get(JsonMusicProperties.JSON_VERSION).asInt())
			.isEqualTo(MusicArtefactParser.getVersion(migratedAlbum))
			.isEqualTo(MusicArtefactParser.getVersion(albumJson))
			.isEqualTo(migrator.getHighestVersion());
		
		assertThat(migratorFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(migratorFilterCounter.getLogRecordCount(Level.WARNING)).isEqualTo(1);
	}

	@Test
	void testUnexpectedJsonVersion() throws JsonMappingException, JsonProcessingException, URISyntaxException {
		
		LogRecordCounter migratorFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.migrator.MusicArtefactMigrator"));
		
		MusicArtefactMigrator migrator = MusicArtefactMigrator.getMigrator();
		
		ObjectNode albumJson = (ObjectNode)mapper.readTree(albumStr2);
		
		// Change version to unexpected version
		int tooHighVersion = migrator.getHighestVersion() + 1;		
		albumJson.put(JsonMusicProperties.JSON_VERSION, tooHighVersion);
		
		int initialVersion = MusicArtefactParser.getVersion(albumJson);
		assertThat(initialVersion).isEqualTo(tooHighVersion);
		
		assertThat(albumJson.get(JsonMusicProperties.JSON_VERSION)).isNotNull();
		assertThat(albumJson.get(JsonMusicProperties.JSON_VERSION).asInt()).isEqualTo(tooHighVersion);
		
		Path jsonFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/SpeakNoEvil.json");
		
		ObjectNode migratedAlbum = migrator.migrateAlbum(albumJson, jsonFilePath);
		
		assertThat(migratedAlbum.get(JsonMusicProperties.JSON_VERSION).asInt())
			.isEqualTo(albumJson.get(JsonMusicProperties.JSON_VERSION).asInt())
			.isEqualTo(MusicArtefactParser.getVersion(migratedAlbum))
			.isEqualTo(MusicArtefactParser.getVersion(albumJson))
			.isEqualTo(tooHighVersion);
		
		assertThat(migratorFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(migratorFilterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
		assertThat(migratorFilterCounter.getLogRecords()).singleElement().satisfies(
				logRecord -> assertThat(logRecord.getMessage()).contains("The album json has an unexpected (too high) json version"));
	}
}
