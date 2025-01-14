/*
 MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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

package org.fl.collectionAlbum.disocgs;

import java.util.Set;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.albums.Album;

public class DiscogsAlbumReleaseMatcher {
	
	public static enum MatchResultType {
		NO_MATCH,
		NO_FORMAT_MATCH,
		MATCH
	}
	
	public static class ReleaseMatchResult {
		
		private final MatchResultType matchResultType;
		private final Set<DiscogsAlbumRelease> matchingReleases;
		
		public ReleaseMatchResult(MatchResultType matchResultType, Set<DiscogsAlbumRelease> matchingReleases) {
			super();
			this.matchResultType = matchResultType;
			this.matchingReleases = matchingReleases;
		}

		public MatchResultType getMatchResultType() {
			return matchResultType;
		}

		public Set<DiscogsAlbumRelease> getMatchingReleases() {
			return matchingReleases;
		}
	}
	
	public static class AlbumMatchResult {
		
		private final MatchResultType matchResultType;
		private final Set<Album> matchingAlbums;
		
		public AlbumMatchResult(MatchResultType matchResultType, Set<Album> matchingAlbums) {
			super();
			this.matchResultType = matchResultType;
			this.matchingAlbums = matchingAlbums;
		}

		public MatchResultType getMatchResultType() {
			return matchResultType;
		}

		public Set<Album> getMatchingAlbums() {
			return matchingAlbums;
		}
		
	}
	
	public static ReleaseMatchResult getPotentialReleaseMatch(Album album) {
		
		Set<DiscogsAlbumRelease> compatibleAuteurAndTitleSet =  DiscogsInventory.getDiscogsInventory().stream()
			.filter(albumRelease -> albumRelease.isAlbumAuteursAndTitleMatching(album))
			.collect(Collectors.toSet());
		
		if (compatibleAuteurAndTitleSet.isEmpty()) {
			// No match on auteurs and title
			
			return new ReleaseMatchResult(MatchResultType.NO_MATCH, compatibleAuteurAndTitleSet);
			
		} else {
			// At least one match on auteurs and title : match on support physique
			
			Set<DiscogsAlbumRelease> compatibleReleaseSet =  compatibleAuteurAndTitleSet.stream()
					.filter(release -> release.isAlbumFormatSupportPhysiquesMatching(album.getFormatAlbum()))
					.collect(Collectors.toSet());
			
			if (compatibleReleaseSet.isEmpty()) {
				return new ReleaseMatchResult(MatchResultType.NO_FORMAT_MATCH, compatibleAuteurAndTitleSet);
			} else {
				return new ReleaseMatchResult(MatchResultType.MATCH, compatibleReleaseSet);
			}
		}
	}

	

}
