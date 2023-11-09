package org.fl.collectionAlbum.mediaPath;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
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

	private static MediaFileInventory mediaFileInventory;
	
	@BeforeAll
	static void readInventory() {
		
		Path audioFileRootPath = Control.getAudioFileRootPath();
		mediaFileInventory = new MediaFileInventory(audioFileRootPath);
	}
	
	@Test
	void shouldFindMediaPath() {
		
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
		
		List<Path> potentialPaths = mediaFileInventory.getPotentialMediaPath(album);
		
		assertThat(potentialPaths)
			.isNotNull()
			.singleElement()
			.hasToString("E:\\Musique\\e\\Bill Evans\\Portrait In Jazz");
	}

}
