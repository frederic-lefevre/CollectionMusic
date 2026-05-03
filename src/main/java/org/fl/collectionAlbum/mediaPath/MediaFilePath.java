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
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.format.ContentNature;

public class MediaFilePath {

	private static final Logger mLog = Logger.getLogger(MediaFilePath.class.getName());
	
	private static final String COVER_START_NAME = "cover.";
	private static final Set<String> coverExtensions = Set.of("jpg", "png");
	private static final Set<String> infoFileExtensions = Set.of("pdf","crt");
	
	private final Path mediaFilesPath;
	private final Set<Album> albumsSet;
	private long mediaFileNumber;
	private Path coverPath;
	private final ContentNature contentNature;
	private List<MediaFile> mediaFiles;
	
	private String mediaFileExtension;
	
	public MediaFilePath(Path mediaFilesPath, ContentNature cn, boolean logWarnings) {
		
		this.mediaFilesPath = mediaFilesPath;
		contentNature = cn;
		albumsSet = new HashSet<>();
		mediaFileExtension = null;
		coverPath = null;
		mediaFileNumber = 0;
		Set<String> mediaExtensions = new HashSet<>();
		
		try (Stream<Path> fileStream = Files.list(mediaFilesPath)) {
			
			Level level = logWarnings ? Level.WARNING : Level.INFO;
			mediaFiles = fileStream
					.filter(path -> Files.isRegularFile(path))
					.map(path -> { 
						Optional<String> extension = getFileNameExtension(path);
						MediaFile m = new MediaFile(path, extension);
						
						if (extension.filter(e -> contentNature.getFileExtensions().contains(e.toLowerCase())).isPresent()) {
							mediaFileNumber++;
							mediaExtensions.add(extension.get());
							return m;
						} else if (path.getFileName().toString().toLowerCase().startsWith(COVER_START_NAME) &&
								extension
								.filter(e -> coverExtensions.contains(e.toLowerCase()))
								.isPresent()) {
							coverPath = path;
							return null;
						} else if (extension
									.filter(e -> infoFileExtensions.contains(e.toLowerCase()))
									.isPresent()) {
							 return null;
						} else {
							mLog.log(level, "Unexpected file in media path : " + path);
							return null;
						}	
					})
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
	
			// Check media files extension (should all be the same)
			if (mediaExtensions.isEmpty()) {
				mLog.log(level, "No media file found in " + mediaFilesPath.toString());
			} else if (mediaExtensions.size() == 1) {
				mediaFileExtension = mediaExtensions.iterator().next();
			} else {
				mLog.log(level, "More than 1 media file type found in " + mediaFilesPath.toString());
				mediaFileExtension = mediaExtensions.toString();
			}
			
		} catch (Exception e) {
			mLog.log(Level.SEVERE, "Exception when listing files in " + mediaFilesPath, e);			
		}
	}

	public Path getPath() {
		return mediaFilesPath;
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
	
	public long getMediaFileNumber() {
		return mediaFileNumber;
	}

	public static boolean isMediaFileName(Path file, ContentNature mediaContentNature) {

		return getFileNameExtension(file)
				.filter(extension -> mediaContentNature.getFileExtensions().contains(extension.toLowerCase()))
				.isPresent();
	}

	private static Optional<String> getFileNameExtension(Path filename) {
		return Optional.ofNullable(filename)
				.map(f -> f.toString())
				.filter(f -> f.contains("."))
				.map(f -> f.substring(f.lastIndexOf(".") + 1));			
	}

	public boolean hasCover() {
		return coverPath != null;
	}

	public String getMediaFileExtension() {
		return mediaFileExtension;
	}

}
