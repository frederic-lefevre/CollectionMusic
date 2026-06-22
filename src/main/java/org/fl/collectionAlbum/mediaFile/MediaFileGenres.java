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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.mediaFile.metadata.MediaFileMetadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaFileGenres {

	private static final Logger logger = Logger.getLogger(MediaFileGenres.class.getName());
	
	private static final String PAS_DE_GENRE = "Genre absent";
	
	public static class GenreParameters {

		private final String genre;
		private final List<MediaFile> mediaFiles;
		private final boolean isNormalizedGenre;
		private long duration;

		private GenreParameters(String genre, boolean isNormalizedGenre) {
			this.genre = genre;
			this.isNormalizedGenre = isNormalizedGenre;
			mediaFiles = new ArrayList<>();
			duration = 0;
		}

		public String genre() {
			return genre;
		}

		public boolean iNormalizedGenre() {
			return isNormalizedGenre;
		}
		
		public List<MediaFile> mediaFiles() {
			return mediaFiles;
		}

		public long duration() {
			return duration;
		}
	};
	
	private final Set<String> normalizedFileGenres;
	private final Map<String, GenreParameters> genresMap;
	private final List<GenreParameters> genresParameterList;

	public MediaFileGenres() {
		this.normalizedFileGenres = new HashSet<String>();
		genresMap = new HashMap<>();
		genresParameterList = new ArrayList<>();
	}
	
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public MediaFileGenres( @JsonProperty("normalizedFileGenres")Set<String> normalizedFileGenres) {
		
		this.normalizedFileGenres = normalizedFileGenres;
		genresMap = new HashMap<>();
		genresParameterList = new ArrayList<>();
	}
	
	public void clearMediaFileGenres() {
		genresMap.clear();
		genresParameterList.clear();
	}
	
	public Set<String> getGenres() {
		return genresMap.keySet();
	}
	
	public List<GenreParameters> getGenresParameterList() {
		return genresParameterList;
	}
	
	public GenreParameters getGenreParameters(String genre) {
		return genresMap.get(genre);
	}
	
	public void registerTrack(MediaFile mediaFile) {
		
		try {
			MediaFileMetadata mediaFileMetadata = mediaFile.getMetadata();
			if (mediaFileMetadata != null) {
				
				addMediaFileToGenre(Optional.ofNullable(mediaFileMetadata.getGenre()).orElse(PAS_DE_GENRE), 
						mediaFile, 
						Optional.ofNullable(mediaFileMetadata.getDuration()).orElse(0L));
			} else {
				addMediaFileToGenre(PAS_DE_GENRE, mediaFile, 0);
			}
		} catch (Exception e) {
			if (mediaFile == null) {
				logger.log(Level.SEVERE, "Trying to register musical genre of null mediaFile", e);
			} else {
				logger.log(Level.SEVERE, "Exception when registering musical genre of track " + mediaFile.getFilePath(), e);
			}
		}
	}
	
	private void addMediaFileToGenre(String genre, MediaFile mediaFile, long duration) {
		
		GenreParameters genreParameters = genresMap.get(genre);
		if (genreParameters == null) {
			genreParameters = new GenreParameters(genre, normalizedFileGenres.contains(genre));
			genresMap.put(genre, genreParameters);
			genresParameterList.add(genreParameters);
		}
		genreParameters.mediaFiles().add(mediaFile);
		genreParameters.duration = genreParameters.duration + duration;
	}
}
