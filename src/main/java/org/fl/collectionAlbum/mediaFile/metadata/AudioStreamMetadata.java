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

import java.util.Map;

public record AudioStreamMetadata (
		MetadataElement<Boolean> isLossless,
		MetadataElement<Long> samplingRate, // sample frequency in Hz
		MetadataElement<Integer> bitDepth,  // number of bit per sample
		MetadataElement<Long> bitRate, // in bits per seconds
		MetadataElement<Integer> numberOfChannels, 
		MetadataElement<Long> trackDuration  // in milliseconds
		) {
	
	static final String IS_LOSSLESS = "Is lossless";
	static final String SAMPLING_RATE = "Sampling rate";
	static final String BIT_DEPTH = "Number of bits per sample";
	static final String BIT_RATE = "Bit rate";
	static final String NUMBER_OF_CHANNELS = "Number of channels";
	static final String TRACK_DURATION = "Track duration";
	
	public Map<String, MetadataElement<?>> getMetadataMap() {
		
		return Map.of(
				IS_LOSSLESS,isLossless,
				SAMPLING_RATE, samplingRate,
				BIT_DEPTH, bitDepth,
				BIT_RATE, bitRate,
				NUMBER_OF_CHANNELS, numberOfChannels,
				TRACK_DURATION, trackDuration
				);
	}
}
