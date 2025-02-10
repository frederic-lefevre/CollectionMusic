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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.*;

import org.fl.util.file.FilesUtils;
import org.junit.jupiter.api.Test;

class MetricsHistoryTest {

	private final static Logger mLog = Logger.getLogger(MetricsHistoryTest.class.getName());
	
	private static final Path historyFolderBase = Path.of("C:\\ForTests\\CollectionMusiqueHistory");
	private static final long now = System.currentTimeMillis();
	private static final long yesterday = System.currentTimeMillis() - 24*3600*1000;
	
	@Test
	void testNullStoragePath() {
		
		assertThatIllegalArgumentException()
			.isThrownBy(() -> new MetricsHistory(null))
			.withMessageContaining("should not be null");
	}
	
	@Test
	void testNonDirectoryPath() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> new MetricsHistory(Path.of("C:\\ForTests\\CollectionMusique\\PortraitInJazz.json")))
			.withMessageContaining("should be a directory");
	}
	
	@Test
	void testNnonExistantStoragePath() {
		
		assertThatIllegalArgumentException()
			.isThrownBy(() -> new MetricsHistory(Path.of("C:\\ForTests\\DoesNotExist")))
			.withMessageContaining("should exist");
	}
	
	@Test
	void testHistoryDirectoryPath() throws IOException {
		
		Path historyPath = historyFolderBase.resolve("testHistoryDirectoryPath");
		Files.createDirectory(historyPath);
		
		assertThat(historyPath).isEmptyDirectory();
		
		MetricsHistory metricsHistory = new MetricsHistory(historyPath);
		
		assertThat(metricsHistory).isNotNull();	
		assertThat(metricsHistory.getMetricsHistory()).isEmpty();
		assertThat(historyPath).isEmptyDirectory();
		
		metricsHistory.addNewMetrics(new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0)));
		
		assertThat(historyPath).isNotEmptyDirectory();
		assertThat(metricsHistory.getMetricsHistory()).singleElement()
			.satisfies(metrics -> {
					assertThat(metrics.getMetricTimeStamp()).isEqualTo(now);
					assertThat(metrics.getMetrics()).containsExactlyInAnyOrderEntriesOf(
							Map.of("albums", 10.0, "Artiste", 5.0)
							);
				});
		
		FilesUtils.deleteDirectoryTree(historyPath, true, mLog);
		assertThat(historyPath).doesNotExist();
	}
	
	@Test
	void testMetricsHistoryOrder() throws IOException {
		
		Metrics todayMetrics = new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0));
		Metrics yesterdayyMetrics = new Metrics(yesterday, Map.of("albums", 8.0, "Artiste", 5.0));
		
		Path historyPath = historyFolderBase.resolve("testMetricsHistoryOrder");
		Files.createDirectory(historyPath);
		
		MetricsHistory metricsHistory = new MetricsHistory(historyPath);
		
		metricsHistory.addNewMetrics(todayMetrics);
		metricsHistory.addNewMetrics(yesterdayyMetrics);
		
		assertThat(metricsHistory.getMetricsHistory()).hasSize(2)
			.satisfiesExactly(
					metric1 -> { 
						assertThat(metric1.getMetricTimeStamp()).isEqualTo(yesterday);
						assertThat(metric1.hasSameMetricsAs(new Metrics(0, Map.of("albums", 8.0, "Artiste", 5.0))));
					},
					metric2 -> { 
						assertThat(metric2.getMetricTimeStamp()).isEqualTo(now);
						assertThat(metric2.hasSameMetricsAs(new Metrics(0, Map.of("albums", 10.0, "Artiste", 5.0))));
					}
					);
		
		FilesUtils.deleteDirectoryTree(historyPath, true, mLog);
		assertThat(historyPath).doesNotExist();
	}
}
