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

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NormalizedAudioMetadataTagsBuilder {

	private static final Logger logger = Logger.getLogger(NormalizedAudioMetadataTagsBuilder.class.getName());
	
	private static final int INVALID_INT_NUMBER = -2;
	private static final int ABSENT_INT_NUMBER = -1;
	private static final String ABSENT_FIELD = "";
	
	private static final List<String> OPTIONAL_FIELDS = List.of(NormalizedAudioMetadataTags.ALBUMARTIST, NormalizedAudioMetadataTags.COMPOSER, NormalizedAudioMetadataTags.DATE);
	
	private int trackNumber;
	private String trackTitle;
	private String albumTitle;
	private String artist;
	private String albumArtist;
	private String composer;
	private String genre;
	private String date;
	private Map<String,NumberFormatException> invalidTrackNumberExceptionMap;
	
	private NormalizedAudioMetadataTagsBuilder() {
		trackNumber = ABSENT_INT_NUMBER;
		trackTitle = ABSENT_FIELD;
		albumTitle = ABSENT_FIELD;
		artist = ABSENT_FIELD;
		albumArtist = ABSENT_FIELD;
		composer = ABSENT_FIELD;
		genre = ABSENT_FIELD;
		date = ABSENT_FIELD;
		invalidTrackNumberExceptionMap = new HashMap<>();
	}
	
	public static NormalizedAudioMetadataTagsBuilder builder() {
		return new NormalizedAudioMetadataTagsBuilder();
	}
	
	public NormalizedAudioMetadataTagsBuilder trackTitle(String trackTitle) {
		this.trackTitle = trackTitle;
		return this;
	}
	
	public NormalizedAudioMetadataTagsBuilder albumTitle(String albumTitle) {
		this.albumTitle =albumTitle;
		return this;
	}
	
	public NormalizedAudioMetadataTagsBuilder artist(String artist) {
		this.artist = artist;
		return this;
	}
	
	public NormalizedAudioMetadataTagsBuilder albumArtist(String albumArtist) {
		this.albumArtist = albumArtist;
		return this;
	}
	
	public NormalizedAudioMetadataTagsBuilder composer(String composer) {
		this.composer = composer;
		return this;
	}
	
	public NormalizedAudioMetadataTagsBuilder genre(String genre) {
		this.genre = genre;
		return this;
	}
	
	public NormalizedAudioMetadataTagsBuilder date(String date) {
		this.date = date;
		return this;
	}
	
	public NormalizedAudioMetadataTagsBuilder trackNumber(String trackNumber) {
		try {
			this.trackNumber = Integer.parseInt(trackNumber);
			if (this.trackNumber < 1) {
				this.trackNumber = INVALID_INT_NUMBER;
			}
		} catch (NumberFormatException e) {
			invalidTrackNumberExceptionMap.put(NormalizedAudioMetadataTags.TRACKNUMBER, e);
			this.trackNumber = INVALID_INT_NUMBER;
		}
		return this;
	}
	
	public NormalizedAudioMetadataTags build(Path audioFilePath) {
		
		checkIntField(trackNumber, NormalizedAudioMetadataTags.TRACKNUMBER, audioFilePath);
		checkStringField(trackTitle, NormalizedAudioMetadataTags.TITLE, audioFilePath);
		checkStringField(albumTitle, NormalizedAudioMetadataTags.ALBUM, audioFilePath);
		checkStringField(artist, NormalizedAudioMetadataTags.ARTIST, audioFilePath);
		checkStringField(albumArtist, NormalizedAudioMetadataTags.ALBUMARTIST, audioFilePath);
		checkStringField(composer, NormalizedAudioMetadataTags.COMPOSER, audioFilePath);
		checkStringField(genre, NormalizedAudioMetadataTags.GENRE, audioFilePath);
		checkStringField(date, NormalizedAudioMetadataTags.DATE, audioFilePath);
		return new NormalizedAudioMetadataTags(
				new MetadataElement<Integer>(NormalizedAudioMetadataTags.TRACKNUMBER, trackNumber), 
				new MetadataElement<String>(NormalizedAudioMetadataTags.TITLE, trackTitle), 
				new MetadataElement<String>(NormalizedAudioMetadataTags.ALBUM, albumTitle), 
				new MetadataElement<String>(NormalizedAudioMetadataTags.ARTIST, artist), 
				new MetadataElement<String>(NormalizedAudioMetadataTags.ALBUMARTIST, albumArtist), 
				new MetadataElement<String>(NormalizedAudioMetadataTags.COMPOSER, composer), 
				new MetadataElement<String>(NormalizedAudioMetadataTags.GENRE, genre), 
				new MetadataElement<String>( NormalizedAudioMetadataTags.DATE, date));
	}
	
	private void checkStringField(String field, String fieldName, Path audioFilePath) {
		
		if (field == null) {
			logger.severe(audioFilePath + " has a null " + fieldName + " metadata field");
		} else if (field.equals(ABSENT_FIELD) && !OPTIONAL_FIELDS.contains(fieldName)) {
			logger.warning(audioFilePath + " has no " + fieldName + " metadata field");
		}
	}
	
	private void checkIntField(int field, String fieldName, Path audioFilePath) {
		
		if (invalidTrackNumberExceptionMap.get(fieldName) != null) {
			logger.log(Level.WARNING, audioFilePath + " has an invalid  " + fieldName + " metadata field", invalidTrackNumberExceptionMap.get(fieldName));
		} else if (field == INVALID_INT_NUMBER) {
			logger.log(Level.WARNING, audioFilePath + " has an invalid  " + fieldName + " metadata field");
		} else if ((field == ABSENT_INT_NUMBER) && !OPTIONAL_FIELDS.contains(fieldName)) {
			logger.warning(audioFilePath + " has no " + fieldName + " metadata field");
		}
	}
}
