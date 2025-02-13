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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.fl.collectionAlbum.Control;
import org.fl.util.json.JsonUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MetricsHistory {

	private static final MetricsDateComparator metricsDateComparator = new MetricsDateComparator();
	private static final ObjectMapper mapper = new ObjectMapper();
	private final static Logger mLog = Logger.getLogger(MetricsHistory.class.getName());
			
	private final Path storagePath;
	private final List<Metrics> metricsHistory;
	
	public MetricsHistory(Path storagePath) throws IOException {
		
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
					.forEach(metrics -> metricsHistory.add(metrics));
			} catch (IOException e) {
				mLog.log(Level.SEVERE,"Exception dans la lecture des fichier metrics json dans la directorie " + storagePath, e);
				throw e;
			}

		} else {
			throw new IllegalArgumentException("The metrics history storage path should be a directory");
		}
	}

	public boolean addNewMetrics(Metrics metrics) {
		
		if (metricsHistory.stream().allMatch(m -> !m.hasSameMetricsAs(metrics))) {
			metricsHistory.add(metrics);
			writeJson(metrics);
			return true;
		} else {
			return false;
		}
	}
	
	public List<Metrics> getMetricsHistory() {
		Collections.sort(metricsHistory, metricsDateComparator);
		return metricsHistory;
	}
	
	private static class MetricsDateComparator implements Comparator<Metrics> {

		@Override
		public int compare(Metrics o1, Metrics o2) {
			
			return (int)(o1.getMetricTimeStamp() - o2.getMetricTimeStamp());
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
