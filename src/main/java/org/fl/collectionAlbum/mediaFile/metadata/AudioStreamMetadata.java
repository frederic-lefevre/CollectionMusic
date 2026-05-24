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

import java.time.Duration;
import java.util.Map;

public record AudioStreamMetadata (
		boolean isLossless,
		long samplingRate, // sample frequency in Hz
		int bitDepth,  // number of bit per sample
		long bitRate, // in bits per seconds
		int numberOfChannels, 
		long trackDuration  // in milliseconds
		) {
	
	private static final String IS_LOSSLESS = "Is lossless";
	private static final String SAMPLING_RATE = "Sampling rate";
	private static final String BIT_DEPTH = "Number of bits per sample";
	private static final String BIT_RATE = "Bit rate";
	private static final String NUMBER_OF_CHANNELS = "Number of channels";
	private static final String TRACK_DURATION = "Track duration";
	
	private String durationToString() {
		Duration duration = Duration.ofMillis(trackDuration);
		long hourPart = duration.toHours();
		if (hourPart == 0) {
			return  String.format("%02d:%02d", 
					duration.toMinutesPart(), 
					duration.toSecondsPart());
		} else {
			return  String.format("%d:%02d:%02d", 
					duration.toHours(), 
					duration.toMinutesPart(), 
					duration.toSecondsPart());
		}
	}
	
	public Map<String, String> getDescription() {
		
		return Map.of(
				IS_LOSSLESS, Boolean.toString(isLossless),
				SAMPLING_RATE, Long.toString(samplingRate),
				BIT_DEPTH, Integer.toString(bitDepth),
				BIT_RATE, Long.toString(bitRate),
				NUMBER_OF_CHANNELS, Integer.toString(numberOfChannels),
				TRACK_DURATION, durationToString()
				);
	}
}
