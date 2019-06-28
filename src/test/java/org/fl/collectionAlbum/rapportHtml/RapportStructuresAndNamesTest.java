package org.fl.collectionAlbum.rapportHtml;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.logging.Logger;

import com.google.gson.JsonObject;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.artistes.Artiste;
import org.junit.jupiter.api.Test;

class RapportStructuresAndNamesTest {

	private static Logger logger = Logger.getLogger(RapportStructuresAndNamesTest.class.getName()) ;
	
	@Test
	void test() {
		
		Control.initControl("file:///C:/FredericPersonnel/musique/RapportCollection/albumCollection.properties" );
		RapportStructuresAndNames.init() ;
		assertEquals("C:\\FredericPersonnel\\musique\\RapportCollection\\rapport", RapportStructuresAndNames.getRapportPath().toString()) ;
		assertEquals("C:\\FredericPersonnel\\musique\\RapportCollection\\rapport_old", RapportStructuresAndNames.getOldRapportPath().toString()) ;
		assertEquals("C:\\FredericPersonnel\\musique\\RapportCollection\\rapport\\albums", RapportStructuresAndNames.getAbsoluteAlbumDir().toString()) ;
		assertEquals("C:\\FredericPersonnel\\musique\\RapportCollection\\rapport\\concerts", RapportStructuresAndNames.getAbsoluteConcertDir().toString()) ;
		assertEquals("C:\\FredericPersonnel\\musique\\RapportCollection\\rapport\\artistes", RapportStructuresAndNames.getAbsoluteArtisteDir().toString()) ;
		assertEquals("C:\\FredericPersonnel\\musique\\RapportCollection\\rapport\\index.html", RapportStructuresAndNames.getAbsoluteHomeCollectionFile().toString()) ;
		assertEquals("C:\\FredericPersonnel\\musique\\RapportCollection\\rapport\\indexConcert.html", RapportStructuresAndNames.getAbsoluteHomeConcertFile().toString()) ;
	}

	@Test
	void test2() {
		
		Control.initControl("file:///C:/FredericPersonnel/musique/RapportCollection/albumCollection.properties" );
		RapportStructuresAndNames.init() ;
	
		JsonObject jArt = new JsonObject() ;
		jArt.addProperty("nom", "Evans") ;
		jArt.addProperty("prenom", "Bill") ;
		jArt.addProperty("naissance", "1929-08-16") ;
		jArt.addProperty("mort",  "1980-09-15") ;
		
		Artiste artiste= new Artiste(jArt, logger) ;
		
		URI pAlbum = RapportStructuresAndNames.getArtisteAlbumRapportRelativePath(artiste) ;
		assertNull(pAlbum) ;

	}
}
