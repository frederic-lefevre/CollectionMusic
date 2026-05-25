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

package org.fl.collectionAlbum.format;

import java.nio.file.Path;
import java.util.function.Function;

import org.fl.collectionAlbum.mediaFile.AiffAudioFile;
import org.fl.collectionAlbum.mediaFile.FlacAudioFile;
import org.fl.collectionAlbum.mediaFile.M4aAudioFile;
import org.fl.collectionAlbum.mediaFile.MediaFile;
import org.fl.collectionAlbum.mediaFile.Mp3AudioFile;
import org.fl.collectionAlbum.mediaFile.WavAudioFile;

public enum AudioFileType implements MediaFileType {
	
	FLAC(true, "flac", (path) -> new FlacAudioFile(path)),
	WAV(true, "wav", (path) -> new WavAudioFile(path)),
	AIFF(true, "aiff", (path) -> new AiffAudioFile(path)),
	MP3(false, "mp3", (path) -> new Mp3AudioFile(path)),
	M4A(false, "m4a", (path) -> new M4aAudioFile(path));
	
	private final boolean isLossLess;
	private final String extension;
	private final Function<Path, MediaFile> mediaFileConstructor;
	
	private AudioFileType(boolean isLossLess, String extension, Function<Path, MediaFile> mediaFileConstructor) {
		this.isLossLess = isLossLess;
		this.extension = extension;
		this.mediaFileConstructor = mediaFileConstructor;
	}
	
	@Override
	public boolean isLossLess() {
		return isLossLess;
	}

	@Override
	public String getExtension() {
		return extension;
	}

	@Override
	public Function<Path, MediaFile> mediaFileConstructor() {
		return mediaFileConstructor;
	}
}
