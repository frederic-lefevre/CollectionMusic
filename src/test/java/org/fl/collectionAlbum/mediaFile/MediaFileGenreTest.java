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

import org.fl.collectionAlbum.mediaFile.metadata.NormalizedAudioMetadataTags;
import org.fl.util.file.FilesUtils;
import org.junit.jupiter.api.Test;

class MediaFileGenreTest {

	@Test
	void testEmptyMusicalGenres() {
		
		MediaFileGenres mediaFileGenres = new MediaFileGenres();
		
		assertThat(mediaFileGenres).isNotNull();
		
		assertThat(mediaFileGenres.getGenres()).isEmpty();
		assertThat(mediaFileGenres.getMediaFileOfGenre(null)).isNull();
		assertThat(mediaFileGenres.getMediaFileOfGenre("Jazz")).isNull();
	}
	
	@Test
	void testMusicalGenres() throws URISyntaxException {
		
		final String expectedGenre = "Classical";
		
		MediaFileGenres mediaFileGenres = new MediaFileGenres();
		assertThat(mediaFileGenres.getGenres()).isEmpty();
		
		Path flacFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/f1.flac");
		FlacAudioFile f1 = new FlacAudioFile(flacFilePath);
		String tagForGenre = f1.getMetadata().getTagForGenre();
		assertThat(tagForGenre).isEqualTo(NormalizedAudioMetadataTags.GENRE);
		assertThat(f1.getMetadata().getNormalizedTags().get(tagForGenre).value()).isEqualTo(expectedGenre);
		assertThat(f1.getAudioMetadata().getNormalizedAudioMetadataTags().genre().value()).isEqualTo(expectedGenre);	

		mediaFileGenres.registerTrack(f1);
		assertThat(mediaFileGenres.getGenres()).hasSize(1);

		assertThat(mediaFileGenres.getMediaFileOfGenre("Jazz")).isNull();
		assertThat(mediaFileGenres.getMediaFileOfGenre(expectedGenre))
			.singleElement().isEqualTo(f1);
		
		mediaFileGenres.clearMediaFileGenres();
		assertThat(mediaFileGenres.getGenres()).isEmpty();
	}
}
