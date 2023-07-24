package org.fl.collectionAlbum;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.logging.Logger;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.rapportHtml.RapportStructuresAndNames;
import org.fl.collectionAlbumGui.CollectionAlbumGui;
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
		
		Control.initControl(CollectionAlbumGui.DEFAULT_PROP_FILE);
		RapportStructuresAndNames.init() ;

		CollectionAlbumContainer albumsContainer = CollectionAlbumContainer.getEmptyInstance(logger) ;

		JsonObject jAlbum = JsonParser.parseString(albumStr1).getAsJsonObject();
		
		albumsContainer.addAlbum(jAlbum);
		
		assertEquals(1, albumsContainer.getCollectionAlbumsMusiques().getNombreAlbums()) ;
		assertEquals(1, albumsContainer.getCollectionArtistes().getNombreArtistes()) ;
		assertEquals(0, albumsContainer.getConcerts().getNombreConcerts()) ;
		assertEquals(0, albumsContainer.getConcertsArtistes().getNombreArtistes()) ;
		
		Album album = albumsContainer.getCollectionAlbumsMusiques().getAlbums().get(0) ;
		
		assertEquals("Portrait in jazz", album.getTitre()) ;
		
		Artiste artiste = albumsContainer.getCollectionArtistes().getArtistes().get(0) ;
		
		assertEquals("Evans", artiste.getNom()) ;
		assertEquals("Bill", artiste.getPrenoms()) ;
		
		URI pAlbum = RapportStructuresAndNames.getArtisteAlbumRapportRelativeUri(artiste) ;
		assertEquals("artistes/albums/i0.html", pAlbum.toString()) ;
	}
}
