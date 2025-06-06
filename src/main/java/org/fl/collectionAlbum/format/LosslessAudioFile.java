/*
 MIT License

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
import java.util.function.BiConsumer;

import org.fl.collectionAlbum.mediaPath.MediaFilePath;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class LosslessAudioFile extends AbstractAudioFile {

	private final int bitDepth;
	
	private static final String BIT_DEPTH_TITLE = "Bit depth";
	
	private static final int HIGH_RES_BIT_DEPTH_THRESHOLD = 16;
	private static final double HIGH_RES_SAMPLING_RATE_THRESHOLD = 48;
	
	public LosslessAudioFile(ObjectNode audioJson, AudioFileType type, String source, int bitDepth, double samplingRate, String note, Set<MediaFilePath> mediaFilePaths) {
		
		super(audioJson, type, source, samplingRate, note, mediaFilePaths);

		this.bitDepth = bitDepth;
	}

	public int getBitDepth() {
		return bitDepth;
	}
	
	@Override
	public boolean isHighRes() {
		return (bitDepth > HIGH_RES_BIT_DEPTH_THRESHOLD) || (getSamplingRate() > HIGH_RES_SAMPLING_RATE_THRESHOLD);
	}
	
	@Override
	public boolean isLossLess() {
		return true;
	}
	
	@Override
	public String displayMediaFileDetail(String separator, boolean withPrefix) {		
		return fileDetail(separator, particularDetail, (sb, s) -> appendCommonAudioFileDetail(sb, s, withPrefix));
	}

	@Override
	public String displayMediaFileDetailWithFileLink(String separator, boolean withPrefix) {
		return fileDetail(separator, particularDetail, (sb, s) -> appendCommonAudioFileDetailWithLink(sb, s, withPrefix));
	}
	
	BiConsumer<StringBuilder, String> particularDetail = (sb, s) -> sb.append(getBitDepth()).append(" bits").append(s);
	
	@Override
	public String displayMediaFileSummary() {
		StringBuilder audioFilesSummary = new StringBuilder();
		audioFilesSummary.append(getBitDepth()).append("-");
		audioFilesSummary.append(Double.valueOf(getSamplingRate()).intValue());
		return audioFilesSummary.toString();
	}

	@Override
	public String displayMediaFileDetailTitles(String separator) {
		StringBuilder audioFilesDetailTitles = new StringBuilder();
		audioFilesDetailTitles.append(BIT_DEPTH_TITLE).append(separator);
		appendCommonAudioFileDetailTitles(audioFilesDetailTitles, separator);
		return audioFilesDetailTitles.toString();
	}

}
