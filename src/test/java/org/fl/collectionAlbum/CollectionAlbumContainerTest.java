package org.fl.collectionAlbum;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

class CollectionAlbumContainerTest {

	private static final Logger logger = Logger.getLogger(CollectionAlbumContainerTest.class.getName()) ;
	
	@Test
	void test() {
		
		CollectionAlbumContainer albumsContainer = CollectionAlbumContainer.getEmptyInstance(logger) ;
		
		assertEquals(0, albumsContainer.getCollectionAlbumsMusiques().getNombreAlbums()) ;
		assertEquals(0, albumsContainer.getConcerts().getNombreConcerts()) ;
	}

}
