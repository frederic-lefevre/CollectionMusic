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
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.mediaFile.metadata.AudioMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.AudioStreamMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.MediaFileMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.MetadataElement;
import org.fl.collectionAlbum.mediaFile.metadata.Mp3Header;
import org.fl.collectionAlbum.mediaFile.metadata.NormalizedAudioMetadataTags;
import org.fl.util.FilterCounter;
import org.fl.util.FilterCounter.LogRecordCounter;
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
		assertThat(f1.getFilePath().toUri()).asString().isEqualTo("file:///C:/ForTests/CollectionMusique/Rock%20Around%20The%20Clock.mp3");
		assertThat(f1.getExtension()).isEqualTo("mp3");
		
		MediaFileMetadata metadata = f1.getMetadata();
		assertThat(metadata).isNotNull();
		assertThat(f1.getSize()).isPresent().hasValue(5213186L);
		assertThat(f1.isValidMediaFile()).isPresent().hasValue(true);
		
		AudioMetadata audioMetadata = f1.getAudioMetadata();
		assertThat(audioMetadata).isNotNull();
		
		AudioStreamMetadata streamInfo = audioMetadata.getAudioStreamMetadata();
		assertThat(streamInfo).isNotNull();
		
		assertThat(streamInfo.samplingRate().value()).isEqualTo(44100);
		assertThat(streamInfo.bitDepth().value()).isEqualTo(0);  // irrelevant
		assertThat(streamInfo.isLossless().value()).isFalse();
		assertThat(streamInfo.numberOfChannels().value()).isEqualTo(2);
		assertThat(streamInfo.bitRate().value()).isEqualTo(320000);
		assertThat(streamInfo.trackDuration().value()).isNull();  // not calculated
		assertThat(f1.hasImbeddedPicture()).hasValue(false);
		
		assertThat(metadata.getFormatSpecificMetadata()).isNotNull().containsExactlyInAnyOrderEntriesOf(
				Map.of("Version", new MetadataElement<>("Version", "MPEG version 1"), "Layer", new MetadataElement<>("Layer", "Layer III"))
				);
		
		NormalizedAudioMetadataTags normalizedAudioTags = audioMetadata.getNormalizedAudioMetadataTags();
		assertThat(normalizedAudioTags).isNotNull();
		assertThat(normalizedAudioTags.artist().value()).isEqualTo("Bill Haley & His Comets");
		assertThat(normalizedAudioTags.albumTitle().value()).isEqualTo("Rock Around The Clock");
		assertThat(normalizedAudioTags.trackNumber().value()).isEqualTo(1);
		assertThat(normalizedAudioTags.trackTitle().value()).isEqualTo("(We're Gonna) Rock Around The Clock");
		assertThat(normalizedAudioTags.date().value()).isEqualTo("2004");
		assertThat(normalizedAudioTags.composer().value()).isEqualTo("");
		assertThat(normalizedAudioTags.genre().value()).isEqualTo("Rock");	
		assertThat(normalizedAudioTags.albumArtist().value()).isEqualTo("Bill Haley");
		
		assertThat(audioMetadata.getAdditionalTags()).isEmpty();
	}
	
	@Test
	void shouldParseMp3File2() throws URISyntaxException {
		
		Path mp3FilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/DrinkingWineSpoDeeODee.mp3");
		
		Mp3AudioFile f1 = new Mp3AudioFile(mp3FilePath);
		assertThat(f1.isValidMediaFile()).isEmpty(); // not parsed yet
		assertThat(f1.hasImbeddedPicture()).isEmpty();
		assertThat(f1.getSize()).isEmpty();
		assertThat(f1.getFilePath().toUri()).asString().isEqualTo("file:///C:/ForTests/CollectionMusique/DrinkingWineSpoDeeODee.mp3");
		assertThat(f1.getExtension()).isEqualTo("mp3");
		
		MediaFileMetadata metadata = f1.getMetadata();
		assertThat(metadata).isNotNull();
		assertThat(f1.getSize()).isPresent().hasValue(6251408L);
		assertThat(f1.isValidMediaFile()).isPresent().hasValue(true);
		
		AudioMetadata audioMetadata = f1.getAudioMetadata();
		assertThat(audioMetadata).isNotNull();
		
		AudioStreamMetadata streamInfo = audioMetadata.getAudioStreamMetadata();
		assertThat(streamInfo).isNotNull();
		
		assertThat(streamInfo.samplingRate().value()).isEqualTo(44100);
		assertThat(streamInfo.bitDepth().value()).isEqualTo(0);  // irrelevant
		assertThat(streamInfo.isLossless().value()).isFalse();
		assertThat(streamInfo.numberOfChannels().value()).isEqualTo(2);
		assertThat(streamInfo.bitRate().value()).isEqualTo(320000);
		assertThat(streamInfo.trackDuration().value()).isNull();  // not calculated
		assertThat(f1.hasImbeddedPicture()).hasValue(true);
		
		assertThat(metadata.getFormatSpecificMetadata()).isNotNull().containsExactlyInAnyOrderEntriesOf(
				Map.of("Version", new MetadataElement<>("Version", "MPEG version 1"), "Layer", new MetadataElement<>("Layer", "Layer III"))
				);
		
		NormalizedAudioMetadataTags normalizedAudioTags = audioMetadata.getNormalizedAudioMetadataTags();
		assertThat(normalizedAudioTags).isNotNull();
		assertThat(normalizedAudioTags.artist().value()).isEqualTo("Johnny Burnette");
		assertThat(normalizedAudioTags.albumTitle().value()).isEqualTo("Johnny Burnette & The Rock'n Roll Trio");
		assertThat(normalizedAudioTags.trackNumber().value()).isEqualTo(12);
		assertThat(normalizedAudioTags.trackTitle().value()).isEqualTo("Drinking Wine Spo-Dee-O-Dee");
		assertThat(normalizedAudioTags.date().value()).isEqualTo("2006");
		assertThat(normalizedAudioTags.composer().value()).isEqualTo("");
		assertThat(normalizedAudioTags.genre().value()).isEqualTo("Rock & Roll");	
		assertThat(normalizedAudioTags.albumArtist().value()).isEqualTo("");
		
		assertThat(audioMetadata.getAdditionalTags()).isEmpty();
	}
	
	@Test
	void shouldParseMp3FileWithUrlFields() throws URISyntaxException {
		
		Path mp3FilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/CarnivalInRio.mp3");
		
		LogRecordCounter filterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(AudioMetadata.class.getName()));
		
		Mp3AudioFile f1 = new Mp3AudioFile(mp3FilePath);
		assertThat(f1.isValidMediaFile()).isEmpty(); // not parsed yet
		assertThat(f1.hasImbeddedPicture()).isEmpty();
		assertThat(f1.getSize()).isEmpty();
		assertThat(f1.getFilePath().toUri()).asString().isEqualTo("file:///C:/ForTests/CollectionMusique/CarnivalInRio.mp3");
		assertThat(f1.getExtension()).isEqualTo("mp3");
		
		MediaFileMetadata metadata = f1.getMetadata();
		assertThat(metadata).isNotNull();
		assertThat(f1.getSize()).isPresent().hasValue(10343347L);
		assertThat(f1.isValidMediaFile()).isPresent().hasValue(true);
		
		AudioMetadata audioMetadata = f1.getAudioMetadata();
		assertThat(audioMetadata).isNotNull();
		
		AudioStreamMetadata streamInfo = audioMetadata.getAudioStreamMetadata();
		assertThat(streamInfo).isNotNull();
		
		assertThat(streamInfo.samplingRate().value()).isEqualTo(44100);
		assertThat(streamInfo.bitDepth().value()).isEqualTo(0);  // irrelevant
		assertThat(streamInfo.isLossless().value()).isFalse();
		assertThat(streamInfo.numberOfChannels().value()).isEqualTo(2);
		assertThat(streamInfo.bitRate().value()).isEqualTo(320000);
		assertThat(streamInfo.trackDuration().value()).isNull();  // not calculated
		
		assertThat(metadata.getFormatSpecificMetadata()).isNotNull().containsExactlyInAnyOrderEntriesOf(
				Map.of("Version", new MetadataElement<>("Version", "MPEG version 1"), "Layer", new MetadataElement<>("Layer", "Layer III"))
				);
		
		NormalizedAudioMetadataTags normalizedAudioTags = audioMetadata.getNormalizedAudioMetadataTags();
		assertThat(normalizedAudioTags).isNotNull();
		assertThat(normalizedAudioTags.artist().value()).isEqualTo("Djavan");
		assertThat(normalizedAudioTags.albumTitle().value()).isEqualTo("Bird of Paradise");
		assertThat(normalizedAudioTags.trackNumber().value()).isEqualTo(1);
		assertThat(normalizedAudioTags.trackTitle().value()).isEqualTo("Carnival In Rio");
		assertThat(normalizedAudioTags.date().value()).isEqualTo("1987");
		assertThat(normalizedAudioTags.composer().value()).isEqualTo("");
		assertThat(normalizedAudioTags.genre().value()).isEqualTo("Brazilian Music");	
		assertThat(normalizedAudioTags.albumArtist().value()).isEqualTo("");
		assertThat(f1.hasImbeddedPicture()).hasValue(true);
		
		assertThat(audioMetadata.getAdditionalTags()).isNotEmpty().hasSize(3);
		
		assertThat(filterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(filterCounter.getLogRecordCount(Level.WARNING)).isEqualTo(1);
		filterCounter.stopLogCountAndFilter();
	}
	
	@Test
	void shouldParseMp3FileWithPopmFields() throws URISyntaxException {
		
		Path mp3FilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/GotPapersOnYouBaby.mp3");
		
		LogRecordCounter filterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(AudioMetadata.class.getName()));
		
		Mp3AudioFile f1 = new Mp3AudioFile(mp3FilePath);
		assertThat(f1.isValidMediaFile()).isEmpty(); // not parsed yet
		assertThat(f1.hasImbeddedPicture()).isEmpty();
		assertThat(f1.getSize()).isEmpty();
		assertThat(f1.getFilePath().toUri()).asString().isEqualTo("file:///C:/ForTests/CollectionMusique/GotPapersOnYouBaby.mp3");
		assertThat(f1.getExtension()).isEqualTo("mp3");
		
		MediaFileMetadata metadata = f1.getMetadata();
		assertThat(metadata).isNotNull();
		assertThat(f1.getSize()).isPresent().hasValue(5542638L);
		assertThat(f1.isValidMediaFile()).isPresent().hasValue(true);
		
		AudioMetadata audioMetadata = f1.getAudioMetadata();
		assertThat(audioMetadata).isNotNull();
		
		AudioStreamMetadata streamInfo = audioMetadata.getAudioStreamMetadata();
		assertThat(streamInfo).isNotNull();
		
		assertThat(streamInfo.samplingRate().value()).isEqualTo(48000);
		assertThat(streamInfo.bitDepth().value()).isEqualTo(0);  // irrelevant
		assertThat(streamInfo.isLossless().value()).isFalse();
		assertThat(streamInfo.numberOfChannels().value()).isEqualTo(2);
		assertThat(streamInfo.bitRate().value()).isEqualTo(320000);
		assertThat(streamInfo.trackDuration().value()).isNull();  // not calculated
		
		assertThat(metadata.getFormatSpecificMetadata()).isNotNull().containsExactlyInAnyOrderEntriesOf(
				Map.of("Version", new MetadataElement<>("Version", "MPEG version 1"), "Layer", new MetadataElement<>("Layer", "Layer III"))
				);
		
		NormalizedAudioMetadataTags normalizedAudioTags = audioMetadata.getNormalizedAudioMetadataTags();
		assertThat(normalizedAudioTags).isNotNull();
		assertThat(normalizedAudioTags.artist().value()).isEqualTo("B.B. King");
		assertThat(normalizedAudioTags.albumTitle().value()).isEqualTo("The Incredible Sould Of B.B. King");
		assertThat(normalizedAudioTags.trackNumber().value()).isEqualTo(1);
		assertThat(normalizedAudioTags.trackTitle().value()).isEqualTo("I've Got Papers On You Baby");
		assertThat(normalizedAudioTags.date().value()).isEqualTo("1970");
		assertThat(normalizedAudioTags.composer().value()).isEqualTo("");
		assertThat(normalizedAudioTags.genre().value()).isEqualTo("Blues");	
		assertThat(normalizedAudioTags.albumArtist().value()).isEqualTo("B.B. King");
		assertThat(f1.hasImbeddedPicture()).hasValue(true);
		
		assertThat(audioMetadata.getAdditionalTags()).isNotEmpty().hasSize(1);
		
		assertThat(filterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(filterCounter.getLogRecordCount(Level.WARNING)).isEqualTo(1);
		filterCounter.stopLogCountAndFilter();
	}
	
	@Test
	void shouldParseMp3File3() throws URISyntaxException {
		
		Path mp3FilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/summertime.mp3");
		
		Mp3AudioFile f1 = new Mp3AudioFile(mp3FilePath);
		assertThat(f1.isValidMediaFile()).isEmpty(); // not parsed yet
		assertThat(f1.hasImbeddedPicture()).isEmpty();
		assertThat(f1.getSize()).isEmpty();
		assertThat(f1.getFilePath().toUri()).asString().isEqualTo("file:///C:/ForTests/CollectionMusique/summertime.mp3");
		assertThat(f1.getExtension()).isEqualTo("mp3");
		
		MediaFileMetadata metadata = f1.getMetadata();
		assertThat(metadata).isNotNull();
		assertThat(f1.getSize()).isPresent().hasValue(16899659L);
		assertThat(f1.isValidMediaFile()).isPresent().hasValue(true);
		
		AudioMetadata audioMetadata = f1.getAudioMetadata();
		assertThat(audioMetadata).isNotNull();
		
		AudioStreamMetadata streamInfo = audioMetadata.getAudioStreamMetadata();
		assertThat(streamInfo).isNotNull();
		
		assertThat(streamInfo.samplingRate().value()).isEqualTo(44100);
		assertThat(streamInfo.bitDepth().value()).isEqualTo(0);  // irrelevant
		assertThat(streamInfo.isLossless().value()).isFalse();
		assertThat(streamInfo.numberOfChannels().value()).isEqualTo(2);
		assertThat(streamInfo.bitRate().value()).isEqualTo(320000);
		assertThat(streamInfo.trackDuration().value()).isNull();  // not calculated
		
		assertThat(metadata.getFormatSpecificMetadata()).isNotNull().containsExactlyInAnyOrderEntriesOf(
				Map.of("Version", new MetadataElement<>("Version", "MPEG version 1"), "Layer", new MetadataElement<>("Layer", "Layer III"))
				);
		
		NormalizedAudioMetadataTags normalizedAudioTags = audioMetadata.getNormalizedAudioMetadataTags();
		assertThat(normalizedAudioTags).isNotNull();
		assertThat(normalizedAudioTags.artist().value()).isEqualTo("Barney Kessel");
		assertThat(normalizedAudioTags.albumTitle().value()).isEqualTo("Two Way Conversation");
		assertThat(normalizedAudioTags.trackNumber().value()).isEqualTo(3);
		assertThat(normalizedAudioTags.trackTitle().value()).isEqualTo("Summertime");
		assertThat(normalizedAudioTags.date().value()).isEqualTo("1974");
		assertThat(normalizedAudioTags.composer().value()).isEqualTo("");
		assertThat(normalizedAudioTags.genre().value()).isEqualTo("Jazz");	
		assertThat(normalizedAudioTags.albumArtist().value()).isEqualTo("Barney Kessel & Red Mitchell");
		assertThat(f1.hasImbeddedPicture()).hasValue(true);
		
		assertThat(audioMetadata.getAdditionalTags()).isEmpty();
	}
	
	@Test
	void shouldParseMp3File4() throws URISyntaxException {
		
		Path mp3FilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/YouDontBelieveMe.mp3");
		
		Mp3AudioFile f1 = new Mp3AudioFile(mp3FilePath);
		assertThat(f1.isValidMediaFile()).isEmpty(); // not parsed yet
		assertThat(f1.hasImbeddedPicture()).isEmpty();
		assertThat(f1.getSize()).isEmpty();
		assertThat(f1.getFilePath().toUri()).asString().isEqualTo("file:///C:/ForTests/CollectionMusique/YouDontBelieveMe.mp3");
		assertThat(f1.getExtension()).isEqualTo("mp3");
		
		MediaFileMetadata metadata = f1.getMetadata();
		assertThat(metadata).isNotNull();
		assertThat(f1.getSize()).isPresent().hasValue(7043993L);
		assertThat(f1.isValidMediaFile()).isPresent().hasValue(true);
		
		AudioMetadata audioMetadata = f1.getAudioMetadata();
		assertThat(audioMetadata).isNotNull();
		
		AudioStreamMetadata streamInfo = audioMetadata.getAudioStreamMetadata();
		assertThat(streamInfo).isNotNull();
		
		assertThat(streamInfo.samplingRate().value()).isEqualTo(44100);
		assertThat(streamInfo.bitDepth().value()).isEqualTo(0);  // irrelevant
		assertThat(streamInfo.isLossless().value()).isFalse();
		assertThat(streamInfo.numberOfChannels().value()).isEqualTo(2);
		assertThat(streamInfo.bitRate().value()).isEqualTo(320000);
		assertThat(streamInfo.trackDuration().value()).isNull();  // not calculated
		
		assertThat(metadata.getFormatSpecificMetadata()).isNotNull().containsExactlyInAnyOrderEntriesOf(
				Map.of("Version", new MetadataElement<>("Version", "MPEG version 1"), "Layer", new MetadataElement<>("Layer", "Layer III"))
				);
		
		NormalizedAudioMetadataTags normalizedAudioTags = audioMetadata.getNormalizedAudioMetadataTags();
		assertThat(normalizedAudioTags).isNotNull();
		assertThat(normalizedAudioTags.artist().value()).isEqualTo("Stray Cats");
		assertThat(normalizedAudioTags.albumTitle().value()).isEqualTo("Gonna Ball");
		assertThat(normalizedAudioTags.trackNumber().value()).isEqualTo(7);
		assertThat(normalizedAudioTags.trackTitle().value()).isEqualTo("You Dont Believe Me");
		assertThat(normalizedAudioTags.date().value()).isEqualTo("1981");
		assertThat(normalizedAudioTags.composer().value()).isEqualTo("");
		assertThat(normalizedAudioTags.genre().value()).isEqualTo("Rock & Roll");	
		assertThat(normalizedAudioTags.albumArtist().value()).isEqualTo("");
		assertThat(f1.hasImbeddedPicture()).hasValue(false);
		
		assertThat(audioMetadata.getAdditionalTags()).isEmpty();
	}
	
	@Test
	void test() {
		byte[] bytes = {0, 0, 0, 0};
		String endOfField = new String(bytes,StandardCharsets.ISO_8859_1);
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		String fieldContent = Utils.decodeByteBuffer(buffer, 4, StandardCharsets.ISO_8859_1);
		
		assertThat(fieldContent.getBytes()).isEqualTo(bytes);
		assertThat(fieldContent).isEqualTo(endOfField);
	}
	
	@Test
	void shouldNotParseMp3FileWithPaddingAfterID3() throws URISyntaxException {

		Path mp3FilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/f1.mp3");
		
		LogRecordCounter mp3FilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(Mp3Header.class.getName()));
				
		Mp3AudioFile f1 = new Mp3AudioFile(mp3FilePath);
		assertThat(f1.isValidMediaFile()).isEmpty(); // not parsed yet
		assertThat(f1.hasImbeddedPicture()).isEmpty();
		assertThat(f1.getSize()).isEmpty();
		
		assertThat(f1.getExtension()).isEqualTo("mp3");
		assertThat(f1.getFilePath().toUri()).asString().isEqualTo("file:///C:/ForTests/CollectionMusique/f1.mp3");
		
		MediaFileMetadata metadata = f1.getMetadata();
		assertThat(metadata).isNull();
		assertThat(f1.getSize()).isPresent().hasValue(3613113L);
		assertThat(f1.isValidMediaFile()).isPresent().hasValue(false);
		
		AudioMetadata audioMetadata = f1.getAudioMetadata();
		assertThat(audioMetadata).isNull();
		
		assertThat(mp3FilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(mp3FilterCounter.getLogRecordCount(Level.WARNING)).isEqualTo(1);
		mp3FilterCounter.stopLogCountAndFilter();
	}
}
