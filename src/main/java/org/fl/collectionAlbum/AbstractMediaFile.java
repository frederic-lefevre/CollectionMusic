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
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public abstract class AbstractMediaFile {

	private final String source;
	private final String note;
	private final JsonObject mediaJson;
	
	private Set<Path> mediaFilePaths;
	private boolean missingOrInvalidMediaFilePath;
	private boolean mediaFilePathNotFound;
	
	private static final String SOURCE_TITLE = "Source";
	private static final String NOTE_TITLE = "Note";
	private static final String FILE_LINK1 = "<a href=\"file:///";
	private static final String FILE_LINK2 = "\">";
	private static final String FILE_LINK3 = "</a>";
	
	protected AbstractMediaFile(JsonObject mediaJson, String source, String note, Set<Path> paths) {
		super();
		this.mediaJson = mediaJson;
		this.source = source;
		this.note = note;
		this.mediaFilePaths = paths;
		
		checkMediaFilePaths();
	}
	
	public abstract String displayMediaFileDetail(String separator);
	public abstract String displayMediaFileDetailWithFileLink(String separator);
	
	public abstract String displayMediaFileSummary();
	
	public String getSource() {
		return source;
	}
	
	public String getNote() {
		return note;
	}
	
	public Set<Path> getMediaFilePaths() {
		return mediaFilePaths;
	}
	
	public void addMediaFilePath(Path path) {
		if (mediaFilePaths == null) {
			mediaFilePaths = new HashSet<>();
		}
		mediaFilePaths.add(path);
		
		modifiyJson();
		checkMediaFilePaths();
	}
	
	public void replaceMediaFilePath(Path path) {

		mediaFilePaths = new HashSet<>();
		mediaFilePaths.add(path);
		
		modifiyJson();
		checkMediaFilePaths();
	}
	
	public void setMediaFilePath(Set<Path> paths) {

		mediaFilePaths = paths;
		
		if (mediaFilePaths != null) {
			modifiyJson();
			checkMediaFilePaths();
		}
	}
	
	public boolean hasMissingOrInvalidMediaFilePath() {
		return missingOrInvalidMediaFilePath;
	}

	public boolean hasMediaFilePathNotFound() {
		return mediaFilePathNotFound;
	}

	private void modifiyJson() {
		JsonArray mediaPathsJson = new JsonArray();
		mediaFilePaths.forEach(filePath -> mediaPathsJson.add(filePath.toString()));
		
		mediaJson.add(JsonMusicProperties.LOCATION, mediaPathsJson);
	}
	
	private void checkMediaFilePaths() {
		if ((mediaFilePaths == null) || mediaFilePaths.isEmpty()) {
			missingOrInvalidMediaFilePath = true;
			mediaFilePathNotFound = true;
		} else {
			missingOrInvalidMediaFilePath = false;
			
			mediaFilePathNotFound = mediaFilePaths.stream().anyMatch(path -> !Files.exists(path));
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
		if (mediaFilePaths != null) {
			mediaFilePaths.forEach(mediaFilePath -> {
				mediaFilesDetails.append(separator)
				.append(FILE_LINK1)
				.append(mediaFilePath)
				.append(FILE_LINK2)
				.append(mediaFilePath)
				.append(FILE_LINK3);
			});
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
