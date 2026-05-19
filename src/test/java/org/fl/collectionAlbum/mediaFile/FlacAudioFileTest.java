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

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		
		f1.parseMetadata();
		assertThat(flacFilterCounter.getLogRecordCount()).isZero();
		flacFilterCounter.stopLogCountAndFilter();
		
		assertThat(f1.isValidMediaFile).isTrue();
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
		
		assertThat(f1.isValidMediaFile).isFalse();
	}
	
	@Test
	void shouldLogWarningOnFlacFileWithID3header() throws URISyntaxException {
		
		Path flacFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/f_withID3_Header.flac");

		LogRecordCounter flacFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(FlacAudioFile.class.getName()));	
		
		FlacAudioFile f1 = new FlacAudioFile(flacFilePath, "flac");
		f1.parseMetadata();
		
		assertThat(flacFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(flacFilterCounter.getLogRecordCount(Level.WARNING)).isEqualTo(1);
		flacFilterCounter.stopLogCountAndFilter();
		
		assertThat(f1.isValidMediaFile).isTrue();
	}
}
