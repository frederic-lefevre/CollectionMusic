/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

import com.google.gson.JsonObject;

public class VideoFile extends AbstractMediaFile {

	private final int width;
	private final int height;
	private final VideoFileType type;
	
	public VideoFile(JsonObject videoJson, VideoFileType type, String source, int width, int height, String note, Set<MediaFilePath> mediaFilePaths) {
		super(videoJson, source, note, mediaFilePaths);
		this.type = type;
		this.width = width;
		this.height = height;
	}

	protected VideoFileType getType() {
		return type;
	}

	protected int getWidth() {
		return width;
	}

	protected int getHeight() {
		return height;
	}

	@Override
	public String displayMediaFileDetail(String separator) {		
		return fileDetail(separator, particularDetail, (sb, s) -> appendCommonMediaFileDetail(sb, s));
	}

	@Override
	public String displayMediaFileDetailWithFileLink(String separator) {
		return fileDetail(separator, particularDetail, (sb, s) -> appendCommonMediaFileDetailWithLink(sb, s));
	}
	
	BiConsumer<StringBuilder, String> particularDetail = (sb, s) ->
		sb.append(getWidth())
			.append("x")
			.append(getHeight())
			.append(" px")
			.append(s)
			.append(getType())
			.append(s);
	
	@Override
	public String displayMediaFileSummary() {
		return getHeight() + "p";	
	}

}
