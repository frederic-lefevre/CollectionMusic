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
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.format.AudioFileType;
import org.fl.collectionAlbum.mediaFile.metadata.AiffChunk;
import org.fl.collectionAlbum.mediaFile.metadata.AiffFormType;
import org.fl.collectionAlbum.mediaFile.metadata.AiffStreamInfo;
import org.fl.collectionAlbum.mediaFile.metadata.AudioMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.ID3Header;
import org.fl.collectionAlbum.mediaFile.metadata.Id3v2Tag;
import org.fl.collectionAlbum.mediaFile.metadata.MetadataElement;
import org.fl.collectionAlbum.mediaFile.metadata.NormalizedAudioMetadataTagsBuilder;

public class AiffAudioFile extends AudioFile {

	private static final Logger logger = Logger.getLogger(AiffAudioFile.class.getName());
	
	private static final int FIRST_BYTES_TO_READ = 2*AiffChunk.CHUNK_ID_LENGTH + 4;
	
	public AiffAudioFile(Path filePath) {
		super(filePath, AudioFileType.AIFF);
	}

	@Override
	protected AudioMetadata parseMetadata() {
		
		AudioMetadata audioMetadata = null;
		// will change if it is not so
		boolean isValidFlacFile = true;
		
		AiffFormType formType = null;
		
		try (FileChannel sbc = FileChannel.open(filePath, StandardOpenOption.READ)) {
			
			size = Optional.of(sbc.size());
			
			ByteBuffer byteBuffer = Utils.readToDirectByteBuffer(sbc, FIRST_BYTES_TO_READ);
			
			if (Utils.nextBytesEquals(byteBuffer, AiffChunk.FORM_CHUNK_ID_BYTES)) {
				
				long fileLength = Utils.get4bytesUnsignedInt(byteBuffer) + AiffChunk.CHUNK_ID_LENGTH + 4;
				if (fileLength != size.get()) {
					logger.severe("Announced file length " + fileLength + " is different from real file length " + size.get() + " for " + filePath);
				}
				
				String formTypeString = Utils.decodeByteBuffer(byteBuffer, AiffChunk.CHUNK_ID_LENGTH, StandardCharsets.ISO_8859_1);
				try {
					formType = AiffFormType.valueOf(formTypeString);
				} catch (IllegalArgumentException e) {
					logger.severe("Invalid FORM Type " + formTypeString + " for file " + filePath);
				}
				
				AiffStreamInfo aiffStreamInfo = new AiffStreamInfo(formType);
				final NormalizedAudioMetadataTagsBuilder normalizedAudioMetadataTagsBuilder = NormalizedAudioMetadataTagsBuilder.builder();
				Map<String, MetadataElement<?>> additionalFieldsMap = new HashMap<String, MetadataElement<?>>();;
				
				boolean commChunkProcessed = false;
				boolean id3ChunkProcessed = false;
				do {
					
					ByteBuffer chunkHeaderBuffer = Utils.readToDirectByteBuffer(sbc, AiffChunk.CHUNK_HEADER_LENGTH);
					AiffChunk chunk = new AiffChunk(chunkHeaderBuffer, filePath);
					if (chunk.getChunkId().equals(AiffChunk.COMM_CHUNK_ID)) {

						ByteBuffer commContentBuffer = Utils.readToDirectByteBuffer(sbc, chunk.getChunkContentLength());
						
						aiffStreamInfo.parseStreamInfo(commContentBuffer);
						commChunkProcessed = true;

					} else if  (chunk.getChunkId().equals(AiffChunk.ID3_CHUNK_ID)) {
						
						// Parse the tags
						ByteBuffer id3ContentBuffer = Utils.readToDirectByteBuffer(sbc, chunk.getChunkContentLength());
						ID3Header id3Header = new ID3Header(id3ContentBuffer, filePath);
						if (id3Header.isID3v2Header() && !id3Header.isHasExtendedHeader()) {
							
							Id3v2Tag id3v2Tag = new Id3v2Tag(id3ContentBuffer, filePath, additionalFieldsMap, normalizedAudioMetadataTagsBuilder);
							hasImbeddedPicture = Optional.of(id3v2Tag.hasImbeddedPicture());

						} else {
							if (! id3Header.isID3v2Header()) {
								logger.log(Level.WARNING, "No ID3 Header found at the begining of MP3 file " + filePath);
							} else {
								logger.log(Level.WARNING, "Unsupported ID3 header in " + filePath);
							}
						}
						id3ChunkProcessed = true;
						
						sbc.position(sbc.position() + chunk.getChunkContentLength()); // TODO replace by parsing
						
					} else if (chunk.getChunkId().equals(AiffChunk.SOUND_DATA_CHUNK_ID)) {

						// skip audio data
						sbc.position(sbc.position() + chunk.getChunkContentLength());

					} else {
						// other chunk types
						sbc.position(sbc.position() + chunk.getChunkContentLength());
					}
				} while ((sbc.position() < sbc.size() - AiffChunk.CHUNK_HEADER_LENGTH)  && (!commChunkProcessed || !id3ChunkProcessed));
				
				audioMetadata = new AudioMetadata(
						aiffStreamInfo.getAudioStreamMetadata(), 
						normalizedAudioMetadataTagsBuilder.build(filePath), 
						additionalFieldsMap, 
						aiffStreamInfo.getFormatSpecificMetadata(), 
						filePath);
				
			} else {
				logger.severe("No mandatory " + AiffChunk.FORM_CHUNK_ID + "chunk id found at the begining of AIFF file " + filePath);
			}
			
		} catch (Exception e) {
			isValidFlacFile = false;
			logger.log(Level.WARNING, "Exception when reading AIFF file " + filePath, e);
		}
		isValidMediaFile = Optional.of(isValidFlacFile);
		return audioMetadata;
	}
}
