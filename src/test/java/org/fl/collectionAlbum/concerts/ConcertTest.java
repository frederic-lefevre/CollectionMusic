/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.fl.collectionAlbum.concerts;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class ConcertTest {
	
	@Test
	void test() {
		
		ListeArtiste la = new ListeArtiste() ;
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>() ;
		lla.add(la) ;
		
		LieuxDesConcerts lieuxDesConcerts = new LieuxDesConcerts() ;
		Concert concert = new Concert(new JsonObject(), lla, lieuxDesConcerts) ;
		
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

		ListeArtiste la = new ListeArtiste() ;
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>() ;
		lla.add(la) ;
		LieuxDesConcerts lieuxDesConcerts = new LieuxDesConcerts() ;
		
		Concert concert = new Concert(jConcert, lla, lieuxDesConcerts) ;
		
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
