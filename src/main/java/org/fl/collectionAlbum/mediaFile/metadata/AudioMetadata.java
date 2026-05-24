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

public class AudioMetadata implements MediaFileMetadata {

	private static final Logger logger = Logger.getLogger(AudioMetadata.class.getName());
	
	private static final List<String> ACCEPTABLE_NON_NORMALIZED_TAGS = List.of("LENGTH", "TRACKTOTAL", "TOTALTRACKS");
	
	private final AudioStreamMetadata audioStreamMetadata;
	private final NormalizedAudioMetadataTags normalizedAudioMetadataTags;
	private final Map<String, MetadataTag<?>> additionnalTags;
	private final Map<String, MetadataTag<?>> normalizedAudioMetadataTagsMap;
	private final Map<String, MetadataTag<?>> allTags;
	
	public AudioMetadata(AudioStreamMetadata audioStreamMetadata, NormalizedAudioMetadataTags audioMetadataTags, Map<String, MetadataTag<?>> additionnalTags, Path filePath) {
		super();
		this.normalizedAudioMetadataTags = audioMetadataTags;
		this.audioStreamMetadata = audioStreamMetadata;
		this.additionnalTags = additionnalTags;
		
		this.normalizedAudioMetadataTagsMap = normalizedAudioMetadataTags.getNormalizedTags();
		
		allTags = new HashMap<>(normalizedAudioMetadataTagsMap);
		allTags.putAll(additionnalTags);
		
		if (logger.isLoggable(Level.INFO) && (additionnalTags.size() > 0)) {
			if (additionnalTags.keySet().stream()
				.filter(tag -> !ACCEPTABLE_NON_NORMALIZED_TAGS.contains(tag))
				.findAny()
				.isPresent()) {
				logger.info(filePath + " has undesired non normalized audio metadata");
			} else if (logger.isLoggable(Level.FINE)) {
				logger.fine(filePath + " has acceptable non normalized audio metadata");
			}
		}
	}

	@Override
	public MetadataTag<?> getTag(String name) {
		
		String nameUpperCase = name.toUpperCase();
		if (normalizedAudioMetadataTagsMap.containsKey(nameUpperCase)) {
			return normalizedAudioMetadataTagsMap.get(nameUpperCase);
		} else {
			return additionnalTags.get(nameUpperCase);
		}
	}

	@Override
	public Map<String, MetadataTag<?>> getAllTags() {
		return allTags;
	}

	@Override
	public Map<String, String> getStreamMetadataDescription() {
		return audioStreamMetadata.getDescription();
	}

	public AudioStreamMetadata getAudioStreamMetadata() {
		return audioStreamMetadata;
	}

	public NormalizedAudioMetadataTags getNormalizedAudioMetadataTags() {
		return normalizedAudioMetadataTags;
	}

	public Map<String, MetadataTag<?>> getAdditionnalTags() {
		return additionnalTags;
	}

}
