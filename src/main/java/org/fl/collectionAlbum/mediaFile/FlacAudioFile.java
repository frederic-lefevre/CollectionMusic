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
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.mediaFile.metadata.AudioMetadata;

public class FlacAudioFile extends AudioFile {

	private static final Logger logger = Logger.getLogger(FlacAudioFile.class.getName());
	
	private static final String FLAC_IDENTIFIER = "fLaC";
	private static final int FLAC_IDENTIFIER_LENGTH = FLAC_IDENTIFIER.length();
	private static final byte[] FLAC_IDENTIFIER_BYTES = FLAC_IDENTIFIER.getBytes(StandardCharsets.ISO_8859_1);
	
	private static final int BYTES_TO_READ = FLAC_IDENTIFIER_LENGTH;
	
	protected FlacAudioFile(Path filePath, String extension) {
		super(filePath, extension);
	}

	@Override
	protected AudioMetadata parseMetadata() {
		
		try (SeekableByteChannel sbc = Files.newByteChannel(filePath, StandardOpenOption.READ)) {

			ByteBuffer byteBuffer = ByteBuffer.allocate(FLAC_IDENTIFIER_LENGTH);
			sbc.read(byteBuffer);
			
			byteBuffer.position(0);
			
			if (! Utils.nextBytesEquals(byteBuffer, FLAC_IDENTIFIER_BYTES)) {
				logger.warning(FLAC_IDENTIFIER + " not found at the start of FLAC file " + filePath);
			}
			
		} catch (Exception e) {
			logger.log(Level.WARNING, "Exception when reading FLAC file " + filePath, e);
		}
		return null;
	}


}
