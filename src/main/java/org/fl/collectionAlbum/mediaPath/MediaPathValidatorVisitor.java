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

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.mediaFile.MediaFile;
import org.fl.collectionAlbum.mediaFile.MediaFileBuilder;

class MediaPathValidatorVisitor extends SimpleFileVisitor<Path> {
	
	private static final Logger logger = Logger.getLogger(MediaPathValidatorVisitor.class.getName());
	
	private static final String COVER_START_NAME = "cover.";
	private static final Set<String> coverExtensions = Set.of("jpg", "png");
	private static final Set<String> infoFileExtensions = Set.of("pdf","crt", "bup", "ifo", "idx2", "clpi", "xml", "png", "bdjo", "jpg", "grn", "txt", "jar", "srt", "map");
	
	private final ContentNature contentNature;
	private final List<MediaFile> mediaFiles;
	private final Set<String> mediaFileExtensions;
	private Path coverPath;
	private final Level multiFolderLoggingLevel;
	private boolean topLevelVisited;
	
	MediaPathValidatorVisitor(ContentNature contentNature, Level multiFolderLoggingLevel) {
		super();
		this.contentNature = contentNature;
		this.mediaFiles = new ArrayList<MediaFile>();
		this.mediaFileExtensions = new HashSet<>();
		this.multiFolderLoggingLevel = multiFolderLoggingLevel;
		coverPath = null;
		topLevelVisited = false;
	}
	
	boolean isMediaFilePresent() {
		return mediaFiles.size() > 0;
	}
	
	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
		
		if (attr.isRegularFile()) {
			
			String extension = MediaFileInventory.getFileNameExtension(path);			
			if (extension == null) {
				logger.warning("Unexpected file with no extension in media path : " + path);
			} else {
				
				MediaFile mediaFile = MediaFileBuilder.builder(contentNature, path, extension).build();
				if (mediaFile != null) {
					// It should be a media file, part of an album
					
					mediaFileExtensions.add(extension);
					
					if (Control.isReadMediaFileMetadata()) {
						// Media file metadata parsing
						mediaFile.getMetadata();
					}
					mediaFiles.add(mediaFile);
				} else if (path.getFileName().toString().toLowerCase().startsWith(COVER_START_NAME) &&
						coverExtensions.contains(extension.toLowerCase())) {
					coverPath = path;
				} else if (infoFileExtensions.contains(extension.toLowerCase())) {
					logger.fine("Information file found in media path " + path);
				} else {
					logger.warning("Unexpected file in media path : " + path);
				}
			}
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attr) {
		
		if (topLevelVisited) {
			logger.log(multiFolderLoggingLevel, () -> "Sub folder found in media path " + file);
		} else {
			topLevelVisited = true;
		}
		return FileVisitResult.CONTINUE;
	}
	
	public List<MediaFile> getMediaFiles() {
		return mediaFiles;
	}

	public Set<String> getMediaFileExtensions() {
		return mediaFileExtensions;
	}

	public Path getCoverPath() {
		return coverPath;
	}
}