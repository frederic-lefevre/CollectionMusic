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
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.ListeConcert;
import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.gui.table.ConcertsScrollJTablePane;

public class ConcertStatistiquesMouseAdapter extends StatistiquesMouseAdapter {

	private Function<Concert, TemporalAccessor> concertDateFunction;
	private Function<ListeConcert, ListeConcert> concertSorter;
	
	private ConcertStatistiquesMouseAdapter() {
		super(0,0, null, null);
	}
	
	protected ConcertStatistiquesMouseAdapter(int anneeDebut, int anneeFin, 
			Function<Concert, TemporalAccessor> concertDateFunction,  Function<ListeConcert, ListeConcert> concertSorter,
			CollectionAlbumContainer collectionAlbumContainer, GenerationPane generationPane) {
		super(anneeDebut, anneeFin, collectionAlbumContainer, generationPane);
		this.concertDateFunction = concertDateFunction;
		this.concertSorter = concertSorter;
	}

	public static class Builder extends AbstractStatistiquesMouseAdapterBuilder {
		
		private Function<Concert,TemporalAccessor> concertDateFunction;
		private Function<ListeConcert, ListeConcert> concertSorter;
		
		protected Builder(CollectionAlbumContainer collectionAlbumContainer, 
				Function<Concert, TemporalAccessor> concertDateFunction,  Function<ListeConcert, ListeConcert> concertSorter, 
				GenerationPane generationPane) {
			super(collectionAlbumContainer, generationPane);
			this.concertDateFunction = concertDateFunction;
			this.concertSorter = concertSorter;
		}

		public static Builder builder(CollectionAlbumContainer collectionAlbumContainer, 
				Function<Concert, TemporalAccessor> concertDateFunction,  Function<ListeConcert, ListeConcert> concertSorter, 
				GenerationPane generationPane) {
			return new Builder(collectionAlbumContainer, concertDateFunction, concertSorter, generationPane);
		}
				
		@Override
		public MouseAdapter build(int anneeDebut, int anneeFin) {
			return new ConcertStatistiquesMouseAdapter(anneeDebut, anneeFin, concertDateFunction, concertSorter, collectionAlbumContainer, generationPane);
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent evt) {
		
		if (SwingUtilities.isLeftMouseButton(evt) && (evt.getClickCount() > 1)) {
			
			List<Concert> concertList = concertSorter.apply(collectionAlbumContainer.getConcertsSastisfying(
					List.of(concert -> isBetween(concertDateFunction.apply(concert).get(ChronoField.YEAR), anneeDebut, anneeFin))))
					.getConcerts();
			
			if (!concertList.isEmpty()) {
				
				ConcertsScrollJTablePane concertsScrollJTablePane = new ConcertsScrollJTablePane(concertList, generationPane);
				
				JOptionPane.showMessageDialog(null, concertsScrollJTablePane, getWindowsTitle("Concerts "), JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
}
