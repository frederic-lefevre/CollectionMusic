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
import org.fl.collectionAlbum.albums.Album;

public class MediaFilePath {

	private static final Logger mLog = Control.getAlbumLog();
	
	private static final Set<String> mediaFileExtensions = 
			Set.of("flac", "mp3", "wma", "aiff", "FLAC", "MP3", "m4a",
					"m2ts", "mkv", "mpls", "VOB", "wav", "m4v", "mp4", "bdmv");
	
	public static final Set<String> extensionSet = new HashSet<>();

	private static final String COVER_NAME = "cover.jpg";
	
	private final Path mediaFilesPath;
	
	private final Set<Album> albumsSet;
	
	private long mediaFileNumber;
	
	private boolean hasCover;
	
	public MediaFilePath(Path mediaFilesPath) {
		this.mediaFilesPath = mediaFilesPath;
		albumsSet = new HashSet<>();
		
		try (Stream<Path> fileStream = Files.list(mediaFilesPath)) {
			
			List<Path> files = fileStream.collect(Collectors.toList());
			mediaFileNumber = files.stream().filter(file -> Files.isRegularFile(file) && isMediaFileName(file)).count();
			hasCover = files.stream().anyMatch(path -> path.getFileName().toString().equalsIgnoreCase(COVER_NAME));
		} catch (Exception e) {
			mLog.log(Level.SEVERE, "Exception when listing files in " + mediaFilesPath, e);
			mediaFileNumber = 0;
			hasCover = false;
		}
	}

	public Path getPath() {
		return mediaFilesPath;
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

	public static boolean isMediaFileName(Path file) {

		return getFileNameExtension(file)
				.filter(extension -> mediaFileExtensions.contains(extension))
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
		return hasCover;
	}
}
