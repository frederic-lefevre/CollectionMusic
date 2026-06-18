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

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.format.AudioFileType;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.mediaFile.metadata.AudioMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.AudioStreamMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.MediaFileMetadata;

public abstract class AudioFile extends MediaFile {

	private AudioMetadata audioMetadata;
	
	protected AudioFile(Path filePath, AudioFileType audioFileType) {
		super(filePath, audioFileType.getExtension());
		audioMetadata = null;
	}
	
	protected abstract AudioMetadata parseMetadata();
	
	@Override
	public MediaFileMetadata getMetadata() {
		return getAudioMetadata();
	}

	public AudioMetadata getAudioMetadata() {

		if ((audioMetadata == null) && Control.isReadMediaFileMetadata() && isValidMediaFile.isEmpty()) {
			// Lazy get
			audioMetadata = parseMetadata();
		}
		return audioMetadata;
	}
	
	@Override
	public boolean hasEquivalentStreamMetadata(MediaFile otherMediaFile) {
		
		if (otherMediaFile instanceof AudioFile otherAudioFile) {
			
			if (getAudioMetadata() == null) {
				return otherAudioFile.getAudioMetadata() == null;
			} else if (otherAudioFile.getAudioMetadata() == null) {
				return false;
			} else {
				AudioStreamMetadata streamInfo =  getAudioMetadata().getAudioStreamMetadata();
				AudioStreamMetadata otherStreamInfo =   otherAudioFile.getAudioMetadata().getAudioStreamMetadata();
				if (streamInfo == null) {
					return otherStreamInfo == null;
				} else if (otherStreamInfo == null) {
					return false;
				} else {
					return streamInfo.isEquivalentTo(otherStreamInfo);
				}
				
			}
		} else {
			return false;
		}		
	}
	
	@Override
	public ContentNature getContentNature() {
		return ContentNature.AUDIO;
	}
	
	@Override
	public String getMediaStreamPattern() {
		AudioMetadata metadata = getAudioMetadata();
		if (metadata == null) {
			return null;
		} else {
			AudioStreamMetadata streamInfo =  metadata.getAudioStreamMetadata();
			return streamInfo.bitDepth().value() + "bits - " + 
					streamInfo.samplingRate().value() + "Hz - " + 
					streamInfo.bitRate().value() + "bits/s - " + 
					streamInfo.numberOfChannels().value() + " channels - " +
					(streamInfo.isLossless().value()?"sans perte":"avec perte"); 
		}

	}
}
