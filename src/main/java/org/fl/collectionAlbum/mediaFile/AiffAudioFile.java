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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.format.AudioFileType;
import org.fl.collectionAlbum.mediaFile.metadata.AudioMetadata;

public class AiffAudioFile extends AudioFile {

	private static final Logger logger = Logger.getLogger(AiffAudioFile.class.getName());
	
	private static final int FIRST_BYTES_TO_READ = 1000;
			
	public AiffAudioFile(Path filePath) {
		super(filePath, AudioFileType.AIFF);
	}

	@Override
	protected AudioMetadata parseMetadata() {
		
		AudioMetadata audioMetadata = null;
		// will change if it is not so
		boolean isValidFlacFile = true;
		
		try (FileChannel sbc = FileChannel.open(filePath, StandardOpenOption.READ)) {
			
			size = Optional.of(sbc.size());
			
			ByteBuffer byteBuffer = Utils.readToDirectByteBuffer(sbc, FIRST_BYTES_TO_READ);
			
		} catch (Exception e) {
			isValidFlacFile = false;
			logger.log(Level.WARNING, "Exception when reading AIFF file " + filePath, e);
		}
		isValidMediaFile = Optional.of(isValidFlacFile);
		return audioMetadata;
	}
}
