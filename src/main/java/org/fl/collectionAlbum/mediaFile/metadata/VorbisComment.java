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
import java.util.logging.Logger;

import org.fl.collectionAlbum.mediaFile.Utils;

public class VorbisComment {

	private static final String SEPARATOR_STRING = "=";
	
	private static final Logger logger = Logger.getLogger(VorbisComment.class.getName());
	
	// Normalized fields
	private static final String TRACKNUMBER = "TRACKNUMBER";
	private static final String TITLE = "TITLE";
	private static final String ALBUM = "ALBUM";
	private static final String ARTIST = "ARTIST";
	private static final String ALBUMARTIST = "ALBUMARTIST";
	private static final String COMPOSER = "COMPOSER";
	private static final String GENRE= "GENRE";
	private static final String DATE = "DATE";
	
	private final String vendorField;
	private final NormalizedAudioMetadataTags normalizedAudioMetadataTags;
	private final Map<String, MetadataElement<?>> additionalFieldsMap;
	
	public VorbisComment(ByteBuffer byteBuffer, Path filePath) {
		
		additionalFieldsMap = new HashMap<>();
		
		int vendorFieldLength = Utils.get4bytesUnsignedIntLittleEndian(byteBuffer);
		
		vendorField = Utils.decodeByteBuffer(byteBuffer, vendorFieldLength, StandardCharsets.UTF_8);
		
		final int numberOfFields = Utils.get4bytesUnsignedIntLittleEndian(byteBuffer);
		
		final NormalizedAudioMetadataTagsBuilder normalizedAudioMetadataTagsBuilder = NormalizedAudioMetadataTagsBuilder.builder();
		for (int i=0; i < numberOfFields; i++) {
			
			int fieldLength = Utils.get4bytesUnsignedIntLittleEndian(byteBuffer);
			String field =  Utils.decodeByteBuffer(byteBuffer, fieldLength, StandardCharsets.UTF_8);
			
			int separatorIndex = field.indexOf(SEPARATOR_STRING);
			if (separatorIndex > 0) {
				
				String fieldKey = field.substring(0, separatorIndex).toUpperCase();
				final String fieldValue;
				if (field.length() > separatorIndex + 1) {
					fieldValue = field.substring(separatorIndex+1);
				} else {
					fieldValue = "";
				}
				
				if (fieldKey.equals(TRACKNUMBER)) {
					normalizedAudioMetadataTagsBuilder.trackNumber(fieldValue);
				} else if (fieldKey.equals(TITLE)) {
					normalizedAudioMetadataTagsBuilder.trackTitle(fieldValue);
				} else if (fieldKey.equals(ALBUM)) {
					normalizedAudioMetadataTagsBuilder.albumTitle(fieldValue);
				} else if (fieldKey.equals(ARTIST)) {
					normalizedAudioMetadataTagsBuilder.artist(fieldValue);
				} else if (fieldKey.equals(ALBUMARTIST)) {
					normalizedAudioMetadataTagsBuilder.albumArtist(fieldValue);
				} else if (fieldKey.equals(COMPOSER)) {
					normalizedAudioMetadataTagsBuilder.composer(fieldValue);
				} else if (fieldKey.equals(GENRE)) {
					normalizedAudioMetadataTagsBuilder.genre(fieldValue);
				} else if (fieldKey.equals(DATE)) {
					normalizedAudioMetadataTagsBuilder.date(fieldValue);
				} else {
					MetadataElement<String> additionalTag = new MetadataElement<String>(fieldKey, fieldValue);
					additionalFieldsMap.put(fieldKey, additionalTag);
				}

			} else if (separatorIndex == 0) {
				logger.severe("Vorbis field without a key: " + field);
			} else {
				logger.severe("Vorbis field without '=' separator: " + field);
			}
		}
		normalizedAudioMetadataTags = normalizedAudioMetadataTagsBuilder.build(filePath);
	}

	public NormalizedAudioMetadataTags getNormalizedAudioMetadataTags() {
		return normalizedAudioMetadataTags;
	}

	public Map<String, MetadataElement<?>> getAdditionalFieldsMap() {
		return additionalFieldsMap;
	}

	public String getVendorField() {
		return vendorField;
	}
}
