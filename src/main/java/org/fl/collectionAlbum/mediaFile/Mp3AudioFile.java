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

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.format.AudioFileType;
import org.fl.collectionAlbum.mediaFile.metadata.AudioMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.Id3v2Tag;
import org.fl.collectionAlbum.mediaFile.metadata.MetadataElement;
import org.fl.collectionAlbum.mediaFile.metadata.Mp3Header;
import org.fl.collectionAlbum.mediaFile.metadata.NormalizedAudioMetadataTags;
import org.fl.collectionAlbum.mediaFile.metadata.NormalizedAudioMetadataTagsBuilder;

public class Mp3AudioFile extends AudioFile {

	private static final Logger logger = Logger.getLogger(Mp3AudioFile.class.getName());
	
	// This is a partial implementation to cover most of the cases
	
	public Mp3AudioFile(Path filePath) {
		super(filePath, AudioFileType.MP3);
	}

	@Override
	protected AudioMetadata parseMetadata() {
		
		AudioMetadata audioMetadata = null;
		
		try (FileChannel sbc = FileChannel.open(filePath, StandardOpenOption.READ)) {
			
			size = Optional.of(sbc.size());
			
			NormalizedAudioMetadataTags normalizedAudioMetadataTags;
			Map<String, MetadataElement<?>> additionalFieldsMap;
			
			ByteBuffer byteBuffer = Utils.readToDirectByteBuffer(sbc, ID3HeaderUtils.BEGIN_HEADER_SIZE);
			
			ByteBuffer id3AndHeaderBuffer;
			
			int id3HeaderLength = ID3HeaderUtils.getID3v2HeaderLength(byteBuffer);
			if (id3HeaderLength > 0) {
				
				// Read the remaining of the ID3 Header and the MP3 Header
				id3AndHeaderBuffer =  Utils.readToDirectByteBuffer(sbc, id3HeaderLength - ID3HeaderUtils.BEGIN_HEADER_SIZE + Mp3Header.MP3_HEADER_SIZE);
				
				// Parse the tags
				Id3v2Tag id3v2Tag = new Id3v2Tag(id3AndHeaderBuffer, id3HeaderLength, filePath);
				
				normalizedAudioMetadataTags = id3v2Tag.getNormalizedAudioMetadataTags();
				additionalFieldsMap = id3v2Tag.getAdditionalFieldsMap();
				
				id3AndHeaderBuffer.position(id3HeaderLength - ID3HeaderUtils.BEGIN_HEADER_SIZE); // TODO Replace by parsing
				
			} else {
				logger.log(Level.WARNING, "No ID3 Header found at the begining of MP3 file " + filePath);

				normalizedAudioMetadataTags = NormalizedAudioMetadataTagsBuilder.builder().build(filePath);
				additionalFieldsMap = new HashMap<String, MetadataElement<?>>();
				id3AndHeaderBuffer =  Utils.readToDirectByteBuffer(sbc, Mp3Header.MP3_HEADER_SIZE);
			}
			
			// Parse stream info in MP3 Header
			Mp3Header mp3Header = new Mp3Header(id3AndHeaderBuffer, filePath, sbc);
			
			isValidMediaFile = Optional.of(mp3Header.isSupportedMp3());
				
			if (isValidMediaFile.get()) {
				audioMetadata = new AudioMetadata(
						mp3Header.getAudioStreamMetadata(), 
						normalizedAudioMetadataTags, 
						additionalFieldsMap, 
						mp3Header.getFormatSpecificMetadata(),
						filePath);
			}
			
		} catch (Exception e) {
			logger.log(Level.WARNING, "Exception when reading MP3 file " + filePath, e);
		}
		
		return audioMetadata;
	}
}
