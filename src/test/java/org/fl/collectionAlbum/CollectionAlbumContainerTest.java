/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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

import static org.assertj.core.api.Assertions.*;

import java.net.URI;
import java.nio.file.Paths;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.format.Format.RangementSupportPhysique;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;
import org.fl.collectionAlbum.format.MediaSupports;
import org.fl.collectionAlbum.rapportHtml.RapportStructuresAndNames;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class CollectionAlbumContainerTest {
	
	@Test
	void testEmptyContainer() {
		
		CollectionAlbumContainer albumsContainer = CollectionAlbumContainer.getEmptyInstance();
		
		TestUtils.assertEmptyCollection(albumsContainer);

		assertThat(albumsContainer.getArtisteKnown("Toto", "Titi")).isNull();
		
		assertThat(MediaSupports.values())
			.allSatisfy(mediaSupport -> assertThat(albumsContainer.getAlbumsWithMediaSupport(mediaSupport).getAlbums()).isEmpty());

		assertThat(RangementSupportPhysique.values())
			.allSatisfy(rangement -> assertThat(albumsContainer.getRangementAlbums(rangement).getAlbums()).isEmpty());
		
		assertThat(albumsContainer.getAlbumsWithMixedContentNature().getAlbums()).isEmpty();
		assertThat(albumsContainer.getAlbumsMissingDiscogsRelease().getAlbums()).isEmpty();
		assertThat(albumsContainer.getAlbumsWithDiscogsRelease().getAlbums()).isEmpty();
		
		assertThat(ContentNature.values())
			.allSatisfy(contentNature -> assertThat(albumsContainer.getAlbumsWithOnlyContentNature(contentNature).getAlbums()).isEmpty());
		
		// This is a singleton and it should be reset to empty
		CollectionAlbumContainer albumsContainer2 = CollectionAlbumContainer.getEmptyInstance();
		assertThat(albumsContainer2).isEqualTo(albumsContainer);
		TestUtils.assertEmptyCollection(albumsContainer);
	}
	
	private static final String albumStr1 = """
			{ 
			  "titre": "Portrait in jazz",
			  "format": {  "cd": 1,
				"audioFiles" : [{
				    "bitDepth": 16 , 
				    "samplingRate" : 44.1, 
				    "source" : "MOFI Fidelity Sound Lab", 
				    "type" : "FLAC",
                    "location": ["E:\\\\Musique\\\\e\\\\Bill Evans\\\\Portrait In Jazz"] 
                   }]
				 },
			  "auteurCompositeurs": [ 
			    {  
			      "nom": "Evans", 
			     "prenom": "Bill", 
			     "naissance": "1929-08-16",
			      "mort": "1980-09-15" 
			    }  
			  ],    
			  "enregistrement": [ "1959-12-28",  "1959-12-28"  ]
			 } 
			""";
	
	@Test
	void testAlbumContainer() {
		
		RapportStructuresAndNames.init();

		MediaFilesInventories.scanMediaFilePaths();
		DiscogsInventory.buildDiscogsInventory();
		
		CollectionAlbumContainer albumsContainer = CollectionAlbumContainer.getEmptyInstance();

		JsonObject jAlbum = JsonParser.parseString(albumStr1).getAsJsonObject();
		
		albumsContainer.addAlbum(jAlbum, Paths.get("dummyPath"));
		
		assertThat(albumsContainer.getCollectionAlbumsMusiques().getNombreAlbums()).isEqualTo(1);
		assertThat(albumsContainer.getCollectionArtistes().getNombreArtistes()).isEqualTo(1);
		assertThat(albumsContainer.getConcerts().getNombreConcerts()).isZero();
		assertThat(albumsContainer.getConcertsArtistes().getNombreArtistes()).isZero();
		
		Album album = albumsContainer.getCollectionAlbumsMusiques().getAlbums().get(0);
		
		assertThat(album.getTitre()).isEqualTo("Portrait in jazz");
		
		Artiste artiste = albumsContainer.getCollectionArtistes().getArtistes().get(0);
		
		assertThat(artiste.getNom()).isEqualTo("Evans");
		assertThat(artiste.getPrenoms()).isEqualTo("Bill");
		
		URI pAlbum = RapportStructuresAndNames.getArtisteAlbumRapportRelativeUri(artiste);
		assertThat(pAlbum).hasToString("artistes/albums/i0.html");
		
		assertThat(album.getAllMediaFiles()).singleElement()
			.satisfies(mediaFile -> assertThat(mediaFile.getMediaFilePaths()).singleElement()
					.satisfies(mediaFilePath -> assertThat(mediaFilePath.getAlbumSet()).singleElement()
							.satisfies(album1 -> assertThat(album1).isEqualTo(album))));

		assertThat(MediaSupports.values())
			.allSatisfy(mediaSupport -> {
				if (mediaSupport == MediaSupports.CD) {
					assertThat(albumsContainer.getAlbumsWithMediaSupport(mediaSupport).getAlbums()).singleElement()
						.satisfies(album1 -> assertThat(album1).isEqualTo(album)); 
				} else {
					assertThat(albumsContainer.getAlbumsWithMediaSupport(mediaSupport).getAlbums()).isEmpty();
				}
			});

		assertThat(RangementSupportPhysique.values())
			.allSatisfy(rangement -> {
				if (rangement == RangementSupportPhysique.RangementCD) {
					assertThat(albumsContainer.getRangementAlbums(rangement).getAlbums()).singleElement()
						.satisfies(album1 -> assertThat(album1).isEqualTo(album));
				} else {
					assertThat(albumsContainer.getRangementAlbums(rangement).getAlbums()).isEmpty(); 
				}		
			});
		
		assertThat(albumsContainer.getAlbumsWithMixedContentNature().getAlbums()).isEmpty();
		assertThat(albumsContainer.getAlbumsMissingDiscogsRelease().getAlbums()).isNotEmpty().singleElement()
			.satisfies(album1 -> assertThat(album1).isEqualTo(album));
		assertThat(albumsContainer.getAlbumsWithDiscogsRelease().getAlbums()).isEmpty();
		
		assertThat(ContentNature.values())
			.allSatisfy(contentNature -> {
				if (contentNature == ContentNature.AUDIO) {
					assertThat(albumsContainer.getAlbumsWithOnlyContentNature(contentNature).getAlbums()).containsExactly(album);
				} else {
					assertThat(albumsContainer.getAlbumsWithOnlyContentNature(contentNature).getAlbums()).isEmpty(); 
				}			
		});
	}
}
