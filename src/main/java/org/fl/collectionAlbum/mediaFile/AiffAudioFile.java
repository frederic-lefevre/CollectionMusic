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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.format.AudioFileType;
import org.fl.collectionAlbum.mediaFile.metadata.AiffChunk;
import org.fl.collectionAlbum.mediaFile.metadata.AudioMetadata;

public class AiffAudioFile extends AudioFile {

	private static final Logger logger = Logger.getLogger(AiffAudioFile.class.getName());
	
	private static final int FIRST_BYTES_TO_READ = 2*AiffChunk.CHUNK_ID_LENGTH + 4;
	
	private static final String AIFF_FORM_TYPE = "AIFF";
	private static final String AIFC_FORM_TYPE = "AIFC";
	
	private String formType;
	
	public AiffAudioFile(Path filePath) {
		super(filePath, AudioFileType.AIFF);
	}

	@Override
	protected AudioMetadata parseMetadata() {
		
		AudioMetadata audioMetadata = null;
		// will change if it is not so
		boolean isValidFlacFile = true;
		
		formType = null;
		
		try (FileChannel sbc = FileChannel.open(filePath, StandardOpenOption.READ)) {
			
			size = Optional.of(sbc.size());
			
			ByteBuffer byteBuffer = Utils.readToDirectByteBuffer(sbc, FIRST_BYTES_TO_READ);
			
			if (Utils.nextBytesEquals(byteBuffer, AiffChunk.FORM_CHUNK_ID_BYTES)) {
				
				long fileLength = Utils.get4bytesUnsignedInt(byteBuffer) + AiffChunk.CHUNK_ID_LENGTH + 4;
				if (fileLength != size.get()) {
					logger.severe("Announced file length " + fileLength + " is different from real file length " + size.get() + " for " + filePath);
				}
				
				formType = Utils.decodeByteBuffer(byteBuffer, AiffChunk.CHUNK_ID_LENGTH, StandardCharsets.ISO_8859_1);
				if (!AIFF_FORM_TYPE.equals(formType) && !AIFC_FORM_TYPE.equals(formType)) {
					logger.severe("Invalid FORM Type " + formType + " for file " + filePath);
				}
				
				boolean commChunkProcessed = false;
				boolean id3ChunkProcessed = false;
				do {
					
					ByteBuffer chunkHeaderBuffer = Utils.readToDirectByteBuffer(sbc, AiffChunk.CHUNK_HEADER_LENGTH);
					AiffChunk chunk = new AiffChunk(chunkHeaderBuffer, filePath);
					System.out.println("Found chunk " + chunk.getChunkId());
					if (chunk.getChunkId().equals(AiffChunk.COMM_CHUNK_ID)) {

						commChunkProcessed = true;
						sbc.position(sbc.position() + chunk.getChunkContentLength()); // TODO replace by parsing
						
					} else if  (chunk.getChunkId().equals(AiffChunk.ID3_CHUNK_ID)) {
						
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

	public String getFormType() {
		return formType;
	}
}
