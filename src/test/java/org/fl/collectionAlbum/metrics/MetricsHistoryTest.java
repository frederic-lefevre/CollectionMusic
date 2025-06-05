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
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.*;

import org.fl.util.FilterCounter;
import org.fl.util.FilterCounter.LogRecordCounter;
import org.fl.util.file.FilesUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MetricsHistoryTest {

	private final static Logger mLog = Logger.getLogger(MetricsHistoryTest.class.getName());
	
	private static Path historyFolderBase;

	private static final long now = System.currentTimeMillis();
	private static final long yesterday = System.currentTimeMillis() - 24*3600*1000;
	private static final long twoDaysAgo = System.currentTimeMillis() - 48*3600*1000;
	
	private static Path historyPath1;
	private static Path historyPath2;
	private static Path historyPath3;
	private static Path historyPath4;
	private static Path historyPath5;
	
	private static void deleteFolderIfExist(Path folder) throws IOException {
		if (Files.exists(folder)) {
			FilesUtils.deleteDirectoryTree(folder, true, mLog);
		}
	}
	
	private static class TestMetricsHistory extends MetricsHistory {

		protected TestMetricsHistory(Path storagePath) throws IOException {
			super(storagePath);
		}

		@Override
		public Map<String, String> getMetricsNamesMap() {
			return Map.of("albums", "Nombre album", "Artiste", "Nombre artiste");
		}

		@Override
		public List<String> getMetricsKeys() {			
			return List.of("albums", "Artiste");
		}		
	}
	
	@BeforeAll
	static void initHistoryFolder() throws IOException, URISyntaxException {
		
		historyFolderBase = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusiqueHistory");
		historyPath1 = historyFolderBase.resolve("testHistoryDirectoryPath1");
		historyPath2 = historyFolderBase.resolve("testHistoryDirectoryPath2");
		historyPath3 = historyFolderBase.resolve("testHistoryDirectoryPath3");
		historyPath4 = historyFolderBase.resolve("testHistoryDirectoryPath4");
		historyPath5 = historyFolderBase.resolve("testHistoryDirectoryPath5");
		
		deleteFolderIfExist(historyPath1);
		deleteFolderIfExist(historyPath2);
		deleteFolderIfExist(historyPath3);
		deleteFolderIfExist(historyPath4);
		deleteFolderIfExist(historyPath5);
		Files.createDirectory(historyPath1);
		Files.createDirectory(historyPath2);
		Files.createDirectory(historyPath3);
		Files.createDirectory(historyPath4);
		Files.createDirectory(historyPath5);
	}
	
	@AfterAll
	static void deleteHistoryFolder() throws IOException {
		deleteFolderIfExist(historyPath1);
		deleteFolderIfExist(historyPath2);
		deleteFolderIfExist(historyPath3);
		deleteFolderIfExist(historyPath4);
		deleteFolderIfExist(historyPath5);
	}
	
	@Test
	void testNullStoragePath() {
		
		assertThatIllegalArgumentException()
			.isThrownBy(() -> new TestMetricsHistory(null))
			.withMessageContaining("should not be null");
	}
	
	@Test
	void testNonDirectoryPath() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> new TestMetricsHistory(FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/PortraitInJazz.json")))
			.withMessageContaining("should be a directory");
	}
	
	@Test
	void testNnonExistantStoragePath() {
		
		assertThatIllegalArgumentException()
			.isThrownBy(() -> new TestMetricsHistory(FilesUtils.uriStringToAbsolutePath("file:///ForTests/DoesNotExist")))
			.withMessageContaining("should exist");
	}
	
	@Test
	void testHistoryDirectoryPath() throws IOException {
				
		assertThat(historyPath1).isEmptyDirectory();
		
		MetricsHistory metricsHistory = new TestMetricsHistory(historyPath1);
		
		assertThat(metricsHistory).isNotNull();	
		assertThat(metricsHistory.getMetricsHistory()).isEmpty();
		assertThat(historyPath1).isEmptyDirectory();
		
		metricsHistory.addNewMetrics(new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0)));
		
		assertThat(historyPath1).isNotEmptyDirectory();
		assertThat(metricsHistory.getMetricsHistory()).singleElement()
			.satisfies(metrics -> {
					assertThat(metrics.getMetricTimeStamp()).isEqualTo(now);
					assertThat(metrics.getMetrics()).containsExactlyInAnyOrderEntriesOf(
							Map.of("albums", 10.0, "Artiste", 5.0)
							);
				});
	}
	
	@Test
	void testMetricsHistoryOrder() throws IOException {
		
		Metrics todayMetrics = new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0));
		Metrics yesterdayMetrics = new Metrics(yesterday, Map.of("albums", 8.0, "Artiste", 5.0));
		
		MetricsHistory metricsHistory = new TestMetricsHistory(historyPath2);
		
		metricsHistory.addNewMetrics(todayMetrics);
		metricsHistory.addNewMetrics(yesterdayMetrics);
		
		assertThat(metricsHistory.getMetricsHistory()).hasSize(2)
			.satisfiesExactly(
					metricNow -> { 
						assertThat(metricNow.getMetricTimeStamp()).isEqualTo(now);
						assertThat(metricNow.hasSameMetricsAs(new Metrics(0, Map.of("albums", 10.0, "Artiste", 5.0))));
					},
					metricYesterday -> { 
						assertThat(metricYesterday.getMetricTimeStamp()).isEqualTo(yesterday);
						assertThat(metricYesterday.hasSameMetricsAs(new Metrics(0, Map.of("albums", 8.0, "Artiste", 5.0))));
					});
	}
	
	@Test
	void testAddingSameMetricsTwice() throws IOException {
		
		Metrics todayMetrics = new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0));
		Metrics todayMetrics2 = new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0));
		Metrics yesterdayMetrics = new Metrics(yesterday, Map.of("albums", 8.0, "Artiste", 5.0));
		Metrics twoDaysAgoMetrics = new Metrics(twoDaysAgo, Map.of("albums", 8.0, "Artiste", 5.0));
		
		MetricsHistory metricsHistory = new TestMetricsHistory(historyPath3);
		
		metricsHistory.addNewMetrics(twoDaysAgoMetrics);
		metricsHistory.addNewMetrics(yesterdayMetrics);
		metricsHistory.addNewMetrics(todayMetrics);
		metricsHistory.addNewMetrics(todayMetrics2);
		
		assertThat(metricsHistory.getMetricsHistory()).hasSize(2)
			.satisfiesExactly(
					metricNow -> { 
						assertThat(metricNow.getMetricTimeStamp()).isEqualTo(now);
						assertThat(metricNow.hasSameMetricsAs(new Metrics(0, Map.of("albums", 10.0, "Artiste", 5.0))));
					},
					metricTwoDaysAgo -> { 
						assertThat(metricTwoDaysAgo.getMetricTimeStamp()).isEqualTo(twoDaysAgo);
						assertThat(metricTwoDaysAgo.hasSameMetricsAs(new Metrics(0, Map.of("albums", 8.0, "Artiste", 5.0))));
					});
	}
	
	@Test
	void testAddingIncompatibleMetrics() throws IOException {
		
		Metrics todayMetrics = new Metrics(now, Map.of("albums", 10.0, "Auteurs", 5.0));
		
		MetricsHistory metricsHistory = new TestMetricsHistory(historyPath4);
		
		LogRecordCounter filterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.metrics.MetricsHistory"));
		
		assertThatIllegalArgumentException()
			.isThrownBy(() -> metricsHistory.addNewMetrics(todayMetrics))
			.withMessageContaining("Adding a incompatible metrics to metricsHistory");
		
		assertThat(filterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(filterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
		assertThat(filterCounter.getLogRecords()).singleElement()
			.satisfies(logRecord -> assertThat(logRecord.getMessage()).contains("Adding a incompatible metrics to metricsHistory"));
	}
	
	@Test
	void testAddingIncompatibleMetrics2() throws IOException {
		
		Metrics yesterdayMetrics = new Metrics(yesterday, Map.of("albums", 8.0, "Artiste", 5.0));
		Metrics todayMetrics = new Metrics(now, Map.of("albums", 10.0, "Auteurs", 5.0));
		
		MetricsHistory metricsHistory = new TestMetricsHistory(historyPath5);
		metricsHistory.addNewMetrics(yesterdayMetrics);
		
		LogRecordCounter filterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.metrics.MetricsHistory"));
		
		assertThatIllegalArgumentException()
			.isThrownBy(() -> metricsHistory.addNewMetrics(todayMetrics))
			.withMessageContaining("Adding a incompatible metrics to metricsHistory");
		
		assertThat(filterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(filterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
		assertThat(filterCounter.getLogRecords()).singleElement()
			.satisfies(logRecord -> assertThat(logRecord.getMessage()).contains("Adding a incompatible metrics to metricsHistory"));
	}
	
	@Test
	void testAddingIncompatibleMetrics3() throws IOException {
		
		Metrics todayMetrics = new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0, "Auteurs", 5.0));
		
		MetricsHistory metricsHistory = new TestMetricsHistory(historyPath4);
		
		LogRecordCounter filterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.metrics.MetricsHistory"));
		
		assertThatIllegalArgumentException()
			.isThrownBy(() -> metricsHistory.addNewMetrics(todayMetrics))
			.withMessageContaining("Adding a incompatible metrics to metricsHistory");
		
		assertThat(filterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(filterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
		assertThat(filterCounter.getLogRecords()).singleElement()
			.satisfies(logRecord -> assertThat(logRecord.getMessage()).contains("Adding a incompatible metrics to metricsHistory"));
	}
}
