/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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

package org.fl.collectionAlbum.mediaPath;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.format.ContentNature;

class MediaFilePathMember {
	
	private static final Logger mLog = Logger.getLogger(MediaFilePathMember.class.getName());
	
	private static final String COVER_START_NAME = "cover.";
	private static final Set<String> coverExtensions = Set.of("jpg", "png");
	private static final Set<String> infoFileExtensions = Set.of("pdf","crt");
	
	private final Path filePath;
	private final Optional<String> extension;
	private final boolean isMediaFile;
	private final boolean isCoverFile;
	private final boolean isInfoFile;
	
	public MediaFilePathMember(Path fp, Optional<String> ext, ContentNature mediaContentNature, Level logLevel) {
		super();
		filePath = fp;
		extension = ext;
		
		isMediaFile = extension.filter(e -> mediaContentNature.getFileExtensions().contains(e.toLowerCase())).isPresent();
		
		isCoverFile = filePath.getFileName().toString().toLowerCase().startsWith(COVER_START_NAME) &&
				extension
				.filter(e -> coverExtensions.contains(e.toLowerCase()))
				.isPresent();
		
		isInfoFile = extension
				.filter(e -> infoFileExtensions.contains(e.toLowerCase()))
				.isPresent();
		
		if (!isMediaFile && !isCoverFile && !isInfoFile) {
			mLog.log(logLevel, "Unexpected file in media path : " + filePath);
		}
	}

	public Path getFilePath() {
		return filePath;
	}

	public Optional<String> getExtension() {
		return extension;
	}

	public boolean isMediaFile() {
		return isMediaFile;
	}

	public boolean isCoverFile() {
		return isCoverFile;
	}
}