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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.json.MusicArtefactParser;
import org.fl.util.FilterCounter;
import org.fl.util.FilterCounter.LogRecordCounter;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

class AlbumVersionMigrator4Test {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	@Test
	void checkTargetVersion() {
		
		AlbumVersionMigrator4 migrator = AlbumVersionMigrator4.getInstance();
		
		assertThat(migrator.targetVersion()).isEqualTo(4);
	}
	
	private static final String alreadyMigratedAlbumStr1 = """
{
  "titre": "Sabbath bloody sabbath",
  "format": {
    "k7": 1,
    "audioFiles": [
      {
        "bitDepth": 16,
        "samplingRate": 44.1,
        "source": "CD",
        "type": "FLAC",
        "location": [
          "b/Black Sabbath/Sabbath Bloody Sabbath/"
        ]
      },
      {
        "bitDepth": 24,
        "samplingRate": 176.4,
        "source": "SACD",
        "type": "FLAC",
        "location": [
          "b/Black Sabbath/Sabbath Bloody Sabbath [24 176]/"
        ]
      }
    ]
  },
  "groupe": [
    {
      "nom": "Black Sabbath"
    }
  ],
  "enregistrement": [
    "1973-07-01",
    "1973-07-31"
  ],
  "jsonVersion": 4,
  "discogs": "9556137",
  "sleeveImg": "b/BlackSabbath/SabbathBloodySabbathK7.jpg"
}	
			""";
	
	@Test
	void albumVersionShouldBeUptoDate() throws JsonMappingException, JsonProcessingException {
		
		AlbumVersionMigrator4 migrator = AlbumVersionMigrator4.getInstance();
		
		ObjectNode albumJson = (ObjectNode)mapper.readTree(alreadyMigratedAlbumStr1);
		
		assertThat(migrator.checkVersion(albumJson)).isFalse();
	}
	
	private static final String albumStr2 = """
{
  "titre": "Sabbath bloody sabbath",
  "format": {
    "k7": 1,
    "audioFiles": [
      {
        "bitDepth": 16,
        "samplingRate": 44.1,
        "source": "CD",
        "type": "FLAC",
        "location": [
          "E:\\\\Musique\\\\b\\\\Black Sabbath\\\\Sabbath Bloody Sabbath"
        ]
      },
      {
        "bitDepth": 24,
        "samplingRate": 176.4,
        "source": "SACD",
        "type": "FLAC",
        "location": [
          "E:\\\\Musique\\\\b\\\\Black Sabbath\\\\Sabbath Bloody Sabbath [24 176]"
        ]
      }
    ]
  },
  "groupe": [
    {
      "nom": "Black Sabbath"
    }
  ],
  "enregistrement": [
    "1973-07-01",
    "1973-07-31"
  ],
  "jsonVersion": 3,
  "discogs": "9556137",
  "sleeveImg": "b/BlackSabbath/SabbathBloodySabbathK7.jpg"
}	
			""";
	
	@Test
	void albumVersionShouldNotBeUptoDate() throws JsonMappingException, JsonProcessingException {
		
		AlbumVersionMigrator4 migrator = AlbumVersionMigrator4.getInstance();
		
		ObjectNode albumJson = (ObjectNode)mapper.readTree(albumStr2);
		
		assertThat(migrator.checkVersion(albumJson)).isTrue();
	}
	
	@Test
	void shouldMigrateAlbum() throws JsonMappingException, JsonProcessingException {

		JsonNode formatJson = assertAndGetFormatJson(albumStr2);
		
		JsonNode jsonAudioFiles = formatJson.get(JsonMusicProperties.AUDIO_FILE);
		assertThat(jsonAudioFiles).isNotNull();
		assertThat(jsonAudioFiles.size()).isEqualTo(2);
		
		assertThat(jsonAudioFiles).satisfiesExactlyInAnyOrder(
				jsonAudioFile -> { 
					JsonNode locationsJson = jsonAudioFile.get( JsonMusicProperties.LOCATION);
					assertThat(locationsJson).isNotNull().singleElement().satisfies(locationJson -> 
						assertThat(locationJson.asText()).isEqualTo("b/Black Sabbath/Sabbath Bloody Sabbath/"));					
				},
				jsonAudioFile -> {
					JsonNode locationsJson = jsonAudioFile.get( JsonMusicProperties.LOCATION);
					assertThat(locationsJson).isNotNull().singleElement().satisfies(locationJson -> 
						assertThat(locationJson.asText()).isEqualTo("b/Black Sabbath/Sabbath Bloody Sabbath [24 176]/"));		
				});
		
	}
	
	private static final String albumStr3 = """
{
  "titre" : "أخلد الألحان بأجمل الأصوات (Les plus belles chansons avec la plus belle voix)",
  "format" : {
    "cd" : 1,
    "audioFiles" : [ {
      "bitDepth" : 16,
      "samplingRate" : 44.1,
      "source" : "CD",
      "type" : "FLAC",
      "location" : [ "E:\\\\Musique\\\\a\\\\Farid El Atrache\\\\(Les plus belles chansons avec la plus belle voix) أخلد الألحان بأجمل الأصوات" ]
    } ]
  },
  "auteurCompositeurs" : [ {
    "nom" : "Atrache",
    "prenom" : "Farid El",
    "naissance" : "1915-01-01",
    "mort" : "1974-12-26"
  } ],
  "enregistrement" : [ "1950-01-01", "1950-01-01" ],
  "jsonVersion" : 3,
  "discogs" : "11944355"
}
			""";
	
	@Test
	void shouldMigrateAlbum2() throws JsonMappingException, JsonProcessingException {
		
		JsonNode formatJson = assertAndGetFormatJson(albumStr3);
		
		JsonNode jsonAudioFiles = formatJson.get(JsonMusicProperties.AUDIO_FILE);
		assertThat(jsonAudioFiles).isNotNull();
		assertThat(jsonAudioFiles.size()).isEqualTo(1);
		
		JsonNode jsonAudioFile = jsonAudioFiles.get(0);
		JsonNode locationsJson = jsonAudioFile.get( JsonMusicProperties.LOCATION);
		assertThat(locationsJson).isNotNull().singleElement().satisfies(locationJson -> 
			assertThat(locationJson.asText()).isEqualTo("a/Farid El Atrache/(Les plus belles chansons avec la plus belle voix) أخلد الألحان بأجمل الأصوات/"));	

		
	}
	
	private static final String albumStr4 = """
{
  "titre" : "Electric ladyland",
  "format" : {
    "cd" : 3,
    "bluerayMixed" : 1,
    "audioFiles" : [ {
      "bitDepth" : 16,
      "samplingRate" : 44.1,
      "source" : "CD",
      "type" : "FLAC",
      "note" : "Remaster Bernie Grundman. Son compressé. Qualité inferieure",
      "location" : [ "E:\\\\Musique\\\\h\\\\Jimi Hendrix\\\\Electric Ladyland\\\\Electric Ladyland (50th, CD, 2018)" ]
    }, {
      "bitDepth" : 24,
      "samplingRate" : 96,
      "source" : "Bluray 2.0",
      "type" : "FLAC",
      "note" : "Remaster Bernie Grundman. Son compressé. Qualité inferieure",
      "location" : [ "E:\\\\Musique\\\\h\\\\Jimi Hendrix\\\\Electric Ladyland\\\\Electric Ladyland [Bluray 2_0, 2018 24-96]" ]
    }, {
      "bitDepth" : 24,
      "samplingRate" : 176.4,
      "source" : "Bluray 5.1",
      "type" : "FLAC",
      "note" : "Remaster Bernie Grundman, Remix Eddie Kramer",
      "location" : [ "E:\\\\Musique\\\\h\\\\Jimi Hendrix\\\\Electric Ladyland\\\\Electric Ladyland [Bluray 5_1, 2018, 24 176]" ]
    }, {
      "bitDepth" : 16,
      "samplingRate" : 44.1,
      "source" : "CD",
      "type" : "FLAC",
      "note" : "Live At The Hollywood Bowl - Electric Ladyland 50th Anniversary",
      "location" : [ "E:\\\\Musique\\\\h\\\\Jimi Hendrix\\\\Live At The Hollywood Bowl" ]
    }, {
      "bitDepth" : 16,
      "samplingRate" : 44.1,
      "source" : "CD",
      "type" : "FLAC",
      "note" : "At Last - The Beginning - The Making Of Electric Ladyland - 50th Anniversary Deluxe Edition",
      "location" : [ "E:\\\\Musique\\\\h\\\\Jimi Hendrix\\\\The Making Of Electric Ladyland" ]
    } ],
    "videoFiles" : [ {
      "width" : 720,
      "height" : 480,
      "source" : "Blu-ray",
      "type" : "M2TS",
      "location" : [ "G:\\\\Video\\\\Musique\\\\h\\\\Jimi Hendrix\\\\Electric Ladyland 1968" ]
    } ]
  },
  "rangement" : "33T",
  "notes" : [ "50th anniversary edition", "Release LC 02361, bar code 1 90758 59022 6, Experience Hendrix 2018, Made in E.U", "Encore un son compressé, qualité inférieure pour le CD et le Bluray version 2.0 96Khz/24bits", "Remaster Bernie Grundman" ],
  "auteurCompositeurs" : [ {
    "nom" : "Hendrix",
    "prenom" : "Jimi"
  } ],
  "enregistrement" : [ "1967-05-06", "1968-09-14" ],
  "jsonVersion" : 3,
  "discogs" : "12793152",
  "discogsFormatValidation" : true
}
			""";
	
	
	@Test
	void shouldMigrateAlbum3() throws JsonMappingException, JsonProcessingException {
		
		JsonNode formatJson = assertAndGetFormatJson(albumStr4);
		
		JsonNode jsonAudioFiles = formatJson.get(JsonMusicProperties.AUDIO_FILE);
		assertThat(jsonAudioFiles).isNotNull();
		assertThat(jsonAudioFiles.size()).isEqualTo(5);
		
		assertThat(jsonAudioFiles).satisfiesExactlyInAnyOrder(
				jsonAudioFile -> { 
					JsonNode locationsJson = jsonAudioFile.get( JsonMusicProperties.LOCATION);
					assertThat(locationsJson).isNotNull().singleElement().satisfies(locationJson -> 
						assertThat(locationJson.asText()).isEqualTo("h/Jimi Hendrix/Electric Ladyland/Electric Ladyland (50th, CD, 2018)/"));					
				},
				jsonAudioFile -> {
					JsonNode locationsJson = jsonAudioFile.get( JsonMusicProperties.LOCATION);
					assertThat(locationsJson).isNotNull().singleElement().satisfies(locationJson -> 
						assertThat(locationJson.asText()).isEqualTo("h/Jimi Hendrix/Electric Ladyland/Electric Ladyland [Bluray 2_0, 2018 24-96]/"));		
				},
				jsonAudioFile -> {
					JsonNode locationsJson = jsonAudioFile.get( JsonMusicProperties.LOCATION);
					assertThat(locationsJson).isNotNull().singleElement().satisfies(locationJson -> 
						assertThat(locationJson.asText()).isEqualTo("h/Jimi Hendrix/Electric Ladyland/Electric Ladyland [Bluray 5_1, 2018, 24 176]/"));		
				},
				jsonAudioFile -> {
					JsonNode locationsJson = jsonAudioFile.get( JsonMusicProperties.LOCATION);
					assertThat(locationsJson).isNotNull().singleElement().satisfies(locationJson -> 
						assertThat(locationJson.asText()).isEqualTo("h/Jimi Hendrix/Live At The Hollywood Bowl/"));		
				},
				jsonAudioFile -> {
					JsonNode locationsJson = jsonAudioFile.get( JsonMusicProperties.LOCATION);
					assertThat(locationsJson).isNotNull().singleElement().satisfies(locationJson -> 
						assertThat(locationJson.asText()).isEqualTo("h/Jimi Hendrix/The Making Of Electric Ladyland/"));		
				});
		
		JsonNode jsonVideoFiles = formatJson.get(JsonMusicProperties.VIDEO_FILE);
		assertThat(jsonVideoFiles).isNotNull();
		assertThat(jsonVideoFiles.size()).isEqualTo(1);
		
		JsonNode jsonVideoFile = jsonVideoFiles.get(0);
		JsonNode locationsJson = jsonVideoFile.get( JsonMusicProperties.LOCATION);
		assertThat(locationsJson).isNotNull().singleElement().satisfies(locationJson -> 
			assertThat(locationJson.asText()).isEqualTo("h/Jimi Hendrix/Electric Ladyland 1968/"));	
	}
	
	private static final String albumStr5 = """
{
  "titre": "Sabbath bloody sabbath",
  "format": {
    "k7": 1,
    "audioFiles": [
      {
        "bitDepth": 16,
        "samplingRate": 44.1,
        "source": "CD",
        "type": "FLAC",
        "location": [
          "E:\\\\MusiqueBad\\\\b\\\\Black Sabbath\\\\Sabbath Bloody Sabbath"
        ]
      },
      {
        "bitDepth": 24,
        "samplingRate": 176.4,
        "source": "SACD",
        "type": "FLAC",
        "location": [
          "E:\\\\Musique\\\\b\\\\Black Sabbath\\\\Sabbath Bloody Sabbath [24 176]"
        ]
      }
    ]
  },
  "groupe": [
    {
      "nom": "Black Sabbath"
    }
  ],
  "enregistrement": [
    "1973-07-01",
    "1973-07-31"
  ],
  "jsonVersion": 3,
  "discogs": "9556137",
  "sleeveImg": "b/BlackSabbath/SabbathBloodySabbathK7.jpg"
}	
			""";
	
	@Test
	void shouldDetectWrongFolder() throws JsonMappingException, JsonProcessingException {
		
AlbumVersionMigrator4 migrator = AlbumVersionMigrator4.getInstance();
		
		ObjectNode albumJson = (ObjectNode)mapper.readTree(albumStr5);
		int nbChildNode = albumJson.size();
		
		assertThat(albumJson.get(JsonMusicProperties.JSON_VERSION).asInt()).isEqualTo(3);
		
		LogRecordCounter migratorFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(AlbumVersionMigrator4.class.getName()));
		
		ObjectNode migratedAlbum = migrator.migrate(albumJson);
		
		assertThat(migratorFilterCounter.getLogRecordCount()).isEqualTo(2);
		assertThat(migratorFilterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(2);
		assertThat(migratorFilterCounter.getLogRecords()).satisfiesExactlyInAnyOrder(
				logRecord -> assertThat(logRecord.getMessage()).contains("The media file path does not exists"),
				logRecord -> assertThat(logRecord.getMessage()).contains("ONE PATH NOT MIGRATED *** The media file").contains("is not under the base folder"));
		
		assertThat(migratedAlbum).isNotNull();

		assertThat(migratedAlbum.get(JsonMusicProperties.JSON_VERSION).asInt())
			.isEqualTo(3)
			.isEqualTo(albumJson.get(JsonMusicProperties.JSON_VERSION).asInt())
			.isEqualTo(MusicArtefactParser.getVersion(migratedAlbum))
			.isEqualTo(MusicArtefactParser.getVersion(albumJson));
		
		assertThat(migratedAlbum.size()).isEqualTo(nbChildNode);
		
		JsonNode formatJson =  albumJson.get(JsonMusicProperties.FORMAT);
		
		JsonNode jsonAudioFiles = formatJson.get(JsonMusicProperties.AUDIO_FILE);
		assertThat(jsonAudioFiles).isNotNull();
		assertThat(jsonAudioFiles.size()).isEqualTo(2);
		
		assertThat(jsonAudioFiles).satisfiesExactlyInAnyOrder(
				jsonAudioFile -> { 
					JsonNode locationsJson = jsonAudioFile.get( JsonMusicProperties.LOCATION);
					assertThat(locationsJson).isNotNull().singleElement().satisfies(locationJson -> 
						assertThat(locationJson.asText()).isEqualTo("E:\\MusiqueBad\\b\\Black Sabbath\\Sabbath Bloody Sabbath"));					
				},
				jsonAudioFile -> {
					JsonNode locationsJson = jsonAudioFile.get( JsonMusicProperties.LOCATION);
					assertThat(locationsJson).isNotNull().singleElement().satisfies(locationJson -> 
						assertThat(locationJson.asText()).isEqualTo("b/Black Sabbath/Sabbath Bloody Sabbath [24 176]/"));		
				});
	}
	
	private JsonNode assertAndGetFormatJson(String albumStr) throws JsonMappingException, JsonProcessingException {
		
		AlbumVersionMigrator4 migrator = AlbumVersionMigrator4.getInstance();
		
		ObjectNode albumJson = (ObjectNode)mapper.readTree(albumStr);
		int nbChildNode = albumJson.size();
		
		assertThat(albumJson.get(JsonMusicProperties.JSON_VERSION).asInt()).isEqualTo(3);
		
		ObjectNode migratedAlbum = migrator.migrate(albumJson);
		
		assertThat(migratedAlbum).isNotNull();

		assertThat(migratedAlbum.get(JsonMusicProperties.JSON_VERSION).asInt())
			.isEqualTo(migrator.targetVersion())
			.isEqualTo(albumJson.get(JsonMusicProperties.JSON_VERSION).asInt())
			.isEqualTo(MusicArtefactParser.getVersion(migratedAlbum))
			.isEqualTo(MusicArtefactParser.getVersion(albumJson));
		
		assertThat(migratedAlbum.size()).isEqualTo(nbChildNode);
		
		return albumJson.get(JsonMusicProperties.FORMAT);
	}
}

