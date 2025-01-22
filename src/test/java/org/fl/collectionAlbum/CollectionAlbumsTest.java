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
		
		Artiste bobDylan = albumsContainer.getArtisteKnown("Dylan", "Bob");
		
		assertThat(bobDylan).isNotNull();
		assertThat(bobDylan.getAlbums().getAlbums()).hasSizeGreaterThan(80);
		assertThat(bobDylan.getConcerts().getConcerts()).hasSizeGreaterThan(5);
		assertThat(bobDylan.getDateNaissance()).isEqualTo("24 mai 1941");
		
		assertThat(albumsContainer.pickRandomAlbums(3)).isNotNull()
			.hasSize(3);
		
		// This is a singleton and it should be reset to empty
		CollectionAlbumContainer albumsContainer2 = CollectionAlbumContainer.getEmptyInstance();
		assertThat(albumsContainer2).isEqualTo(albumsContainer);
		TestUtils.assertEmptyCollection(albumsContainer);
	}
}
