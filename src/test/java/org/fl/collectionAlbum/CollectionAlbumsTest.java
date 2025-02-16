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

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.metrics.CollectionMetrics;
import org.fl.collectionAlbum.metrics.ConcertMetrics;
import org.fl.collectionAlbum.metrics.Metrics;
import org.fl.collectionAlbumGui.ProgressInformationPanel;
import org.junit.jupiter.api.Test;

class CollectionAlbumsTest {

	@Test
	void testGlobalBuild() {
		
		CollectionAlbums collectionAlbums = new CollectionAlbums(new ArrayList<>(), new ProgressInformationPanel());
		
		CollectionAlbumContainer albumsContainer = collectionAlbums.doInBackground();
		
		assertThat(albumsContainer).isNotNull();
		
		assertThat(albumsContainer.getCollectionAlbumsMusiques()).isNotNull();
		assertThat(albumsContainer.getConcerts()).isNotNull();
		assertThat(albumsContainer.getCollectionArtistes()).isNotNull();
		assertThat(albumsContainer.getConcertsArtistes()).isNotNull();
		assertThat(albumsContainer.getCalendrierArtistes()).isNotNull();
		assertThat(albumsContainer.getStatChronoComposition()).isNotNull();
		assertThat(albumsContainer.getStatChronoEnregistrement()).isNotNull();
		
		assertThat(albumsContainer.getCollectionAlbumsMusiques().getNombreAlbums()).isGreaterThan(1700);
		assertThat(albumsContainer.getConcerts().getNombreConcerts()).isGreaterThan(180);
		assertThat(albumsContainer.getCollectionArtistes().getNombreArtistes()).isGreaterThan(700);
		assertThat(albumsContainer.getConcertsArtistes().getNombreArtistes()).isGreaterThan(145);

		assertThat(albumsContainer.getArtisteKnown("Toto", "Titi")).isNull();
		
		assertThat(albumsContainer.getAlbumsWithMixedContentNature().getAlbums()).hasSizeGreaterThan(10);
		assertThat(albumsContainer.getAlbumsWithDiscogsRelease().getAlbums()).hasSizeGreaterThan(1600);
		assertThat(albumsContainer.getAlbumsWithAudioFile().getAlbums()).hasSizeGreaterThan(1680);
		assertThat(albumsContainer.getAlbumsWithNoArtiste().getAlbums())
			.hasSizeGreaterThan(10)
			.allSatisfy(album -> assertThat(album.hasArtiste()).isFalse());
		
		Artiste bobDylan = albumsContainer.getArtisteKnown("Dylan", "Bob");
		
		assertThat(bobDylan).isNotNull();
		assertThat(bobDylan.getAlbums().getAlbums())
			.hasSizeGreaterThan(80)
			.allSatisfy(album -> assertThat(album.hasArtiste()).isTrue());
		assertThat(bobDylan.getConcerts().getConcerts()).hasSizeGreaterThan(5);
		assertThat(bobDylan.getDateNaissance()).isEqualTo("24 mai 1941");
		
		assertThat(albumsContainer.pickRandomAlbums(3)).isNotNull()
			.hasSize(3);
		
		assertThat(albumsContainer.pickRandomAlbumsViaArtiste(4)).isNotNull()
			.hasSize(4);
		
		// Test album metrics
		Metrics collectionMetrics = CollectionMetrics.buildCollectionMetrics(0, albumsContainer);
		
		assertThat(collectionMetrics.getMetricTimeStamp()).isZero();
		assertThat(collectionMetrics.getMetrics()).hasSize(12);
		
		assertThat(collectionMetrics.getMetrics().get("totalPhysique")).isNotNull().isGreaterThan(2155);
		assertThat(collectionMetrics.getMetrics().get("xnbcd")).isNotNull().isGreaterThan(1280);
		assertThat(collectionMetrics.getMetrics().get("xnbk7")).isNotNull().isGreaterThan(57);
		assertThat(collectionMetrics.getMetrics().get("xnbVinyl")).isNotNull().isGreaterThan(775);
		assertThat(collectionMetrics.getMetrics().get("xnbMiniVinyl")).isNotNull().isGreaterThan(10);
		assertThat(collectionMetrics.getMetrics().get("xnbminicd")).isNotNull().isGreaterThan(4);
		assertThat(collectionMetrics.getMetrics().get("xnbminidvd")).isNotNull().isGreaterThan(1);
		assertThat(collectionMetrics.getMetrics().get("xnbvhs")).isNotNull().isGreaterThan(7);
		assertThat(collectionMetrics.getMetrics().get("xnbdvd")).isNotNull().isGreaterThan(16);
		assertThat(collectionMetrics.getMetrics().get("xnbblueray")).isNotNull().isGreaterThan(5);
		assertThat(collectionMetrics.getMetrics().get("nombreAlbum")).isNotNull().isGreaterThan(1710);
		assertThat(collectionMetrics.getMetrics().get("nombreArtiste")).isNotNull().isGreaterThan(738);
		
		// Test concert metrics
		Metrics concertMetrics = ConcertMetrics.buildConcertMetrics(0, albumsContainer);
		assertThat(concertMetrics.getMetricTimeStamp()).isZero();
		assertThat(concertMetrics.getMetrics()).hasSize(2);
		
		assertThat(concertMetrics.getMetrics().get("totalPhysique")).isNull();
		assertThat(concertMetrics.getMetrics().get("nombreArtiste")).isNotNull().isGreaterThan(148);
		assertThat(concertMetrics.getMetrics().get("nombreConcert")).isNotNull().isGreaterThan(183);
		
		// This is a singleton and it should be reset to empty
		CollectionAlbumContainer albumsContainer2 = CollectionAlbumContainer.getEmptyInstance();
		assertThat(albumsContainer2).isEqualTo(albumsContainer);
		TestUtils.assertEmptyCollection(albumsContainer);
	}
}
