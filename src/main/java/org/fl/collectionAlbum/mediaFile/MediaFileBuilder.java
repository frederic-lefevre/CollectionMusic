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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.fl.collectionAlbum.format.AudioFileType;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.format.VideoFileType;

public class MediaFileBuilder {
	
	private static final Map<String, AudioFileType> audioExtensionsMap = new HashMap<>();
	private static final Map<String, VideoFileType> videoExtensionsMap = new HashMap<>();
	
	private final ContentNature contentNature;
	private final Path filePath;
	private final String extension;
	
	private MediaFileBuilder() {
		this.contentNature = null;
		this.filePath = null;
		this.extension = null;
	}
	
	private MediaFileBuilder(ContentNature contentNature, Path filePath, String extension) {
		
		this.contentNature = contentNature;
		this.filePath = filePath;
		this.extension = extension;
	}
	
	public static MediaFileBuilder builder(ContentNature contentNature, Path filePath, String extension) {
		return new MediaFileBuilder(contentNature, filePath, extension);
	}
	
	public MediaFile build() {

		if (extension == null) {
			return null;
		}

		return switch (contentNature) {
		case AUDIO -> {
			AudioFileType audioFileType = getAudioFileType(extension);

			if (audioFileType == AudioFileType.FLAC) {
				yield new FlacAudioFile(filePath, extension);
			} else if (audioFileType == AudioFileType.WAV) {
				yield new WavAudioFile(filePath, extension);
			} else if (audioFileType == AudioFileType.AIFF) {
				yield new AiffAudioFile(filePath, extension);
			} else if (audioFileType == AudioFileType.MP3) {
				yield new Mp3AudioFile(filePath, extension);
			} else if (audioFileType == AudioFileType.M4A) {
				yield new M4aAudioFile(filePath, extension);
			} else {
				yield null;
			}
		}
		case VIDEO -> {
			VideoFileType videoFileType = getVideoFileType(extension);

			if (videoFileType == null) {
				yield null;
			} else {
				yield new VideoFile(filePath, extension);
			}
		}
		};
	}

	private static AudioFileType getAudioFileType(String extension) {

		
		AudioFileType audioFileType = audioExtensionsMap.get(extension);
		if (audioFileType == null) {
			
			String lowerExtension = extension.toLowerCase();
			audioFileType = Stream.of(AudioFileType.values())
				.filter(a -> lowerExtension.equals(a.getExtension()))
				.findFirst()
				.orElse(null);
			
			if (audioFileType != null) {
				audioExtensionsMap.put(extension, audioFileType);
			}
		} 
		return audioFileType;
	}
	
	private static VideoFileType getVideoFileType(String extension) {
		
		VideoFileType videoFileType = videoExtensionsMap.get(extension);
		if (videoFileType == null) {
			String lowerExtension = extension.toLowerCase();
			videoFileType = Stream.of(VideoFileType.values())
					.filter(a -> lowerExtension.equals(a.getExtension()))
					.findFirst()
					.orElse(null);
			if (videoFileType != null) {
				videoExtensionsMap.put(extension, videoFileType);
			}
		}
		return videoFileType;
	}
}
