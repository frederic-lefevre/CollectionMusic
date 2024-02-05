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

package org.fl.collectionAlbum.mediaPath;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.Format.ContentNature;
import org.fl.collectionAlbum.albums.Album;

public class MediaFilePath {

	private static final Logger mLog = Control.getAlbumLog();
	
	private static final Set<String> coverExtensions = Set.of("jpg", "png");
	
	private static final Set<String> infoFileExtensions = Set.of("pdf","crt");
	
	public static final Set<String> extensionSet = new HashSet<>();

	private static final String COVER_START_NAME = "cover.";
	
	private final Path mediaFilesPath;
	
	private final Set<Album> albumsSet;
	
	private long mediaFileNumber;
	
	private Path coverPath;
	
	private final ContentNature contentNature;
	
	private String mediaFileExtension;
	
	private static class MediaFilePathMember {
		
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
	
	public MediaFilePath(Path mediaFilesPath, ContentNature cn, boolean logWarnings) {
		
		this.mediaFilesPath = mediaFilesPath;
		contentNature = cn;
		albumsSet = new HashSet<>();
		mediaFileExtension = null;
		
		try (Stream<Path> fileStream = Files.list(mediaFilesPath)) {
			
			Level level = logWarnings ? Level.WARNING : Level.INFO;
			List<MediaFilePathMember> files = fileStream
					.filter(file -> Files.isRegularFile(file))
					.map(f -> new MediaFilePathMember(f, getFileNameExtension(f), contentNature, level)).collect(Collectors.toList());
			
			mediaFileNumber = files.stream().filter(file -> file.isMediaFile()).count();
			
			Set<String> mediaExtensions = files.stream().filter(file -> file.isMediaFile()).map(f -> f.getExtension().get()).collect(Collectors.toSet());		
			if (mediaExtensions.isEmpty()) {
				mLog.log(level, "No media file found in " + mediaFilesPath.toString());
			} else if (mediaExtensions.size() == 1) {
				mediaFileExtension = mediaExtensions.iterator().next();
			} else {
				mLog.log(level, "More than 1 media file type found in " + mediaFilesPath.toString());
				mediaFileExtension = mediaExtensions.toString();
			}
			
			coverPath = files.stream().filter(f -> f.isCoverFile()).map(f -> f.getFilePath()).findFirst().orElse(null);
			
		} catch (Exception e) {
			mLog.log(Level.SEVERE, "Exception when listing files in " + mediaFilesPath, e);
			mediaFileNumber = 0;
			coverPath = null;
		}
	}

	public Path getPath() {
		return mediaFilesPath;
	}
	
	public Path getCoverPath() {
		return coverPath;
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
				.map(f -> f.substring(f.lastIndexOf(".") + 1))
				.map(ext -> { extensionSet.add(ext);
				return ext;
					});
				
	}

	public boolean hasCover() {
		return coverPath != null;
	}

	public String getMediaFileExtension() {
		return mediaFileExtension;
	}
	

}
