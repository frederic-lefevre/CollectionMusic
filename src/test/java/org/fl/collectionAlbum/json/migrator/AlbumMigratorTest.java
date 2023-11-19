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

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.json.MusicArtefactParser;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class AlbumMigratorTest {
	
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
	void shouldMigrateAlbum() {
		
		AlbumMigrator migrator = AlbumMigrator.getMigrator();
		
		JsonObject albumJson = JsonParser.parseString(albumStr1).getAsJsonObject();
		
		int initialVersion = MusicArtefactParser.getVersion(albumJson);
		assertThat(initialVersion).isZero();
		
		assertThat(albumJson.get(JsonMusicProperties.JSON_VERSION)).isNull();
		
		JsonObject migratedAlbum = migrator.migrate(albumJson);
		
		assertThat(migratedAlbum.get(JsonMusicProperties.JSON_VERSION).getAsInt())
			.isEqualTo(albumJson.get(JsonMusicProperties.JSON_VERSION).getAsInt())
			.isEqualTo(MusicArtefactParser.getVersion(migratedAlbum))
			.isEqualTo(MusicArtefactParser.getVersion(albumJson))
			.isEqualTo(migrator.versionMigrators.size());
	}

}
