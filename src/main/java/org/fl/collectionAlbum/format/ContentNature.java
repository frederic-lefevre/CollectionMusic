/*
 MIT License

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

package org.fl.collectionAlbum.format;

import java.util.Set;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.json.AbstractMediaFileParser;
import org.fl.collectionAlbum.json.AudioFileParser;
import org.fl.collectionAlbum.json.VideoFileParser;

public enum ContentNature { 
	AUDIO("audio", JsonMusicProperties.AUDIO_FILE, Set.of("flac", "mp3", "wma", "aiff", "m4a", "wav"), true), 
	VIDEO("video", JsonMusicProperties.VIDEO_FILE, Set.of("m2ts", "mkv", "mpls", "vob", "m4v", "mp4", "bdmv"), false);
	
	private final String nom;
	private final String jsonProperty;
	private final Set<String> fileExtensions;
	private final boolean strictCheckings;
	private AbstractMediaFileParser mediaFileParser;
	
	private ContentNature(String n, String jp, Set<String> exts, boolean sc) {
		nom = n;
		jsonProperty = jp;
		fileExtensions = exts;
		strictCheckings = sc;
	}
	
	public String getNom() {
		return nom;
	}
	
	public String getJsonProperty() {
		return jsonProperty;
	}

	public Set<String> getFileExtensions() {
		return fileExtensions;
	}
	
	public AbstractMediaFileParser getMediaFileParser() {
		if (mediaFileParser == null) {
			if (this == AUDIO) {
				mediaFileParser = new AudioFileParser();
			} else if (this == VIDEO) {
				mediaFileParser = new VideoFileParser();
			}
		}
		return mediaFileParser;
	}
	
	public boolean strictCheckings() {
		return strictCheckings;
	}
}