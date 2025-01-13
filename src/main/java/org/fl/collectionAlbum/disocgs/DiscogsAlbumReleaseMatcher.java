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

import java.util.AbstractMap;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.format.MediaSupportCategories;
import org.fl.discogsInterface.inventory.InventoryCsvAlbum;

public class DiscogsAlbumReleaseMatcher {

	private static final EnumMap<MediaSupportCategories,String> formatMatchMap = new EnumMap<>(Map.ofEntries( 
			new AbstractMap.SimpleEntry<MediaSupportCategories,String>(MediaSupportCategories.CD, "CD"), 
			new AbstractMap.SimpleEntry<MediaSupportCategories,String>(MediaSupportCategories.K7, "Cass"),
			new AbstractMap.SimpleEntry<MediaSupportCategories,String>(MediaSupportCategories.VinylLP, "LP"),
			new AbstractMap.SimpleEntry<MediaSupportCategories,String>(MediaSupportCategories.MiniVinyl, "Single"),
			new AbstractMap.SimpleEntry<MediaSupportCategories,String>(MediaSupportCategories.MiniCD, "CD"),
			new AbstractMap.SimpleEntry<MediaSupportCategories,String>(MediaSupportCategories.MiniDVD, "DVD"),
			new AbstractMap.SimpleEntry<MediaSupportCategories,String>(MediaSupportCategories.VHS, "VHS"),
			new AbstractMap.SimpleEntry<MediaSupportCategories,String>(MediaSupportCategories.DVD, "DVD"),
			new AbstractMap.SimpleEntry<MediaSupportCategories,String>(MediaSupportCategories.BluRay, "Blu-ray")));
	
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
			.filter(albumRelease -> isAlbumAuteursAndTitleMatching(album, albumRelease.getInventoryCsvAlbum()))
			.collect(Collectors.toSet());
		
		if (compatibleAuteurAndTitleSet.isEmpty()) {
			// No match on auteurs and title
			
			return new ReleaseMatchResult(MatchResultType.NO_MATCH, compatibleAuteurAndTitleSet);
			
		} else {
			// At least one match on auteurs and title : match on support physique
			
			Set<DiscogsAlbumRelease> compatibleReleaseSet =  compatibleAuteurAndTitleSet.stream()
					.filter(release -> isAlbumFormatSupportPhysiquesMatching(album.getFormatAlbum(), release.getInventoryCsvAlbum()))
					.collect(Collectors.toSet());
			
			if (compatibleReleaseSet.isEmpty()) {
				return new ReleaseMatchResult(MatchResultType.NO_FORMAT_MATCH, compatibleAuteurAndTitleSet);
			} else {
				return new ReleaseMatchResult(MatchResultType.MATCH, compatibleReleaseSet);
			}
		}
	}

	
	public static AlbumMatchResult getPotentialAlbumMatch(DiscogsAlbumRelease discogsRelease, List<Album> albums) {
		
		Set<Album> compatibleAuteurAndTitleSet = albums.stream()
				.filter(album -> isAlbumAuteursAndTitleMatching(album, discogsRelease.getInventoryCsvAlbum()))
				.collect(Collectors.toSet());
			
		if (compatibleAuteurAndTitleSet.isEmpty()) {
			// No match on auteurs and title
			
			return new AlbumMatchResult(MatchResultType.NO_MATCH, compatibleAuteurAndTitleSet);
		} else {
			
			Set<Album> compatibleAlbumSet = compatibleAuteurAndTitleSet.stream()
				.filter(album -> isAlbumFormatSupportPhysiquesMatching(album.getFormatAlbum(), discogsRelease.getInventoryCsvAlbum()))
						.collect(Collectors.toSet());
			
			if (compatibleAlbumSet.isEmpty()) {
				return new AlbumMatchResult(MatchResultType.NO_FORMAT_MATCH, compatibleAuteurAndTitleSet);
			} else {
				return new AlbumMatchResult(MatchResultType.MATCH, compatibleAlbumSet);
			}
		}		
	}

	
	private static boolean isAlbumAuteursAndTitleMatching(Album album, InventoryCsvAlbum inventoryCsvAlbum) {
		
		return (inventoryCsvAlbum.getTitle().toLowerCase().contains(album.getTitre().toLowerCase()) &&
				inventoryCsvAlbum.getArtists().stream()
					.anyMatch(artist -> album.getAuteurs().stream().map(Artiste::getNomComplet)
							.anyMatch(albumArtist -> artist.contains(albumArtist))));

	}

	private static boolean isAlbumFormatSupportPhysiquesMatching(Format albumFormat, InventoryCsvAlbum inventoryCsvAlbum) {
		
		return albumFormat.getSupportsPhysiques().stream()
				.allMatch(supportPhysique -> isSupportPhysiquePresent(supportPhysique, inventoryCsvAlbum));

	}
	
	private static boolean isSupportPhysiquePresent(MediaSupportCategories supportPhysique, InventoryCsvAlbum inventoryCsvAlbum) {
		
		return inventoryCsvAlbum.getFormats().stream()
			.anyMatch(inventoryCsvAlbumFormat -> inventoryCsvAlbumFormat.contains(getFormatMatch(supportPhysique)));
	}
	

	public static String getFormatMatch(MediaSupportCategories supportPhysique) {
		return formatMatchMap.get(supportPhysique);
	}
}
