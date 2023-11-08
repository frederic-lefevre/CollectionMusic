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

import java.nio.file.Path;
import java.util.function.BiConsumer;

import com.google.gson.JsonObject;

public class LosslessAudioFile extends AbstractAudioFile {

	private final int bitDepth;
	
	private static final String BIT_DEPTH_TITLE = "Bit depth";
	
	private static final int HIGH_RES_BIT_DEPTH_THRESHOLD = 16;
	private static final double HIGH_RES_SAMPLING_RATE_THRESHOLD = 48;
	
	public LosslessAudioFile(JsonObject audioJson, AudioFileType type, String source, int bitDepth, double samplingRate, String note, Path path) {
		
		super(audioJson, type, source, samplingRate, note, path);

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
	public String displayMediaFileDetail(String separator) {		
		return fileDetail(separator, particularDetail, (sb, s) -> appendCommonAudioFileDetail(sb, s));
	}

	@Override
	public String displayMediaFileDetailWithFileLink(String separator) {
		return fileDetail(separator, particularDetail, (sb, s) -> appendCommonAudioFileDetailWithLink(sb, s));
	}
	
	BiConsumer<StringBuilder, String> particularDetail = (sb, s) -> sb.append(getBitDepth()).append(" bits").append(s);
	
	@Override
	public String displayMediaFileSummary() {
		StringBuilder audioFilesSummary = new StringBuilder();
		audioFilesSummary.append(getBitDepth()).append("-");
		audioFilesSummary.append(Double.valueOf(getSamplingRate()).intValue());
		return audioFilesSummary.toString();
	}

	public static String getAudioFilePropertyTitles(String separator) {
		return BIT_DEPTH_TITLE + separator + AbstractAudioFile.getAudioFilePropertyTitles(separator);
	}

}
