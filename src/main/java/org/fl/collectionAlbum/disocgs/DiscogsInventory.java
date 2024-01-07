/*
 * MIT License

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

import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.discogsInterface.inventory.Inventory;
import org.fl.discogsInterface.inventory.InventoryCsvAlbum;
import org.fl.util.json.JsonUtils;

public class DiscogsInventory {

	public static class DiscogsAlbumRelease {
		
		private InventoryCsvAlbum inventoryCsvAlbum;
		private Set<Album> collectionAlbums;
		
		protected DiscogsAlbumRelease(InventoryCsvAlbum inventoryCsvAlbum) {
			this.inventoryCsvAlbum = inventoryCsvAlbum;
			collectionAlbums = new HashSet<>();
		}

		public Set<Album> getCollectionAlbum() {
			return collectionAlbums;
		}

		public void addCollectionAlbums(Album collectionAlbum) {
			this.collectionAlbums.add(collectionAlbum);
		}

		public InventoryCsvAlbum getInventoryCsvAlbum() {
			return inventoryCsvAlbum;
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
					info.append(JsonUtils.jsonPrettyPrint(collectionAlbum.getJson()))
						.append("\n-------------------------------------\n")
					);
			}
			
			return info.toString();
		}
		
		private static void addPropertyInfo(StringBuilder info, String name, Object value) {
			info.append(name).append(": ").append(Optional.ofNullable(value).map(v -> v.toString()).orElse("valeur null")).append("\n");
		}
	}
	
	private final static Logger albumLog = Control.getAlbumLog();
	
	private static DiscogsInventory discogsInventoryInstance;
	
	private static DiscogsInventory getInstance() {
		if (discogsInventoryInstance == null) {
			discogsInventoryInstance = new DiscogsInventory();
		}
		return discogsInventoryInstance;
	}
	
	private final Path disocgsInventoryCsvPath;
	
	private LinkedHashMap<String,DiscogsAlbumRelease> discogsAlbumReleaseMap;
	
	// DiscogsAlbumRalease values maintained as List to be displayed in a JTable
	private final List<DiscogsAlbumRelease> discogsAlbumReleases;
	
	private DiscogsInventory() {
		
		disocgsInventoryCsvPath = Control.getDiscogsCollectionCsvExportPath();
		
		discogsAlbumReleases = Inventory.parseCsvFile(disocgsInventoryCsvPath, albumLog).stream()
				.map(DiscogsAlbumRelease::new).collect(Collectors.toList());
		
		discogsAlbumReleaseMap = new LinkedHashMap<>();
		discogsAlbumReleases.forEach(release -> discogsAlbumReleaseMap.put(release.getInventoryCsvAlbum().getReleaseId(), release));
	}

	public static void rebuildDiscogsInventory() {
		getInstance().rebuildDiscogsReleasesInventory();
	}
	
	public static List<DiscogsAlbumRelease> getDiscogsInventory() {

		return getInstance().discogsAlbumReleases;
	}
	
	private DiscogsAlbumRelease getDiscogsRelease(String discogsReleaseId) {
		return discogsAlbumReleaseMap.get(discogsReleaseId);
	}
	
	private void rebuildDiscogsReleasesInventory() {
		
		discogsAlbumReleases.clear();
		Inventory.parseCsvFile(disocgsInventoryCsvPath, albumLog).forEach(csvRelease -> discogsAlbumReleases.add(new DiscogsAlbumRelease(csvRelease)));
		
		discogsAlbumReleaseMap.clear();
		discogsAlbumReleases.forEach(release -> discogsAlbumReleaseMap.put(release.getInventoryCsvAlbum().getReleaseId(), release));
	}
	
	public static DiscogsAlbumRelease getDiscogsAlbumRelease(String releaseId) {		
		return getInstance().getDiscogsRelease(releaseId);
	}
	
	public static void linkToAlbum(String releaseId, Album album) {
		
		DiscogsAlbumRelease discogsAlbumRelease = getInstance().getDiscogsRelease(releaseId);
		if (discogsAlbumRelease == null) {
			albumLog.severe("L'album " + album.getTitre() + " est lié à une release discogs (" + releaseId + ") inconnue dans l'inventaire");
		} else {
			discogsAlbumRelease.addCollectionAlbums(album);
		}
	}
}
