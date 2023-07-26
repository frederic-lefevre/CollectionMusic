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

public abstract class AbstractMediaFile {

	private final String source;
	private final String note;
	
	private static final String SOURCE_TITLE = "Source";
	private static final String NOTE_TITLE = "Note";
	
	protected AbstractMediaFile(String source, String note) {
		super();
		this.source = source;
		this.note = note;
	}
	
	
	public abstract String displayMediaFileDetail(String separator);
	
	public abstract String displayMediaFileSummary();
	
	protected String getSource() {
		return source;
	}
	
	protected String getNote() {
		return note;
	}
	
	protected void appendCommonMediaFileDetail(StringBuilder mediaFilesDetails, String separator) {
		mediaFilesDetails.append(getSource());
		String note = getNote();
		if ((note != null) && (!note.isEmpty())) {
			mediaFilesDetails.append(separator).append(getNote());
		}
	}
	
	protected static String getMediaFilePropertyTitles(String separator) {
		return SOURCE_TITLE + separator + NOTE_TITLE;
	}
}
