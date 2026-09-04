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

public class DiscogsMetricsHistory extends MetricsHistory {

	private static final String METRIC_NAME = "Evolution de la collection Discogs";
	
	private static final String MAX_VALUE = "Valeur élevée";
	private static final String MEDIAN_VALUE = "Valeur médiane";
	private static final String MIN_VALUE = "Valeur faible";
	
	// Singleton
	private static DiscogsMetricsHistory discogsMetricsHistory;
	
	public static DiscogsMetricsHistory buildDiscogsMetricsHistory(Path storagePath) throws IOException {
		if (discogsMetricsHistory == null) {
			discogsMetricsHistory = new DiscogsMetricsHistory(storagePath);
		}
		return discogsMetricsHistory;
	}
	
	private DiscogsMetricsHistory(Path storagePath) throws IOException {
		super(storagePath, METRIC_NAME);
	}

	@Override
	public MetricAttributesList getMetricsAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

}
