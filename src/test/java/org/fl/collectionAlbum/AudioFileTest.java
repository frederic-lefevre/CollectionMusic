/*
 MIT License

Copyright (c) 2017, 2022 Frederic Lefevre

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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class AudioFileTest {

	private final static Logger logger = Logger.getLogger(AudioFileTest.class.getName());
	
	@Test
	void shouldHaveDefaultValues() {
		
		String audioFileStr1 = "{}" ;
		JsonObject jf1 = JsonParser.parseString(audioFileStr1).getAsJsonObject();
		
		AudioFile audio = new AudioFile(jf1, logger);
		assertThat(audio.getBitDepth()).isEqualTo(16);
		assertThat(audio.getSamplingRate()).isEqualTo(44.1);
		assertThat(audio.getType()).isEqualTo("FLAC");
		assertThat(audio.getSource()).isEqualTo("CD");
	}
	
	@Test
	void shouldAcceptNullWithError() {
		
		AudioFile audio = new AudioFile(null, logger);
		
		assertThat(audio.getBitDepth()).isZero();
		assertThat(audio.getSamplingRate()).isZero();
		assertThat(audio.getType()).isNull();
		assertThat(audio.getSource()).isNull();
		
	}
	
	@Test
	void shouldDeserializeToAudioFile() {
		
		String audioFileStr1 = """
			{"bitDepth": 24 , 
			 "samplingRate" : 96, 
			 "source" : "SACD" }
				""" ;
		JsonObject jf1 = JsonParser.parseString(audioFileStr1).getAsJsonObject();
		
		AudioFile audio = new AudioFile(jf1, logger);
		assertThat(audio.getBitDepth()).isEqualTo(24);
		assertThat(audio.getSamplingRate()).isEqualTo(96);
		assertThat(audio.getType()).isEqualTo("FLAC");
		assertThat(audio.getSource()).isEqualTo("SACD");
		assertThat(audio.getNote()).isNull();
	}
	
	@Test
	void shouldDeserializeToAudioFile2() {
		
		String audioFileStr1 = """
				{"bitDepth": 32 , 
				 "samplingRate" : 192, 
				 "source" : "MOFI Fidelity Sound Lab", 
				 "type" : "WAV",
				 "note" : "Remaster Ocean view" }
				""" ;
		JsonObject jf1 = JsonParser.parseString(audioFileStr1).getAsJsonObject();
		
		AudioFile audio = new AudioFile(jf1, logger);
		assertThat(audio.getBitDepth()).isEqualTo(32);
		assertThat(audio.getSamplingRate()).isEqualTo(192);
		assertThat(audio.getType()).isEqualTo("WAV");
		assertThat(audio.getSource()).isEqualTo("MOFI Fidelity Sound Lab");
		assertThat(audio.getNote()).isEqualTo("Remaster Ocean view");
	}
}
