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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.fl.collectionAlbum.Control;
import org.fl.util.json.JsonUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class MetricsHistory {

	private static final MetricsDateComparator metricsDateComparator = new MetricsDateComparator();
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final Logger mLog = Logger.getLogger(MetricsHistory.class.getName());
			
	private final Path storagePath;
	private final String name;
	private final List<Metrics> metricsHistory;
	private Metrics presentMetrics;
	
	protected MetricsHistory(Path storagePath, String name) throws IOException {
		
		this.name = name;
		presentMetrics = null;
		if (storagePath == null) {
			throw new IllegalArgumentException("The metrics history storage path should not be null");
		} else if (! Files.exists(storagePath)) {
			throw new IllegalArgumentException("The metrics history storage path should exist");
		}
		
		this.storagePath = storagePath;
		this.metricsHistory = new ArrayList<>();
		
		if (Files.isDirectory(storagePath)) {
			// Read metrics files
			try (Stream<Path> stream = Files.list(storagePath)) {
				stream
					.filter(path -> Files.isRegularFile(path))
					.map(path -> {
						try {
							return mapper.readValue(Files.newInputStream(path), Metrics.class);
						} catch (Exception e) {
							mLog.log(Level.SEVERE,"Exception dans la deserialization du fichier metrics json " + path, e);
							return null;
						}
					})
					.filter(Objects::nonNull)
					.forEachOrdered(metrics -> metricsHistory.add(metrics));
			} catch (IOException e) {
				mLog.log(Level.SEVERE,"Exception dans la lecture des fichier metrics json dans la directorie " + storagePath, e);
				throw e;
			}

		} else {
			throw new IllegalArgumentException("The metrics history storage path should be a directory");
		}
	}

	public String getName() {
		return name;
	}
	
	public Metrics getPresentMetrics() {
		return presentMetrics;
	}

	public boolean hasEvolved() {
		return presentMetrics != null;
	}
	
	public void setPresentMetricsIfNew(Metrics presentMetrics) {
		if (metricsHistory.stream().allMatch(m -> !m.hasSameMetricsAs(presentMetrics))) {
			this.presentMetrics = presentMetrics;
		}
	}

	protected boolean addAndWriteNewMetricsToHistory(Metrics metrics) {
	
		if (! hasMetricsCompatibleWithMetricNames(metrics)) {
			
			String errorMessage = "Adding a incompatible metrics to metricsHistory.\nMetrics added: " + Objects.toString(metrics.getMetrics()) 
				+ "\nHistory metrics pattern: " + Objects.toString(getMetricsAttributes().getKeySet());
			mLog.severe(errorMessage);
			throw new IllegalArgumentException(errorMessage);	
			
		} else if (metricsHistory.stream().allMatch(m -> !m.hasSameMetricsAs(metrics))) {
			// Do not add the same metrics twice
			metricsHistory.add(metrics);
			writeJson(metrics);
			return true;
		} else {
			return false;
		}
	}
	
	protected Metrics addPresentMetricsToHistory(Metrics metrics) {
		if (addAndWriteNewMetricsToHistory(metrics)) {
			this.presentMetrics = null;
			return metrics;
		} else {
			return null;
		}
		
	}
	
	private boolean hasMetricsCompatibleWithMetricNames(Metrics metrics) {
		return ((metrics.getMetrics().size() == getMetricsAttributes().size()) &&
				metrics.getMetrics().keySet().stream().allMatch(key -> getMetricsAttributes().containsKey(key)));
	}
	
	public List<Metrics> getMetricsHistory() {
		Collections.sort(metricsHistory, metricsDateComparator);
		return metricsHistory;
	}
	
	public abstract MetricAttributesList getMetricsAttributes();
	
	public static class MetricAttributesList extends ArrayList<MetricAttributes> {

		private static final long serialVersionUID = 1L;
		private final Set<String> keySet;
		
		public MetricAttributesList(List<MetricAttributes> metricAttributes) {		
			super(metricAttributes);
			keySet = new HashSet<>(metricAttributes.stream().map(m -> m.getMetricKey()).toList());
		}
		
		private boolean containsKey(String key) {
			return keySet.contains(key);
		}
		
		private Set<String> getKeySet() {
			return keySet;
		}
	}
	
	public static class MetricAttributes {
		
		private final String metricKey;
		private final String metricName;
		private final int representationWidth; // Width of the column that holds the metric
		
		MetricAttributes(String metricKey, String metricName, int representationWidth) {
			super();
			this.metricKey = metricKey;
			this.metricName = metricName;
			this.representationWidth = representationWidth;
		}

		public String getMetricKey() {
			return metricKey;
		}

		public String getMetricName() {
			return metricName;
		}

		public int getRepresentationWidth() {
			return representationWidth;
		}
	}
	
	private static class MetricsDateComparator implements Comparator<Metrics> {

		@Override
		public int compare(Metrics o1, Metrics o2) {
			
			long difference = o1.getMetricTimeStamp() - o2.getMetricTimeStamp();
			if (difference > 0) {
				return -1;
			} else if (difference < 0) {
				return 1;
			} else {
				return 0;
			}
		}		
	}
	
	private boolean writeJson(Metrics metrics) {
		
		Path filePath = findUnexistantFileName(storagePath.resolve("m" + metrics.getMetricTimeStamp()));
		try (BufferedWriter buff = Files.newBufferedWriter(filePath, Control.getCharset())) {
			
			buff.write(JsonUtils.jsonPrettyPrint(mapper.valueToTree(metrics))) ;
			mLog.fine(() -> "Ecriture du fichier metrics json: " + filePath);
			return true;

		} catch (Exception e) {			
			mLog.log(Level.SEVERE,"Erreur dans l'écriture du fichier json " + filePath, e);
			return false;
		}
	}
	
	private Path findUnexistantFileName(Path filePath) {
		
		if (Files.exists(filePath)) {
			return findUnexistantFileName(filePath, 0);
		} else {
			return filePath;
		}
	}
	
	private Path findUnexistantFileName(Path filePath, Integer suffix) {
		
		if (Files.exists(filePath.resolve(suffix.toString()))) {
			return findUnexistantFileName(filePath, suffix + 1);
		} else {
			return filePath;
		}
	}
}
