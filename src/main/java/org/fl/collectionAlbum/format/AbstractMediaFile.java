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

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class AbstractMediaFile {

	private static final Logger logger = Logger.getLogger(AbstractMediaFile.class.getName());
	
	private final String source;
	private final String note;
	private final ObjectNode mediaJson;
	
	private Set<MediaFilePath> mediaFilePaths;
	private boolean missingOrInvalidMediaFilePath;
	private boolean mediaFilePathNotFound;
	
	private static final String SOURCE_TITLE = "Source";
	private static final String NOTE_TITLE = "Note";
	private static final String FILE_LINK1 = "<a href=\"";
	private static final String FILE_LINK2 = "\">";
	private static final String FILE_LINK3 = "</a>";
	
	protected AbstractMediaFile(ObjectNode mediaJson, String source, String note, Set<MediaFilePath> mediaFilePaths) {
		super();
		this.mediaJson = mediaJson;
		this.source = source;
		this.note = note;
		this.mediaFilePaths = mediaFilePaths;
		
		checkMediaFilePaths();
	}
	
	public abstract String displayMediaFileDetail(String separator, boolean withPrefix);
	public abstract String displayMediaFileDetailWithFileLink(String separator, boolean withPrefix);
	public abstract String displayMediaFileDetailTitles(String separator);
	
	public abstract String displayMediaFileSummary();
	
	public String getSource() {
		return source;
	}
	
	public String getNote() {
		return note;
	}
	
	public Set<MediaFilePath> getMediaFilePaths() {
		return mediaFilePaths;
	}
	
	public void setMediaFilePath(Set<MediaFilePath> mediaFilePaths, URI mediaFilesRoot) {

		this.mediaFilePaths = mediaFilePaths;
		
		if (this.mediaFilePaths != null) {
			modifiyJson(mediaFilesRoot);
			checkMediaFilePaths();
		}
	}
	
	public boolean hasMissingOrInvalidMediaFilePath() {
		return missingOrInvalidMediaFilePath;
	}

	public boolean hasMediaFilePathNotFound() {
		return mediaFilePathNotFound;
	}

	private void modifiyJson(URI mediaFilesRoot) {
		ArrayNode mediaPathsJson = JsonNodeFactory.instance.arrayNode();
		mediaFilePaths.stream()
			.map(mediaFilePath -> mediaFilePath.getPath())
			.map(absolutePath -> getRelativeMediaLocationPath(absolutePath, mediaFilesRoot))
			.filter(Objects::nonNull)
			.forEach(relativePathString -> mediaPathsJson.add(relativePathString));
		
		mediaJson.set(JsonMusicProperties.LOCATION, mediaPathsJson);
	}
	
	private String getRelativeMediaLocationPath(Path absoluteLocationPath, URI baseMediaFileUri) {
		
		if (! Files.exists(absoluteLocationPath)) {
			logger.severe("The media file path does not exists: " + absoluteLocationPath);
		}
		
		URI absoluteLocationUri = absoluteLocationPath.toUri();
		if (! absoluteLocationUri.toString().startsWith(baseMediaFileUri.toString())) {
			logger.severe("The media file " + absoluteLocationPath + " is not under the base folder " + baseMediaFileUri);
			return null;
		} else {
			URI relativeResultUri = baseMediaFileUri.relativize(absoluteLocationUri);
			
			// getPath() decodes the URI
			String relativeResultUriString = relativeResultUri.getPath();

			return relativeResultUriString;
		}
	}
	
	private void checkMediaFilePaths() {
		if ((mediaFilePaths == null) || mediaFilePaths.isEmpty()) {
			missingOrInvalidMediaFilePath = true;
			mediaFilePathNotFound = true;
		} else {
			missingOrInvalidMediaFilePath = false;
			
			mediaFilePathNotFound = mediaFilePaths.stream().anyMatch(mediaFilePath -> !Files.exists(mediaFilePath.getPath()));
		}
	}
	
	protected void appendCommonMediaFileDetail(StringBuilder mediaFilesDetails, String separator, boolean withPrefix) {
		if (withPrefix) {
			mediaFilesDetails.append(SOURCE_TITLE).append(": ");
		}
		mediaFilesDetails.append(getSource());
		String note = getNote();
		if ((note != null) && (!note.isEmpty())) {
			mediaFilesDetails.append(separator).append(getNote());
		}
	}
	
	protected void appendCommonMediaFileDetailWithLink(StringBuilder mediaFilesDetails, String separator, boolean withPrefix) {
		appendCommonMediaFileDetail(mediaFilesDetails, separator, withPrefix);
		if (mediaFilePaths != null) {
			mediaFilePaths.forEach(mediaFilePath -> {
				mediaFilesDetails.append(separator)
				.append(FILE_LINK1)
				.append(mediaFilePath.getPath().toUri())
				.append(FILE_LINK2)
				.append(mediaFilePath.getPath())
				.append(FILE_LINK3);
			});
		}
	}
	
	protected void appendCommonMediaFileDetailTitles(StringBuilder mediaFilesDetails,String separator) {
		mediaFilesDetails.append(SOURCE_TITLE).append(separator).append(NOTE_TITLE);
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

}
