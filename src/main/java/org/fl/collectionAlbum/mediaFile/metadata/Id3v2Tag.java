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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.mediaFile.Utils;

public class Id3v2Tag {

	private static final Logger logger = Logger.getLogger(Id3v2Tag.class.getName());
	
	private static final int KEY_FIELD_LENGTH = 4;
	private static final int FLAG_LENGTH = 2;
	
	private static final String ALBUM_ARTIST = "TPE2";	
	private static final String ALBUM = "TALB";
	private static final String ARTIST = "TPE1";	
	private static final String COMPOSER = "TCOM";	
	private static final String CONDUCTOR = "TPE3";	
	private static final String GENRE = "TCON";	
	private static final String TITLE = "TIT2";	
	private static final String YEAR = "TYER";
	private static final String RECORDING_YEAR = "TDRC";
	private static final String TRACK_NUMBER = "TRCK";
	private static final String ATTACHED_PICTURE = "APIC";
	private static final String POPM = "POPM";
	private static final String PRIV = "PRIV";
	private static final String MCDI = "MCDI";
	  
	private static final String CONDUCTOR_STRING = "Chef d'orchestre";	
	
	private static final byte[] END_OF_TAG_BYTES = {0, 0, 0, 0};
	private static final String END_OF_TAG = new String(END_OF_TAG_BYTES, StandardCharsets.ISO_8859_1);
	
	private static final Charset[] encoding = {
			StandardCharsets.ISO_8859_1, 
			StandardCharsets.UTF_16, 
			StandardCharsets.UTF_16BE, 
			StandardCharsets.UTF_8};
	
	private static final int[] END_OF_FRAME_LENGTH = { 1, 2, 2, 1};
	
	private final NormalizedAudioMetadataTags normalizedAudioMetadataTags;
	private final Map<String, MetadataElement<?>> additionalFieldsMap;
	private final NormalizedAudioMetadataTagsBuilder normalizedAudioMetadataTagsBuilder;
	private boolean hasImbeddedPicture;
	  
	public Id3v2Tag(ByteBuffer byteBuffer, Path filePath) {

		hasImbeddedPicture = false;
		additionalFieldsMap = new HashMap<>();
		normalizedAudioMetadataTagsBuilder = NormalizedAudioMetadataTagsBuilder.builder();

		Id3v2TagParsing(byteBuffer, filePath, normalizedAudioMetadataTagsBuilder, additionalFieldsMap);
		
		normalizedAudioMetadataTags = normalizedAudioMetadataTagsBuilder.build(filePath);
	}
	
	public Id3v2Tag(ByteBuffer byteBuffer, Path filePath,  Map<String, MetadataElement<?>> additionalFieldsMap, NormalizedAudioMetadataTagsBuilder normalizedAudioMetadataTagsBuilder) {

		hasImbeddedPicture = false;
		this.additionalFieldsMap = additionalFieldsMap;
		this.normalizedAudioMetadataTagsBuilder = normalizedAudioMetadataTagsBuilder;

		Id3v2TagParsing(byteBuffer, filePath, normalizedAudioMetadataTagsBuilder, additionalFieldsMap);
		
		normalizedAudioMetadataTags = normalizedAudioMetadataTagsBuilder.build(filePath);
	}
	
	private void Id3v2TagParsing(
			ByteBuffer byteBuffer, 
			Path filePath, 
			NormalizedAudioMetadataTagsBuilder normalizedAudioMetadataTagsBuilder, 
			Map<String, MetadataElement<?>> additionalFieldsMap) {
		
		try {
			boolean tagRemain = true;
			do {
				String tagKey = Utils.decodeByteBuffer(byteBuffer, KEY_FIELD_LENGTH, StandardCharsets.ISO_8859_1);

				if (END_OF_TAG.equals(tagKey)) {
					tagRemain = false;
				} else {

					int fieldLength = Utils.get4bytesUnsignedInt(byteBuffer);  
					// Length of the following bytes : charset(1) + BOM id (FF FE, if it is UTF-16*, 00 00 if ISO_8859_1)
					// + field(n) + 1 (ISO_8859_1, UTF-8) or 2 bytes (UTF-16*) at 00 depending on encoding (!)
					if (fieldLength > byteBuffer.remaining() + 8) {
						throw new Id3ParsingException("Found too long tag field length: " + fieldLength + 
								"\n in file " + filePath + 
								"\n for tag key=" + tagKey + 
								" at buffer offset 0x" + Integer.toHexString(byteBuffer.position()) +
								"\n Remaining in buffer=" + byteBuffer.remaining());
					}

					// Skip flags
					byteBuffer.position(byteBuffer.position() + FLAG_LENGTH);
					
					int charSetIndex;
					int fieldContentLength = fieldLength;
					if ((tagKey.charAt(0) == 'W') || POPM.equals(tagKey) || PRIV.equals(tagKey) || MCDI.equals(tagKey)) {
						// Special case for URL fields which has no encoding info
						charSetIndex = 0;
					} else {
						charSetIndex = Utils.get1byteUnsignedInt(byteBuffer);
						if (charSetIndex > END_OF_FRAME_LENGTH.length - 1) {
							throw new Id3ParsingException("Found out of bound charset index: " + charSetIndex + "\n in file " + filePath + "\n for tag key=" + tagKey);
						}
						fieldContentLength = fieldContentLength -1;
					}

					Charset fieldCharset = encoding[charSetIndex];
					int endOfFrameLength = END_OF_FRAME_LENGTH[charSetIndex];	
					fieldContentLength = fieldContentLength -  endOfFrameLength;

					if (ALBUM.equals(tagKey)) {
						String fieldContent = getFieldContent(byteBuffer, fieldContentLength, endOfFrameLength, fieldCharset);
						normalizedAudioMetadataTagsBuilder.albumTitle(fieldContent);

					} else if (ARTIST.equals(tagKey)) {
						String fieldContent = getFieldContent(byteBuffer, fieldContentLength, endOfFrameLength, fieldCharset);
						normalizedAudioMetadataTagsBuilder.artist(fieldContent);

					} else if (GENRE.equals(tagKey)) {
						String fieldContent = getFieldContent(byteBuffer, fieldContentLength, endOfFrameLength, fieldCharset);
						String genre = Id3Genre.getGenre(fieldContent);
						normalizedAudioMetadataTagsBuilder.genre(genre);

					} else if (TITLE.equals(tagKey)) {
						String fieldContent = getFieldContent(byteBuffer, fieldContentLength, endOfFrameLength, fieldCharset);
						normalizedAudioMetadataTagsBuilder.trackTitle(fieldContent);

					} else if (TRACK_NUMBER.equals(tagKey)) {
						String fieldContent = getFieldContent(byteBuffer, fieldContentLength, endOfFrameLength, fieldCharset);
						normalizedAudioMetadataTagsBuilder.trackNumber(fieldContent);

					} else if (YEAR.equals(tagKey) || RECORDING_YEAR.equals(tagKey)) {
						String fieldContent = getFieldContent(byteBuffer, fieldContentLength, endOfFrameLength, fieldCharset);
						normalizedAudioMetadataTagsBuilder.date(fieldContent);

					} else if (COMPOSER.equals(tagKey)) {
						String fieldContent = getFieldContent(byteBuffer, fieldContentLength, endOfFrameLength, fieldCharset);
						normalizedAudioMetadataTagsBuilder.composer(fieldContent);

					} else if (ALBUM_ARTIST.equals(tagKey)) {
						String fieldContent = getFieldContent(byteBuffer, fieldContentLength, endOfFrameLength, fieldCharset);
						normalizedAudioMetadataTagsBuilder.albumArtist(fieldContent);

					} else if (ATTACHED_PICTURE.equals(tagKey)) {
						// Ignore and skip embedded picture
						byteBuffer.position(byteBuffer.position() + fieldContentLength + endOfFrameLength);
						hasImbeddedPicture = true;

					} else if (CONDUCTOR.equals(tagKey)) {
						String fieldContent = getFieldContent(byteBuffer, fieldContentLength, endOfFrameLength, fieldCharset);

						MetadataElement<String> additionalTag = new MetadataElement<String>(CONDUCTOR_STRING, fieldContent);
						additionalFieldsMap.put(CONDUCTOR_STRING, additionalTag);

					} else {
						String fieldContent = getFieldContent(byteBuffer, fieldContentLength, endOfFrameLength, fieldCharset);
						MetadataElement<String> additionalTag = new MetadataElement<String>(tagKey, fieldContent);
						additionalFieldsMap.put(tagKey, additionalTag);
					}					
				}
			} while (tagRemain);

		} catch (Exception e) {
			logger.log(Level.WARNING, "Unsupported ID3 Tag for file " + filePath, e);
		}
	}
	
	private String getFieldContent(ByteBuffer byteBuffer, int fieldContentLength, int endOfFrameLength, Charset fieldCharset) {
		
		String fieldContent = Utils.decodeByteBuffer(byteBuffer, fieldContentLength, fieldCharset);
		
		byte[] endOfFrameBytes = new byte[endOfFrameLength];
		
		byteBuffer.get(endOfFrameBytes);
		if (endOfFrameBytes[0] != 0) {
			// assume there is no end of string bytes and the end is part of the content
			// Rewind to the start of content and take all the bytes (no end of frame bytes)
			byteBuffer.position(byteBuffer.position() - (fieldContentLength + endOfFrameLength));
			return Utils.decodeByteBuffer(byteBuffer, fieldContentLength + endOfFrameLength, fieldCharset);
		} else {
			return fieldContent;
		}
	}
	
	public NormalizedAudioMetadataTags getNormalizedAudioMetadataTags() {
		return normalizedAudioMetadataTags;
	}

	public NormalizedAudioMetadataTagsBuilder getNormalizedAudioMetadataTagsBuilder() {
		return normalizedAudioMetadataTagsBuilder;
	}

	public Map<String, MetadataElement<?>> getAdditionalFieldsMap() {
		return additionalFieldsMap;
	}

	public boolean hasImbeddedPicture() {
		return hasImbeddedPicture;
	}
}
