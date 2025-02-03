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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.json.MusicArtefactParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

class AlbumVersionMigrator2Test {

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
	void checkTargetVersion() {
		
		AlbumVersionMigrator2 migrator = AlbumVersionMigrator2.getInstance();
		
		assertThat(migrator.targetVersion()).isEqualTo(2);
	}
	
	@Test
	void albumVersionShouldBeKo() throws JsonMappingException, JsonProcessingException {
		
		AlbumVersionMigrator2 migrator = AlbumVersionMigrator2.getInstance();
		
		ObjectNode albumJson = (ObjectNode)mapper.readTree(albumStr1);
		
		assertThat(migrator.checkVersion(albumJson)).isFalse();
	}
	
	private static final String albumStr2 = """
{ 
  "titre": "Van Halen",
  "format": {
    "cd": 1,
    "audioFiles": [
      {
        "bitDepth": 24,
        "samplingRate": 192,
        "source": "File",
        "type": "FLAC",
        "location": "E:\\\\Musique\\\\v\\\\Van Halen\\\\Van Halen [24 - 192]"
      }
    ]
  },
  "groupe": [
    {
      "nom": "Van Halen"
    }
  ],
  "enregistrement": [
    "1977-09-01",
    "1977-11-01"
  ],
  "jsonVersion" : 1
}
			""" ;
	
	private static final String albumStr3 = """
			{
	  "titre": "A bigger bang, Live on Copacabana beach",
	  "format": {
	    "cd": 2,
	    "dvd": 2,
	    "audioFiles": [
	      {
	        "bitDepth": 16,
	        "samplingRate": 44.1,
	        "source": "CD",
	        "type": "FLAC",
	        "location": "E:\\\\Musique\\\\r\\\\The Rolling Stones\\\\A Bigger Bang, Live On Copacabana Beach"
	      }
	    ],
	    "videoFiles": [
	      {
	        "width": 720,
	        "height": 480,
	        "source": "DVD",
	        "type": "M4V",
	        "location": "G:\\\\Video\\\\r\\\\The Rolling Stones\\\\A Bigger Bang, Live On Copacabana Beach"
	      }
	    ]
	  },
	  "rangement": "33T",
	  "groupe": [
	    {
	      "nom": "Rolling Stones",
	      "article": "The"
	    }
	  ],
	  "enregistrement": [
	    "2005-11-22",
	    "2006-02-18"
	  ],
      "jsonVersion" : 1
	}	
				""";
	
	private static final String albumStr4 = """
			{
  "titre": "Du rire aux larmes",
  "format": {
    "cd": 2
  },
  "auteurCompositeurs": [
    {
      "nom": "Perret",
      "prenom": "Pierre",
      "naissance": "1934-07-09"
    }
  ],
  "enregistrement": [
    "1963-01-01",
    "1995-12-31"
  ],
  "jsonVersion" : 1
}
			""";
	
	private static final String albumStr5 = """
	{
  "titre": "Suites pour violoncelle",
  "format": {
    "cd": 2,
    "dvd": 1,
	"audioFiles" : [{
		"bitDepth": 16, 
		"samplingRate" : 44.1, 
		"source" : "CD", 
		"type" : "FLAC" }],
	"videoFiles" : [{
		"width": 720, 
		"height" : 480, 
		"source" : "DVD", 
		"type" : "MKV" }]
  },
  "interpretes": [
    {
      "nom": "Queyras",
      "prenom": "Jean-Guihen",
      "naissance": "1967-03-11"
    }
  ],
  "auteurCompositeurs": [
    {
      "nom": "Bach",
      "prenom": "Jean Sebastien"
    }
  ],
  "enregistrement": [
    "2007-03-01",
    "2007-03-31"
  ],
  "composition": [
    "1717-01-01",
    "1723-01-01"
  ],
  "jsonVersion" : 1
}		
			""";
	
	@Test
	void albumVersionShouldBeOk() throws JsonMappingException, JsonProcessingException {
		
		AlbumVersionMigrator2 migrator = AlbumVersionMigrator2.getInstance();
		
		ObjectNode albumJson = (ObjectNode)mapper.readTree(albumStr2);
		
		assertThat(migrator.checkVersion(albumJson)).isTrue();
	}
	
	
	@ParameterizedTest
	@ValueSource(strings = {albumStr2, albumStr3, albumStr4, albumStr5})
	void shouldMigrateAlbum(String albumStr) throws JsonMappingException, JsonProcessingException {
		
		AlbumVersionMigrator2 migrator = AlbumVersionMigrator2.getInstance();
		
		ObjectNode albumJson = (ObjectNode)mapper.readTree(albumStr);
		
		assertThat(albumJson.get(JsonMusicProperties.JSON_VERSION)).isNotNull();
		
		assertThat(albumJson.get(JsonMusicProperties.JSON_VERSION).asInt()).isEqualTo(1);
		
		List<String> audioFilesLocation = getMediaFilePathString(albumJson, JsonMusicProperties.AUDIO_FILE);
		List<String> videoFilesLocation = getMediaFilePathString(albumJson, JsonMusicProperties.VIDEO_FILE);
		
		ObjectNode migratedAlbum = migrator.migrate(albumJson);
		
		assertThat(migratedAlbum.get(JsonMusicProperties.JSON_VERSION).asInt())
			.isEqualTo(migrator.targetVersion())
			.isEqualTo(albumJson.get(JsonMusicProperties.JSON_VERSION).asInt())
			.isEqualTo(MusicArtefactParser.getVersion(migratedAlbum))
			.isEqualTo(MusicArtefactParser.getVersion(albumJson));
		
		List<String> migratedAudioFilesLocation = getMigratedMediaFilePathString(migratedAlbum, JsonMusicProperties.AUDIO_FILE);
		List<String> migratedVideoFilesLocation = getMigratedMediaFilePathString(migratedAlbum, JsonMusicProperties.VIDEO_FILE);
		
		if (audioFilesLocation == null) {
			assertThat(migratedAudioFilesLocation).isNull();
		} else {
			assertThat(migratedAudioFilesLocation).hasSameElementsAs(audioFilesLocation);
		}
		if (videoFilesLocation == null) {
			assertThat(migratedVideoFilesLocation).isNull();
		} else {
			assertThat(migratedVideoFilesLocation).hasSameElementsAs(videoFilesLocation);
		}
	}
	
	private List<JsonNode> getMediaFilePathJsonElement(ObjectNode albumJson, String mediaFileProperty) {
		
		JsonNode mediafiles = albumJson.get(JsonMusicProperties.FORMAT).get(mediaFileProperty);
		
		if (mediafiles == null) {
			return null;
		} else {
			List<JsonNode> locations = new ArrayList<>();
			
			mediafiles.forEach(mediafile -> {
				locations.add(mediafile.get(JsonMusicProperties.LOCATION));
			});
			return locations;
		}

	}
	
	private List<String> getMediaFilePathString(ObjectNode albumJson, String mediaFileProperty) {
		
		List<JsonNode>  mediaList = getMediaFilePathJsonElement(albumJson, mediaFileProperty);
		if (mediaList == null) {
			return null;
		} else {
			return getMediaFilePathJsonElement(albumJson, mediaFileProperty).stream()
				.map(jElem -> {
					if (jElem  == null) {
						return null;
					} else {
						assertThat(jElem.isTextual()).isTrue();
						return jElem.asText();
					}		
				})
				.collect(Collectors.toList());
		}
	}
	
	private List<String> getMigratedMediaFilePathString(ObjectNode albumJson, String mediaFileProperty) {
		
		List<JsonNode>  mediaList = getMediaFilePathJsonElement(albumJson, mediaFileProperty);
		if (mediaList == null) {
			return null;
		} else {
			return getMediaFilePathJsonElement(albumJson, mediaFileProperty).stream()
				.map(jElem -> {
					if (jElem  == null) {
						return null;
					} else {
						assertThat(jElem.isArray()).isTrue();
						assertThat(jElem).singleElement().satisfies(mediaPath -> 
							assertThat(mediaPath.isTextual()).isTrue()
						);
						return jElem.get(0).asText();
					}		
				})
				.collect(Collectors.toList());
		}
	}
}
