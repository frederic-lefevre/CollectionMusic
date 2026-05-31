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

import org.fl.collectionAlbum.mediaFile.metadata.AudioMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.AudioStreamMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.MediaFileMetadata;
import org.fl.util.file.FilesUtils;
import org.junit.jupiter.api.Test;

class Mp3AudioFileTest {

	@Test
	void shouldParseMp3File() throws URISyntaxException {
		
		Path mp3FilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/Rock%20Around%20The%20Clock.mp3");
		
		Mp3AudioFile f1 = new Mp3AudioFile(mp3FilePath);
		assertThat(f1.isValidMediaFile()).isEmpty(); // not parsed yet
		assertThat(f1.hasImbeddedPicture()).isEmpty();
		assertThat(f1.getSize()).isEmpty();
		
		f1.parseMetadata();
		assertThat(f1.getExtension()).isEqualTo("mp3");
		
		assertThat(f1.getSize()).isPresent().hasValue(5213186L);

		assertThat(f1.getFilePath().toUri()).asString().isEqualTo("file:///C:/ForTests/CollectionMusique/Rock%20Around%20The%20Clock.mp3");
		
		MediaFileMetadata metadata = f1.getMetadata();
		assertThat(metadata).isNotNull();
		
		AudioMetadata audioMetadata = f1.getAudioMetadata();
		assertThat(audioMetadata).isNotNull();
		
		AudioStreamMetadata streamInfo = audioMetadata.getAudioStreamMetadata();
		assertThat(streamInfo).isNotNull();
		
		assertThat(streamInfo.samplingRate().value()).isEqualTo(44100);
		assertThat(streamInfo.bitDepth().value()).isEqualTo(0);  // irrelevant
		assertThat(streamInfo.isLossless().value()).isFalse();
		assertThat(streamInfo.numberOfChannels().value()).isEqualTo(2);
		assertThat(streamInfo.bitRate().value()).isEqualTo(320000);
		assertThat(streamInfo.trackDuration().value()).isEqualTo(0);  // not calculated
	}
}
