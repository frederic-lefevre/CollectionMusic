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

import static org.assertj.core.api.Assertions.*;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.json.MusicArtefactParser;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class AlbumVersionMigrator1Test {

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
		
		AlbumVersionMigrator1 migrator = AlbumVersionMigrator1.getInstance();
		
		assertThat(migrator.targetVersion()).isEqualTo(1);
	}
	
	@Test
	void albumVersionShouldBeOk() {
		
		AlbumVersionMigrator1 migrator = AlbumVersionMigrator1.getInstance();
		
		JsonObject albumJson = JsonParser.parseString(albumStr1).getAsJsonObject();
		
		assertThat(migrator.checkVersion(albumJson)).isTrue();
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
        "type": "FLAC"
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
	
	@Test
	void albumVersionShouldBeKo() {
		
		AlbumVersionMigrator1 migrator = AlbumVersionMigrator1.getInstance();
		
		JsonObject albumJson = JsonParser.parseString(albumStr2).getAsJsonObject();
		
		assertThat(migrator.checkVersion(albumJson)).isFalse();
	}
	
	@Test
	void shouldMigrateAlbum() {
		
		AlbumVersionMigrator1 migrator = AlbumVersionMigrator1.getInstance();
		
		JsonObject albumJson = JsonParser.parseString(albumStr1).getAsJsonObject();
		
		assertThat(albumJson.get(JsonMusicProperties.JSON_VERSION)).isNull();
		
		JsonObject migratedAlbum = migrator.migrate(albumJson);
		
		assertThat(migratedAlbum.get(JsonMusicProperties.JSON_VERSION).getAsInt())
			.isEqualTo(migrator.targetVersion())
			.isEqualTo(albumJson.get(JsonMusicProperties.JSON_VERSION).getAsInt())
			.isEqualTo(MusicArtefactParser.getVersion(migratedAlbum))
			.isEqualTo(MusicArtefactParser.getVersion(albumJson));

	}
	
	private static final String albumStr3 = """
{ 
  "titre": "Van Halen",
  "format": {
    "cd": 1,
    "audioFiles": [
      {
        "bitDepth": 24,
        "samplingRate": 192,
        "source": "File",
        "type": "FLAC"
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
  "jsonVersion" : 2
}
			""" ;
	
	@Test
	void shouldNotMigrateAlbum() {
		
		AlbumVersionMigrator1 migrator = AlbumVersionMigrator1.getInstance();
		
		JsonObject albumJson = JsonParser.parseString(albumStr3).getAsJsonObject();
		
		int initialVersion = MusicArtefactParser.getVersion(albumJson);
		assertThat(initialVersion)
			.isGreaterThanOrEqualTo(migrator.targetVersion());
		
		JsonObject migratedAlbum = migrator.migrate(albumJson);
		
		assertThat(MusicArtefactParser.getVersion(migratedAlbum))
			.isEqualTo(initialVersion)
			.isEqualTo(MusicArtefactParser.getVersion(albumJson));

	}
}
