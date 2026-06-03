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

package org.fl.collectionAlbum.mediaFile.metadata;

import java.nio.ByteBuffer;
import java.util.Map;

import org.fl.collectionAlbum.mediaFile.Utils;

public class FlacStreamInfoMetadataBlock {
	
	private static final int MD5_CHECKSUM_SIZE = 16;
	private final AudioStreamMetadata audioStreamMetadata;

	private static final String MIN_BLOCK_SIZE = "Minimum block size";
	private static final String MAX_BLOCK_SIZE = "Maximum block size";
	private static final String MIN_FRAME_SIZE = "Minimum frame size";
	private static final String MAX_FRAME_SIZE = "Maximum frame size";
	
	private final int minBlockSize;
	private final int maxBlockSize;
	private final int minFrameSize;
	private final int maxFrameSize;

	private final Map<String, MetadataElement<?>> formatSpecificMetadata;
	
	public FlacStreamInfoMetadataBlock(ByteBuffer byteBuffer) {

		minBlockSize = Utils.get2bytesUnsignedInt(byteBuffer);
		maxBlockSize = Utils.get2bytesUnsignedInt(byteBuffer);
		minFrameSize = Utils.get3bytesUnsignedInt(byteBuffer);
		maxFrameSize = Utils.get3bytesUnsignedInt(byteBuffer);
	
		// Read 8 next bytes as unsigned
		int b1 = Utils.get1byteUnsignedInt(byteBuffer);
		int b2 = Utils.get1byteUnsignedInt(byteBuffer);
		int b3 = Utils.get1byteUnsignedInt(byteBuffer);
		int b4 = Utils.get1byteUnsignedInt(byteBuffer);
		int b5 = Utils.get1byteUnsignedInt(byteBuffer);
		int b6 = Utils.get1byteUnsignedInt(byteBuffer);
		int b7 = Utils.get1byteUnsignedInt(byteBuffer);
		int b8 = Utils.get1byteUnsignedInt(byteBuffer);
		
		int sampleRate = (b1 << 12) +
			      (b2 << 4) +
			      ((b3 & 0xF0) >>> 4);
		
		int numberOfChannels = ((b3 & 0x0E) >>> 1) + 1;

		int bitsPerSample = ((b3 & 0x01) << 4) +
			      ((b4 & 0xF0) >>> 4) + 1;
		
		int totalNumberOfSamples = b8 + (b7 << 8) + (b6 << 16) + (b5 << 24) + ((b4 & 0x0F) << 32);
		
		long trackLength = ((long)totalNumberOfSamples * 1000) / sampleRate;
		
		// do not read md5 for now, skip it
		byteBuffer.position(byteBuffer.position() + MD5_CHECKSUM_SIZE);

		int bitsRate = bitsPerSample * sampleRate;
		
		audioStreamMetadata = AudioStreamMetadataBuilder.build(true, sampleRate, bitsPerSample, bitsRate, numberOfChannels, trackLength);
		
		formatSpecificMetadata = Map.of(
				MIN_BLOCK_SIZE, new MetadataElement<>(MIN_BLOCK_SIZE, minBlockSize),
				MAX_BLOCK_SIZE, new MetadataElement<>(MAX_BLOCK_SIZE, maxBlockSize),
				MIN_FRAME_SIZE, new MetadataElement<>(MIN_FRAME_SIZE, minFrameSize),
				MAX_FRAME_SIZE, new MetadataElement<>(MAX_FRAME_SIZE, maxFrameSize)
				);
	}

	public AudioStreamMetadata getAudioStreamMetadata() {
		return audioStreamMetadata;
	}

	public int getMinBlockSize() {
		return minBlockSize;
	}

	public int getMaxBlockSize() {
		return maxBlockSize;
	}

	public int getMinFrameSize() {
		return minFrameSize;
	}

	public int getMaxFrameSize() {
		return maxFrameSize;
	}

	public Map<String, MetadataElement<?>> getFormatSpecificMetadata() {
		return formatSpecificMetadata;
	}

}
