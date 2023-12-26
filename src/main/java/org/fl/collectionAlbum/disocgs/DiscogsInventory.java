/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.discogsInterface.inventory.Inventory;
import org.fl.discogsInterface.inventory.InventoryCsvAlbum;

public class DiscogsInventory {

	private final static Logger albumLog = Control.getAlbumLog();
	
	private static DiscogsInventory discogsInventoryInstance;
	
	private static DiscogsInventory getInstance() {
		if (discogsInventoryInstance == null) {
			discogsInventoryInstance = new DiscogsInventory();
		}
		return discogsInventoryInstance;
	}
	
	private final Path disocgsInventoryCsvPath;
	
	private List<InventoryCsvAlbum> discogsInventory;
	
	private DiscogsInventory() {
		
		disocgsInventoryCsvPath = Control.getDiscogsCollectionCsvExportPath();
	}

	public static List<InventoryCsvAlbum> getDiscogsInventory() {
		
		getInstance().discogsInventory = Inventory.parseCsvFile(getInstance().disocgsInventoryCsvPath, albumLog);
		return getInstance().discogsInventory;
	}
	
	public static boolean containsOneAndOnlyOneAlbum(List<String> artists, String title) {
		return getInstance().containsOneAndOnlyOne(artists, title);
	}
	
	private boolean containsOneAndOnlyOne(List<String> artists, String title) {
		
		if (discogsInventory == null) {
			discogsInventory = Inventory.parseCsvFile(getInstance().disocgsInventoryCsvPath, albumLog);
		}
		return (1 == discogsInventory.stream()
			.filter(discogsRelease -> discogsRelease.getTitle().toLowerCase().contains(title.toLowerCase()))
			.filter(discogsRelease -> discogsRelease.getArtists().stream().anyMatch(artist -> artists.contains(artist)))
			.count());
	}
}
