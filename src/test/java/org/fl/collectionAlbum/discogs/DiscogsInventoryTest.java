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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Month;
import java.time.Year;
import java.util.List;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.disocgs.DiscogsInventory.DiscogsAlbumRelease;
import org.fl.discogsInterface.inventory.InventoryCsvAlbum;
import org.junit.jupiter.api.Test;

class DiscogsInventoryTest {

	private static final String JACO_RELEASE_ID = "10131632";  // must exist in discogs
	private static final String JACO_ALBUM_TITLE = "Truth, Liberty & Soul - Live In NYC The Complete 1982 NPR Jazz Alive! Recordings";
	
	@Test
	void shouldGetDiscogsCsvInventory() throws IOException {
		
		int releaseCount = Files.readAllLines(Control.getDiscogsCollectionCsvExportPath()).size() - 1;
		
		List<DiscogsAlbumRelease> inventory = DiscogsInventory.getDiscogsInventory();
		
		assertThat(inventory).isNotNull().hasSize(releaseCount)
			.anySatisfy(album -> {
				
				InventoryCsvAlbum csvAlbum = album.getInventoryCsvAlbum();
				assertThat(csvAlbum.getArtists()).isNotNull().singleElement()
					.satisfies(artist -> assertThat(artist).isEqualTo("Jaco Pastorius"));
				assertThat(csvAlbum.getCatalogNumbers()).isNotNull().singleElement()
					.satisfies(catalogNb -> assertThat(catalogNb).isEqualTo("HLP-9027"));
				assertThat(csvAlbum.getTitle()).isEqualTo(JACO_ALBUM_TITLE);
				assertThat(csvAlbum.getLabels()).isNotNull().singleElement()
					.satisfies(label -> assertThat(label).isEqualTo("Resonance Records"));
				assertThat(csvAlbum.getFormats()).isNotNull().hasSize(6)
					.satisfiesExactlyInAnyOrder(
							format -> assertThat(format).isEqualTo("3xLP"),
							format -> assertThat(format).isEqualTo("Album"),
							format -> assertThat(format).isEqualTo("Ltd"),
							format -> assertThat(format).isEqualTo("180 + Box"),
							format -> assertThat(format).isEqualTo("RSD"),
							format -> assertThat(format).isEqualTo("Num"));
				assertThat(csvAlbum.getRating()).isNull();
				assertThat(csvAlbum.getReleased()).isEqualTo(Year.of(2017));
				assertThat(csvAlbum.getReleaseId()).isEqualTo(JACO_RELEASE_ID);
				assertThat(csvAlbum.getCollectionFolder()).isEqualTo("p");
				assertThat(csvAlbum.getDateAdded())
					.hasYear(2023).hasMonth(Month.SEPTEMBER).hasDayOfMonth(24)
					.hasHour(23).hasMinute(39).hasSecond(34);
				assertThat(csvAlbum.getCollectionMediaCondition()).isEqualTo("Near Mint (NM or M-)");
				assertThat(csvAlbum.getCollectionSleeveCondition()).isEqualTo("Near Mint (NM or M-)");
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
	
	@Test
	void shouldFindAlbum() {
		
		assertThat(DiscogsInventory.containsOneAndOnlyOneAlbum(List.of("Albert Collins"), "Ice Pickin'")).isTrue();
	}
	
	@Test
	void shouldNotFindAlbumWithMultipleOccurences() {
		
		assertThat(DiscogsInventory.containsOneAndOnlyOneAlbum(List.of("Bob Dylan"), "Blonde on Blonde")).isFalse();
	}
	
	@Test
	void shouldNotFindAlbumAbsentAlbums() {
		
		assertThat(DiscogsInventory.containsOneAndOnlyOneAlbum(List.of("Bob Dylan"), "A Love Supreme")).isFalse();
		assertThat(DiscogsInventory.containsOneAndOnlyOneAlbum(List.of("John Coltrane"), "Blonde on Blonde")).isFalse();
	}
	
	@Test
	void shouldGetPotentialReleaseMatch() {
		
		assertThat(DiscogsInventory.getPotentialReleaseMatch(List.of("Soft Machine"), "Third"))
			.isNotNull().singleElement()
			.satisfies(release -> {
				assertThat(release.getInventoryCsvAlbum().getArtists()).contains("Soft Machine");
				assertThat(release.getInventoryCsvAlbum().getTitle().toLowerCase()).isEqualTo("third");
			});
	}
	
	@Test
	void shouldGetSeveralPotentialReleaseMatch() {
		
		assertThat(DiscogsInventory.getPotentialReleaseMatch(List.of("Bob Dylan"), "Blonde on Blonde"))
			.isNotNull().hasSizeGreaterThan(1)
			.allSatisfy(release -> {
				assertThat(release.getInventoryCsvAlbum().getArtists()).contains("Bob Dylan");
				assertThat(release.getInventoryCsvAlbum().getTitle()).isEqualToIgnoringCase("Blonde on Blonde");
			});

	}
	
	@Test
	void shouldNotGetPotentialReleaseMatch() {
		
		assertThat(DiscogsInventory.getPotentialReleaseMatch(List.of("Charlie Christian"), "Third"))
			.isNotNull().isEmpty();
	}
}
