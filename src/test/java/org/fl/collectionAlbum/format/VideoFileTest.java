/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

import static org.assertj.core.api.Assertions.assertThat;

import org.fl.collectionAlbum.json.VideoFileParser;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VideoFileTest {
	
	@Test
	void shouldHaveAllValues() {
		
		String videoFileStr1 = "{}" ;
		JsonObject jf1 = JsonParser.parseString(videoFileStr1).getAsJsonObject();
		
		VideoFileParser videoFileParser = new VideoFileParser();
		VideoFile videoFile = videoFileParser.parseMediaFile(jf1);
		assertThat(videoFile).isNull();
	}

	@Test
	void shouldAcceptNullWithError() {
		
		VideoFileParser videoFileParser = new VideoFileParser();
		VideoFile videoFile = videoFileParser.parseMediaFile(null);
		assertThat(videoFile).isNull();
	}
	
	@Test
	void shouldNotDeserializeToVideoFile() {
		
		String videoFileStr1 = """
			{"width": 720, 
			 "height" : 480, 
			 "source" : "DVD" }
				""" ;
		JsonObject jf1 = JsonParser.parseString(videoFileStr1).getAsJsonObject();
		
		VideoFileParser videoFileParser = new VideoFileParser();
		VideoFile videoFile = videoFileParser.parseMediaFile(jf1);
		assertThat(videoFile).isNull();
	}
	
	@Test
	void shouldDeserializeToVideoFileWithoutNote() {
		
		String videoFileStr1 = """
			{"width": 720, 
			 "height" : 480,
			 "type" : "MKV", 
			 "source" : "DVD" }
				""" ;
		JsonObject jf1 = JsonParser.parseString(videoFileStr1).getAsJsonObject();
		
		VideoFileParser videoFileParser = new VideoFileParser();
		VideoFile videoFile = videoFileParser.parseMediaFile(jf1);
		assertThat(videoFile).isNotNull();
		assertThat(videoFile.getHeight()).isEqualTo(480);
		assertThat(videoFile.getWidth()).isEqualTo(720);
		assertThat(videoFile.getSource()).isEqualTo("DVD");
		assertThat(videoFile.getType()).isEqualTo(VideoFileType.MKV);
		assertThat(videoFile.getNote()).isNull();
	}
	
	@Test
	void shouldDeserializeToVideoFile() {
		
		String videoFileStr1 = """
			{"width": 720, 
			 "height" : 480,
			 "type" : "MKV", 
			 "source" : "DVD",
			 "note" : "version noir et blanc" }
				""" ;
		JsonObject jf1 = JsonParser.parseString(videoFileStr1).getAsJsonObject();
		
		VideoFileParser videoFileParser = new VideoFileParser();
		VideoFile videoFile = videoFileParser.parseMediaFile(jf1);
		assertThat(videoFile).isNotNull();
		assertThat(videoFile.getHeight()).isEqualTo(480);
		assertThat(videoFile.getWidth()).isEqualTo(720);
		assertThat(videoFile.getSource()).isEqualTo("DVD");
		assertThat(videoFile.getType()).isEqualTo(VideoFileType.MKV);
		assertThat(videoFile.getNote()).isEqualTo("version noir et blanc");
		
		assertThat(videoFile.displayMediaFileDetailTitles(";")).isEqualTo("Width;Height;Type;Source;Note");
	}
}
