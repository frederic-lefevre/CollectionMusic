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
import java.util.function.BiConsumer;

import org.fl.collectionAlbum.mediaPath.MediaFilePath;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class LossyAudioFile extends AbstractAudioFile {
	
	private final double bitRate;

	private static final String BIT_RATE_TITLE = "Bit rate";
	
	public LossyAudioFile(ObjectNode audioJson, AudioFileType type, String source, double bitRate, double samplingRate, String note, Set<MediaFilePath> mediaFilePaths) {
		
		super(audioJson, type, source, samplingRate, note, mediaFilePaths);
		this.bitRate = bitRate;
	}

	public double getBitRate() {
		return bitRate;
	}
	
	@Override
	public boolean isHighRes() {
		return false;
	}
	
	@Override
	public boolean isLossLess() {
		return false;
	}
	
	@Override
	public String displayMediaFileSummary() {
		StringBuilder audioFilesSummary = new StringBuilder();
		audioFilesSummary.append(getType().name()).append(" ");
		audioFilesSummary.append(Double.valueOf(getBitRate()).intValue());
		return audioFilesSummary.toString();
	}
	
	@Override
	public String displayMediaFileDetail(String separator, boolean withPrefix) {		
		return fileDetail(separator, particularDetail, (sb, s) -> appendCommonAudioFileDetail(sb, s, withPrefix));
	}

	@Override
	public String displayMediaFileDetailWithFileLink(String separator, boolean withPrefix) {
		return fileDetail(separator, particularDetail, (sb, s) -> appendCommonAudioFileDetailWithLink(sb, s, withPrefix));
	}
	
	BiConsumer<StringBuilder, String> particularDetail = (sb, s) -> sb.append(getBitRate()).append(" kbit/s").append(s);

	@Override
	public String displayMediaFileDetailTitles(String separator) {
		StringBuilder audioFilesDetailTitles = new StringBuilder();
		audioFilesDetailTitles.append(BIT_RATE_TITLE).append(separator);
		appendCommonAudioFileDetailTitles(audioFilesDetailTitles, separator);
		return audioFilesDetailTitles.toString();
	}
}
