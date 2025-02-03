/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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

package org.fl.collectionAlbum.format;

import java.util.Set;
import java.util.logging.Logger;

import org.fl.collectionAlbum.mediaPath.MediaFilePath;

import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class AbstractAudioFile extends AbstractMediaFile {

	private final static Logger albumLog = Logger.getLogger(AbstractAudioFile.class.getName());
	
	private final AudioFileType type;
	private final double samplingRate;
	
	private static final String TYPE_TITLE = "Type";
	private static final String SAMPLING_RATE_TITLE = "Sampling Rate";
	
	protected AbstractAudioFile(ObjectNode audioJson, AudioFileType type, String source, double samplingRate, String note, Set<MediaFilePath> mediaFilePaths) {
		super(audioJson, source, note, mediaFilePaths);
		this.type = type;
		this.samplingRate = samplingRate;
		
		// Check type versus media file paths extensions
		if ((mediaFilePaths != null) && !mediaFilePaths.isEmpty()) {
			mediaFilePaths.stream()
				.filter(mediaFilePath -> !type.getExtensions().contains(mediaFilePath.getMediaFileExtension()))
				.forEach(mediaFilePath -> albumLog.warning("Extension mismatch for " + audioJson + "\n Waited extension " + mediaFilePath.getMediaFileExtension()));
		} 
	}
	
	public AudioFileType getType() {
		return type;
	}

	public double getSamplingRate() {
		return samplingRate;
	}
	
	protected void appendCommonAudioFileDetail(StringBuilder audioFilesDetails, String separator, boolean withPrefix) {
		appendCommonAudioSpecs(audioFilesDetails, separator, withPrefix);
		appendCommonMediaFileDetail(audioFilesDetails, separator, withPrefix);
	}
	
	protected void appendCommonAudioFileDetailWithLink(StringBuilder audioFilesDetails, String separator, boolean withPrefix) {
		appendCommonAudioSpecs(audioFilesDetails, separator, withPrefix);
		appendCommonMediaFileDetailWithLink(audioFilesDetails, separator, withPrefix);
	}
	
	protected void appendCommonAudioFileDetailTitles(StringBuilder audioFilesDetails, String separator) {
		audioFilesDetails.append(SAMPLING_RATE_TITLE).append(separator).append(TYPE_TITLE).append(separator);
		appendCommonMediaFileDetailTitles(audioFilesDetails, separator);
	}
	
	private void appendCommonAudioSpecs(StringBuilder audioFilesDetails, String separator, boolean withPrefix) {
		audioFilesDetails
			.append(getSamplingRate())
			.append(" KHz")
			.append(separator);
		if (withPrefix) {
			audioFilesDetails.append(TYPE_TITLE).append(": ");
		}
		audioFilesDetails
			.append(getType())
			.append(separator);
	}
	
	public abstract boolean isHighRes();
	public abstract boolean isLossLess();

}
