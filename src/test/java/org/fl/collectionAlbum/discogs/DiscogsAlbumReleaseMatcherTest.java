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

package org.fl.collectionAlbum.discogs;

import static org.assertj.core.api.Assertions.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumRelease;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher.AlbumMatchResult;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher.MatchResultType;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher.ReleaseMatchResult;
import org.fl.collectionAlbum.format.MediaSupportCategories;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

class DiscogsAlbumReleaseMatcherTest {

	private static final ObjectMapper mapper = new ObjectMapper();
	
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
      "s/Soft Machine/Third"
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
"jsonVersion": 3
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
          "h/Jimi Hendrix/Electric Ladyland/Electric Ladyland [823 359-2W. Germany - 1984]"
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
  "jsonVersion": 3
}			
			""";
	
	private final static String electricLadylandSingleCD = """
{
  "titre": "Electric ladyland",
  "format": {
    "cd": 1,
    "audioFiles": [
      {
        "bitDepth": 16,
        "samplingRate": 44.1,
        "source": "CD",
        "type": "FLAC",
        "note": "Remaster Ocean view",
        "location": [
          "h/Jimi Hendrix/Electric Ladyland/Electric Ladyland [847 233-2, Polygram 1993]"
        ]
      }
    ]
  },
  "notes": [
    "Release Polydor LC O309, 847 233-2, bar code 7 31484 72332 0, 1993",
    "Son compressé, qualité inférieure",
    "Remaster Ocean view"
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
  "jsonVersion": 3
}			
			""";
	
	private final static String softMachineThirdK7 = """
{
"titre": "Third",
"format": {
"k7": 1,
"audioFiles": [
  {
    "bitDepth": 16,
    "samplingRate": 44.1,
    "source": "CD",
    "type": "FLAC",
    "location": [
      "s/Soft Machine/Third"
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
"jsonVersion": 3
}
			""" ;
	
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
      "s/Soft Machine/Third"
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
"jsonVersion": 3
}
			""" ;
	
	@BeforeAll
	static void initInventory() {
		MediaFilesInventories.clearInventories();
		MediaFilesInventories.scanMediaFilePaths();
		DiscogsInventory.buildDiscogsInventory();
	}
	
	@Test
	void shouldThrowNPEWhenAlbumIsNull() {
		
		assertThatNullPointerException().isThrownBy(() -> DiscogsAlbumReleaseMatcher.getPotentialReleaseMatch(null));
	}
	
	
	@Test
	void shouldGetPotentialReleaseAndAlbumMatches() throws JsonMappingException, JsonProcessingException {
		
		ReleaseMatchResult releaseMatchResult = DiscogsAlbumReleaseMatcher.getPotentialReleaseMatch(getAlbumFromJson(softMachineThird));
		
		assertThat(releaseMatchResult.getMatchResultType()).isEqualTo(MatchResultType.MATCH);
		assertThat(releaseMatchResult.getMatchingReleases())
			.isNotNull().singleElement()
			.satisfies(release -> {
				assertThat(release.getInventoryCsvAlbum().getArtists()).contains("Soft Machine");
				assertThat(release.getInventoryCsvAlbum().getTitle().toLowerCase()).isEqualTo("third");
				assertThat(release.getInventoryCsvAlbum().getFormats()).anyMatch(format -> format.contains("LP"));
			});
		
		DiscogsAlbumRelease thirdRelease = releaseMatchResult.getMatchingReleases().iterator().next();
		
		List<Album> albums = List.of(getAlbumFromJson(softMachineThird), getAlbumFromJson(electricLadylandDoubleCD));
		
		AlbumMatchResult albumMatchResult = thirdRelease.getPotentialAlbumMatch(albums);
		
		assertThat(albumMatchResult.getMatchResultType()).isEqualTo(MatchResultType.MATCH);
		assertThat(albumMatchResult.getMatchingAlbums())
			.isNotNull().singleElement()
			.satisfies(album -> 
				assertThat(album.getTitre().toLowerCase()).isEqualTo("third")
			);
		
		List<Album> albums2 = List.of(getAlbumFromJson(softMachineThirdK7), getAlbumFromJson(electricLadylandDoubleCD));
		
		AlbumMatchResult albumMatchResult2 = thirdRelease.getPotentialAlbumMatch(albums2);
		
		assertThat(albumMatchResult2.getMatchResultType()).isEqualTo(MatchResultType.NO_FORMAT_MATCH);
		assertThat(albumMatchResult2.getMatchingAlbums())
			.isNotNull().singleElement()
			.satisfies(album -> 
				assertThat(album.getTitre().toLowerCase()).isEqualTo("third")
			);
		
		List<Album> albums3 = List.of(getAlbumFromJson(electricLadylandDoubleCD));
		
		AlbumMatchResult albumMatchResult3 = thirdRelease.getPotentialAlbumMatch(albums3);
		
		assertThat(albumMatchResult3.getMatchResultType()).isEqualTo(MatchResultType.NO_MATCH);
	}
	
	@Test
	void shouldGetSeveralPotentialReleaseMatch() throws JsonMappingException, JsonProcessingException {
		
		ReleaseMatchResult releaseMatchResult = DiscogsAlbumReleaseMatcher.getPotentialReleaseMatch(getAlbumFromJson(electricLadylandSingleCD));
		
		assertThat(releaseMatchResult.getMatchResultType()).isEqualTo(MatchResultType.MATCH);
		assertThat(releaseMatchResult.getMatchingReleases())
			.isNotNull().hasSizeGreaterThan(1)
			.allSatisfy(release -> {
				assertThat(release.getInventoryCsvAlbum().getArtists()).anyMatch(artist -> artist.contains("Jimi Hendrix"));
				assertThat(release.getInventoryCsvAlbum().getTitle().toLowerCase()).isEqualTo("electric ladyland");
				assertThat(release.getInventoryCsvAlbum().getFormats()).anyMatch(format -> format.contains("CD"));
			});

	}
	
	@Test
	void shouldGetPotentialReleaseMatchOnAuteursAndTitleOnly() throws JsonMappingException, JsonProcessingException {
		
		ReleaseMatchResult releaseMatchResult = DiscogsAlbumReleaseMatcher.getPotentialReleaseMatch(getAlbumFromJson(softMachineThirdK7));
		
		assertThat(releaseMatchResult.getMatchResultType()).isEqualTo(MatchResultType.NO_FORMAT_MATCH);
		assertThat(releaseMatchResult.getMatchingReleases())
			.isNotNull().singleElement()
			.satisfies(release -> {
				assertThat(release.getInventoryCsvAlbum().getArtists()).contains("Soft Machine");
				assertThat(release.getInventoryCsvAlbum().getTitle().toLowerCase()).isEqualTo("third");
				assertThat(release.getInventoryCsvAlbum().getFormats()).anyMatch(format -> format.contains("LP"));
			});
	}
	
	@Test
	void shouldNotGetPotentialReleaseMatch() throws JsonMappingException, JsonProcessingException {
		
		ReleaseMatchResult releaseMatchResult = DiscogsAlbumReleaseMatcher.getPotentialReleaseMatch(getAlbumFromJson(nonExistentAlbum));
		
		assertThat(releaseMatchResult.getMatchResultType()).isEqualTo(MatchResultType.NO_MATCH);
		assertThat(releaseMatchResult.getMatchingReleases())
			.isNotNull().isEmpty();
	}

	@Test
	void shouldCoverAllSupportCategories() {
		
		Arrays.stream(MediaSupportCategories.values()).forEachOrdered(supportCategory ->
				assertThat(DiscogsAlbumRelease.getFormatMatch(supportCategory)).isNotNull()
				);
	}
	
	private static Album getAlbumFromJson(String albumStr) throws JsonMappingException, JsonProcessingException {
		
		ObjectNode jAlbum = (ObjectNode)mapper.readTree(albumStr);

		ListeArtiste la = new ListeArtiste();
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
		lla.add(la);

		return new Album(jAlbum, lla, Path.of("dummyPath"));
	}
}
