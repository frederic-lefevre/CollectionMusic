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

package org.fl.collectionAlbum.discogs;

import static org.assertj.core.api.Assertions.*;

import org.fl.collectionAlbum.disocgs.DiscogsAlbumRelease;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DiscogsAlbumReleaseTest {

	@BeforeAll
	static void initInventory() {
		MediaFilesInventories.clearInventories();
		MediaFilesInventories.scanMediaFilePaths();
		DiscogsInventory.buildDiscogsInventory();
	}
	
	
	@Test
	void shouldThrowNPE() {
		
		assertThat(DiscogsInventory.getDiscogsInventory()).isNotEmpty();
		assertThatNullPointerException().isThrownBy(() -> DiscogsInventory.getDiscogsInventory().get(0).getPotentialAlbumMatch(null));
	}
	
	@Test
	void shouldGetAlbumRelease() {
		
		DiscogsAlbumRelease discogsAlbum = getDiscogsAlbumRelease("Symphonies Nos. 25/28/29/35/36/38/40/41");
		
		assertThat(discogsAlbum).isNotNull();
		
	}

	private static DiscogsAlbumRelease getDiscogsAlbumRelease(String titre) {
		
		return DiscogsInventory.getDiscogsInventory().stream()
			.filter(i -> i.getInventoryCsvAlbum().getTitle().equals(titre))
			.findFirst().get();
	}

}
