/*
 MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

import org.fl.collectionAlbum.Format.SupportPhysique;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.disocgs.DiscogsInventory.DiscogsAlbumRelease;
import org.fl.discogsInterface.inventory.InventoryCsvAlbum;

public class DiscogsAlbumReleaseMatcher {

	private static final EnumMap<SupportPhysique,String> formatMatchMap = new EnumMap<>(Map.ofEntries( 
			new AbstractMap.SimpleEntry<SupportPhysique,String>(SupportPhysique.CD, "CD"), 
			new AbstractMap.SimpleEntry<SupportPhysique,String>(SupportPhysique.K7, "Cass"),
			new AbstractMap.SimpleEntry<SupportPhysique,String>(SupportPhysique.Vinyl33T, "LP"),
			new AbstractMap.SimpleEntry<SupportPhysique,String>(SupportPhysique.Vinyl45T, "Single"),
			new AbstractMap.SimpleEntry<SupportPhysique,String>(SupportPhysique.MiniCD, "CD"),
			new AbstractMap.SimpleEntry<SupportPhysique,String>(SupportPhysique.MiniDVD, "DVD"),
			new AbstractMap.SimpleEntry<SupportPhysique,String>(SupportPhysique.Mini33T, ""),
			new AbstractMap.SimpleEntry<SupportPhysique,String>(SupportPhysique.Maxi45T, "Maxi"),
			new AbstractMap.SimpleEntry<SupportPhysique,String>(SupportPhysique.VHS, "VHS"),
			new AbstractMap.SimpleEntry<SupportPhysique,String>(SupportPhysique.DVD, "DVD"),
			new AbstractMap.SimpleEntry<SupportPhysique,String>(SupportPhysique.BluRay, "Blu-ray")));
	
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
	
	public static ReleaseMatchResult getPotentialReleaseMatch(Album album) {
		
		List<String> artists = album.getAuteurs().stream().map(Artiste::getNomComplet).collect(Collectors.toList());
		
		Set<DiscogsAlbumRelease> compatibleAuteurAndTitleSet =  DiscogsInventory.getDiscogsInventory().stream()
			.filter(albumRelease -> isAlbumAuteursAndTitleMatching(album, artists, albumRelease.getInventoryCsvAlbum()))
			.collect(Collectors.toSet());
		
		if (compatibleAuteurAndTitleSet.isEmpty()) {
			// No match on auteurs and title
			
			return new ReleaseMatchResult(MatchResultType.NO_MATCH, compatibleAuteurAndTitleSet);
			
		} else {
			// At least one match on auteurs and title : match on support physique
			
			Set<SupportPhysique> supportPhysiques = album.getFormatAlbum().getSupportsPhysiques();
			Set<DiscogsAlbumRelease> compatibleReleaseSet =  compatibleAuteurAndTitleSet.stream()
					.filter(release -> isAlbumFormatMatching(supportPhysiques, release.getInventoryCsvAlbum()))
					.collect(Collectors.toSet());
			
			if (compatibleReleaseSet.isEmpty()) {
				return new ReleaseMatchResult(MatchResultType.NO_FORMAT_MATCH, compatibleAuteurAndTitleSet);
			} else {
				return new ReleaseMatchResult(MatchResultType.MATCH, compatibleReleaseSet);
			}
		}
	}
	
	private static boolean isAlbumAuteursAndTitleMatching(Album album, List<String> artists, InventoryCsvAlbum inventoryCsvAlbum) {
		
		return (inventoryCsvAlbum.getTitle().toLowerCase().contains(album.getTitre().toLowerCase()) &&
				inventoryCsvAlbum.getArtists().stream().anyMatch(artist -> artists.stream().anyMatch(albumArtist -> artist.contains(albumArtist))));

	}

	private static boolean isAlbumFormatMatching(Set<SupportPhysique> supportPhysiques, InventoryCsvAlbum inventoryCsvAlbum) {
		
		return supportPhysiques.stream()
				.allMatch(supportPhysique -> isSupportPhysiquePresent(supportPhysique, inventoryCsvAlbum));

	}
	
	private static boolean isSupportPhysiquePresent(SupportPhysique supportPhysique, InventoryCsvAlbum inventoryCsvAlbum) {
		
		return inventoryCsvAlbum.getFormats().stream()
			.anyMatch(inventoryCsvAlbumFormat -> inventoryCsvAlbumFormat.contains(formatMatchMap.get(supportPhysique)));
	}
}
