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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Id3v2Tag {

	private static final String BAND = "TPE2";
	private static final byte[] BAND_BYTES = BAND.getBytes(StandardCharsets.ISO_8859_1);
	
	private static final String ALBUM = "TALB";
	private static final byte[] ALBUM_BYTES = ALBUM.getBytes(StandardCharsets.ISO_8859_1);
	
	private static final String ARTIST = "TPE1";
	private static final byte[] ARTIST_BYTES = ARTIST.getBytes(StandardCharsets.ISO_8859_1);
	
	private static final String COMPOSER = "TCOM";
	private static final byte[] COMPOSER_BYTES = COMPOSER.getBytes(StandardCharsets.ISO_8859_1);
	
	private static final String CONDUCTOR = "TPE3";
	private static final byte[] CONDUCTOR_BYTES = CONDUCTOR.getBytes(StandardCharsets.ISO_8859_1);
	
	private static final String GENRE = "TCON";
	private static final byte[] GENRE_BYTES = GENRE.getBytes(StandardCharsets.ISO_8859_1);
	
	private static final String LENGTH = "TLEN";
	private static final byte[] LENGTH_BYTES = LENGTH.getBytes(StandardCharsets.ISO_8859_1);
	
	private static final String DATE = "TDAT";
	private static final byte[] DATE_BYTES = DATE.getBytes(StandardCharsets.ISO_8859_1);
	
	private static final String TITLE = "TIT2";
	private static final byte[] TITLE_BYTES = TITLE.getBytes(StandardCharsets.ISO_8859_1);
	
	private static final String YEAR = "TYER";
	private static final byte[] YEAR_BYTES = YEAR.getBytes(StandardCharsets.ISO_8859_1);
	  
	private final NormalizedAudioMetadataTags normalizedAudioMetadataTags;
	private final Map<String, MetadataElement<?>> additionalFieldsMap;

	  
	public Id3v2Tag(ByteBuffer byteBuffer, long id3HeaderLength, Path filePath) {
		
		additionalFieldsMap = new HashMap<>();
		
		normalizedAudioMetadataTags = NormalizedAudioMetadataTagsBuilder.builder().albumTitle("Album title").artist("artist").trackNumber("01").genre("genre").trackTitle("titre").build(filePath);

	}
	
	public NormalizedAudioMetadataTags getNormalizedAudioMetadataTags() {
		return normalizedAudioMetadataTags;
	}

	public Map<String, MetadataElement<?>> getAdditionalFieldsMap() {
		return additionalFieldsMap;
	}
}
