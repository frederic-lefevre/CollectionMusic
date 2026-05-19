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

import org.fl.collectionAlbum.mediaFile.Utils;

public class FlacStreamInfoMetadataBlock {

	private AudioStreamMetadata audioStreamMetadata;

	private final int minBlockSize;
	private final int maxBlockSize;
	private final int minFrameSize;
	private final int maxFrameSize;
	private final int samplingRate;
	private final int samplingRatePerChannel;
	private final int bitsPerSample;
	private final int noOfChannels;
	private final int noOfSamples;
	private final float trackLength;
	private final String md5;

	public FlacStreamInfoMetadataBlock(ByteBuffer byteBuffer) {

		minBlockSize = Utils.get2bytesUnsignedInt(byteBuffer);
		maxBlockSize = Utils.get2bytesUnsignedInt(byteBuffer);
		minFrameSize = Utils.get3bytesUnsignedInt(byteBuffer);
		maxFrameSize = Utils.get3bytesUnsignedInt(byteBuffer);
		
	
		samplingRate = 0;
		samplingRatePerChannel = 0;
		bitsPerSample = 0;
		noOfChannels = 0;
		noOfSamples = 0;
		trackLength = 0;
		md5 = null;
	}
}
