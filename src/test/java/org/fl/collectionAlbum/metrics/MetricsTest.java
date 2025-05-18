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

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class MetricsTest {

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final long now = System.currentTimeMillis();
	private static final long yesterday = System.currentTimeMillis() - 24*3600*1000;
	
	@Test
	void testEmptyMetric() {
		
		Metrics metrics = new Metrics(now, new HashMap<>());	
		assertThat(metrics.getMetricTimeStamp()).isEqualTo(now);
		
		Metrics metrics2 = new Metrics(yesterday, new HashMap<>());
		
		assertThat(metrics.hasSameMetricsAs(metrics2)).isTrue();
		assertThat(metrics2.hasSameMetricsAs(metrics)).isTrue();
	}
	
	@Test
	void testEqualMetrics() {
		
		Metrics metrics = new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0));
		assertThat(metrics.getMetricTimeStamp()).isEqualTo(now);
		
		Metrics metrics2 = new Metrics(yesterday,  Map.of("Artiste", 5.0, "albums", 10.0));
		
		assertThat(metrics.hasSameMetricsAs(metrics2)).isTrue();
		assertThat(metrics2.hasSameMetricsAs(metrics)).isTrue();
	}
	
	@Test
	void testNotEqualMetrics() {
		
		Metrics metrics = new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0));
		assertThat(metrics.getMetricTimeStamp()).isEqualTo(now);
		
		Metrics metrics2 = new Metrics(yesterday,  Map.of("Artiste", 5.0, "albums", 11.0));
		
		assertThat(metrics.hasSameMetricsAs(metrics2)).isFalse();
		assertThat(metrics2.hasSameMetricsAs(metrics)).isFalse();
	}
	
	@Test
	void testSameTypeMetrics() {
		
		Metrics metrics = new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0));		
		Metrics metrics2 = new Metrics(yesterday,  Map.of("Artiste", 5.0, "albums", 11.0));
		
		assertThat(metrics.hasSameMetricsTypeAs(metrics2)).isTrue();
		assertThat(metrics2.hasSameMetricsTypeAs(metrics)).isTrue();
	}
	
	@Test
	void testNotEqualMetrics2() {
		
		Metrics metrics = new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0));
		assertThat(metrics.getMetricTimeStamp()).isEqualTo(now);
		
		Metrics metrics2 = new Metrics(yesterday,  Map.of("Artiste", 5.0, "albums", 10.0, "cd", 1.0));
		
		assertThat(metrics.hasSameMetricsAs(metrics2)).isFalse();
		assertThat(metrics2.hasSameMetricsAs(metrics)).isFalse();
	}
	
	@Test
	void testDifferentTypeMetrics() {
		
		Metrics metrics = new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0));		
		Metrics metrics2 = new Metrics(yesterday,  Map.of("Artiste", 5.0, "albums", 11.0, "cd", 1.0));
		
		assertThat(metrics.hasSameMetricsTypeAs(metrics2)).isFalse();
		assertThat(metrics2.hasSameMetricsTypeAs(metrics)).isFalse();
	}
	
	@Test
	void testDifferentTypeMetrics2() {
		
		Metrics metrics = new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0, "vinyl", 2.0));		
		Metrics metrics2 = new Metrics(yesterday,  Map.of("Artiste", 5.0, "albums", 11.0, "cd", 1.0));
		
		assertThat(metrics.hasSameMetricsTypeAs(metrics2)).isFalse();
		assertThat(metrics2.hasSameMetricsTypeAs(metrics)).isFalse();
	}
	
	@Test
	void testNotEqualMetrics3() {
		
		Metrics metrics = new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0, "cd", 1.0));
		assertThat(metrics.getMetricTimeStamp()).isEqualTo(now);
		
		Metrics metrics2 = new Metrics(yesterday,  Map.of("Artiste", 5.0, "albums", 10.0));
		
		assertThat(metrics.hasSameMetricsAs(metrics2)).isFalse();
		assertThat(metrics2.hasSameMetricsAs(metrics)).isFalse();
	}
	
	@Test
	void testMetricsSerialization() throws JsonProcessingException {
		
		Metrics metrics = new Metrics(now, Map.of("albums", 10.0, "Artiste", 5.0, "cd", 1.0));
		assertThat(metrics.getMetricTimeStamp()).isEqualTo(now);
		
		String s = mapper.writeValueAsString(metrics);
		
		Metrics metrics2 = mapper.readValue(s, Metrics.class);
		assertThat(metrics.hasSameMetricsAs(metrics2)).isTrue();
		assertThat(metrics2.hasSameMetricsAs(metrics)).isTrue();
		
	}
}
