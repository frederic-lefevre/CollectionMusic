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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.mediaFile.metadata.AudioMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.FlacMetaDataBlockHeader;
import org.fl.collectionAlbum.mediaFile.metadata.FlacMetaDataBlockHeader.BlockType;

public class FlacAudioFile extends AudioFile {

	private static final Logger logger = Logger.getLogger(FlacAudioFile.class.getName());
	
	private static final String FLAC_IDENTIFIER = "fLaC";
	private static final int FLAC_IDENTIFIER_LENGTH = FLAC_IDENTIFIER.length();
	private static final byte[] FLAC_IDENTIFIER_BYTES = FLAC_IDENTIFIER.getBytes(StandardCharsets.ISO_8859_1);
	
	private static final int STREAMINFO_BLOCK_SIZE = 150;
	private static final int BYTES_TO_READ = FLAC_IDENTIFIER_LENGTH + STREAMINFO_BLOCK_SIZE;
	
	protected FlacAudioFile(Path filePath, String extension) {
		super(filePath, extension);
	}

	@Override
	protected AudioMetadata parseMetadata() {
		
		// will change if it is not so
		isValidMediaFile = true;
		
		try (FileChannel sbc = FileChannel.open(filePath, StandardOpenOption.READ)) {
				
			ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BYTES_TO_READ);		
			sbc.read(byteBuffer);
			
			byteBuffer.position(0);
			
			if (! Utils.nextBytesEquals(byteBuffer, FLAC_IDENTIFIER_BYTES)) {
				
				// Check if the file begins with a ID3 Header
				byteBuffer.position(0);
				int id3HeaderLength = ID3HeaderUtils.getID3v2HeaderLength(byteBuffer);
				if (id3HeaderLength > 0) {
					
					// there is a ID3 Header
					// skip the ID3 header	
					sbc.position(id3HeaderLength);
					byteBuffer.clear();
					
					sbc.read(byteBuffer);
					byteBuffer.position(0);
				
					if (Utils.nextBytesEquals(byteBuffer, FLAC_IDENTIFIER_BYTES)) {
						logger.warning(filePath + " is a FLAC file that starts with a ID3 header");	
					} else {
						isValidMediaFile = false;
						logger.severe(filePath + " is not a FLAC file (ID3 header at beginning but no FLAC identifier after)");
					}
				} else {
					isValidMediaFile = false;
					logger.severe(filePath + " is not a FLAC file (no FLAC identifier or ID3 header at beginning)");
				}
			}
			
			// Read the first metadata block that should be streamInfo
			if (isValidMediaFile) {
				
				FlacMetaDataBlockHeader firstBlock = new FlacMetaDataBlockHeader(byteBuffer);
				
				if (firstBlock.getBlockType() == BlockType.STREAMINFO) {
					
				} else {
					isValidMediaFile = false;
					logger.severe(filePath + " has no StreamInfo metadatablock as first block");
				}
			}
			
		} catch (Exception e) {
			isValidMediaFile = false;
			logger.log(Level.WARNING, "Exception when reading FLAC file " + filePath, e);
		}
		return null;
	}

}
