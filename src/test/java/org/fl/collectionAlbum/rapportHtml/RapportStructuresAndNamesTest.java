package org.fl.collectionAlbum.rapportHtml;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.LieuxDesConcerts;
import org.fl.collectionAlbumGui.CollectionAlbumGui;
import org.junit.jupiter.api.Test;

class RapportStructuresAndNamesTest {

	private static Logger logger = Logger.getLogger(RapportStructuresAndNamesTest.class.getName()) ;
	
	@Test
	void test() {
		
		Control.initControl(CollectionAlbumGui.DEFAULT_PROP_FILE);
		RapportStructuresAndNames.init() ;
		assertEquals(CollectionAlbumGui.MUSIQUE_DIRECTORY_URI + "RapportCollection/rapport/", RapportStructuresAndNames.getRapportPath().toUri().toString()) ;
		assertEquals(CollectionAlbumGui.MUSIQUE_DIRECTORY_URI + "RapportCollection/rapport_old/", RapportStructuresAndNames.getOldRapportPath().toUri().toString()) ;
		assertEquals(CollectionAlbumGui.MUSIQUE_DIRECTORY_URI + "RapportCollection/rapport/albums/", RapportStructuresAndNames.getAbsoluteAlbumDir().toUri().toString()) ;
		assertEquals(CollectionAlbumGui.MUSIQUE_DIRECTORY_URI + "RapportCollection/rapport/concerts/", RapportStructuresAndNames.getAbsoluteConcertDir().toUri().toString()) ;
		assertEquals(CollectionAlbumGui.MUSIQUE_DIRECTORY_URI + "RapportCollection/rapport/index.html", RapportStructuresAndNames.getAbsoluteHomeCollectionFile().toUri().toString()) ;
		assertEquals(CollectionAlbumGui.MUSIQUE_DIRECTORY_URI + "RapportCollection/rapport/indexConcert.html", RapportStructuresAndNames.getAbsoluteHomeConcertFile().toUri().toString()) ;
	}

	@Test
	void test2() {
		
		Control.initControl(CollectionAlbumGui.DEFAULT_PROP_FILE);
		RapportStructuresAndNames.init() ;
	
		JsonObject jArt = new JsonObject() ;
		jArt.addProperty("nom", "Evans") ;
		jArt.addProperty("prenom", "Bill") ;
		jArt.addProperty("naissance", "1929-08-16") ;
		jArt.addProperty("mort",  "1980-09-15") ;
		
		Artiste artiste= new Artiste(jArt, logger) ;
		
		URI pAlbum = RapportStructuresAndNames.getArtisteAlbumRapportRelativePath(artiste) ;
		assertEquals("artistes/albums/i0.html", pAlbum.toString()) ;

	}
	
	private static final String albumStr1 = "{ " +
			 " \"titre\": \"Portrait in jazz\"," +
			 " \"format\": {  \"cd\": 1   }, "	+
			"  \"auteurCompositeurs\": [ "		+
			"    {  "							+
			"      \"nom\": \"Evans\", "		+
			"     \"prenom\": \"Bill\", "		+
			"      \"naissance\": \"1929-08-16\"," +
			"      \"mort\": \"1980-09-15\"  "      +
			"    }   "                              +
			"  ],    "								+
			"  \"enregistrement\": [ \"1959-12-28\",  \"1959-12-28\"  ]  " +
			" } " ;

	private static final String albumStr2 = "{ " +
			 " \"titre\": \"Portrait in rock\"," +
			 " \"format\": {  \"cd\": 1   }, "	+
			"  \"auteurCompositeurs\": [ "		+
			"    {  "							+
			"      \"nom\": \"Fake\", "		+
			"     \"prenom\": \"Bill\", "		+
			"      \"naissance\": \"1929-08-16\"," +
			"      \"mort\": \"1980-09-15\"  "      +
			"    }   "                              +
			"  ],    "								+
			"  \"enregistrement\": [ \"1959-12-28\",  \"1959-12-28\"  ],  " +
			" \"notes\": [ \"Version mono\" ] "					 +
			" } " ;

	@Test
	void test3() {
		
		Control.initControl(CollectionAlbumGui.DEFAULT_PROP_FILE);
		RapportStructuresAndNames.init() ;

		JsonObject jAlbum = JsonParser.parseString(albumStr1).getAsJsonObject();

		ListeArtiste la = new ListeArtiste(logger) ;
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>() ;
		lla.add(la) ;

		Album album = new Album(jAlbum, lla, logger) ;
		Artiste bill = album.getAuteurs().get(0) ;
		album.addMusicArtfactArtistesToList(la);
		
		URI pAlbum = RapportStructuresAndNames.getArtisteAlbumRapportRelativePath(bill) ;
		assertEquals("artistes/albums/i0.html", pAlbum.toString()) ;

		URI pInfoAlbum = RapportStructuresAndNames.getAlbumRapportRelativePath(album) ;
		assertNull(pInfoAlbum) ;
		
		JsonObject jAlbum2 = JsonParser.parseString(albumStr2).getAsJsonObject();
		Album album2 = new Album(jAlbum2, lla, logger) ;
		album2.addMusicArtfactArtistesToList(la);
		Artiste fake = album2.getAuteurs().get(0) ;
		
		URI pAlbum2 = RapportStructuresAndNames.getArtisteAlbumRapportRelativePath(fake) ;
		assertEquals("artistes/albums/i1.html", pAlbum2.toString()) ;
		
		URI pInfoAlbum2 = RapportStructuresAndNames.getAlbumRapportRelativePath(album2) ;
		assertEquals("albums/i0.html", pInfoAlbum2.toString()) ;
	}
	
	private static final String concertStr1 = "{ " +
			  "\"auteurCompositeurs\": [ "			+
			                        " { "			+
			        " \"nom\": \"Bridgewater\","	+
			       "  \"prenom\": \"Dee Dee\","		+
			        " \"naissance\": \"1950-05-27\""+
			        "  }  ], "					+
			   "\"date\": \"1990-07-19\","			+
			   "\"lieu\": \"Juan-les-Pins, Alpes-Maritimes\"," +
			   "\"imageTicket\": ["					+
			       "\"/Annees1990/1990/07_Juillet/RayCharles01.jpg\"" +
			   " ]  } " ;
	@Test
	void test4() {
		
		Control.initControl(CollectionAlbumGui.DEFAULT_PROP_FILE);
		RapportStructuresAndNames.init() ;
		
		JsonObject jConcert = JsonParser.parseString(concertStr1).getAsJsonObject();
		
		ListeArtiste la = new ListeArtiste(logger) ;
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>() ;
		lla.add(la) ;
		
		LieuxDesConcerts lieuxDesConcerts = new LieuxDesConcerts() ;
		Concert concert = new Concert(jConcert, lla, lieuxDesConcerts, logger) ;
		concert.addMusicArtfactArtistesToList(la);
		List<Artiste> lDeeDee = concert.getAuteurs() ;
		Artiste deeDee = lDeeDee.get(0) ;
		
		URI uriDeedee = RapportStructuresAndNames.getArtisteConcertRapportRelativePath(deeDee) ;
		assertEquals("artistes/concerts/i0.html", uriDeedee.toString()) ;
		
		URI uriConcert = RapportStructuresAndNames.getConcertRapportRelativePath(concert) ;
		assertEquals("concerts/i0.html", uriConcert.toString()) ;
		
		URI uriTicket = RapportStructuresAndNames.getTicketImageAbsoluteUri(concert.getTicketImages().get(0)) ;
		assertEquals(Control.getCollectionProperties().getProperty("concert.ticketImgDir.name") + "/Annees1990/1990/07_Juillet/RayCharles01.jpg", uriTicket.toString()) ;
	}
}
