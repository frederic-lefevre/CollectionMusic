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

package org.fl.collectionAlbum.discogs;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.disocgs.DiscogsInventory.DiscogsAlbumRelease;
import org.fl.discogsInterface.inventory.InventoryCsvAlbum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DiscogsInventoryTest {

	private static final String JACO_RELEASE_ID = "25261330";  // must exist in discogs
	private static final String JACO_ALBUM_TITLE = "Truth, Liberty & Soul - Live In NYC The Complete 1982 NPR Jazz Alive! Recordings";
	
	@BeforeAll
	static void initInventory() {
		DiscogsInventory.buildDiscogsInventory();
	}
	
	@Test
	void shouldGetDiscogsCsvInventory() throws IOException {
		
		int releaseCount = Files.readAllLines(Control.getDiscogsCollectionCsvExportPath()).size() - 1;
		
		List<DiscogsAlbumRelease> inventory = DiscogsInventory.getDiscogsInventory();
		
		assertThat(inventory).isNotNull().hasSize(releaseCount);
		
		List<DiscogsAlbumRelease> inventoryJaco = inventory.stream()
				.filter(album -> album.getInventoryCsvAlbum().getArtists().contains("Jaco Pastorius"))
				.collect(Collectors.toList());
		
		assertThat(inventoryJaco)
			.anySatisfy(album -> {
				
				InventoryCsvAlbum csvAlbum = album.getInventoryCsvAlbum();
				assertThat(csvAlbum.getArtists()).isNotNull().singleElement()
					.satisfies(artist -> assertThat(artist).isEqualTo("Jaco Pastorius"));
				assertThat(csvAlbum.getCatalogNumbers()).isNotNull().singleElement()
					.satisfies(catalogNb -> assertThat(catalogNb).isEqualTo("HLP-9027B"));
				assertThat(csvAlbum.getTitle()).isEqualTo(JACO_ALBUM_TITLE);
				assertThat(csvAlbum.getLabels()).isNotNull().singleElement()
					.satisfies(label -> assertThat(label).isEqualTo("Resonance Records"));
				assertThat(csvAlbum.getFormats()).isNotNull().hasSize(8)
					.satisfiesExactlyInAnyOrder(
							format -> assertThat(format).isEqualTo("3xLP"),
							format -> assertThat(format).isEqualTo("Album"),
							format -> assertThat(format).isEqualTo("Ltd"),
							format -> assertThat(format).isEqualTo("180"),
							format -> assertThat(format).isEqualTo("RSD"),
							format -> assertThat(format).isEqualTo("RM"),
							format -> assertThat(format).isEqualTo("RE"),
							format -> assertThat(format).isEqualTo("Num"));
				assertThat(csvAlbum.getRating()).isNull();
				assertThat(csvAlbum.getReleased().atMonth(1)).isEqualTo(YearMonth.of(2022, 1));
				assertThat(csvAlbum.getReleaseId()).isEqualTo(JACO_RELEASE_ID);
				assertThat(csvAlbum.getCollectionFolder()).isEqualTo("Jaco Pastorius");
				assertThat(csvAlbum.getDateAdded())
					.hasYear(2024).hasMonth(Month.MAY).hasDayOfMonth(16)
					.hasHour(12).hasMinute(56).hasSecond(05);
				assertThat(csvAlbum.getCollectionNotes()).isEqualTo("Limited Edition 2nd pressing of 5000. Number 3801");
		});
	}
	
	@Test
	void shouldGetRelease() {
		
		assertThat(DiscogsInventory.getDiscogsAlbumRelease(JACO_RELEASE_ID)).isNotNull()
			.satisfies(album -> assertThat(album.getInventoryCsvAlbum().getTitle()).isEqualTo(JACO_ALBUM_TITLE));
	}
	
	@Test
	void shouldNotGetRelease() {
		
		assertThat(DiscogsInventory.getDiscogsAlbumRelease("ThisOneDoesNotExist")).isNull();
	}

}
