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
import java.util.Map;

import org.fl.collectionAlbum.mediaFile.metadata.AudioMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.AudioStreamMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.MediaFileMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.MetadataElement;
import org.fl.collectionAlbum.mediaFile.metadata.NormalizedAudioMetadataTags;
import org.fl.util.file.FilesUtils;
import org.junit.jupiter.api.Test;

class AiffAudioFileTest {
	
	@Test
	void shouldParseAiffFile() throws URISyntaxException {
		
		Path aiffFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/I_LoveYou.aiff");
		
		AiffAudioFile f1 = new AiffAudioFile(aiffFilePath);
		assertThat(f1.getFilePath().toUri()).asString().isEqualTo("file:///C:/ForTests/CollectionMusique/I_LoveYou.aiff");
		assertThat(f1.isValidMediaFile()).isEmpty(); // not parsed yet
		assertThat(f1.hasImbeddedPicture()).isEmpty();
		assertThat(f1.getSize()).isEmpty();
		assertThat(f1.getExtension()).isEqualTo("aiff");
		
		MediaFileMetadata metadata = f1.getMetadata();
		assertThat(metadata).isNotNull();
		
		assertThat(f1.isValidMediaFile()).isPresent().hasValue(true);
		assertThat(f1.getSize()).isPresent().hasValue(83042068L);
		
		AudioMetadata audioMetadata = f1.getAudioMetadata();
		assertThat(audioMetadata).isNotNull();
		
		AudioStreamMetadata streamInfo = audioMetadata.getAudioStreamMetadata();
		assertThat(streamInfo).isNotNull();
	
		
		assertThat(streamInfo.samplingRate().value()).isEqualTo(96000);
		assertThat(streamInfo.bitDepth().value()).isEqualTo(24);
		assertThat(streamInfo.isLossless().value()).isTrue();
		assertThat(streamInfo.numberOfChannels().value()).isEqualTo(2);
		assertThat(streamInfo.bitRate().value()).isEqualTo(24*96000);
		assertThat(streamInfo.trackDuration().value()).isEqualTo(141572);
		assertThat(f1.hasImbeddedPicture()).isPresent().hasValue(true);
		
		NormalizedAudioMetadataTags normalizedAudioTags = audioMetadata.getNormalizedAudioMetadataTags();
		assertThat(normalizedAudioTags).isNotNull();
		
		assertThat(normalizedAudioTags.artist().value()).isEqualTo("Lou Reed");
		assertThat(normalizedAudioTags.albumTitle().value()).isEqualTo("Lou Reed Remastered (2016)");
		assertThat(normalizedAudioTags.trackNumber().value()).isEqualTo(6);
		assertThat(normalizedAudioTags.trackTitle().value()).isEqualTo("I Love You");
		assertThat(normalizedAudioTags.date().value()).isEqualTo("1972");
		assertThat(normalizedAudioTags.composer().value()).isEqualTo("");
		assertThat(normalizedAudioTags.genre().value()).isEqualTo("Rock");	
		assertThat(normalizedAudioTags.albumArtist().value()).isEqualTo("Lou Reed");
		
		
		assertThat(metadata.getFormatSpecificMetadata()).isNotNull().containsExactlyInAnyOrderEntriesOf(
				Map.of(
						"AIFF form type", new MetadataElement<>("AIFF form type", "AIFF")
				));
	}
	
}
