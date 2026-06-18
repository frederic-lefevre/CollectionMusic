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

package org.fl.collectionAlbum.mediaFile;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.mediaFile.metadata.MediaFileMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.MetadataElement;

public abstract class MediaFile {
	
	protected final Path filePath;
	private final String extension;
	protected Optional<Long> size;
	protected Optional<Boolean> hasImbeddedPicture;
	protected Optional<Boolean> isValidMediaFile;

	protected MediaFile(Path filePath, String extension) {
		super();
		this.filePath = filePath;
		this.extension = extension;
		this.isValidMediaFile = Optional.empty();
		this.hasImbeddedPicture = Optional.empty();
		this.size = Optional.empty();
	}

	public Path getFilePath() {
		return filePath;
	}

	public String getExtension() {
		return extension;
	}
	
	public Path getFileName() {
		return filePath.getFileName();
	}
	
	public abstract MediaFileMetadata getMetadata();
	public abstract boolean hasEquivalentStreamMetadata(MediaFile otherMediaFile);	
	public abstract ContentNature getContentNature();
	public abstract String getMediaStreamPattern();

	public Optional<Boolean> isValidMediaFile() {
		return isValidMediaFile;
	}
	
	public Optional<Boolean> hasImbeddedPicture() {
		return hasImbeddedPicture;
	}

	public Optional<Long> getSize() {
		return size;
	}
	
	public boolean haveMetadata(MetadataElement<?>... metadataElements) {
		
		return Optional.ofNullable(getMetadata())
				.map(metadata -> Stream.of(metadataElements)
						.allMatch(metadataElement -> Optional.ofNullable(metadata.getStreamMetadata())
								.map(streamMetadata -> streamMetadata.get(metadataElement.name()).value().equals(metadataElement.value()))
								.orElse(false)))
				.orElse(false);
	}
}