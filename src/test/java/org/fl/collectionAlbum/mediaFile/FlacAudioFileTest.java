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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.mediaFile.metadata.AudioMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.AudioStreamMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.MediaFileMetadata;
import org.fl.util.FilterCounter;
import org.fl.util.FilterCounter.LogRecordCounter;
import org.fl.util.file.FilesUtils;
import org.junit.jupiter.api.Test;

class FlacAudioFileTest {

	@Test
	void shouldParseFlacFile() throws URISyntaxException {
		
		Path flacFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/f1.flac");
		
		LogRecordCounter flacFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(FlacAudioFile.class.getName()));	
		
		FlacAudioFile f1 = new FlacAudioFile(flacFilePath, "flac");
		assertThat(f1.isValidMediaFile()).isEmpty(); // not parsed yet
		assertThat(f1.hasImbeddedPicture()).isEmpty();
		assertThat(f1.getSize()).isEmpty();
		
		f1.parseMetadata();
		assertThat(flacFilterCounter.getLogRecordCount()).isZero();
		flacFilterCounter.stopLogCountAndFilter();
		
		assertThat(f1.isValidMediaFile()).isPresent().hasValue(true);
		assertThat(f1.getExtension()).isEqualTo("flac");
		assertThat(f1.hasImbeddedPicture()).isPresent().hasValue(false);
		assertThat(f1.getSize()).isPresent().hasValue(873559L);

		assertThat(f1.getFilePath().toUri()).asString().isEqualTo("file:///C:/ForTests/CollectionMusique/f1.flac");
		
		MediaFileMetadata metadata = f1.getMetadata();
		assertThat(metadata).isNotNull();
		
		AudioMetadata audioMetadata = f1.getAudioMetadata();
		assertThat(audioMetadata).isNotNull();
		
		AudioStreamMetadata streamInfo = audioMetadata.getAudioStreamMetadata();
		assertThat(streamInfo).isNotNull();
		
		assertThat(streamInfo.samplingRate()).isEqualTo(44100);
		assertThat(streamInfo.bitDepth()).isEqualTo(16);
		assertThat(streamInfo.isLossless()).isTrue();
		assertThat(streamInfo.numberOfChannels()).isEqualTo(2);
		assertThat(streamInfo.bitRate()).isEqualTo(16*44100);
		assertThat(streamInfo.trackDuration()).isEqualTo(24240);
	}
	
	@Test
	void shouldLogErrorOnBadFlacFile() throws URISyntaxException {
		
		Path flacFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/f1_bad.flac");

		LogRecordCounter flacFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(FlacAudioFile.class.getName()));	
		
		FlacAudioFile f1 = new FlacAudioFile(flacFilePath, "flac");
		f1.parseMetadata();
		
		assertThat(flacFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(flacFilterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
		flacFilterCounter.stopLogCountAndFilter();
		
		assertThat(f1.isValidMediaFile()).isPresent().hasValue(false);
	}
	
	@Test
	void shouldLogWarningOnFlacFileWithID3header() throws URISyntaxException {
		
		Path flacFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/f_withID3_Header.flac");

		LogRecordCounter flacFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(FlacAudioFile.class.getName()));
		
		FlacAudioFile f1 = new FlacAudioFile(flacFilePath, "flac");
		MediaFileMetadata metadata = f1.getMetadata();
		assertThat(metadata).isNotNull();

		assertThat(flacFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(flacFilterCounter.getLogRecordCount(Level.WARNING)).isEqualTo(1);
		flacFilterCounter.stopLogCountAndFilter();
		
		assertThat(f1.isValidMediaFile()).isPresent().hasValue(true);
			
		AudioMetadata audioMetadata = f1.getAudioMetadata();
		assertThat(audioMetadata).isNotNull();
		
		AudioStreamMetadata streamInfo = audioMetadata.getAudioStreamMetadata();
		assertThat(streamInfo).isNotNull();
		
		assertThat(streamInfo.samplingRate()).isEqualTo(44100);
		assertThat(streamInfo.bitDepth()).isEqualTo(16);
		assertThat(streamInfo.isLossless()).isTrue();
		assertThat(streamInfo.numberOfChannels()).isEqualTo(2);
		assertThat(streamInfo.bitRate()).isEqualTo(16*44100);
		assertThat(streamInfo.trackDuration()).isEqualTo(506440);
	}
}
