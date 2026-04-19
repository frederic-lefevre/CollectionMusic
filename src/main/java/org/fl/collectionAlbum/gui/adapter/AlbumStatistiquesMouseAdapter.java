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

import java.awt.event.MouseEvent;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.gui.table.AbstractAlbumsTableModel;
import org.fl.collectionAlbum.gui.table.AlbumsScrollJTablePane;

public class AlbumStatistiquesMouseAdapter extends StatistiquesMouseAdapter {
	
	private Function<Album,TemporalAccessor> albumDateFunction;
	private Function<ListeAlbum, ListeAlbum> albumListSorter;
	
	private AlbumStatistiquesMouseAdapter() {
		super(0, 0, null, null);
	}
	
	private AlbumStatistiquesMouseAdapter(int anneeDebut, int anneeFin, Function<Album, TemporalAccessor> albumDateFunction, Function<ListeAlbum, ListeAlbum> albumListSorter,
			CollectionAlbumContainer collectionAlbumContainer, GenerationPane generationPane) {
		super(anneeDebut, anneeFin, collectionAlbumContainer, generationPane);
		this.albumDateFunction = albumDateFunction;
		this.albumListSorter = albumListSorter;
	}
	
	public static class Builder extends AbstractStatistiquesMouseAdapterBuilder {
		
		private Function<Album,TemporalAccessor> albumDateFunction;
		private Function<ListeAlbum, ListeAlbum> albumListSorter;

		private Builder(CollectionAlbumContainer collectionAlbumContainer, 
				Function<Album,TemporalAccessor> albumDateFunction, 
				Function<ListeAlbum, ListeAlbum> albumListSorter, 
				GenerationPane generationPane) {
			super(collectionAlbumContainer, generationPane);
			this.albumDateFunction = albumDateFunction;
			this.albumListSorter = albumListSorter;		
		}
		
		public static Builder builder(CollectionAlbumContainer collectionAlbumContainer, 
				Function<Album,TemporalAccessor> albumDateFunction, 
				Function<ListeAlbum, ListeAlbum> albumListSorter,
				GenerationPane generationPane) {
			return new Builder(collectionAlbumContainer, albumDateFunction, albumListSorter, generationPane);
		}
		
		@Override
		public AlbumStatistiquesMouseAdapter build(int anneeDebut, int anneeFin) {
			return new AlbumStatistiquesMouseAdapter(anneeDebut, anneeFin, albumDateFunction, albumListSorter, collectionAlbumContainer, generationPane);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent evt) {
		
		if (SwingUtilities.isLeftMouseButton(evt) && (evt.getClickCount() > 1)) {

			List<Album> albumList = albumListSorter.apply(collectionAlbumContainer.getAlbumsSastisfying(
						List.of(album -> isBetween(
								Optional.ofNullable(albumDateFunction.apply(album)).map(date -> date.get(ChronoField.YEAR)).orElse(null), 
								anneeDebut, 
								anneeFin))))
					.getAlbums();
			
			if (! albumList.isEmpty()) {
				// Table to display the result albums
				AlbumsScrollJTablePane albumsScrollJTablePane = new AlbumsScrollJTablePane(albumList, 
						AbstractAlbumsTableModel.ACQUISITION_COLUMN_LIST,
						generationPane);
			
				JOptionPane.showMessageDialog(null, albumsScrollJTablePane, getWindowsTitle("Albums "), JOptionPane.PLAIN_MESSAGE);
			}
		}
	}	
}
