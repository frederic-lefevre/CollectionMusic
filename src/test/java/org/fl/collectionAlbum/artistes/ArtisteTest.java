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

package org.fl.collectionAlbum.artistes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

class ArtisteTest {
	
	@Test
	void test() {
		
		JsonObject jArt = new JsonObject() ;
		jArt.addProperty("nom", "Evans") ;
		jArt.addProperty("prenom", "Bill") ;
		jArt.addProperty("naissance", "1929-08-16") ;
		jArt.addProperty("mort",  "1980-09-15") ;
		
		Artiste artiste= new Artiste(jArt) ;
		assertEquals("Evans", artiste.getNom()) ;
		assertEquals("Bill", artiste.getPrenoms()) ;
		
		assertEquals(0, artiste.getNbAlbum());
		assertEquals(0, artiste.getNbConcert());
		
		assertNull(artiste.getInstruments()) ;
		
		assertEquals(0, artiste.getAlbums().getNombreAlbums()) ;
		assertEquals(0, artiste.getConcerts().getNombreConcerts()) ;
		
		assertEquals(0, artiste.getAlbumsFormat().getPoids()) ;
	}

}
