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

package org.fl.collectionAlbum.mediaFile.metadata;

import java.util.Map;

public record NormalizedAudioMetadataTags(
	MetadataElement<Integer> trackNumber,
	MetadataElement<String> trackTitle,
	MetadataElement<String> albumTitle,
	MetadataElement<String> artist,
	MetadataElement<String> albumArtist,
	MetadataElement<String> composer,
	MetadataElement<String> genre,
	MetadataElement<String> date) {

	static final String TRACKNUMBER = "TRACKNUMBER";
	static final String TITLE = "TITLE";
	static final String ALBUM = "ALBUM";
	static final String ARTIST = "ARTIST";
	static final String ALBUMARTIST = "ALBUMARTIST";
	static final String COMPOSER = "COMPOSER";
	static final String GENRE= "GENRE";
	static final String DATE = "DATE";
	
	public Map<String, MetadataElement<?>> getNormalizedTags() {
		
		return Map.of(
				TRACKNUMBER, trackNumber,
				TITLE, trackTitle,
				ALBUM, albumTitle,
				ARTIST, artist,
				ALBUMARTIST, albumArtist,
				COMPOSER, composer,
				GENRE, genre,
				DATE, date);
	}
	
}
