package org.fl.collectionAlbum;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

import org.fl.collectionAlbum.rapportHtml.RapportStructuresAndNames;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class CollectionAlbumContainerTest {

	private static final Logger logger = Logger.getLogger(CollectionAlbumContainerTest.class.getName()) ;
	
	@Test
	void testEmptyContainer() {
		
		CollectionAlbumContainer albumsContainer = CollectionAlbumContainer.getEmptyInstance(logger) ;
		
		assertEquals(0, albumsContainer.getCollectionAlbumsMusiques().getNombreAlbums()) ;
		assertEquals(0, albumsContainer.getConcerts().getNombreConcerts()) ;
		assertEquals(0, albumsContainer.getCollectionArtistes().getNombreArtistes()) ;
		assertEquals(0, albumsContainer.getConcertsArtistes().getNombreArtistes()) ;
		assertNotNull(albumsContainer.getCalendrierArtistes()) ;
		assertNull(albumsContainer.getArtisteKnown("Toto", "Titi")) ;
		assertNotNull(albumsContainer.getStatChronoComposition()) ;
		assertNotNull(albumsContainer.getStatChronoEnregistrement()) ;
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
	void testAlbumContainer() {
		
		Control.initControl("file:///C:/FredericPersonnel/musique/RapportCollection/albumCollection.properties" );
		RapportStructuresAndNames.init() ;

		CollectionAlbumContainer albumsContainer = CollectionAlbumContainer.getEmptyInstance(logger) ;

		JsonObject jAlbum = new JsonParser().parse(albumStr1).getAsJsonObject();
		
		albumsContainer.addAlbum(jAlbum);
		
		assertEquals(1, albumsContainer.getCollectionArtistes().getNombreArtistes()) ;
	}
}
