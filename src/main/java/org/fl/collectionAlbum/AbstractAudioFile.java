/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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

import java.nio.file.Path;

public abstract class AbstractAudioFile extends AbstractMediaFile {

	private final AudioFileType type;
	private final double samplingRate;
	
	private static final String TYPE_TITLE = "Type";
	private static final String SAMPLING_RATE_TITLE = "Sampling Rate";
	
	protected AbstractAudioFile(AudioFileType type, String source, double samplingRate, String note, Path path) {
		super(source, note, path);
		this.type = type;
		this.samplingRate = samplingRate;
	}
	
	protected AudioFileType getType() {
		return type;
	}

	protected double getSamplingRate() {
		return samplingRate;
	}
	
	protected void appendCommonAudioFileDetail(StringBuilder audioFilesDetails, String separator) {
		audioFilesDetails.append(getSamplingRate()).append(" KHz").append(separator);
		audioFilesDetails.append(getType()).append(separator);
		appendCommonMediaFileDetail(audioFilesDetails, separator);
	}
	
	public abstract boolean isHighRes();
	public abstract boolean isLossLess();
	
	protected static String getAudioFilePropertyTitles(String separator) {
		return SAMPLING_RATE_TITLE + separator + TYPE_TITLE + separator + AbstractMediaFile.getMediaFilePropertyTitles(separator);
	}
}
