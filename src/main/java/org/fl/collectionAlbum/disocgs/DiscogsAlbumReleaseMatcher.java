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

import java.util.List;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.disocgs.DiscogsInventory.DiscogsAlbumRelease;
import org.fl.discogsInterface.inventory.InventoryCsvAlbum;

public class DiscogsAlbumReleaseMatcher {

	public static List<DiscogsAlbumRelease> getPotentialReleaseMatch(Album album) {
		
		List<String> artists = album.getAuteurs().stream().map(Artiste::getNomComplet).collect(Collectors.toList());
		
		return DiscogsInventory.getDiscogsInventory().stream()
			.filter(albumRelease -> isAlbumAndReleaseMatching(album, artists, albumRelease.getInventoryCsvAlbum()))
			.collect(Collectors.toList());
	}
	
	private static boolean isAlbumAndReleaseMatching(Album album, List<String> artists, InventoryCsvAlbum inventoryCsvAlbum) {
		
		return (inventoryCsvAlbum.getTitle().toLowerCase().contains(album.getTitre().toLowerCase()) &&
				inventoryCsvAlbum.getArtists().stream().anyMatch(artist -> artists.stream().anyMatch(albumArtist -> artist.contains(albumArtist))));

	}

}
