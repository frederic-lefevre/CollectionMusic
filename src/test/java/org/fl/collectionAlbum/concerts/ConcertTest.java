package org.fl.collectionAlbum.concerts;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class ConcertTest {

	private static Logger logger = Logger.getLogger(ConcertTest.class.getName()) ;
	
	@Test
	void test() {
		
		ListeArtiste la = new ListeArtiste(logger) ;
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>() ;
		lla.add(la) ;
		
		LieuxDesConcerts lieuxDesConcerts = new LieuxDesConcerts() ;
		Concert concert = new Concert(new JsonObject(), lla, lieuxDesConcerts, logger) ;
		
		assertNotNull(concert);
	}
	
	private static final String concertStr1 = """
			{ 
			  "auteurCompositeurs": [{ 
			     "nom": "Bridgewater",
			     "prenom": "Dee Dee",
			     "naissance": "1950-05-27"
			   }],
			   "date": "1990-07-01",
			   "lieu": "Juan-les-Pins, Alpes-Maritimes",
			   "imageTicket": ["/Annees1990/1990/07_Juillet/RayCharles01.jpg"] 
			 } 
		""" ;

	@Test
	void testConcert1() {
		
		JsonObject jConcert = JsonParser.parseString(concertStr1).getAsJsonObject();

		ListeArtiste la = new ListeArtiste(logger) ;
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>() ;
		lla.add(la) ;
		LieuxDesConcerts lieuxDesConcerts = new LieuxDesConcerts() ;
		
		Concert concert = new Concert(jConcert, lla, lieuxDesConcerts, logger) ;
		
		LieuConcert juan = concert.getLieuConcert() ;
		assertEquals("Juan-les-Pins, Alpes-Maritimes", juan.getLieu()) ;
		
		assertEquals(0, juan.getNombreConcert()) ;
		assertEquals(1, concert.getTicketImages().size()) ;
		assertEquals("/Annees1990/1990/07_Juillet/RayCharles01.jpg", concert.getTicketImages().get(0)) ;
		
		juan.addConcert(concert);
		assertEquals(1, juan.getNombreConcert()) ;
		
		List<Artiste> lDeeDee = concert.getAuteurs() ;
		
		assertEquals(1, lDeeDee.size()) ;
		
		Artiste deeDee = lDeeDee.get(0) ;
		assertEquals("Dee Dee", deeDee.getPrenoms()) ;
		assertEquals("Bridgewater", deeDee.getNom()) ;
		assertEquals(0, deeDee.getNbAlbum()) ;
		assertEquals(0, deeDee.getNbConcert()) ;
		
		concert.addMusicArtfactArtistesToList(la);
		assertEquals(1, deeDee.getNbConcert()) ;
		
		assertEquals(concert, deeDee.getConcerts().getConcerts().get(0)) ;
		
	}
}
