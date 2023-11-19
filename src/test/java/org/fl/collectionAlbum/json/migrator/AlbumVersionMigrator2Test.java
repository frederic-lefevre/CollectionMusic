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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.json.MusicArtefactParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class AlbumVersionMigrator2Test {

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
	void albumVersionShouldBeKo() {
		
		AlbumVersionMigrator2 migrator = AlbumVersionMigrator2.getInstance();
		
		JsonObject albumJson = JsonParser.parseString(albumStr1).getAsJsonObject();
		
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
	
	@Test
	void albumVersionShouldBeOk() {
		
		AlbumVersionMigrator2 migrator = AlbumVersionMigrator2.getInstance();
		
		JsonObject albumJson = JsonParser.parseString(albumStr2).getAsJsonObject();
		
		assertThat(migrator.checkVersion(albumJson)).isTrue();
	}
	
	
	@ParameterizedTest
	@ValueSource(strings = {albumStr2, albumStr3, albumStr4})
	void shouldMigrateAlbum(String albumStr) {
		
		AlbumVersionMigrator2 migrator = AlbumVersionMigrator2.getInstance();
		
		JsonObject albumJson = JsonParser.parseString(albumStr).getAsJsonObject();
		
		assertThat(albumJson.get(JsonMusicProperties.JSON_VERSION)).isNotNull();
		
		assertThat(albumJson.get(JsonMusicProperties.JSON_VERSION).getAsInt()).isEqualTo(1);
		
		List<String> audioFilesLocation = getMediaFilePathString(albumJson, JsonMusicProperties.AUDIO_FILE);
		List<String> videoFilesLocation = getMediaFilePathString(albumJson, JsonMusicProperties.VIDEO_FILE);
		
		JsonObject migratedAlbum = migrator.migrate(albumJson);
		
		assertThat(migratedAlbum.get(JsonMusicProperties.JSON_VERSION).getAsInt())
			.isEqualTo(migrator.targetVersion())
			.isEqualTo(albumJson.get(JsonMusicProperties.JSON_VERSION).getAsInt())
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
	
	private List<JsonElement> getMediaFilePathJsonElement(JsonObject albumJson, String mediaFileProperty) {
		
		JsonArray mediafiles = albumJson.get(JsonMusicProperties.FORMAT).getAsJsonObject()
				.getAsJsonArray(mediaFileProperty);
		
		if (mediafiles == null) {
			return null;
		} else {
			List<JsonElement> locations = new ArrayList<>();
			
			mediafiles.forEach(mediafile -> {
				locations.add(mediafile.getAsJsonObject().get(JsonMusicProperties.LOCATION));
			});
			return locations;
		}

	}
	
	private List<String> getMediaFilePathString(JsonObject albumJson, String mediaFileProperty) {
		
		List<JsonElement>  mediaList = getMediaFilePathJsonElement(albumJson, mediaFileProperty);
		if (mediaList == null) {
			return null;
		} else {
			return getMediaFilePathJsonElement(albumJson, mediaFileProperty).stream()
				.map(jElem -> {
					if (jElem  == null) {
						return null;
					} else {
						assertThat(jElem.isJsonPrimitive()).isTrue();
						return jElem.getAsString();
					}		
				})
				.collect(Collectors.toList());
		}
	}
	
	private List<String> getMigratedMediaFilePathString(JsonObject albumJson, String mediaFileProperty) {
		
		List<JsonElement>  mediaList = getMediaFilePathJsonElement(albumJson, mediaFileProperty);
		if (mediaList == null) {
			return null;
		} else {
			return getMediaFilePathJsonElement(albumJson, mediaFileProperty).stream()
				.map(jElem -> {
					if (jElem  == null) {
						return null;
					} else {
						assertThat(jElem.isJsonArray()).isTrue();
						assertThat(jElem.getAsJsonArray().size()).isEqualTo(1);
						assertThat(jElem.getAsJsonArray().get(0).isJsonPrimitive()).isTrue();
						return jElem.getAsJsonArray().get(0).getAsString();
					}		
				})
				.collect(Collectors.toList());
		}
	}
}
