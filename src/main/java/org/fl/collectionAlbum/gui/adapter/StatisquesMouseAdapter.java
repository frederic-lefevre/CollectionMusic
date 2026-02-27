/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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

package org.fl.collectionAlbum.gui.adapter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.function.Function;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.gui.table.AlbumsScrollJTablePane;

public class StatisquesMouseAdapter extends MouseAdapter {
	
	
	private int anneeDebut;
	private int anneeFin;
	private Function<Album,TemporalAccessor> albumDateFunction;
	private Function<ListeAlbum, ListeAlbum> albumListSorter;
	private CollectionAlbumContainer collectionAlbumContainer;
	private GenerationPane generationPane;
	
	private StatisquesMouseAdapter() {
	}
	
	
	private StatisquesMouseAdapter(int anneeDebut, int anneeFin, Function<Album, TemporalAccessor> albumDateFunction, Function<ListeAlbum, ListeAlbum> albumListSorter,
			CollectionAlbumContainer collectionAlbumContainer, GenerationPane generationPane) {
		super();
		this.anneeDebut = anneeDebut;
		this.anneeFin = anneeFin;
		this.albumDateFunction = albumDateFunction;
		this.albumListSorter = albumListSorter;
		this.collectionAlbumContainer = collectionAlbumContainer;
		this.generationPane = generationPane;
	}


	public static class Builder {
		
		private Function<Album,TemporalAccessor> albumDateFunction;
		private Function<ListeAlbum, ListeAlbum> albumListSorter;
		private CollectionAlbumContainer collectionAlbumContainer;
		private GenerationPane generationPane;
		
		private Builder() {
		}
		
		private Builder(CollectionAlbumContainer collectionAlbumContainer, 
				Function<Album,TemporalAccessor> albumDateFunction, 
				Function<ListeAlbum, ListeAlbum> albumListSorter, 
				GenerationPane generationPane) {
			this.collectionAlbumContainer = collectionAlbumContainer;
			this.albumDateFunction = albumDateFunction;
			this.albumListSorter = albumListSorter;
			this.generationPane = generationPane;
		}
		
		public static Builder builder(CollectionAlbumContainer collectionAlbumContainer, 
				Function<Album,TemporalAccessor> albumDateFunction, 
				Function<ListeAlbum, ListeAlbum> albumListSorter,
				GenerationPane generationPane) {
			return new Builder(collectionAlbumContainer, albumDateFunction, albumListSorter, generationPane);
		}
		
		public StatisquesMouseAdapter build(int anneeDebut, int anneeFin) {
			return new StatisquesMouseAdapter(anneeDebut, anneeFin, albumDateFunction, albumListSorter, collectionAlbumContainer, generationPane);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent evt) {
		
		if (SwingUtilities.isLeftMouseButton(evt) && (evt.getClickCount() > 1)) {

			ListeAlbum listeAlbum = albumListSorter.apply(collectionAlbumContainer.getAlbumsSastisfying(
						List.of(album -> isBetween(albumDateFunction.apply(album).get(ChronoField.YEAR), anneeDebut, anneeFin))));
			
			// Table to display the result albums
			AlbumsScrollJTablePane albumsScrollJTablePane = new AlbumsScrollJTablePane(listeAlbum.getAlbums(), generationPane);
			
			JOptionPane.showMessageDialog(null, albumsScrollJTablePane, "Albums choisis aléatoirement", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private boolean isBetween(int val, int minInclusive, int maxExclusive) {
		return (val >= minInclusive && val < maxExclusive);
	}
}
