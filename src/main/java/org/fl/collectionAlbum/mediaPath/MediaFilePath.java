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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.mediaFile.MediaFile;
import org.fl.collectionAlbum.mediaFile.MediaFileBuilder;

public class MediaFilePath {

	private static final Logger mLog = Logger.getLogger(MediaFilePath.class.getName());
	
	private static final String COVER_START_NAME = "cover.";
	private static final Set<String> coverExtensions = Set.of("jpg", "png");
	private static final Set<String> infoFileExtensions = Set.of("pdf","crt", "bup", "ifo", "idx2");
	
	private final Path mediaFilesPath;
	private final ContentNature contentNature;
	private final Set<Album> albumsSet;
	private Path coverPath;
	private List<MediaFile> mediaFiles;
	private boolean hasEquivalentMetadata;
	
	private String mediaFileExtension;
	
	public MediaFilePath(Path mediaFilesPath, ContentNature contentNature, boolean logWarnings) {
		
		this.mediaFilesPath = mediaFilesPath;
		this.contentNature = contentNature;
		albumsSet = new HashSet<>();
		mediaFileExtension = null;
		coverPath = null;
		hasEquivalentMetadata = false;
		Set<String> mediaFileExtensions = new HashSet<>();
		
		try (Stream<Path> fileStream = Files.list(mediaFilesPath)) {
			
			Level level = logWarnings ? Level.WARNING : Level.INFO;
			mediaFiles = fileStream
					.filter(path -> Files.isRegularFile(path))
					.map(path -> { 
						String extension = getFileNameExtension(path);
						
						if (extension == null) {
							mLog.log(level, "Unexpected file with no extension in media path : " + path);
							return null;
						} else {
							MediaFile mediaFile = MediaFileBuilder.builder(contentNature, path, extension).build();
							if (mediaFile != null) {

								mediaFileExtensions.add(extension);
								
								if (Control.isReadMediaFileMetadata()) {
									mediaFile.getMetadata();
								}
								return mediaFile;
							} else if (path.getFileName().toString().toLowerCase().startsWith(COVER_START_NAME) &&
									coverExtensions.contains(extension.toLowerCase())) {
								coverPath = path;
								return null;
							} else if (infoFileExtensions.contains(extension.toLowerCase())) {
								return null;
							} else {
								mLog.log(level, "Unexpected file in media path : " + path);
								return null;
							}
						}
					})
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
	
			// Check media files extension (should all be the same)
			if (mediaFileExtensions.isEmpty()) {
				mLog.log(level, "No media file found directly under " + mediaFilesPath.toString());
			} else if (mediaFileExtensions.size() == 1) {
				mediaFileExtension = mediaFileExtensions.iterator().next();
			} else {
				mLog.log(level, "More than 1 media file type found in " + mediaFilesPath.toString());
				mediaFileExtension = mediaFileExtensions.toString();
			}
			
			hasEquivalentMetadata = (mediaFiles == null) ||
					mediaFiles.isEmpty() ||
					mediaFiles.stream().allMatch(m -> mediaFiles.get(0).hasEquivalentStreamMetadata(m));
			
		} catch (Exception e) {
			mLog.log(Level.SEVERE, "Exception when listing files in " + mediaFilesPath, e);			
		}
	}

	public Path getPath() {
		return mediaFilesPath;
	}
	
	public ContentNature getContentNature() {
		return contentNature;
	}

	public Path getCoverPath() {
		return coverPath;
	}
	
	public List<MediaFile> getMediaFiles() {
		return mediaFiles;
	}

	public void addAlbum(Album album) {
		albumsSet.add(album);
	}
	
	public Set<Album> getAlbumSet() {
		return albumsSet;
	}
	
	public int getMediaFileNumber() {
		return mediaFiles.size();
	}

	public static boolean isMediaFileName(Path file, ContentNature mediaContentNature) {

		String extension = getFileNameExtension(file);
		if (extension != null) {
			return mediaContentNature.getFileExtensions().contains(extension.toLowerCase());
		} else {
			return false;
		}
	}
	
	private static String getFileNameExtension(Path path) {
		
		String fileName = path.toString();
	    int dotIndex = fileName.lastIndexOf(".");
	    if (dotIndex >= 0) {
	        return fileName.substring(dotIndex + 1);
	    } else {
	    	return null;
	    }		
	}

	public boolean hasCover() {
		return coverPath != null;
	}

	public String getMediaFileExtension() {
		return mediaFileExtension;
	}
	
	public boolean hasEquivalentStreamMetadata() {
		return hasEquivalentMetadata;
	}
	
	// TODO completer ci dessous
	public boolean checkVersusAlbumDeclaration(int bitDepth, double samplingRate) {
		
		mediaFiles.stream().map(m -> m.getMetadata().getStreamMetadata());
		return true;
	}

}
