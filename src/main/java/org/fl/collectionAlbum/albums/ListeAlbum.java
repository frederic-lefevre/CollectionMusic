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

package org.fl.collectionAlbum.albums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.RangementComparator;
import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.utils.ListUtils;

public class ListeAlbum {
	
	private final List<Album> albums;

	private Format formatListeAlbum;

	private ListeAlbum() {

		formatListeAlbum = new Format(null);
		albums = new ArrayList<Album>();
	}

	public void addAlbum(Album a) {
		if (!albums.contains(a)) {
			albums.add(a);
			formatListeAlbum.incrementFormat(a.getFormatAlbum());
		}
	}

	public void reset() {
		albums.clear();
		formatListeAlbum = new Format(null);
	}
	
	public ListeAlbum sortAlphaOnTitle() {
		Collections.sort(albums, new AlbumAlphaComparator());
		return this;
	}
	
	public ListeAlbum sortChronoEnregistrement() {
		Collections.sort(albums, new AlbumEnregistrementComparator());
		return this;
	}

	public ListeAlbum sortChronoComposition() {
		Collections.sort(albums, new AlbumCompositionComparator());
		return this;
	}

	public ListeAlbum sortRangementAlbum() {
		Collections.sort(albums, new RangementComparator());
		return this;
	}

	public Format getFormatListeAlbum() {
		return formatListeAlbum;
	}

	public int getNombreAlbums() {
		return albums.size();
	}

	public List<Album> getAlbums() {
		return albums;
	}
	
	public List<Album> pickRandomAlbums(int nbAlbum) {
		return ListUtils.pickRandomDistinctElements(albums, nbAlbum);
	}

	public Album pickRandomAlbum() {
		return ListUtils.pickRandomElement(albums);
	}
	
	public static class Builder {
		
		private final ListeAlbum listeAlbum;
		
		private Builder() {
			listeAlbum = new ListeAlbum();
		}
		
		public static Builder getBuilder() {
			return new Builder();
		}
		
		public Builder from(List<Album> la) {
			listeAlbum.reset();
			if (la != null) {
				la.forEach(album -> listeAlbum.addAlbum(album));
			}
			return this;
		}
		
		public Builder withAlbumSatisfying(Predicate<Album> albumPredicate) {
			
			return from(listeAlbum.getAlbums().stream()
					.filter(albumPredicate)
					.collect(Collectors.toList()));
		}
		
		public ListeAlbum build() {
			return listeAlbum;
		}
	}
}
