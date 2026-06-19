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

import org.fl.util.file.FilesUtils;
import org.junit.jupiter.api.Test;

public class MediaStreamPatternsTest {

	@Test
	void testEmptyStreamPatterns() {
		
		MediaStreamPatterns mediaStreamPatterns = new MediaStreamPatterns();
		
		assertThat(mediaStreamPatterns).isNotNull();
		assertThat(mediaStreamPatterns.getMediaStreamPatternList()).isEmpty();
	}
	
	@Test
	void testStreamPatterns() throws URISyntaxException {
		
		MediaStreamPatterns mediaStreamPatterns = new MediaStreamPatterns();
		
		Path flacFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/f1.flac");
		FlacAudioFile f1 = new FlacAudioFile(flacFilePath);
		
		assertThat(mediaStreamPatterns).isNotNull();
		assertThat(mediaStreamPatterns.getMediaStreamPatternList()).isEmpty();
		
		mediaStreamPatterns.registerTrack(f1);
		
		assertThat(mediaStreamPatterns.getMediaStreamPatternList())
			.singleElement()
			.satisfies(mediaStreamPattern -> {
				assertThat(mediaStreamPattern.descriptionKey()).isEqualTo(f1.getMediaStreamPattern());
				assertThat(mediaStreamPattern.mediaFileList())
					.singleElement()
					.isEqualTo(f1);
			});
	}
}
