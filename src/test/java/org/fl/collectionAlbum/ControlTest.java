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

package org.fl.collectionAlbum;

import static org.assertj.core.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.fl.collectionAlbum.format.ContentNature;
import org.fl.util.RunningContext;
import org.fl.util.file.FilesUtils;
import org.junit.jupiter.api.Test;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

class ControlTest {
	
	private static final String APPLICATION_NAME = "org.fl.collectionAlbum";
	
	@Test
	void controlBasicTest() {
		
		assertThat(Control.getCharset()).isNotNull();
		assertThat(Control.getCollectionDirectoryName()).isNotNull();
		assertThat(Control.getConcertDirectoryName()).isNotNull();
		assertThat(Control.getAlbumSleevesImgUri()).isNotNull().isNotEmpty();
		assertThat(Control.getConcertTicketImgUri()).isNotNull().isNotEmpty();
		assertThat(Control.getCssForGui()).isNotNull().isNotEmpty();
		
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
				assertThat(Control.getMediaFileRootPath(contentNature)).isNotNull().exists().isAbsolute().isDirectory());
	}
	
	@Test
	void controlMediaPathFileUriTest() {
		
		Stream.of(ContentNature.values())
			.forEach(contentNature -> 
				assertThat(Control.getMediaFileRootUri(contentNature)).isNotNull()
					.satisfies(mediaRootUri -> 
						assertThat(FilesUtils.uriStringToAbsolutePath(mediaRootUri.toString())).isNotNull().exists().isAbsolute().isDirectory()));
	}
	
	@Test
	void controlOsActionTest() {
		
		assertThat(Control.getOsActionOnDiscogsRelease()).isNotNull().isNotEmpty();
		assertThat(Control.getOsActionOnMediaFilePath()).isNotNull().isNotEmpty();
		assertThat(Control.getOsActionsOnAlbum()).isNotNull().isNotEmpty();
		assertThat(Control.getOsActionsOnConcert()).isNotNull().isNotEmpty();
	}
	
	@Test
	void runningContextTest() {
		
		RunningContext runningContext = Control.getMusicRunningContext();
		
		assertThat(runningContext).isNotNull();
		assertThat(runningContext.getName()).isNotNull().isEqualTo(APPLICATION_NAME);
		
		JsonNode applicationInfo = runningContext.getApplicationInfo(false);
		assertThat(applicationInfo).isNotNull();
		
		JsonNode buildInformation = applicationInfo.get("buildInformation");
		assertThat(buildInformation).isNotEmpty().hasSize(3)
		.satisfiesExactlyInAnyOrder(
				buildInfo -> { 
					assertThat(buildInfo.get("moduleName")).isNotNull();
					assertThat(buildInfo.get("moduleName").asString()).isEqualTo(APPLICATION_NAME);
					assertThat(buildInfo.get("version")).isNotNull();
					assertThat(buildInfo.get("version").asString()).isNotEmpty();
				},
				buildInfo -> { 
					assertThat(buildInfo.get("moduleName")).isNotNull();
					assertThat(buildInfo.get("moduleName").asString()).isEqualTo("org.fl.util");
					assertThat(buildInfo.get("version")).isNotNull();
					assertThat(buildInfo.get("version").asString()).isNotEmpty();
				},
				buildInfo -> { 
					assertThat(buildInfo.get("moduleName")).isNotNull();
					assertThat(buildInfo.get("moduleName").asString()).isEqualTo("org.fl.discogsInterface");
					assertThat(buildInfo.get("version")).isNotNull();
					assertThat(buildInfo.get("version").asString()).isNotEmpty();
				}
				);
	}

	@Test
	void propertyFileTest() throws URISyntaxException {
		
		RunningContext runningContext = Control.getMusicRunningContext();
		
		URL propertyFileLocation = runningContext.getPropertiesLocation();
		assertThat(propertyFileLocation).isNotNull();

		URI propertyFileUri = propertyFileLocation.toURI();
		assertThat(propertyFileUri.isAbsolute()).isTrue();
		Path propertyFilePath = Paths.get(propertyFileUri);
		
		assertThat(propertyFilePath).exists().isRegularFile();
	}
	
	@Test
	void buildInformationTest() throws JacksonException, URISyntaxException {
		
		RunningContext runningContext = Control.getMusicRunningContext();
		
		assertThat(runningContext).isNotNull();
		
		JsonNode buildInformation = runningContext.getBuildInformationAsJson();
		assertThat(buildInformation).isNotNull();

		assertThat(buildInformation).isNotEmpty().hasSize(3)
			.satisfiesExactlyInAnyOrder(
				buildInfo -> assertModuleBuildInfo(buildInfo, APPLICATION_NAME),
				buildInfo -> assertModuleBuildInfo(buildInfo, "org.fl.discogsInterface"),
				buildInfo -> assertModuleBuildInfo(buildInfo, "org.fl.util")
			);
	}
	
	private void assertModuleBuildInfo(JsonNode buildInfo, String moduleName) {
		assertThat(buildInfo).hasSize(14);
		assertThat(buildInfo.has("builderName")).isTrue();
		assertThat(buildInfo.has("builderEmail")).isTrue();
		assertThat(buildInfo.has("gitCommitIdDescribe")).isTrue();
		assertThat(buildInfo.get("moduleName")).isNotNull();
		assertThat(buildInfo.get("moduleName").asString()).isEqualTo(moduleName);
		assertThat(buildInfo.has("version")).isTrue();
		assertThat(buildInfo.has("buildtime")).isTrue();
		assertThat(buildInfo.has("builder")).isTrue();
		assertThat(buildInfo.has("buildhost")).isTrue();
		assertThat(buildInfo.has("buildOs")).isTrue();
		assertThat(buildInfo.has("gitBranch")).isTrue();
		assertThat(buildInfo.has("gitCommitId")).isTrue();
		assertThat(buildInfo.has("gitCommitUrl")).isTrue();
		assertThat(buildInfo.has("gitCommitTime")).isTrue();
		assertThat(buildInfo.has("gitDirty")).isTrue();
	}
}
