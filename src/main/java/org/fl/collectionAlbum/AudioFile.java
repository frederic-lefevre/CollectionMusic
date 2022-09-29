/*
 MIT License

Copyright (c) 2017, 2022 Frederic Lefevre

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

package org.fl.collectionAlbum;

import java.util.Optional;
import java.util.logging.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AudioFile {

	private final String type;
	private final String source;
	private final int bitDepth;
	private final double samplingRate;
	private final String note;
	
	private static final String DEFAULT_TYPE = "FLAC";
	private static final String DEFAULT_SOURCE = "CD";
	private static final int DEFAULT_BIT_DEPTH = 16;
	private static final double DEFAULT_SAMPLING_RATE = 44.1;
	
	public AudioFile(JsonObject audioFileJson, Logger fl) {
		
		if (audioFileJson != null) {
			type = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.TYPE))
					.map(JsonElement::getAsString)
					.orElse(DEFAULT_TYPE);
			source = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.SOURCE))
					.map(JsonElement::getAsString)
					.orElse(DEFAULT_SOURCE);
			bitDepth = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.BIT_DEPTH))
					.map(JsonElement::getAsInt)
					.orElse(DEFAULT_BIT_DEPTH);
			samplingRate = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.SAMPLING_RATE))
					.map(JsonElement::getAsDouble)
					.orElse(DEFAULT_SAMPLING_RATE);
			note = Optional.ofNullable(audioFileJson.get(JsonMusicProperties.NOTE))
					.map(JsonElement::getAsString)
					.orElse(null);
		} else {
			fl.severe("Constructor AudioFile null parameter");
			type = null;
			source = null;
			bitDepth = 0;
			samplingRate = 0;
			note = null;
		}
	}

	public String getType() {
		return type;
	}

	public String getSource() {
		return source;
	}

	public int getBitDepth() {
		return bitDepth;
	}

	public double getSamplingRate() {
		return samplingRate;
	}
	
	public String getNote() {
		return note;
	}
	
	public String displayAudioFileDetail(String separator) {
		StringBuilder audioFilesDetails = new StringBuilder();
		audioFilesDetails.append(getBitDepth()).append(" bits").append(separator);
		audioFilesDetails.append(getSamplingRate()).append(" KHz").append(separator);
		audioFilesDetails.append(getType()).append(separator);
		audioFilesDetails.append(getSource());
		String note = getNote();
		if ((note != null) && (!note.isEmpty())) {
			audioFilesDetails.append(separator).append(getNote());
		}
		return audioFilesDetails.toString();
	}

	public String displayAudioFileSummary() {
		StringBuilder audioFilesSummary = new StringBuilder();
		audioFilesSummary.append(getBitDepth()).append("-");
		audioFilesSummary.append(Double.valueOf(getSamplingRate()).intValue());
		return audioFilesSummary.toString();
	}
}
