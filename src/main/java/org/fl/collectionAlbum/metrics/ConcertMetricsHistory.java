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

package org.fl.collectionAlbum.metrics;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.fl.collectionAlbum.CollectionAlbumContainer;

public class ConcertMetricsHistory extends MetricsHistory {

	private static final String NB_ARTISTE = "nombreArtiste";
	private static final String NB_CONCERT = "nombreConcert";
	
	// Singleton
	private static ConcertMetricsHistory concertMetricsHistory;
	
	public static ConcertMetricsHistory buildConcertMetricsHistory(Path storagePath) throws IOException {
		
		if (concertMetricsHistory == null) {
			concertMetricsHistory = new ConcertMetricsHistory(storagePath);
		}
		return concertMetricsHistory;
	}
	
	public Metrics addPresentConcertMetricsToHistory(long ts, CollectionAlbumContainer collectionAlbumContainer) {	

		Metrics presentMetrics = getConcertMetrics(ts, collectionAlbumContainer);
		addNewMetrics(presentMetrics);
		
		return presentMetrics;
	}
	
	public Metrics getConcertMetrics(long ts, CollectionAlbumContainer collectionAlbumContainer) {	

		return new Metrics(ts, 		Map.of(
				NB_ARTISTE, (double)collectionAlbumContainer.getConcertsArtistes().getNombreArtistes(),
				NB_CONCERT, (double)collectionAlbumContainer.getConcerts().getNombreConcerts()));
	}
	
	private ConcertMetricsHistory(Path storagePath) throws IOException {
		super(storagePath);
	}

	@Override
	public Map<String, String> getMetricsNamesMap() {

		return Map.of(NB_ARTISTE, "Nombre d'artistes", NB_CONCERT, "Nombre de concerts");
	}

	@Override
	public List<String> getMetricsKeys() {
		return List.of(NB_CONCERT, NB_ARTISTE);
	}
}
