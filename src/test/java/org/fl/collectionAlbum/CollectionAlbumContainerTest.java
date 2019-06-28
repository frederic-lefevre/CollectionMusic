package org.fl.collectionAlbum;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

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

}
