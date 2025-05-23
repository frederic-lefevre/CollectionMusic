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

package org.fl.collectionAlbum.format;

import static org.assertj.core.api.Assertions.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.json.VideoFileParser;
import org.fl.util.FilterCounter;
import org.fl.util.FilterCounter.LogRecordCounter;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class VideoFileTest {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	@Test
	void shouldHaveAllValues() throws JsonMappingException, JsonProcessingException {
		
		LogRecordCounter videoFileParserFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.VideoFileParser"));
		LogRecordCounter parserHelpersFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.ParserHelpers"));
		
		String videoFileStr1 = "{}" ;
		ObjectNode jf1 = (ObjectNode)mapper.readTree(videoFileStr1);
		
		VideoFileParser videoFileParser = new VideoFileParser();
		VideoFile videoFile = videoFileParser.parseMediaFile(jf1);
		assertThat(videoFile).isNull();
		
		assertThat(videoFileParserFilterCounter.getLogRecordCount()).isEqualTo(3);
		assertThat(videoFileParserFilterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(3);
		
		if (parserHelpersFilterCounter.isLoggable(Level.INFO)) {
			assertThat(parserHelpersFilterCounter.getLogRecordCount()).isEqualTo(2);
			assertThat(parserHelpersFilterCounter.getLogRecordCount(Level.INFO)).isEqualTo(1);
		} else {
			assertThat(parserHelpersFilterCounter.getLogRecordCount()).isEqualTo(1);
		}
		assertThat(parserHelpersFilterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
	}

	@Test
	void shouldAcceptNullWithError() {
		
		LogRecordCounter videoFileParserFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.VideoFileParser"));
		
		VideoFileParser videoFileParser = new VideoFileParser();
		VideoFile videoFile = videoFileParser.parseMediaFile(null);
		assertThat(videoFile).isNull();
		
		assertThat(videoFileParserFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(videoFileParserFilterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
	}
	
	@Test
	void shouldNotDeserializeToVideoFile() throws JsonMappingException, JsonProcessingException {
		
		LogRecordCounter videoFileParserFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.VideoFileParser"));
		LogRecordCounter parserHelpersFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.ParserHelpers"));
		
		String videoFileStr1 = """
			{"width": 720, 
			 "height" : 480, 
			 "source" : "DVD" }
				""" ;
		ObjectNode jf1 = (ObjectNode)mapper.readTree(videoFileStr1);
		
		VideoFileParser videoFileParser = new VideoFileParser();
		VideoFile videoFile = videoFileParser.parseMediaFile(jf1);
		assertThat(videoFile).isNull();
		
		assertThat(videoFileParserFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(videoFileParserFilterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
		
		if (parserHelpersFilterCounter.isLoggable(Level.INFO)) {
			assertThat(parserHelpersFilterCounter.getLogRecordCount()).isEqualTo(1);
			assertThat(parserHelpersFilterCounter.getLogRecordCount(Level.INFO)).isEqualTo(1);
		} else {
			assertThat(parserHelpersFilterCounter.getLogRecordCount()).isEqualTo(0);
		}
	}
	
	@Test
	void shouldDeserializeToVideoFileWithoutNote() throws JsonMappingException, JsonProcessingException {
		
		LogRecordCounter parserHelpersFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.ParserHelpers"));
		
		String videoFileStr1 = """
			{"width": 720, 
			 "height" : 480,
			 "type" : "MKV", 
			 "source" : "DVD" }
				""" ;
		ObjectNode jf1 = (ObjectNode)mapper.readTree(videoFileStr1);
		
		VideoFileParser videoFileParser = new VideoFileParser();
		VideoFile videoFile = videoFileParser.parseMediaFile(jf1);
		assertThat(videoFile).isNotNull();
		assertThat(videoFile.getHeight()).isEqualTo(480);
		assertThat(videoFile.getWidth()).isEqualTo(720);
		assertThat(videoFile.getSource()).isEqualTo("DVD");
		assertThat(videoFile.getType()).isEqualTo(VideoFileType.MKV);
		assertThat(videoFile.getNote()).isNull();
		
		if (parserHelpersFilterCounter.isLoggable(Level.INFO)) {
			assertThat(parserHelpersFilterCounter.getLogRecordCount()).isEqualTo(1);
			assertThat(parserHelpersFilterCounter.getLogRecordCount(Level.INFO)).isEqualTo(1);
		} else {
			assertThat(parserHelpersFilterCounter.getLogRecordCount()).isEqualTo(0);
		}
	}
	
	@Test
	void shouldDeserializeToVideoFile() throws JsonMappingException, JsonProcessingException {
		
		LogRecordCounter parserHelpersFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.ParserHelpers"));
		
		String videoFileStr1 = """
			{"width": 720, 
			 "height" : 480,
			 "type" : "MKV", 
			 "source" : "DVD",
			 "note" : "version noir et blanc" }
				""" ;
		ObjectNode jf1 = (ObjectNode)mapper.readTree(videoFileStr1);
		
		VideoFileParser videoFileParser = new VideoFileParser();
		VideoFile videoFile = videoFileParser.parseMediaFile(jf1);
		assertThat(videoFile).isNotNull();
		assertThat(videoFile.getHeight()).isEqualTo(480);
		assertThat(videoFile.getWidth()).isEqualTo(720);
		assertThat(videoFile.getSource()).isEqualTo("DVD");
		assertThat(videoFile.getType()).isEqualTo(VideoFileType.MKV);
		assertThat(videoFile.getNote()).isEqualTo("version noir et blanc");
		
		assertThat(videoFile.displayMediaFileDetailTitles(";")).isEqualTo("Width;Height;Type;Source;Note");
		
		if (parserHelpersFilterCounter.isLoggable(Level.INFO)) {
			assertThat(parserHelpersFilterCounter.getLogRecordCount()).isEqualTo(1);
			assertThat(parserHelpersFilterCounter.getLogRecordCount(Level.INFO)).isEqualTo(1);
		} else {
			assertThat(parserHelpersFilterCounter.getLogRecordCount()).isEqualTo(0);
		}
	}
}
