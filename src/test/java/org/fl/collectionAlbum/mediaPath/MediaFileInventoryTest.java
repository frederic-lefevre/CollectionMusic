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

package org.fl.collectionAlbum.mediaPath;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.Format.ContentNature;
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
		
		Path audioFileRootPath = Control.getMediaFileRootPath(ContentNature.AUDIO);
		audioFileInventory = new MediaFileInventory(audioFileRootPath);
		
		Path videoFileRootPath = Control.getMediaFileRootPath(ContentNature.VIDEO);
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
		
		List<MediaFilePath> potentialPaths = audioFileInventory.getPotentialMediaPath(album);
		
		assertThat(potentialPaths)
			.isNotNull()
			.singleElement()
			.satisfies(audioPath -> {
				assertThat(audioPath.getPath()).hasToString("E:\\Musique\\e\\Bill Evans\\Portrait In Jazz");
				assertThat(audioPath.hasCover()).isTrue();
			});
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
		
		List<MediaFilePath> potentialAudioPaths = audioFileInventory.getPotentialMediaPath(album);
		
		assertThat(potentialAudioPaths)
			.isNotNull()
			.hasSize(2)
			.allSatisfy(audioPath -> {
				assertThat(audioPath.getPath().toString()).contains("A Bigger Bang, Live On Copacabana Beach");
				assertThat(audioPath.hasCover()).isTrue();
			});
		
		List<MediaFilePath> potentialVideoPaths = videoFileInventory.getPotentialMediaPath(album);
		
		assertThat(potentialVideoPaths)
			.isNotNull()
			.singleElement()
			.matches(audioPath -> audioPath.getPath().toString().contains("A Bigger Bang"));
	}

}
