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

package org.fl.collectionAlbum;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.rapportHtml.RapportStructuresAndNames;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class CollectionAlbumContainerTest {
	
	@Test
	void testEmptyContainer() {
		
		CollectionAlbumContainer albumsContainer = CollectionAlbumContainer.getEmptyInstance() ;
		
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
		
		Control.initControl();
		RapportStructuresAndNames.init() ;

		CollectionAlbumContainer albumsContainer = CollectionAlbumContainer.getEmptyInstance() ;

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
