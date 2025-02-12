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
import java.util.AbstractMap.SimpleEntry;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.format.Format.RangementSupportPhysique;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;
import org.fl.collectionAlbum.metrics.CollectionMetrics;
import org.fl.collectionAlbum.metrics.Metrics;
import org.fl.collectionAlbum.format.MediaSupports;
import org.fl.collectionAlbum.rapportHtml.RapportStructuresAndNames;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

class CollectionAlbumContainerTest {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
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
		
		Metrics collectionMetrics = CollectionMetrics.buildCollectionMetrics(0, albumsContainer);
		
		assertThat(collectionMetrics.getMetricTimeStamp()).isZero();
		assertThat(collectionMetrics.getMetrics()).hasSize(3)
			.contains(new SimpleEntry<>("nombreAlbum", (double)0), new SimpleEntry<>("nombreArtiste", (double)0));
		
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
	void testAlbumContainer() throws JsonMappingException, JsonProcessingException {

		RapportStructuresAndNames.renew();
		
		MediaFilesInventories.scanMediaFilePaths();
		DiscogsInventory.buildDiscogsInventory();
		
		CollectionAlbumContainer albumsContainer = CollectionAlbumContainer.getEmptyInstance();

		ObjectNode jAlbum = (ObjectNode)mapper.readTree(albumStr1);
		
		albumsContainer.addAlbum(jAlbum, Paths.get("dummyPath"));
		
		assertThat(albumsContainer.getCollectionAlbumsMusiques().getNombreAlbums()).isEqualTo(1);
		assertThat(albumsContainer.getCollectionArtistes().getNombreArtistes()).isEqualTo(1);
		assertThat(albumsContainer.getConcerts().getNombreConcerts()).isZero();
		assertThat(albumsContainer.getConcertsArtistes().getNombreArtistes()).isZero();
		
		Album album = albumsContainer.getCollectionAlbumsMusiques().getAlbums().get(0);
		
		assertThat(album.getTitre()).isEqualTo("Portrait in jazz");
		
		Artiste artiste = albumsContainer.getCollectionArtistes().getArtistes().get(0);
		
		assertThat(album.hasArtiste()).isTrue();
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
		
		assertThat(albumsContainer.getAlbumsWithNoArtiste().getAlbums()).isEmpty();
		
		assertThat(albumsContainer.pickRandomAlbums(3)).isNotNull()
			.singleElement()
			.satisfies(alb -> assertThat(alb.getTitre()).isEqualTo("Portrait in jazz"));
		
		assertThat(albumsContainer.pickRandomAlbumsViaArtiste(3)).isNotNull()
			.singleElement()
			.satisfies(alb -> assertThat(alb.getTitre()).isEqualTo("Portrait in jazz"));
		
		Metrics collectionMetrics = CollectionMetrics.buildCollectionMetrics(0, albumsContainer);
		
		assertThat(collectionMetrics.getMetricTimeStamp()).isZero();
		assertThat(collectionMetrics.getMetrics()).hasSize(12)
			.contains(
					new SimpleEntry<>("totalPhysique", (double)1),
					new SimpleEntry<>("nombreAlbum", (double)1), 
					new SimpleEntry<>("nombreArtiste", (double)1),
					new SimpleEntry<>("xnbcd", (double)1),
					new SimpleEntry<>("xnbk7", (double)0),
					new SimpleEntry<>("xnbVinyl", (double)0),
					new SimpleEntry<>("xnbdvd", (double)0)
					);
	}
}
