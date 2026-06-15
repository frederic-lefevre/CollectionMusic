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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.mediaFile.MediaFile;

public class MediaFilePath {
	
	private final Path mediaFilesPath;
	private final ContentNature contentNature;
	private final Set<Album> albumsSet;
	private final Path coverPath;
	private final List<MediaFile> mediaFiles;
	private final Optional<Boolean> hasEquivalentMetadata;
	private final String mediaFileExtension;
	
	public MediaFilePath(Path mediaFilesPath, ContentNature contentNature, List<MediaFile> mediaFiles, Path coverPath, String mediaFileExtension) {
		
		this.mediaFilesPath = mediaFilesPath;
		this.contentNature = contentNature;
		this.mediaFiles = mediaFiles;
		albumsSet = new HashSet<>();
		this.mediaFileExtension = mediaFileExtension;
		this.coverPath = coverPath;
			
		if (Control.isReadMediaFileMetadata()) {
			hasEquivalentMetadata = Optional.of(
					(mediaFiles == null) ||
					mediaFiles.isEmpty() ||
					mediaFiles.stream().allMatch(m -> mediaFiles.get(0).hasEquivalentStreamMetadata(m)));
		} else {
			hasEquivalentMetadata = Optional.empty();
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

	public boolean hasCover() {
		return coverPath != null;
	}

	public String getMediaFileExtension() {
		return mediaFileExtension;
	}
	
	// All tracks in the media file path have equivalent metadata:
	// same file type, same number of channels, sampling rate, bit depth, bit rate, lossless or not
	public Optional<Boolean> hasEquivalentStreamMetadata() {
		return hasEquivalentMetadata;
	}
}
