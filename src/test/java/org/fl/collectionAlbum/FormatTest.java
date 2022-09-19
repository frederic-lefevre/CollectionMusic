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

class FormatTest {

	private final static Logger logger = Logger.getLogger(FormatTest.class.getName());
	
	@Test
	void test1() {
		
		String formatStr1 = "{}" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1, logger) ;

		assertThat(format1.getPoids()).isZero();

	}

	@Test
	void test2() {
		
		String formatStr1 = "{\"cd\": 3 }" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1, logger) ;

		assertThat(format1.getPoids()).isEqualTo(3);

	}
	
	@Test
	void test3() {
		
		String formatStr1 = "{\"cd\": 2 , \"45t\" : 1 }" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1, logger) ;
		
		assertThat(format1.getPoids()).isEqualTo(2.5);
		assertThat(format1.getAudioFiles()).isNull();
	}
	
	@Test
	void test4() {
		
		String formatStr1 = "{\"33t\": 2 , \"45t\" : 1 }" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1, logger) ;
		
		String formatStr2 = "{\"cd\": 2 , \"45t\" : 1 }" ;
		JsonObject jf2 = JsonParser.parseString(formatStr2).getAsJsonObject();
		Format format2 = new Format(jf2, logger) ;
		
		Format format3 = new Format(null, logger) ;
		assertThat(format3.getPoids()).isZero();
		
		format3.incrementFormat(format1);
		assertThat(format1.getPoids()).isEqualTo(format3.getPoids());
		
		format3.incrementFormat(format2);
		assertThat(format3.getPoids()).isEqualTo(format1.getPoids() + format2.getPoids());
		
		assertThat(format3.hasAudioFiles()).isFalse();
		assertThat(format3.getAudioFiles()).isNull();
	}
	
	@Test
	void test5() {
		
		String formatStr1 = """
				{"cd": 2 , 
				"45t" : 1,
				"audioFiles" : [{
				    "bitDepth": 32 , 
				    "samplingRate" : 192, 
				    "source" : "MOFI Fidelity Sound Lab", 
				    "type" : "WAV" }]
				}
				""";
		
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1, logger) ;
		
		assertThat(format1.getPoids()).isEqualTo(2.5);
		

		assertThat(format1.getAudioFiles()).singleElement()
			.satisfies(audio -> {
				assertThat(audio).isNotNull();
				assertThat(audio.getBitDepth()).isEqualTo(32);
				assertThat(audio.getSamplingRate()).isEqualTo(192);
				assertThat(audio.getType()).isEqualTo("WAV");
				assertThat(audio.getSource()).isEqualTo("MOFI Fidelity Sound Lab");
			});
		
	}
	
	@Test
	void test6() {
		
		String formatStr1 = """
				{"cd": 2 , 
				"45t" : 1,
				"audioFiles" : [{
				    "bitDepth": 32 , 
				    "samplingRate" : 192, 
				    "source" : "MOFI Fidelity Sound Lab", 
				    "type" : "WAV",
				    "note" : "Mix Bob Smith" },
				    {
				    "bitDepth": 24 , 
				    "samplingRate" : 88, 
				    "source" : "CD", 
				    "type" : "FLAC" }]
				}
				""";
		
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1, logger) ;
		
		assertThat(format1.getPoids()).isEqualTo(2.5);
		

		assertThat(format1.getAudioFiles()).hasSize(2)
			.anySatisfy(audio -> {
				assertThat(audio).isNotNull();
				assertThat(audio.getBitDepth()).isEqualTo(32);
				assertThat(audio.getSamplingRate()).isEqualTo(192);
				assertThat(audio.getType()).isEqualTo("WAV");
				assertThat(audio.getSource()).isEqualTo("MOFI Fidelity Sound Lab");
				assertThat(audio.getNote()).isEqualTo("Mix Bob Smith");
			})
			.anySatisfy(audio -> {
				assertThat(audio).isNotNull();
				assertThat(audio.getBitDepth()).isEqualTo(24);
				assertThat(audio.getSamplingRate()).isEqualTo(88);
				assertThat(audio.getType()).isEqualTo("FLAC");
				assertThat(audio.getSource()).isEqualTo("CD");
				assertThat(audio.getNote()).isNull();
			});
		
	}
}
