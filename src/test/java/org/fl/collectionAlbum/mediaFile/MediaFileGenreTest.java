/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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

package org.fl.collectionAlbum.mediaFile;

import static org.assertj.core.api.Assertions.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Set;

import org.fl.util.file.FilesUtils;
import org.junit.jupiter.api.Test;

class MediaFileGenreTest {

	@Test
	void testEmptyMusicalGenres() {
		
		MediaFileGenres mediaFileGenres = new MediaFileGenres();
		
		assertThat(mediaFileGenres).isNotNull();
		
		assertThat(mediaFileGenres.getGenres()).isEmpty();
		assertThat(mediaFileGenres.getGenresParameterList()).isEmpty();
		assertThat(mediaFileGenres.getGenreParameters(null)).isNull();
		assertThat(mediaFileGenres.getGenreParameters("Jazz")).isNull();
	}
	
	@Test
	void testMusicalGenres() throws URISyntaxException {
		
		final String expectedGenre = "Classical";
		
		MediaFileGenres mediaFileGenres = new MediaFileGenres();
		assertThat(mediaFileGenres.getGenres()).isEmpty();
		
		Path flacFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/f1.flac");
		FlacAudioFile f1 = new FlacAudioFile(flacFilePath);
		String genre = f1.getMetadata().getGenre();
		assertThat(genre).isEqualTo(expectedGenre);
		assertThat(f1.getAudioMetadata().getNormalizedAudioMetadataTags().genre().value()).isEqualTo(expectedGenre);	

		mediaFileGenres.registerTrack(f1);
		assertThat(mediaFileGenres.getGenres()).hasSize(1);

		assertThat(mediaFileGenres.getGenreParameters("Jazz")).isNull();
		assertThat(mediaFileGenres.getGenreParameters(expectedGenre))
			.isNotNull()
			.satisfies(genreParameters -> {
				assertThat(genreParameters.genre()).isEqualTo(expectedGenre);
				assertThat(genreParameters.iNormalizedGenre()).isFalse();
				assertThat(genreParameters.mediaFiles()).singleElement().isEqualTo(f1);
				assertThat(genreParameters.duration()).isEqualTo(24240);
			});
		
		mediaFileGenres.clearMediaFileGenres();
		assertThat(mediaFileGenres.getGenres()).isEmpty();
	}
	
	@Test
	void testMusicalGenres2() throws URISyntaxException {
		
		final String expectedGenre = "Classical";
		
		MediaFileGenres mediaFileGenres = new MediaFileGenres(Set.of(expectedGenre));
		
		Path flacFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/f1.flac");
		FlacAudioFile f1 = new FlacAudioFile(flacFilePath);
		mediaFileGenres.registerTrack(f1);
		assertThat(mediaFileGenres.getGenres()).singleElement().isEqualTo(expectedGenre);

		assertThat(mediaFileGenres.getGenreParameters(expectedGenre))
			.isNotNull()
			.satisfies(genreParameters -> {
				assertThat(genreParameters.genre()).isEqualTo(expectedGenre);
				assertThat(genreParameters.iNormalizedGenre()).isTrue();
				assertThat(genreParameters.mediaFiles()).singleElement().isEqualTo(f1);
				assertThat(genreParameters.duration()).isEqualTo(24240);
			});
	}
}
