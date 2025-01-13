/*
 * MIT License

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher.AlbumMatchResult;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher.MatchResultType;
import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.format.MediaSupportCategories;
import org.fl.discogsInterface.inventory.InventoryCsvAlbum;

public class DiscogsAlbumRelease {
	
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
	
	private InventoryCsvAlbum inventoryCsvAlbum;
	private Set<Album> collectionAlbums;
	
	protected DiscogsAlbumRelease(InventoryCsvAlbum inventoryCsvAlbum) {
		this.inventoryCsvAlbum = inventoryCsvAlbum;
		collectionAlbums = new HashSet<>();
	}

	public Set<Album> getCollectionAlbums() {
		return collectionAlbums;
	}

	public boolean isLinkedToAlbum() {
		return (collectionAlbums != null) && !collectionAlbums.isEmpty();
	}
	
	public void addCollectionAlbums(Album collectionAlbum) {
		this.collectionAlbums.add(collectionAlbum);
	}

	public InventoryCsvAlbum getInventoryCsvAlbum() {
		return inventoryCsvAlbum;
	}
	
	public boolean checkAllAlbumsFormatQuantityMatch() {
		return collectionAlbums.stream().allMatch(this::checkOneAlbumFormatQuantityMatch);
	}
	
	public boolean checkOneAlbumFormatQuantityMatch(Album album) {
		return isAlbumFormatSupportPhysiquesMatching(album.getFormatAlbum());
	}
	
	public AlbumMatchResult getPotentialAlbumMatch(List<Album> albums) {
		
		Set<Album> compatibleAuteurAndTitleSet = albums.stream()
				.filter(album -> isAlbumAuteursAndTitleMatching(album))
				.collect(Collectors.toSet());
			
		if (compatibleAuteurAndTitleSet.isEmpty()) {
			// No match on auteurs and title
			
			return new AlbumMatchResult(MatchResultType.NO_MATCH, compatibleAuteurAndTitleSet);
		} else {
			
			Set<Album> compatibleAlbumSet = compatibleAuteurAndTitleSet.stream()
				.filter(album -> isAlbumFormatSupportPhysiquesMatching(album.getFormatAlbum()))
						.collect(Collectors.toSet());
			
			if (compatibleAlbumSet.isEmpty()) {
				return new AlbumMatchResult(MatchResultType.NO_FORMAT_MATCH, compatibleAuteurAndTitleSet);
			} else {
				return new AlbumMatchResult(MatchResultType.MATCH, compatibleAlbumSet);
			}
		}		
	}

	
	public boolean isAlbumAuteursAndTitleMatching(Album album) {
		
		return (getInventoryCsvAlbum().getTitle().toLowerCase().contains(album.getTitre().toLowerCase()) &&
				getInventoryCsvAlbum().getArtists().stream()
					.anyMatch(artist -> album.getAuteurs().stream().map(Artiste::getNomComplet)
							.anyMatch(albumArtist -> artist.contains(albumArtist))));

	}
	
	public boolean isAlbumFormatSupportPhysiquesMatching(Format albumFormat) {
		
		return albumFormat.getSupportsPhysiquesNumbers().entrySet().stream()
				.allMatch(supportPhysiquesEntry -> isSupportPhysiquePresent(supportPhysiquesEntry.getKey(), supportPhysiquesEntry.getValue()));

	}
	
	private boolean isSupportPhysiquePresent(MediaSupportCategories supportPhysique, Double quantity) {
		
		return getInventoryCsvAlbum().getFormats().stream()
			.anyMatch(inventoryCsvAlbumFormat -> isSupportPhysiquePresent(inventoryCsvAlbumFormat, supportPhysique, quantity));
	}
	

	private static boolean isSupportPhysiquePresent(String inventoryCsvAlbumFormat, MediaSupportCategories supportPhysique, Double quantity) {
		if (quantity == 1) {
			return inventoryCsvAlbumFormat.contains(formatMatchMap.get(supportPhysique));
		} else {
			return inventoryCsvAlbumFormat.contains(formatMatchMap.get(supportPhysique)) &&
					inventoryCsvAlbumFormat.contains(Format.poidsToString(quantity));
		}
	}
	
	public static String getFormatMatch(MediaSupportCategories supportPhysique) {
		return formatMatchMap.get(supportPhysique);
	}
	
	public String getInfo() {
		
		StringBuilder info = new StringBuilder();
		
		addPropertyInfo(info, "releaseId", inventoryCsvAlbum.getReleaseId());
		addPropertyInfo(info, "Titre", inventoryCsvAlbum.getTitle());
		addPropertyInfo(info, "Artistes", inventoryCsvAlbum.getArtists());
		addPropertyInfo(info, "Formats", inventoryCsvAlbum.getFormats());
		addPropertyInfo(info, "Labels", inventoryCsvAlbum.getLabels());
		addPropertyInfo(info, "Numéros de catalogues", inventoryCsvAlbum.getCatalogNumbers());
		addPropertyInfo(info, "Notation", inventoryCsvAlbum.getRating());
		addPropertyInfo(info, "Année de sortie", inventoryCsvAlbum.getReleased());
		addPropertyInfo(info, "Dossier de collection", inventoryCsvAlbum.getCollectionFolder());
		addPropertyInfo(info, "Date d'ajout", inventoryCsvAlbum.getDateAdded());
		addPropertyInfo(info, "Etat du media", inventoryCsvAlbum.getCollectionMediaCondition());
		addPropertyInfo(info, "Etat de la pochette", inventoryCsvAlbum.getCollectionSleeveCondition());
		addPropertyInfo(info, "Notes", inventoryCsvAlbum.getCollectionNotes());
		
		if (collectionAlbums.isEmpty()) {
			info.append("\n-------------------------------------\n  Non lié à un album de la collection\n");
			
		} else {
			info.append("\n-------------------------------------\n  Albums de la collection liés\n");
			
			collectionAlbums.forEach(collectionAlbum -> 
				info.append(collectionAlbum.getJsonString())
					.append("\n-------------------------------------\n")
				);
		}
		
		return info.toString();
	}
	
	private static void addPropertyInfo(StringBuilder info, String name, Object value) {
		info.append(name).append(": ").append(Optional.ofNullable(value).map(v -> v.toString()).orElse("valeur null")).append("\n");
	}
}