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

package org.fl.collectionAlbum.metrics;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.fl.collectionAlbum.disocgs.DiscogsInterface;

public class DiscogsMetricsHistory extends MetricsHistory<DiscogsInterface> {

	private static final String METRIC_NAME = "Evolution de la collection Discogs";
	
	private static final String NB_RELEASE = "nbRelease";
	private static final String MAX_VALUE = "maxValue";
	private static final String MEDIAN_VALUE = "medianValue";
	private static final String MIN_VALUE = "minValue";
	
	// Singleton
	private static DiscogsMetricsHistory discogsMetricsHistory;
	
	public static DiscogsMetricsHistory buildDiscogsMetricsHistory(Path storagePath) throws IOException {
		if (discogsMetricsHistory == null) {
			discogsMetricsHistory = new DiscogsMetricsHistory(storagePath);
		}
		return discogsMetricsHistory;
	}
	
	@Override
	protected Metrics getMetricsFromSource(long ts, DiscogsInterface metricsSource) {
		// TODO Auto-generated method stub
		return new Metrics(ts, 		Map.of(
				));
	}
	
	private DiscogsMetricsHistory(Path storagePath) throws IOException {
		super(storagePath, METRIC_NAME);
	}

	@Override
	public MetricAttributesList getMetricsAttributes() {
		return new MetricAttributesList(List.of(
				new MetricAttributes(NB_RELEASE, "Nombre de releases", 300),
				new MetricAttributes(MAX_VALUE, "Valeur élevée (€)", 300),
				new MetricAttributes(MEDIAN_VALUE, "Valeur médiane (€)", 300),
				new MetricAttributes(MIN_VALUE, "Valeur faible (€)", 300)));
	}
}
