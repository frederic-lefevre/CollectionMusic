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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.mediaFile.metadata.AudioStreamMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.MediaFileMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.MetadataElement;
import org.fl.util.FilterCounter;
import org.fl.util.FilterCounter.LogRecordCounter;
import org.fl.util.file.FilesUtils;
import org.junit.jupiter.api.Test;

class MediaFileBuilderTest {

	@Test
	void shouldBuildFile() throws URISyntaxException {
		
		Path flacFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/f1.flac");
		
		MediaFile mediaFile = MediaFileBuilder.builder(ContentNature.AUDIO, flacFilePath, "flac").build();
		assertThat(mediaFile).isNotNull();
		assertThat(mediaFile.getFilePath()).isEqualTo(flacFilePath);
		assertThat(mediaFile.getExtension()).isEqualTo("flac");
		assertThat(mediaFile).isInstanceOf(FlacAudioFile.class);
		
		MediaFileMetadata metadata = mediaFile.getMetadata();
		assertThat(metadata).isNotNull();
		
		Map<String, MetadataElement<?>> streamMetadata =  metadata.getStreamMetadata();
		assertThat(streamMetadata).isNotNull();
		
		MetadataElement<?> bitDepthMetadataElement = streamMetadata.get(AudioStreamMetadata.BIT_DEPTH);
		assertThat(bitDepthMetadataElement).isNotNull();
		assertThat(bitDepthMetadataElement.name()).isEqualTo(AudioStreamMetadata.BIT_DEPTH);
		assertThat(bitDepthMetadataElement.value())
			.isInstanceOf(Integer.class)
			.isEqualTo(16);
	}
	
	@Test
	void shouldBuildFile2() throws URISyntaxException {
		
		Path flacFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/f1.flac");
		
		MediaFile mediaFile = MediaFileBuilder.builder(ContentNature.AUDIO, flacFilePath, "FLAC").build();
		assertThat(mediaFile).isNotNull();
		assertThat(mediaFile.getFilePath()).isEqualTo(flacFilePath);
		assertThat(mediaFile.getExtension()).isEqualTo("flac");
		assertThat(mediaFile).isInstanceOf(FlacAudioFile.class);
	}
	
	@Test
	void shouldNotBuildFile() throws URISyntaxException {
		
		Path flacFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/PortraitInJazz.json");
		
		MediaFile mediaFile = MediaFileBuilder.builder(ContentNature.AUDIO, flacFilePath, "json").build();
		assertThat(mediaFile).isNull();
	}
	
	@Test
	void shouldBuildFileWithWrongExtension() throws URISyntaxException {
		
		Path flacFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/PortraitInJazz.json");
		
		MediaFile mediaFile = MediaFileBuilder.builder(ContentNature.AUDIO, flacFilePath, "flac").build();
		assertThat(mediaFile).isNotNull();  // No checking is made on the extension parameter versus the extension of the file path
		assertThat(mediaFile.getFilePath()).isEqualTo(flacFilePath);
		assertThat(mediaFile.getExtension()).isEqualTo("flac");
		assertThat(mediaFile).isInstanceOf(FlacAudioFile.class);
		
		LogRecordCounter flacFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(FlacAudioFile.class.getName()));
		
		MediaFileMetadata metadata = mediaFile.getMetadata();
		assertThat(metadata).isNull();
		
		assertThat(flacFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(flacFilterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
	}
	
	@Test
	void shouldBuildNotVideoFile() throws URISyntaxException {
		
		Path flacFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/f1.flac");
		
		MediaFile mediaFile = MediaFileBuilder.builder(ContentNature.VIDEO, flacFilePath, "flac").build();
		assertThat(mediaFile).isNull();
	}
	
	@Test
	void shouldBuildVideoFileWithWrongExtension() throws URISyntaxException {
		
		Path flacFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/f1.flac");
		
		MediaFile mediaFile = MediaFileBuilder.builder(ContentNature.VIDEO, flacFilePath, "mkv").build();
		assertThat(mediaFile).isNotNull();
		assertThat(mediaFile.getFilePath()).isEqualTo(flacFilePath);
		assertThat(mediaFile.getExtension()).isEqualTo("mkv");
		assertThat(mediaFile).isInstanceOf(VideoFile.class);
	}
}
