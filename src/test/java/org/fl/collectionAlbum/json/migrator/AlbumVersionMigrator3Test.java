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

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.json.MusicArtefactParser;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

class AlbumVersionMigrator3Test {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	private static final String albumStr1 = """
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
	void checkTargetVersion() {
		
		AlbumVersionMigrator3 migrator = AlbumVersionMigrator3.getInstance();
		
		assertThat(migrator.targetVersion()).isEqualTo(3);
	}
	
	@Test
	void albumVersionShouldBeUptoDate() throws JsonMappingException, JsonProcessingException {
		
		AlbumVersionMigrator3 migrator = AlbumVersionMigrator3.getInstance();
		
		ObjectNode albumJson = (ObjectNode)mapper.readTree(albumStr1);
		
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
  "jsonVersion": 2,
  "discogs": "9556137",
  "sleeveImg": "C:\\\\FredericPersonnel\\\\Loisirs\\\\musique\\\\PochettesAlbums\\\\b\\\\BlackSabbath\\\\SabbathBloodySabbathK7.jpg"
}	
			""";

	@Test
	void albumVersionShouldNotBeUptoDate() throws JsonMappingException, JsonProcessingException {
		
		AlbumVersionMigrator3 migrator = AlbumVersionMigrator3.getInstance();
		
		ObjectNode albumJson = (ObjectNode)mapper.readTree(albumStr2);
		
		assertThat(migrator.checkVersion(albumJson)).isTrue();
	}
	
	@Test
	void shouldMigrateAlbum() throws JsonMappingException, JsonProcessingException {
		
		AlbumVersionMigrator3 migrator = AlbumVersionMigrator3.getInstance();
		
		ObjectNode albumJson = (ObjectNode)mapper.readTree(albumStr2);
		
		assertThat(albumJson.get(JsonMusicProperties.JSON_VERSION).asInt()).isEqualTo(2);
		
		ObjectNode migratedAlbum = migrator.migrate(albumJson);
		
		assertThat(migratedAlbum).isNotNull();
		
		assertThat(migratedAlbum.get(JsonMusicProperties.JSON_VERSION).asInt())
			.isEqualTo(migrator.targetVersion())
			.isEqualTo(albumJson.get(JsonMusicProperties.JSON_VERSION).asInt())
			.isEqualTo(MusicArtefactParser.getVersion(migratedAlbum))
			.isEqualTo(MusicArtefactParser.getVersion(albumJson));
		
		JsonNode migratedNode = migratedAlbum.get(JsonMusicProperties.SLEEVE_IMG);
		
		assertThat(migratedNode).isNotNull();
		
		String migratedNodeValue = migratedNode.asText();
		
		assertThat(migratedNodeValue).isEqualTo("b/BlackSabbath/SabbathBloodySabbathK7.jpg");
		
	}
}
