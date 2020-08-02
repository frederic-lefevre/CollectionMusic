package org.fl.collectionAlbum.albums;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class AlbumTest {

	private static Logger logger = Logger.getLogger(AlbumTest.class.getName()) ;

	@Test
	void testEmptyAlbum() {
		
		ListeArtiste la = new ListeArtiste(logger) ;
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>() ;
		lla.add(la) ;
		
		Album album = new Album(new JsonObject(), lla, logger) ;
		
		assertNotNull(album) ;
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
			"  \"enregistrement\": [ \"1959-12-28\",  \"1959-12-28\" ],  " +
			"  \"liensUrl\":  [ \"http://somwhere\" ] " +
			" } " ;
			  
	@Test
	void testAlbum1() {
		
		JsonObject jAlbum = JsonParser.parseString(albumStr1).getAsJsonObject();

		ListeArtiste la = new ListeArtiste(logger) ;
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>() ;
		lla.add(la) ;

		Album album = new Album(jAlbum, lla, logger) ;
		
		assertEquals("Portrait in jazz", album.getTitre()) ;
		List<String> liens = album.getUrlLinks();
		assertNotNull(liens);
		assertEquals(1, liens.size());
		assertEquals("http://somwhere", liens.get(0));
		
		Artiste bill = album.getAuteurs().get(0) ;
		assertNotNull(bill) ;
		assertEquals("Bill", bill.getPrenoms()) ;
		assertEquals("Evans", bill.getNom()) ;
		
		assertEquals(0, bill.getNbAlbum()) ;
		
		album.addMusicArtfactArtistesToList(la);
		assertEquals(1, bill.getNbAlbum()) ;
		
		assertEquals(album, bill.getAlbums().getAlbums().get(0)) ;

	}
}
