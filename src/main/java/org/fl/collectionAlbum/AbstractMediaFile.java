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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;

public abstract class AbstractMediaFile {

	private final String source;
	private final String note;
	
	private Path mediaFilePath;
	private boolean missingOrInvalidMediaFilePath;
	private boolean mediaFilePathNotFound;
	
	private static final String SOURCE_TITLE = "Source";
	private static final String NOTE_TITLE = "Note";
	private static final String FILE_LINK1 = "<a href=\"file:///";
	private static final String FILE_LINK2 = "\">";
	private static final String FILE_LINK3 = "</a>";
	
	protected AbstractMediaFile(String source, String note, Path path) {
		super();
		this.source = source;
		this.note = note;
		this.mediaFilePath = path;
		
		checkMediaFilePath();
	}
	
	public abstract String displayMediaFileDetail(String separator);
	public abstract String displayMediaFileDetailWithFileLink(String separator);
	
	public abstract String displayMediaFileSummary();
	
	protected String getSource() {
		return source;
	}
	
	protected String getNote() {
		return note;
	}
	
	protected Path getMediaFilePath() {
		return mediaFilePath;
	}
	
	public void setMediaFilePath(Path path) {
		this.mediaFilePath = path;
		checkMediaFilePath();
	}
	
	public boolean hasMissingOrInvalidMediaFilePath() {
		return missingOrInvalidMediaFilePath;
	}

	public boolean hasMediaFilePathNotFound() {
		return mediaFilePathNotFound;
	}

	private void checkMediaFilePath() {
		if (mediaFilePath == null) {
			missingOrInvalidMediaFilePath = true;
			mediaFilePathNotFound = true;
		} else {
			missingOrInvalidMediaFilePath = false;
			if (Files.exists(mediaFilePath)) {
				mediaFilePathNotFound = false;
			} else {
				mediaFilePathNotFound = true;
			}
		}
	}
	
	protected void appendCommonMediaFileDetail(StringBuilder mediaFilesDetails, String separator) {
		mediaFilesDetails.append(getSource());
		String note = getNote();
		if ((note != null) && (!note.isEmpty())) {
			mediaFilesDetails.append(separator).append(getNote());
		}
	}
	
	protected void appendCommonMediaFileDetailWithLink(StringBuilder mediaFilesDetails, String separator) {
		appendCommonMediaFileDetail(mediaFilesDetails, separator);
		if (mediaFilePath != null) {
			mediaFilesDetails.append(separator)
			.append(FILE_LINK1)
			.append(getMediaFilePath())
			.append(FILE_LINK2)
			.append(getMediaFilePath())
			.append(FILE_LINK3);
		}
	}
	
	protected String fileDetail(
			String separator, 
			BiConsumer<StringBuilder, String> particularDetailBuilder, 
			BiConsumer<StringBuilder, String> commonDetailBuilder) {
		StringBuilder audioFilesDetails = new StringBuilder();
		particularDetailBuilder.accept(audioFilesDetails, separator);
		commonDetailBuilder.accept(audioFilesDetails, separator);
		return audioFilesDetails.toString();
	}
	
	protected static String getMediaFilePropertyTitles(String separator) {
		return SOURCE_TITLE + separator + NOTE_TITLE;
	}
}
