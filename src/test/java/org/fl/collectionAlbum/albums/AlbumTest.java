package org.fl.collectionAlbum.albums;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class AlbumTest {

	private static Logger logger = Logger.getLogger(AlbumTest.class.getName()) ;

	@Test
	void testEmptyAlbum() {
		
		Album album = new Album(new JsonObject(), logger) ;
		
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
			"  \"enregistrement\": [ \"1959-12-28\",  \"1959-12-28\"  ]  " +
			" } " ;
			  
	@Test
	void testAlbum1() {
		
		JsonObject jAlbum = new JsonParser().parse(albumStr1).getAsJsonObject();

		Album album = new Album(jAlbum, logger) ;
		
		assertEquals("Portrait in jazz", album.getTitre()) ;
		
//		assertEquals(1, album.getAuteurs().get(0).getNbAlbum()) ;

	}
}
