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

public class AiffStreamInfo {
	
	private static final String AIFF_FORM_TYPE = "AIFF form type";
	
	private AudioStreamMetadata audioStreamMetadata;
	private final Map<String, MetadataElement<?>> formatSpecificMetadata;
	
	public AiffStreamInfo(AiffFormType formType) {
		
		audioStreamMetadata = null;
		formatSpecificMetadata = Map.of(AIFF_FORM_TYPE, new MetadataElement<>(AIFF_FORM_TYPE, formType.name()));	
	}
	
	public AudioStreamMetadata parseStreamInfo(ByteBuffer byteBuffer) {
		
		int numberOfChannels = Utils.get2bytesUnsignedInt(byteBuffer);
		long numSampleFrames =  Utils.get4bytesUnsignedInt(byteBuffer) ;
		int sampleSize = Utils.get2bytesUnsignedInt(byteBuffer);
		long samplingRate = Utils.get10bytesUnsignedLong(byteBuffer);
		
		long trackLength = ((long)numSampleFrames * 1000) / samplingRate;
		int bitsRate = sampleSize * (int)samplingRate;
		
		audioStreamMetadata = AudioStreamMetadataBuilder.build(true, samplingRate, sampleSize, bitsRate, numberOfChannels, trackLength);
		return audioStreamMetadata;
	}
	
	public AudioStreamMetadata getAudioStreamMetadata() {
		return audioStreamMetadata;
	}
	
	public Map<String, MetadataElement<?>> getFormatSpecificMetadata() {
		return formatSpecificMetadata;
	}
}
