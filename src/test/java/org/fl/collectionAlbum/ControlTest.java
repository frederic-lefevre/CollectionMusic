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

package org.fl.collectionAlbum;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.fl.collectionAlbum.format.ContentNature;
import org.fl.util.RunningContext;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

class ControlTest {
	
	@Test
	void controlBasicTest() {
		
		assertThat(Control.getCharset()).isNotNull();
		assertThat(Control.getCollectionDirectoryName()).isNotNull();
		assertThat(Control.getConcertDirectoryName()).isNotNull();
		assertThat(Control.getConcertTicketImgUri()).isNotNull();
		assertThat(Control.getCssForGui()).isNotNull();
		
		assertThat(Control.getDisplayFolderAction()).isNotNull();
		assertThat(Control.getDisplayUrlAction()).isNotNull();
	
	}
	
	@Test
	void controlMetricsHistoryTest() {
		
		assertThat(Control.getCollectionMetricsHsitory()).isNotNull()
			.satisfies(metricsHistory -> assertThat(metricsHistory.getMetricsHistory()).isNotNull());
		assertThat(Control.getConcertMetricsHsitory()).isNotNull()
			.satisfies(metricsHistory -> assertThat(metricsHistory.getMetricsHistory()).isNotNull());
	}
	
	@Test
	void controlMediaPathFileTest() {
		
		Stream.of(ContentNature.values())
			.forEach(contentNature -> 
				assertThat(Control.getMediaFileRootPath(contentNature)).isNotNull());
	}
	
	@Test
	void controlOsActionTest() {
		
		assertThat(Control.getOsActionOnDiscogsRelease()).isNotNull().isNotEmpty();
		assertThat(Control.getOsActionOnMediaFilePath()).isNotNull().isNotEmpty();
		assertThat(Control.getOsActionsOnAlbum()).isNotNull().isNotEmpty();
	}
	
	@Test
	void runningContextTest() {
		
		RunningContext runningContext = Control.getMusicRunningContext();
		
		assertThat(runningContext).isNotNull();
		assertThat(runningContext.getName()).isNotNull().isEqualTo("org.fl.collectionAlbum");
		
		JsonNode applicationInfo = runningContext.getApplicationInfo(false);
		assertThat(applicationInfo).isNotNull();
		
		JsonNode buildInformation = applicationInfo.get("buildInformation");
		assertThat(buildInformation).isNotEmpty().hasSize(3)
		.satisfiesExactlyInAnyOrder(
				buildInfo -> { 
					assertThat(buildInfo.get("moduleName")).isNotNull();
					assertThat(buildInfo.get("moduleName").asText()).isEqualTo("org.fl.collectionAlbum");
				},
				buildInfo -> { 
					assertThat(buildInfo.get("moduleName")).isNotNull();
					assertThat(buildInfo.get("moduleName").asText()).isEqualTo("org.fl.util");
				},
				buildInfo -> { 
					assertThat(buildInfo.get("moduleName")).isNotNull();
					assertThat(buildInfo.get("moduleName").asText()).isEqualTo("discogsInterface");
				}
				);
		
	}

}
