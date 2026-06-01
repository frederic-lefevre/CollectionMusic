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

public class AudioStreamMetadataBuilder {

	static AudioStreamMetadata build(boolean isLossless,
			long samplingRate, // sample frequency in Hz
			int bitDepth,  // number of bit per sample
			long bitRate, // in bits per seconds
			int numberOfChannels, 
			Long trackDuration  
			) {
		
		return new AudioStreamMetadata(
				new MetadataElement<Boolean>(AudioStreamMetadata.IS_LOSSLESS, isLossless),
				new MetadataElement<Long>(AudioStreamMetadata.SAMPLING_RATE, samplingRate),
				new MetadataElement<Integer>(AudioStreamMetadata.BIT_DEPTH, bitDepth),
				new MetadataElement<Long>(AudioStreamMetadata.BIT_RATE, bitRate),
				new MetadataElement<Integer>(AudioStreamMetadata.NUMBER_OF_CHANNELS, numberOfChannels),
				new MetadataElement<Long>(AudioStreamMetadata.TRACK_DURATION, trackDuration)
				);
	}
}
