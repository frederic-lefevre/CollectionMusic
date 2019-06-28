package org.fl.collectionAlbum.albums;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

class AlbumTest {

	private static Logger logger = Logger.getLogger(AlbumTest.class.getName()) ;

	@Test
	void testEmptyAlbum() {
		
		Album album = new Album(new JsonObject(), logger) ;
		
		assertNotNull(album) ;
	}

}
