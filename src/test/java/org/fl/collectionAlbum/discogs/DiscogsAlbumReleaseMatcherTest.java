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

package org.fl.collectionAlbum.discogs;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class DiscogsAlbumReleaseMatcherTest {

	private final static String softMachineThird = """
{
"titre": "Third",
"format": {
"33t": 2,
"audioFiles": [
  {
    "bitDepth": 16,
    "samplingRate": 44.1,
    "source": "CD",
    "type": "FLAC",
    "location": [
      "E:\\\\Musique\\\\s\\\\Soft Machine\\\\Third"
    ]
  }
]
},
"groupe": [
{
  "nom": "Soft Machine"
}
],
"enregistrement": [
"1970-01-04",
"1970-01-31"
],
"jsonVersion": 2
}
			""" ;
	
	private final static String electricLadylandDoubleCD = """
{
  "titre": "Electric ladyland",
  "format": {
    "cd": 2,
    "audioFiles": [
      {
        "bitDepth": 16,
        "samplingRate": 44.1,
        "source": "CD",
        "type": "FLAC",
        "location": [
          "E:\\\\Musique\\\\h\\\\Jimi Hendrix\\\\Electric Ladyland\\\\Electric Ladyland [823 359-2W. Germany - 1984]"
        ]
      }
    ]
  },
  "notes": [
    "Release Polydor LC O309, 823 359-2, bar code 0 42282 33592 0, 1984",
    "Pas de compression, pas de réduction de bruit, meilleure qualité",
    "Erreur dans l'ordre des titre: face 1, 4, 2 puis 3"
  ],
  "auteurCompositeurs": [
    {
      "nom": "Hendrix",
      "prenom": "Jimi"
    }
  ],
  "enregistrement": [
    "1967-05-06",
    "1968-08-30"
  ],
  "jsonVersion": 2
}			
			""";
	
	private final static String nonExistentAlbum = """
{
"titre": "Third",
"format": {
"33t": 2,
"audioFiles": [
  {
    "bitDepth": 16,
    "samplingRate": 44.1,
    "source": "CD",
    "type": "FLAC",
    "location": [
      "E:\\\\Musique\\\\s\\\\Soft Machine\\\\Third"
    ]
  }
]
},
"groupe": [
{
  "nom": "Pink Floyd"
}
],
"enregistrement": [
"1970-01-04",
"1970-01-31"
],
"jsonVersion": 2
}
			""" ;
	
	
	@Test
	void shouldGetPotentialReleaseMatch() {
		
		assertThat(DiscogsAlbumReleaseMatcher.getPotentialReleaseMatch(getAlbumFromJson(softMachineThird)))
			.isNotNull().singleElement()
			.satisfies(release -> {
				assertThat(release.getInventoryCsvAlbum().getArtists()).contains("Soft Machine");
				assertThat(release.getInventoryCsvAlbum().getTitle().toLowerCase()).isEqualTo("third");
			});
	}
	
	@Test
	void shouldGetSeveralPotentialReleaseMatch() {
		
		assertThat(DiscogsAlbumReleaseMatcher.getPotentialReleaseMatch(getAlbumFromJson(electricLadylandDoubleCD)))
			.isNotNull().hasSizeGreaterThan(1)
			.allSatisfy(release -> {
				assertThat(release.getInventoryCsvAlbum().getArtists()).anyMatch(artist -> artist.contains("Jimi Hendrix"));
				assertThat(release.getInventoryCsvAlbum().getTitle().toLowerCase()).isEqualTo("electric ladyland");
			});

	}
	
	@Test
	void shouldNotGetPotentialReleaseMatch() {
		
		assertThat(DiscogsAlbumReleaseMatcher.getPotentialReleaseMatch(getAlbumFromJson(nonExistentAlbum)))
			.isNotNull().isEmpty();
	}

	private static Album getAlbumFromJson(String albumStr) {
		
		JsonObject jAlbum = JsonParser.parseString(albumStr).getAsJsonObject();

		ListeArtiste la = new ListeArtiste();
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
		lla.add(la);

		return new Album(jAlbum, lla, Path.of("dummyPath"));
	}
}
