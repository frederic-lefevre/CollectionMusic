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
import java.nio.file.Path;
import java.util.logging.Logger;

import org.fl.collectionAlbum.mediaFile.Mp3AudioFile;
import org.fl.collectionAlbum.mediaFile.Utils;

public class Mp3Header {

	private static final Logger logger = Logger.getLogger(Mp3AudioFile.class.getName());
	
	public static final int MP3_HEADER_SIZE = 4;

	private static final int SYNC_BYTE_1 = 0xFF;
	private static final int SYNC_BYTE_2_MASK = 0xE0;
	private static final int VERSION_MASK = 0x18;
	private static final int LAYER_MASK = 0x06;
	private static final int BIT_RATE_MASK = 0xF0;
	private static final int SAMPLING_RATE_MASK = 0x0C;
	private static final int CHANNEL_MODE_MASK = 0x60;
	 
	private static final int VERSION_1 = 3;
	private static final int VERSION_2 = 2;
	private static final int VERSION_2_5 = 0;
	private static final int LAYER_1 = 3;
	private static final int LAYER_2 = 2;
	private static final int LAYER_3 = 1;
	
	private static final String[] MP3_VERSION_VALUES = {"MPEG version 2.5", "Invalid version", "MPEG version 2", "MPEG version 1"};
	private static final String[] LAYER_VALUES = {"Invalid Layer", "Layer III", "Layer II", "Layer I"};
	
	private static final long INVALID_RATE = -1;
	private static final long FREE_BIT_RATE = 0;
	private static final long[] V1_L1_BIT_RATE_VALUES = {FREE_BIT_RATE, 32000, 64000, 96000, 128000, 160000, 192000, 224000, 256000, 288000, 320000, 352000, 384000, 416000, 448000, INVALID_RATE};
	private static final long[] V1_L2_BIT_RATE_VALUES = {FREE_BIT_RATE, 32000, 48000, 96000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000, 384000, INVALID_RATE};
	private static final long[] V1_L3_BIT_RATE_VALUES = {FREE_BIT_RATE, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000, INVALID_RATE};
	private static final long[] V2_L1_BIT_RATE_VALUES = {FREE_BIT_RATE, 32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 176000, 192000, 224000, 256000, INVALID_RATE};
	private static final long[] V2_L2_L3_BIT_RATE_VALUES = {FREE_BIT_RATE, 8000, 16000, 24000, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, INVALID_RATE};
	
	private static final long[] V1_SAMPLING_RATE_VALUES = {44100, 48000, 32000, INVALID_RATE};
	private static final long[] V2_SAMPLING_RATE_VALUES = {2205, 24000, 12000, INVALID_RATE};
	private static final long[] V2_5_SAMPLING_RATE_VALUES = {32000, 16000, 8000, INVALID_RATE};
	
	private static final int[] NUMBER_OF_CHANNELS_VALUES = {2, 2, 2, 1};
	
	private final AudioStreamMetadata audioStreamMetadata;
	private final String version;
	private final String layer;
	
	public Mp3Header(ByteBuffer byteBuffer, Path filePath) {
		
		long bitRate = INVALID_RATE;
		long samplingRate = INVALID_RATE;
		int numberOfChannels = -1;
		
		// Read the 4 bytes of header bytes as unsigned
		int b1 = Utils.get1byteUnsignedInt(byteBuffer);
		int b2 = Utils.get1byteUnsignedInt(byteBuffer);
		int b3 = Utils.get1byteUnsignedInt(byteBuffer);
		int b4 = Utils.get1byteUnsignedInt(byteBuffer);
		
		if (((b1 & SYNC_BYTE_1) != SYNC_BYTE_1) || ((b2 & SYNC_BYTE_2_MASK) != SYNC_BYTE_2_MASK)) {
			
			logger.warning("Invalid MP3 Header (no 0xFF 0b111xxxxx sync bytes) in " + filePath);
			version = null;
			layer = null;
		} else {
			
			int versionIndex = (b2 & VERSION_MASK) >> 3;
			version = MP3_VERSION_VALUES[versionIndex];
			
			int layerIndex = (b2 & LAYER_MASK) >> 1;
			layer = LAYER_VALUES[layerIndex];
			
			int bitRateIndex = (b3 & BIT_RATE_MASK) >> 4;
			bitRate = getBitRateValue(bitRateIndex, versionIndex, layerIndex);
			
			int samplingRateIndex = (b3 & SAMPLING_RATE_MASK) >> 2;
			samplingRate = getSamplingRateValue(samplingRateIndex, versionIndex);
			
			int numberOfChannelIndex = (b4 & CHANNEL_MODE_MASK) >> 6;
			numberOfChannels = NUMBER_OF_CHANNELS_VALUES[numberOfChannelIndex];
		}
		
		audioStreamMetadata = AudioStreamMetadataBuilder.build(false, samplingRate, 0, bitRate, numberOfChannels, 0);

		/*
		System.out.println("Version=" + version);
		System.out.println("Layer=" + layer);
		System.out.println("BitRate=" + bitRate);
		System.out.println("SamplingRate=" + samplingRate);
		System.out.println("NumberOfChannels=" + numberOfChannels);
		*/

	}
	
	public AudioStreamMetadata getAudioStreamMetadata() {
		return audioStreamMetadata;
	}

	public String getVersion() {
		return version;
	}

	public String getLayer() {
		return layer;
	}
	
	private long getBitRateValue(int bitRateIndex, int versionIndex, int layerIndex) {
		
		if (versionIndex == VERSION_1) {
			if (layerIndex == LAYER_1) {
				return V1_L1_BIT_RATE_VALUES[bitRateIndex];
			} else if (layerIndex == LAYER_2) {
				return V1_L2_BIT_RATE_VALUES[bitRateIndex];
			} else if (layerIndex == LAYER_3) {
				return V1_L3_BIT_RATE_VALUES[bitRateIndex];
			} 
		} else if ((versionIndex == VERSION_2) || (versionIndex == VERSION_2_5)) {
			if (layerIndex == LAYER_1) {
				return V2_L1_BIT_RATE_VALUES[bitRateIndex];
			} else if ((layerIndex == LAYER_2) || (layerIndex == LAYER_3)) {
				return V2_L2_L3_BIT_RATE_VALUES[bitRateIndex];
			}
		}
		return INVALID_RATE;
	}
	
	private long getSamplingRateValue(int samplingRateIndex, int versionIndex) {
		return switch (versionIndex) {
		case VERSION_1 -> V1_SAMPLING_RATE_VALUES[samplingRateIndex];
		case VERSION_2 -> V2_SAMPLING_RATE_VALUES[samplingRateIndex];
		case VERSION_2_5 -> V2_5_SAMPLING_RATE_VALUES[samplingRateIndex];
		default -> INVALID_RATE;
		};
	}
}
