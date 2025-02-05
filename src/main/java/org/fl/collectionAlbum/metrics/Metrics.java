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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Metrics {

	private final LocalDateTime metricDate;	
	private final Map<String, Double> metrics;
	
	protected Metrics(LocalDateTime metricDate, Map<String,Double> metrics) {
		
		if (metricDate == null) {
			throw new IllegalArgumentException("Illegal argument metrics date null");
		}
		if (metrics == null) {
			throw new IllegalArgumentException("Illegal argument metrics map null");
		}
		
		this.metricDate = metricDate;
		this.metrics = new HashMap<>();
		this.metrics.putAll(metrics);
	}
	
	public LocalDateTime getMetricDate() {
		return metricDate;
	}
	
	protected boolean hasSameMetricsAs(Metrics m) {
		
		if (m == null) {
			throw new IllegalArgumentException("Illegal argument null");
		} else {			
			return ((m.metrics.size() == metrics.size()) &&
					metrics.entrySet().stream()
						.allMatch(entry -> 
							(m.metrics.containsKey(entry.getKey()) && 
							(m.metrics.get(entry.getKey()).equals(metrics.get(entry.getKey()))))));
		}
	}
}
