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

package org.fl.collectionAlbum.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTabbedPane;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.gui.adapter.AlbumStatistiquesMouseAdapter;
import org.fl.collectionAlbum.gui.adapter.ConcertStatistiquesMouseAdapter;
import org.fl.collectionAlbum.stat.StatistiquesView.Granularite;

public class CalendarsTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	private final List<UpdatableElement> updatableElements;
	
	public CalendarsTabbedPane(CollectionAlbumContainer collectionAlbumContainer, GenerationPane generationPane) {
		
		super();
		
		updatableElements = new ArrayList<>();
		
		AlbumStatistiquesMouseAdapter.Builder stCompositionBuilder = 
				AlbumStatistiquesMouseAdapter.Builder.builder(collectionAlbumContainer, 
						album -> album.getDebutComposition(),
						listeAlbum -> listeAlbum.sortChronoComposition(),
						generationPane);
		AlbumStatistiquesMouseAdapter.Builder stEnregistrementBuilder = 
				AlbumStatistiquesMouseAdapter.Builder.builder(collectionAlbumContainer, 
						album -> album.getDebutEnregistrement(), 
						listeAlbum -> listeAlbum.sortChronoEnregistrement(), 
						generationPane);
		AlbumStatistiquesMouseAdapter.Builder stAcquisitionBuilder = 
				AlbumStatistiquesMouseAdapter.Builder.builder(collectionAlbumContainer, 
						album -> album.getAcquisitionDate(), 
						listeAlbum -> listeAlbum.sortChronoAcquisition(), 
						generationPane);
		
		ConcertStatistiquesMouseAdapter.Builder stConcertBuilder =
				ConcertStatistiquesMouseAdapter.Builder.builder(collectionAlbumContainer, 
						concert -> concert.getDateConcert(), 
						listeConcert -> listeConcert.sortChrono(), 
						generationPane);
		
		StatisticsScrollPane compositionStatisticsPane = 
				new StatisticsScrollPane(collectionAlbumContainer.getStatChronoComposition(), Granularite.PAR_DECENNIE, stCompositionBuilder);
		StatisticsScrollPane enregistrementStatisticsPane = 
				new StatisticsScrollPane(collectionAlbumContainer.getStatChronoEnregistrement(), Granularite.PAR_AN, stEnregistrementBuilder);
		StatisticsScrollPane acquisitionStatisticsPane = 
				new StatisticsScrollPane(collectionAlbumContainer.getStatChronoAcquisition(), Granularite.PAR_AN, stAcquisitionBuilder);
		
		StatisticsScrollPane concertStatisticsPane = 
				new StatisticsScrollPane(collectionAlbumContainer.getStatChronoConcert(), Granularite.PAR_AN, stConcertBuilder);
		
		updatableElements.add(compositionStatisticsPane);
		updatableElements.add(enregistrementStatisticsPane);
		updatableElements.add(acquisitionStatisticsPane);
		updatableElements.add(concertStatisticsPane);
		
		addTab("Composition des albums", compositionStatisticsPane);
		addTab("Enregistrement des albums", enregistrementStatisticsPane);
		addTab("Acquisition des albums", acquisitionStatisticsPane);
		addTab("Concerts", concertStatisticsPane);
		
		CalendarPane calendarPane = new CalendarPane(collectionAlbumContainer, generationPane);
		updatableElements.addAll(calendarPane.getUpdatableElements());
		addTab("Anniversaires", calendarPane);
	}
	
	public List<UpdatableElement> getUpdatableElements() {
		return updatableElements;
	}
}
