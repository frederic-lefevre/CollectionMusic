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

package org.fl.collectionAlbum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.utils.ListUtils;

public class RandomAlbumPicker {

	public static List<Album> pickRandomAlbums(ListeAlbum listeAlbum, int nbAlbum) {
		return listeAlbum.pickRandomAlbums(nbAlbum);
	}
	
	public static List<Album> pickRandomAlbumsViaArtiste(List<Artiste> artistList, long nbAlbum) {
		
		if (artistList.size()/10 <= nbAlbum) {
			// Algorithm suitable for a large number of albums requested
		
			RandomAlbumPicker randomAlbumPicker = new RandomAlbumPicker(artistList);
			if (randomAlbumPicker.getNumberOfDistinctAlbums() <= nbAlbum) {
				return randomAlbumPicker.getAllDistinctAlbums();
			} else {

				return Stream.generate(() -> randomAlbumPicker.pickRandomAlbumsOfOneArtist())
					.map(albumsOfOneArtist -> ListUtils.pickRemoveRandomElement(albumsOfOneArtist))
					.distinct()
					.limit(nbAlbum)
					.collect(Collectors.toList());
			}			
		} else {
			
			return Stream.generate(() -> ListUtils.pickRandomElement(artistList))
				.map(artiste -> artiste.getAlbums().pickRandomAlbum())
				.distinct()
				.limit(nbAlbum)
				.collect(Collectors.toList());
		}		
	}
	
	private final List<List<Album>> albumsOfArtists;
	private final long numberOfDistinctAlbums;
	
	private RandomAlbumPicker(List<Artiste> artistList) {
		
		albumsOfArtists = new ArrayList<>();
		artistList.forEach(artist -> albumsOfArtists.add(new ArrayList<>(artist.getAlbums().getAlbums())));
		
		numberOfDistinctAlbums = albumsOfArtists.stream().flatMap(Collection::stream).distinct().count();
	}
	
	private RandomAlbumPicker() {
		albumsOfArtists = new ArrayList<>();
		numberOfDistinctAlbums = 0;
	}
	
	private long getNumberOfDistinctAlbums() {
		return numberOfDistinctAlbums;
	}
	
	private List<Album> getAllDistinctAlbums() {
		return albumsOfArtists.stream().flatMap(Collection::stream).distinct().toList();
	}
	
	private List<Album> pickRandomAlbumsOfOneArtist() {
		
		List<Album> albumsOfOneArtist = ListUtils.pickRandomElement(albumsOfArtists);
		if (albumsOfOneArtist.isEmpty()) {
			albumsOfArtists.remove(albumsOfOneArtist);
			return pickRandomAlbumsOfOneArtist();
		} else {
			return albumsOfOneArtist;
		}
	}
}
