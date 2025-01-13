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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.discogsInterface.inventory.Inventory;

public class DiscogsInventory {

	private final static Logger albumLog = Logger.getLogger(DiscogsInventory.class.getName());
	
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
		
		discogsAlbumReleases = new ArrayList<>();		
		discogsAlbumReleaseMap = new LinkedHashMap<>();
	}

	public static void buildDiscogsInventory() {
		getInstance().buildDiscogsReleasesInventory();
	}
	
	public static List<DiscogsAlbumRelease> getDiscogsInventory() {

		return getInstance().discogsAlbumReleases;
	}
	
	private DiscogsAlbumRelease getDiscogsRelease(String discogsReleaseId) {
		return discogsAlbumReleaseMap.get(discogsReleaseId);
	}
	
	private void buildDiscogsReleasesInventory() {
		
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
