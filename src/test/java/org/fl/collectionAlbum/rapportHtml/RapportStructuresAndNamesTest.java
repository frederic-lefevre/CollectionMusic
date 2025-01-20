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


package org.fl.collectionAlbum.rapportHtml;

import static org.assertj.core.api.Assertions.*;

import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.LieuxDesConcerts;
import org.junit.jupiter.api.Test;

class RapportStructuresAndNamesTest {

	private static final String MUSIQUE_DIRECTORY_URI = "file:///C:/FredericPersonnel/Loisirs/musique/";
	
	@Test
	void test() {
		
		RapportStructuresAndNames.init();
		assertThat(RapportStructuresAndNames.getRapportPath().toUri()).hasToString(MUSIQUE_DIRECTORY_URI + "RapportCollection/rapport/");
		assertThat(RapportStructuresAndNames.getOldRapportPath().toUri()).hasToString(MUSIQUE_DIRECTORY_URI + "RapportCollection/rapport_old/");
		assertThat(RapportStructuresAndNames.getAbsoluteAlbumDir().toUri()).hasToString(MUSIQUE_DIRECTORY_URI + "RapportCollection/rapport/albums/");
		assertThat(RapportStructuresAndNames.getAbsoluteConcertDir().toUri()).hasToString(MUSIQUE_DIRECTORY_URI + "RapportCollection/rapport/concerts/");
		assertThat(RapportStructuresAndNames.getAbsoluteHomeCollectionFile().toUri()).hasToString(MUSIQUE_DIRECTORY_URI + "RapportCollection/rapport/index.html");
		assertThat(RapportStructuresAndNames.getAbsoluteHomeConcertFile().toUri()).hasToString(MUSIQUE_DIRECTORY_URI + "RapportCollection/rapport/indexConcert.html");
	}

	@Test
	void test2() {

		RapportStructuresAndNames.init() ;
	
		JsonObject jArt = new JsonObject() ;
		jArt.addProperty("nom", "Evans") ;
		jArt.addProperty("prenom", "Bill") ;
		jArt.addProperty("naissance", "1929-08-16") ;
		jArt.addProperty("mort",  "1980-09-15") ;
		
		Artiste artiste= new Artiste(jArt);
		
		URI pAlbum = RapportStructuresAndNames.getArtisteAlbumRapportRelativeUri(artiste);
		assertThat(pAlbum.toString()).isEqualTo("artistes/albums/i0.html");
	}
	
	private static final String albumStr1 = """	
			{ 
			  "titre": "Portrait in jazz",
			  "format": {  "cd": 1   }, 
			  "auteurCompositeurs": [ 
			    {  
			      "nom": "Evans",
			      "prenom": "Bill", 
			      "naissance": "1929-08-16",
			      "mort": "1980-09-15"  
			    }  
			  ],   
			  "enregistrement": [ "1959-12-28",  "1959-12-28"  ] 
			 } 
			""" ;

	private static final String albumStr2 = """
			{ 
			  "titre": "Portrait in rock",
			  "format": {  "cd": 1   }, 
			  "auteurCompositeurs": [ 
			    { 
			      "nom": "Fake", 
			      "prenom": "Bill", 
			      "naissance": "1929-08-16",
			      "mort": "1980-09-15" 
			    }   
			  ],   
			  "enregistrement": [ "1959-12-28",  "1959-12-28"  ], 
			  "notes": [ "Version mono" ]
			 }			
			""";

	@Test
	void test3() {
		
		RapportStructuresAndNames.init();

		JsonObject jAlbum = JsonParser.parseString(albumStr1).getAsJsonObject();

		ListeArtiste la = new ListeArtiste();
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
		lla.add(la);

		Album album = new Album(jAlbum, lla, Path.of("dummyPath"));
		Artiste bill = album.getAuteurs().get(0);
		album.addMusicArtfactArtistesToList(la);
		
		URI pAlbum = RapportStructuresAndNames.getArtisteAlbumRapportRelativeUri(bill);
		assertThat(pAlbum).hasToString("artistes/albums/i0.html");

		URI pInfoAlbum = RapportStructuresAndNames.getAlbumRapportRelativeUri(album);
		assertThat(pInfoAlbum).isNull();
		
		JsonObject jAlbum2 = JsonParser.parseString(albumStr2).getAsJsonObject();
		Album album2 = new Album(jAlbum2, lla, Path.of("dummyPath"));
		album2.addMusicArtfactArtistesToList(la);
		Artiste fake = album2.getAuteurs().get(0);
		
		URI pAlbum2 = RapportStructuresAndNames.getArtisteAlbumRapportRelativeUri(fake);
		assertThat(pAlbum2).hasToString("artistes/albums/i1.html");

		URI pInfoAlbum2 = RapportStructuresAndNames.getAlbumRapportRelativeUri(album2);
		assertThat(pInfoAlbum2).hasToString("albums/i0.html");
	}
	
	private static final String concertStr1 = """
			{ 
			  "auteurCompositeurs": [ 
			      { 
			        "nom": "Bridgewater",
			        "prenom": "Dee Dee",
			        "naissance": "1950-05-27"
			       }  ],
			   "date": "1990-07-19",
			   "lieu": "Juan-les-Pins, Alpes-Maritimes",
			   "imageTicket": [
			       "/Annees1990/1990/07_Juillet/RayCharles01.jpg"
			    ]  
			 } 
			 """;
	@Test
	void test4() {

		RapportStructuresAndNames.init();
		
		JsonObject jConcert = JsonParser.parseString(concertStr1).getAsJsonObject();
		
		ListeArtiste la = new ListeArtiste();
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
		lla.add(la) ;
		
		LieuxDesConcerts lieuxDesConcerts = new LieuxDesConcerts();
		Concert concert = new Concert(jConcert, lla, lieuxDesConcerts, Path.of("dummyPath"));
		concert.addMusicArtfactArtistesToList(la);
		List<Artiste> lDeeDee = concert.getAuteurs();
		Artiste deeDee = lDeeDee.get(0);
		
		URI uriDeedee = RapportStructuresAndNames.getArtisteConcertRapportRelativeUri(deeDee);
		assertThat(uriDeedee).hasToString("artistes/concerts/i0.html");
		
		URI uriConcert = RapportStructuresAndNames.getConcertRapportRelativeUri(concert);
		assertThat(uriConcert).hasToString("concerts/i0.html");
		
		URI uriTicket = RapportStructuresAndNames.getTicketImageAbsoluteUri(concert.getTicketImages().get(0));
		assertThat(uriTicket).hasToString(Control.getConcertTicketImgUri() + "/Annees1990/1990/07_Juillet/RayCharles01.jpg");
	}
}
