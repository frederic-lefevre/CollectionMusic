package org.fl.collectionAlbum.artistes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

class ArtisteTest {

	private static Logger logger = Logger.getLogger(ArtisteTest.class.getName()) ;
	
	@Test
	void test() {
		
		JsonObject jArt = new JsonObject() ;
		jArt.addProperty("nom", "Evans") ;
		jArt.addProperty("prenom", "Bill") ;
		jArt.addProperty("naissance", "1929-08-16") ;
		jArt.addProperty("mort",  "1980-09-15") ;
		
		Artiste artiste= new Artiste(jArt, logger) ;
		assertEquals("Evans", artiste.getNom()) ;
		assertEquals("Bill", artiste.getPrenoms()) ;
	}

}
