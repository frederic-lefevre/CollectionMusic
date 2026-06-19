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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaStreamPatterns {

	public class MediaStreamPattern {
		
		private final String descriptionKey;
		private final List<MediaFile> mediaFileList;
		
		private MediaStreamPattern(String descriptionKey) {
			
			this.descriptionKey = descriptionKey;
			mediaFileList = new ArrayList<>();
		}
		
		public String descriptionKey() {
			return descriptionKey;
		}
		
		public List<MediaFile> mediaFileList() {
			return mediaFileList;
		}
	}
	
	private final Map<String, MediaStreamPattern> mediaStreamPatternMap;
	private final List<MediaStreamPattern> mediaStreamPatternList;
	
	public MediaStreamPatterns() {
		mediaStreamPatternMap = new HashMap<>();
		mediaStreamPatternList = new ArrayList<>();
	}
	
	public void clearMediaStreamPatterns() {
		mediaStreamPatternMap.clear();
		mediaStreamPatternList.clear();
	}
	
	public List<MediaStreamPattern> getMediaStreamPatternList() {
		return mediaStreamPatternList;
	}
	
	public void registerTrack(MediaFile mediaFile) {
		
		String pattern = mediaFile.getMediaStreamPattern();
		if (pattern != null) {
			MediaStreamPattern mediaStreamPattern = mediaStreamPatternMap.get(pattern);
			if (mediaStreamPattern == null) {
				mediaStreamPattern = new MediaStreamPattern(pattern);
				mediaStreamPatternMap.put(pattern, mediaStreamPattern);
				mediaStreamPatternList.add(mediaStreamPattern);
			}
			mediaStreamPattern.mediaFileList().add(mediaFile);
		}
	}
}
