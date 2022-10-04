package org.fl.collectionAlbum;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.logging.Logger;

import org.fl.collectionAlbum.jsonParsers.VideoFileParser;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VideoFileTest {

	private final static Logger logger = Logger.getLogger(VideoFileTest.class.getName());
	
	@Test
	void shouldHaveAllValues() {
		
		String videoFileStr1 = "{}" ;
		JsonObject jf1 = JsonParser.parseString(videoFileStr1).getAsJsonObject();
		
		VideoFile videoFile = VideoFileParser.parseVideoFile(jf1, logger);
		assertThat(videoFile).isNull();
	}

	@Test
	void shouldAcceptNullWithError() {
		
		VideoFile videoFile = VideoFileParser.parseVideoFile(null, logger);
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
		
		VideoFile videoFile = VideoFileParser.parseVideoFile(jf1, logger);
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
		
		VideoFile videoFile = VideoFileParser.parseVideoFile(jf1, logger);
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
		
		VideoFile videoFile = VideoFileParser.parseVideoFile(jf1, logger);
		assertThat(videoFile).isNotNull();
		assertThat(videoFile.getHeight()).isEqualTo(480);
		assertThat(videoFile.getWidth()).isEqualTo(720);
		assertThat(videoFile.getSource()).isEqualTo("DVD");
		assertThat(videoFile.getType()).isEqualTo(VideoFileType.MKV);
		assertThat(videoFile.getNote()).isEqualTo("version noir et blanc");
	}
}
