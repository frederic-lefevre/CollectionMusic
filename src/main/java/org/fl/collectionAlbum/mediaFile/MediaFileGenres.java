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
import java.util.Set;
import java.util.logging.Logger;

import org.fl.collectionAlbum.mediaFile.metadata.NormalizedAudioMetadataTags;

public class MediaFileGenres {

	private static final Logger logger = Logger.getLogger(MediaFileGenres.class.getName());
	
	private static final String PAS_DE_GENRE = "Genre absent";
	
	private final Map<String, List<MediaFile>> genresMap;
	
	public MediaFileGenres() {
		genresMap = new HashMap<>();
	}
	
	public void clearMediaFileGenres() {
		genresMap.clear();
	}
	
	public Set<String> getGenres() {
		return genresMap.keySet();
	}
	
	public List<MediaFile> getMediaFileOfGenre(String genre) {
		return genresMap.get(genre);
	}
	
	public void registerTrack(MediaFile mediaFile) {
		
		Object genreObject = mediaFile.getMetadata().getNormalizedTags().get(NormalizedAudioMetadataTags.GENRE).value();
		if (genreObject != null) {
			if (genreObject instanceof String genre) {
				addMediaFileToGenre(mediaFile, genre);
			}  else {
				logger.severe("The value of a genre metadata should be a String but is " + genreObject.getClass().getName());
			}
		} else {
			addMediaFileToGenre(mediaFile, PAS_DE_GENRE);
		}
	}
	
	private void addMediaFileToGenre(MediaFile mediaFile, String genre) {
		List<MediaFile> mediaFiles = genresMap.get(genre);
		if (mediaFiles == null) {
			mediaFiles = new ArrayList<>();
			genresMap.put(genre, mediaFiles);
		}
		mediaFiles.add(mediaFile);
	}
}
