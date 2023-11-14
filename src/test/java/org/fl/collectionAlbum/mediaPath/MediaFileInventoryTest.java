package org.fl.collectionAlbum.mediaPath;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class MediaFileInventoryTest {

	private static MediaFileInventory audioFileInventory;
	private static MediaFileInventory videoFileInventory;
	
	@BeforeAll
	static void readInventory() {
		
		Path audioFileRootPath = Control.getAudioFileRootPath();
		audioFileInventory = new MediaFileInventory(audioFileRootPath);
		
		Path videoFileRootPath = Control.getVideoFileRootPath();
		videoFileInventory = new MediaFileInventory(videoFileRootPath);
	}
	
	@Test
	void shouldFindAudioPath() {
		
		String albumStr1 = """
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
		
		JsonObject jAlbum = JsonParser.parseString(albumStr1).getAsJsonObject();

		ListeArtiste la = new ListeArtiste();
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
		lla.add(la);

		Album album = new Album(jAlbum, lla, Path.of("dummyPath"));
		
		List<Path> potentialPaths = audioFileInventory.getPotentialMediaPath(album);
		
		assertThat(potentialPaths)
			.isNotNull()
			.singleElement()
			.hasToString("E:\\Musique\\e\\Bill Evans\\Portrait In Jazz");
	}

	@Test
	void shouldFindMediaPath() {
		
		String albumStr1 = """
				{
  "titre": "A bigger bang, Live on Copacabana beach",
  "format": {
    "cd": 2,
	"dvd": 2,
	"audioFiles" : [{
		"bitDepth": 16, 
		"samplingRate" : 44.1, 
		"source" : "CD", 
		"type" : "FLAC" }],
	"videoFiles" : [{
		"width": 720, 
		"height" : 480, 
		"source" : "DVD", 
		"type" : "M4V" }]
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
  ]
}
				""" ;
		
		JsonObject jAlbum = JsonParser.parseString(albumStr1).getAsJsonObject();

		ListeArtiste la = new ListeArtiste();
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
		lla.add(la);

		Album album = new Album(jAlbum, lla, Path.of("dummyPath"));
		
		List<Path> potentialAudioPaths = audioFileInventory.getPotentialMediaPath(album);
		
		assertThat(potentialAudioPaths)
			.isNotNull()
			.hasSize(2)
			.allMatch(audioPath -> audioPath.toString().contains("A Bigger Bang, Live On Copacabana Beach"));
		
		List<Path> potentialVideoPaths = videoFileInventory.getPotentialMediaPath(album);
		
		assertThat(potentialVideoPaths)
			.isNotNull()
			.singleElement()
			.matches(audioPath -> audioPath.toString().contains("A Bigger Bang"));
	}
	
	@Test
	void shouldSelectMediaFileExtension() {
		
		assertThat(MediaFileInventory.isMediaFileName(Paths.get("toto.flac"))).isTrue();
		assertThat(MediaFileInventory.isMediaFileName(Paths.get("toto.mp3"))).isTrue();
		assertThat(MediaFileInventory.isMediaFileName(Paths.get("toto.wma"))).isTrue();
		assertThat(MediaFileInventory.isMediaFileName(Paths.get("toto.txt"))).isFalse();
		assertThat(MediaFileInventory.isMediaFileName(Paths.get("toto.jpg"))).isFalse();
		assertThat(MediaFileInventory.isMediaFileName(Paths.get("toto"))).isFalse();
		assertThat(MediaFileInventory.isMediaFileName(Paths.get(""))).isFalse();
		assertThat(MediaFileInventory.isMediaFileName(null)).isFalse();
		
		MediaFileInventory.extensionSet.forEach(extension -> System.out.println(extension));
	}
}
