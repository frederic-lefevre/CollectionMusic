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

package org.fl.collectionAlbum.gui.table;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.renderer.CollectionBooleanRenderer;
import org.fl.collectionAlbum.gui.renderer.CollectionNumberRenderer;
import org.fl.collectionAlbum.mediaFile.MediaFile;
import org.fl.collectionAlbum.utils.CollectionUtils;

import static org.fl.collectionAlbum.mediaFile.metadata.AudioStreamMetadata.*;
import static org.fl.collectionAlbum.mediaFile.metadata.NormalizedAudioMetadataTags.*;

public class MediaFileTableColumns {

	private static final String FICHIERS = "Fichiers";
	
	private static final List<TableColumnParameter<MediaFile>> mediaFileCommonColumnsParameters = List.of(
			new TableColumnParameter<MediaFile>(FICHIERS, 200, null, null, String.class, (f) -> f.getFileName())
			);
	
	private static Function<MediaFile, Object> streamInfoGetter(String metadataName) {
		return (f) -> Optional.ofNullable(f.getMetadata()).map(m -> m.getStreamMetadata().get(metadataName).value()).orElse(null);
	}
			
	private static Function<MediaFile, Object> normalizedTagGetter(String metadataName) {
		return (f) -> Optional.ofNullable(f.getMetadata()).map(m -> m.getNormalizedTags().get(metadataName).value()).orElse(null);
	}
	
	private static final List<TableColumnParameter<MediaFile>> audioStreamInfoColumnsParameters = List.of(
			new TableColumnParameter<MediaFile>(IS_LOSSLESS, 70, new CollectionBooleanRenderer(), 
					null, Boolean.class, streamInfoGetter(IS_LOSSLESS)),
			new TableColumnParameter<MediaFile>(SAMPLING_RATE, 120, new CollectionNumberRenderer(), 
					new CollectionUtils.LongComparator(), Long.class, streamInfoGetter(SAMPLING_RATE)),
			new TableColumnParameter<MediaFile>(BIT_DEPTH, 100, new CollectionNumberRenderer(), 
					new CollectionUtils.IntegerComparator(), Integer.class, streamInfoGetter(BIT_DEPTH)),
			new TableColumnParameter<MediaFile>(BIT_RATE, 100, new CollectionNumberRenderer(), 
					new CollectionUtils.LongComparator(), Long.class, streamInfoGetter(BIT_RATE)),
			new TableColumnParameter<MediaFile>(NUMBER_OF_CHANNELS, 60, new CollectionNumberRenderer(), 
					new CollectionUtils.IntegerComparator(), Integer.class, streamInfoGetter(NUMBER_OF_CHANNELS)),
			new TableColumnParameter<MediaFile>(TRACK_DURATION, 80, new CollectionNumberRenderer(), 
					new CollectionUtils.LongComparator(), Long.class, streamInfoGetter(TRACK_DURATION))
			);
	
	private static final List<TableColumnParameter<MediaFile>> normalizedAudioTagsColumnParameters = List.of(
			new TableColumnParameter<MediaFile>(TRACKNUMBER, 100, new CollectionNumberRenderer(), 
					new CollectionUtils.IntegerComparator(), Integer.class, normalizedTagGetter(TRACKNUMBER)),
			new TableColumnParameter<MediaFile>(TITLE, 200, null, 
					null, String.class, normalizedTagGetter(TITLE)),
			new TableColumnParameter<MediaFile>(ALBUM, 200, null, 
					null, String.class, normalizedTagGetter(ALBUM)),
			new TableColumnParameter<MediaFile>(ARTIST, 150, null, 
					null, String.class, normalizedTagGetter(ARTIST)),
			new TableColumnParameter<MediaFile>(ALBUMARTIST, 100, null, 
					null, String.class, normalizedTagGetter(ALBUMARTIST)),
			new TableColumnParameter<MediaFile>(COMPOSER, 100, null, 
					null, String.class, normalizedTagGetter(COMPOSER)),
			new TableColumnParameter<MediaFile>(GENRE, 100, null, 
					null, String.class, normalizedTagGetter(GENRE)),
			new TableColumnParameter<MediaFile>(DATE, 80, null, 
					null, String.class, normalizedTagGetter(DATE))
			);
	
	private static final List<TableColumnParameter<MediaFile>> audioColumnsParameters = 
			Stream.of(mediaFileCommonColumnsParameters, audioStreamInfoColumnsParameters, normalizedAudioTagsColumnParameters)
			.flatMap(Collection::stream).toList();
	
	private static final List<TableColumnParameter<MediaFile>> videoColumnsParameters = mediaFileCommonColumnsParameters;
	
	public static List<TableColumnParameter<MediaFile>> mediaColumnsParameters(ContentNature contentNature) {
		
		return switch(contentNature) {
		case AUDIO -> audioColumnsParameters;
		case VIDEO -> videoColumnsParameters;
		};
	}
	
}
