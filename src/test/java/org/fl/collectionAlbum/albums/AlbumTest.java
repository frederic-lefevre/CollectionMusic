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

package org.fl.collectionAlbum.albums;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher.MatchResultType;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher.ReleaseMatchResult;
import org.fl.collectionAlbum.format.AbstractAudioFile;
import org.fl.collectionAlbum.format.AbstractMediaFile;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.format.LosslessAudioFile;
import org.fl.collectionAlbum.format.MediaSupports;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;
import org.fl.discogsInterface.inventory.InventoryCsvAlbum;
import org.fl.util.FilterCounter;
import org.fl.util.FilterCounter.LogRecordCounter;
import org.fl.util.json.JsonUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

class AlbumTest {

	private final static Logger albumLog = Logger.getLogger(AlbumTest.class.getName());
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	@BeforeAll
	static void initInventory() {
		MediaFilesInventories.clearInventories();
		MediaFilesInventories.scanMediaFilePaths();
		DiscogsInventory.buildDiscogsInventory();
	}
	
	@Test
	void testEmptyAlbum() {

		LogRecordCounter albumParserFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.AlbumParser"));		
		LogRecordCounter albumFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.albums.Album"));	
		LogRecordCounter formatFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.format.Format"));	
		LogRecordCounter parserHelpersFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.ParserHelpers"));

		
		ListeArtiste la = new ListeArtiste();
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
		lla.add(la);

		Album album = new Album(JsonNodeFactory.instance.objectNode(), lla, Path.of("dummyPath"));

		assertThat(album).isNotNull();
		assertThat(album.hasAudioFiles()).isFalse();
		assertThat(album.hasVideoFiles()).isFalse();
		assertThat(album.hasMediaFiles()).isFalse();
		assertThat(album.hasDiscogsRelease()).isFalse();
		assertThat(album.hasArtiste()).isFalse();
		assertThat(album.hasIntervenant()).isFalse();
		assertThat(album.hasHighResAudio()).isFalse();
		assertThat(album.hasNotes()).isFalse();
		assertThat(album.hasOnlyLossLessAudio()).isFalse();
		assertThat(album.hasProblem()).isFalse();
		assertThat(album.hasSpecificCompositionDates()).isFalse();
		assertThat(album.hasUrlLinks()).isFalse();
		
		assertThat(album.getAllMediaFiles()).isEmpty();
		
		assertMediaSupports(album, Collections.emptySet());
		
		assertThat(album.getContentNatures()).isEmpty();
		
		assertThat(albumParserFilterCounter.getLogRecordCount()).isEqualTo(2);
		assertThat(albumParserFilterCounter.getLogRecordCount(Level.WARNING)).isEqualTo(2);
		
		assertThat(albumFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(albumFilterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
		
		assertThat(formatFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(formatFilterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
		
		assertThat(parserHelpersFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(parserHelpersFilterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
	}

	private static final String albumStr1 = """
			{ 
			  "titre": "Portrait in jazz",
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
	void testAlbum1() throws JsonMappingException, JsonProcessingException {
		
		ObjectNode jAlbum = (ObjectNode)mapper.readTree(albumStr1);

		ListeArtiste la = new ListeArtiste();
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
		lla.add(la);

		Album album = new Album(jAlbum, lla, Path.of("dummyPath"));
		
		testAlbumProperties(album, la);
		
		assertThat(album.hasMissingOrInvalidMediaFilePath(ContentNature.AUDIO)).isTrue();
		assertThat(album.hasMediaFilePathNotFound(ContentNature.AUDIO)).isTrue();
		assertThat(album.hasProblem()).isTrue();
		
		List<? extends AbstractMediaFile> audioFiles = album.getFormatAlbum().getMediaFiles(ContentNature.AUDIO);
		assertThat(audioFiles).isNotNull()
			.singleElement()
			.asInstanceOf(InstanceOfAssertFactories.type(AbstractAudioFile.class))
			.satisfies(audio -> {
				assertThat(audio.hasMissingOrInvalidMediaFilePath()).isTrue();
				assertThat(audio.isLossLess()).isTrue();
				
				assertThat(audio).isNotNull().isInstanceOf(LosslessAudioFile.class);
				
				LosslessAudioFile lossLessAudio = (LosslessAudioFile)audio;
				assertThat(lossLessAudio.getBitDepth()).isEqualTo(16);
				assertThat(lossLessAudio.getSamplingRate()).isEqualTo(44.1);
				assertThat(lossLessAudio.getType().name()).isEqualTo("FLAC");
				assertThat(lossLessAudio.getSource()).isEqualTo("MOFI Fidelity Sound Lab");
				assertThat(audio.getMediaFilePaths()).isNull();
			});

		assertThat(album.getAllMediaFiles()).hasSameElementsAs(audioFiles);
		
		assertThat(album.getCoverImage()).isNull();
		
		// Add the audio file path
		AbstractAudioFile audioFile = (AbstractAudioFile) audioFiles.get(0);
		audioFile.addMediaFilePath(new MediaFilePath(Paths.get("E:/Musique/e/Bill Evans/Waltz for Debby/"), ContentNature.AUDIO, ContentNature.AUDIO.strictCheckings()));
		
		assertThat(album.getFormatAlbum().getMediaFiles(ContentNature.AUDIO)).singleElement().satisfies(audio -> {
			assertThat(audio.hasMissingOrInvalidMediaFilePath()).isFalse();
			assertThat(audio.getMediaFilePaths())
				.isNotNull()
				.singleElement()
				.satisfies(audioPath -> assertThat(audioPath.getPath()).hasToString("E:\\Musique\\e\\Bill Evans\\Waltz for Debby"));
		});

		assertThat(album.getCoverImage())
			.isNotNull()
			.hasToString("E:\\Musique\\e\\Bill Evans\\Waltz for Debby\\cover.jpg");
		
		// Get the json from the album (should be modified with the path)
		ObjectNode modifiedJson = album.getJson();
		
		// Recreate an album from this json
		ListeArtiste la2 = new ListeArtiste();
		List<ListeArtiste> lla2 = new ArrayList<ListeArtiste>();
		lla.add(la2);
		Album album2 = new Album(modifiedJson, lla2, Path.of("dummyPath2"));
		
		testAlbumProperties(album2, la2);
		
		// The album has now a valid media file path
		assertThat(album2.hasMissingOrInvalidMediaFilePath(ContentNature.AUDIO)).isFalse();
		assertThat(album2.hasMediaFilePathNotFound(ContentNature.AUDIO)).isFalse();
		assertThat(album2.hasProblem()).isFalse();
		
		assertThat(album2.getFormatAlbum().getMediaFiles(ContentNature.AUDIO)).isNotNull()
			.singleElement()
			.asInstanceOf(InstanceOfAssertFactories.type(AbstractAudioFile.class))
			.satisfies(audio -> {
				assertThat(audio.hasMissingOrInvalidMediaFilePath()).isFalse();
				assertThat(audio.isLossLess()).isTrue();
				
				assertThat(audio).isNotNull().isInstanceOf(LosslessAudioFile.class);
				
				LosslessAudioFile lossLessAudio = (LosslessAudioFile)audio;
				assertThat(lossLessAudio.getBitDepth()).isEqualTo(16);
				assertThat(lossLessAudio.getSamplingRate()).isEqualTo(44.1);
				assertThat(lossLessAudio.getType().name()).isEqualTo("FLAC");
				assertThat(lossLessAudio.getSource()).isEqualTo("MOFI Fidelity Sound Lab");
				assertThat(audio.getMediaFilePaths())
					.isNotNull()
					.singleElement()
					.satisfies(audioPath -> assertThat(audioPath.getPath()).hasToString("E:\\Musique\\e\\Bill Evans\\Waltz for Debby"));
			});
		
		// Fix the audio file path
		AbstractAudioFile audioFile2 = (AbstractAudioFile) album2.getFormatAlbum().getMediaFiles(ContentNature.AUDIO).get(0);
		audioFile2.setMediaFilePath(Set.of(new MediaFilePath(Paths.get("E:/Musique/e/Bill Evans/Portrait In Jazz/"), ContentNature.AUDIO, ContentNature.AUDIO.strictCheckings())));
		
		assertThat(album2.getFormatAlbum().getMediaFiles(ContentNature.AUDIO)).singleElement().satisfies(audio -> {
			assertThat(audio.hasMissingOrInvalidMediaFilePath()).isFalse();
			assertThat(audio.getMediaFilePaths())
				.isNotNull()
				.singleElement()
				.satisfies(audioPath -> assertThat(audioPath.getPath()).hasToString("E:\\Musique\\e\\Bill Evans\\Portrait In Jazz"));
		});
		
		// Get the json from the album (should be modified with the fixed path)
		ObjectNode modifiedJson2 = album2.getJson();
		
		// Recreate an album from this json
		Path jsonFilePath = Path.of("C:\\ForTests\\CollectionMusique\\PortraitInJazz.json");
		
		ListeArtiste la3 = new ListeArtiste();
		List<ListeArtiste> lla3 = new ArrayList<ListeArtiste>();
		lla.add(la3);
		Album album3 = new Album(modifiedJson2, lla3, jsonFilePath);
		
		testAlbumProperties(album3, la3);
		
		// The album has now a fixed media file path
		assertThat(album3.hasMissingOrInvalidMediaFilePath(ContentNature.AUDIO)).isFalse();
		assertThat(album3.hasMediaFilePathNotFound(ContentNature.AUDIO)).isFalse();
		assertThat(album3.hasProblem()).isFalse();
		
		assertThat(album3.getFormatAlbum().getMediaFiles(ContentNature.AUDIO)).isNotNull()
			.singleElement()
			.asInstanceOf(InstanceOfAssertFactories.type(AbstractAudioFile.class))
			.satisfies(audio -> {
				assertThat(audio.hasMissingOrInvalidMediaFilePath()).isFalse();
				assertThat(audio.isLossLess()).isTrue();
				
				assertThat(audio).isNotNull().isInstanceOf(LosslessAudioFile.class);
				
				LosslessAudioFile lossLessAudio = (LosslessAudioFile)audio;
				assertThat(lossLessAudio.getBitDepth()).isEqualTo(16);
				assertThat(lossLessAudio.getSamplingRate()).isEqualTo(44.1);
				assertThat(lossLessAudio.getType().name()).isEqualTo("FLAC");
				assertThat(lossLessAudio.getSource()).isEqualTo("MOFI Fidelity Sound Lab");
				assertThat(audio.getMediaFilePaths())
					.isNotNull()
					.singleElement()
					.satisfies(audioPath -> assertThat(audioPath.getPath()).hasToString("E:\\Musique\\e\\Bill Evans\\Portrait In Jazz"));
			});
		
		try {
			Files.delete(jsonFilePath);
		} catch (IOException e) {
			fail("Exception deleting " + jsonFilePath + " : " + e.getMessage());
		}
		
		// Write json in file
		album3.writeJson();
		
		// Read back json and check album
		ObjectNode readBackJson = (ObjectNode)JsonUtils.getJsonObjectFromPath(jsonFilePath, Control.getCharset(), albumLog);
		
		ListeArtiste la4 = new ListeArtiste();
		List<ListeArtiste> lla4 = new ArrayList<ListeArtiste>();
		lla.add(la4);
		Album album4 = new Album(readBackJson, lla4, jsonFilePath);
		
		testAlbumProperties(album4, la4);
		
		assertThat(album4.hasMissingOrInvalidMediaFilePath(ContentNature.AUDIO)).isFalse();
		assertThat(album4.hasMediaFilePathNotFound(ContentNature.AUDIO)).isFalse();
		assertThat(album4.hasProblem()).isFalse();
		
		assertThat(album4.getFormatAlbum().getMediaFiles(ContentNature.AUDIO)).isNotNull()
			.singleElement()
			.asInstanceOf(InstanceOfAssertFactories.type(AbstractAudioFile.class))
			.satisfies(audio -> {
				assertThat(audio.hasMissingOrInvalidMediaFilePath()).isFalse();
				assertThat(audio.isLossLess()).isTrue();
				
				assertThat(audio).isNotNull().isInstanceOf(LosslessAudioFile.class);
				
				LosslessAudioFile lossLessAudio = (LosslessAudioFile)audio;
				assertThat(lossLessAudio.getBitDepth()).isEqualTo(16);
				assertThat(lossLessAudio.getSamplingRate()).isEqualTo(44.1);
				assertThat(lossLessAudio.getType().name()).isEqualTo("FLAC");
				assertThat(lossLessAudio.getSource()).isEqualTo("MOFI Fidelity Sound Lab");
				assertThat(audio.getMediaFilePaths())
					.isNotNull()
					.singleElement()
					.satisfies(audioPath -> assertThat(audioPath.getPath()).hasToString("E:\\Musique\\e\\Bill Evans\\Portrait In Jazz"));
			});
		
	}
	
	@Test
	void testAlbumPotentialMediaFilesSearch() throws JsonMappingException, JsonProcessingException {

		ObjectNode jAlbum = (ObjectNode)mapper.readTree(albumStr1);

		ListeArtiste la = new ListeArtiste();
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
		lla.add(la);

		Album album = new Album(jAlbum, lla, Path.of("dummyPath"));

		List<MediaFilePath> potentialPaths = album.searchPotentialMediaFilesPaths(ContentNature.AUDIO);

		assertThat(potentialPaths).isNotNull().singleElement()
			.satisfies(mediaFilePath -> assertThat(mediaFilePath.getPath()).hasToString("E:\\Musique\\e\\Bill Evans\\Portrait In Jazz"));
		
		assertThat(album.hasProblem()).isTrue();
		
		assertThat(album.getContentNatures()).containsExactly(ContentNature.AUDIO);
	}
	
	private static final String albumStr3 = """
{
  "titre": "Magical mystery tour",
  "format": {
    "cd": 1,
    "audioFiles": [
      {
        "bitDepth": 16,
        "samplingRate": 44.1,
        "source": "CD",
        "type": "FLAC",
        "location": [
          "E:\\\\Musique\\\\b\\\\The Beatles\\\\Magical Mystery Tour"
        ]
      }
    ]
  },
  "groupe": [
    {
      "nom": "Beatles",
      "article": "The"
    }
  ],
  "enregistrement": [
    "1966-11-24",
    "1967-11-07"
  ],
  "jsonVersion": 2
}
			""";
	
	@Test
	void testAlbumPotentialDiscogsReleaseSearch() throws JsonMappingException, JsonProcessingException {
		
		ObjectNode jAlbum = (ObjectNode)mapper.readTree(albumStr3);

		ListeArtiste la = new ListeArtiste();
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
		lla.add(la);

		Album album = new Album(jAlbum, lla, Path.of("dummyPath"));
		
		ReleaseMatchResult releaseMatchResult = album.searchPotentialDiscogsReleases();
		
		assertThat(releaseMatchResult.getMatchResultType()).isEqualTo(MatchResultType.MATCH);
		
		assertThat(releaseMatchResult.getMatchingReleases()).isNotNull().singleElement()
			.satisfies(discogsRelease -> {
				InventoryCsvAlbum csvRelease = discogsRelease.getInventoryCsvAlbum();
				assertThat(csvRelease.getArtists()).singleElement().isEqualTo("The Beatles");
				assertThat(csvRelease.getReleaseId()).isEqualTo("2519903");
			});
		
		assertThat(album.hasProblem()).isFalse();
		assertMediaSupports(album, Set.of(MediaSupports.CD));
		
		assertThat(album.getContentNatures()).containsExactly(ContentNature.AUDIO);
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
  ]
}
			""" ;
	
	@Test
	void testAlbumPotentialMediaFilesSearch2() throws JsonMappingException, JsonProcessingException {

		ObjectNode jAlbum = (ObjectNode)mapper.readTree(albumStr2);

		ListeArtiste la = new ListeArtiste();
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
		lla.add(la);

		Album album = new Album(jAlbum, lla, Path.of("dummyPath"));

		List<MediaFilePath> potentialPaths = album.searchPotentialMediaFilesPaths(ContentNature.AUDIO);

		assertThat(potentialPaths).isNotNull().singleElement()
		.satisfies(mediaFilePath -> assertThat(mediaFilePath.getPath()).hasToString("E:\\Musique\\v\\Van Halen\\Van Halen [24 - 192]"));
		
		assertMediaSupports(album, Set.of(MediaSupports.CD));
		
		assertThat(album.getContentNatures()).containsExactly(ContentNature.AUDIO);
		assertThat(album.hasProblem()).isTrue();
	}
	
	@Test
	void testDiscogsProperties() throws JsonMappingException, JsonProcessingException {

		ObjectNode jAlbum = (ObjectNode)mapper.readTree(albumStr2);

		ListeArtiste la = new ListeArtiste();
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
		lla.add(la);

		Album album = new Album(jAlbum, lla, Path.of("dummyPath"));
		
		assertThat(album.hasProblem()).isTrue();
		
		assertThat(album.getDiscogsLink()).isNull();
		assertThat(album.getDiscogsFormatValidation()).isFalse();
		
		assertThat(album.getJson().get(JsonMusicProperties.DISCOGS)).isNull();
		assertThat(album.getJson().get(JsonMusicProperties.DISCOGS_VALID)).isNull();
		
		String discogsLink ="123456";
		album.setDiscogsLink(discogsLink);
		
		assertThat(album.getDiscogsLink()).isNotNull().isEqualTo(discogsLink);
		assertThat(album.getDiscogsFormatValidation()).isFalse();
		
		assertThat(album.getJson().get(JsonMusicProperties.DISCOGS)).isNotNull();
		assertThat(album.getJson().get(JsonMusicProperties.DISCOGS_VALID)).isNull();
		
		album.setDiscogsFormatValid(true);
		assertThat(album.getDiscogsFormatValidation()).isTrue();
		
		assertThat(album.getJson().get(JsonMusicProperties.DISCOGS)).isNotNull();
		assertThat(album.getJson().get(JsonMusicProperties.DISCOGS_VALID)).isNotNull();
	}
	
	private void testAlbumProperties(Album album, ListeArtiste la) {
		
		assertThat(album.getTitre()).isEqualTo("Portrait in jazz");

		List<String> liens = album.getUrlLinks();
		assertThat(liens)
			.isNotNull()
			.singleElement()
			.isEqualTo("http://somwhere");
		
		assertThat(album.hasAudioFiles()).isTrue();
		assertThat(album.hasVideoFiles()).isFalse();
		assertThat(album.hasMediaFiles()).isTrue();
		
		assertThat(album.hasHighResAudio()).isFalse();
		assertThat(album.hasSpecificCompositionDates()).isFalse();
		assertThat(album.hasOnlyLossLessAudio()).isTrue();
		assertThat(album.hasArtiste()).isTrue();
		assertThat(album.hasIntervenant()).isFalse();
		
		assertThat(album.hasDiscogsRelease()).isFalse();
		assertThat(album.getDiscogsFormatValidation()).isFalse();
		
		assertThat(album.hasContentNature(ContentNature.AUDIO)).isTrue();
		assertThat(album.hasContentNature(ContentNature.VIDEO)).isFalse();
		
		assertThat(album.missesAudioFile()).isFalse();
		assertThat(album.missesVideoFile()).isFalse();
		
		Artiste bill = album.getAuteurs().get(0);
		assertThat(bill).isNotNull();
		assertThat(bill.getPrenoms()).isEqualTo("Bill");
		assertThat(bill.getNom()).isEqualTo("Evans");
		
		assertThat(bill.getNbAlbum()).isZero();
		
		album.addMusicArtfactArtistesToList(la);
		assertThat(bill.getNbAlbum()).isEqualTo(1);
		
		assertThat(bill.getAlbums()).isNotNull();
		assertThat(bill.getAlbums().getAlbums())
			.isNotNull()
			.singleElement()
			.isEqualTo(album);
		
		assertMediaSupports(album, Set.of(MediaSupports.CD));
		
		assertThat(album.getContentNatures()).containsExactly(ContentNature.AUDIO);
	}
	
	private void assertMediaSupports(Album album, Set<MediaSupports> mediaSupports) {
		
		assertThat(MediaSupports.values()).allSatisfy((mediaSupport -> 
			assertThat(album.hasMediaSupport(mediaSupport)).isEqualTo(mediaSupports.contains(mediaSupport))));

	}
}
