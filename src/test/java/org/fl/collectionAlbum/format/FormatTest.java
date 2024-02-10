/*
 MIT License

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

import java.util.List;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class FormatTest {
	
	@Test
	void test1() {
		
		String formatStr1 = "{}" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1) ;

		assertThat(format1.getPoids()).isZero();

	}

	@Test
	void test2() {
		
		String formatStr1 = "{\"cd\": 3 }" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1) ;

		assertThat(format1.getPoids()).isEqualTo(3);
		assertThat(format1.hasMediaFiles(ContentNature.AUDIO)).isFalse();
		assertThat(format1.hasMediaFiles(ContentNature.VIDEO)).isFalse();
	}
	
	@Test
	void test3() {
		
		String formatStr1 = "{\"cd\": 2 , \"45t\" : 1 }" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1) ;
		
		assertThat(format1.getPoids()).isEqualTo(2.5);
		assertThat(format1.getMediaFiles(ContentNature.AUDIO)).isEmpty();
		assertThat(format1.getMediaFiles(ContentNature.VIDEO)).isEmpty();
		assertThat(format1.getAllMediaFiles()).isEmpty();
		assertThat(format1.hasMediaFiles(ContentNature.AUDIO)).isFalse();
		assertThat(format1.hasMediaFiles(ContentNature.VIDEO)).isFalse();
		assertThat(format1.hasContentNature(ContentNature.AUDIO)).isTrue();
		assertThat(format1.hasContentNature(ContentNature.VIDEO)).isFalse();
	}
	
	@Test
	void test3b() {
		
		String formatStr1 = "{\"cd\": 2 , \"dvd\" : 1 }" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1) ;
		
		assertThat(format1.getPoids()).isEqualTo(3);
		assertThat(format1.getMediaFiles(ContentNature.AUDIO)).isEmpty();
		assertThat(format1.getMediaFiles(ContentNature.VIDEO)).isEmpty();
		assertThat(format1.getAllMediaFiles()).isEmpty();
		assertThat(format1.hasMediaFiles(ContentNature.AUDIO)).isFalse();
		assertThat(format1.hasMediaFiles(ContentNature.VIDEO)).isFalse();
		assertThat(format1.hasContentNature(ContentNature.AUDIO)).isTrue();
		assertThat(format1.hasContentNature(ContentNature.VIDEO)).isTrue();
	}
	
	@Test
	void test4() {
		
		String formatStr1 = "{\"33t\": 2 , \"45t\" : 1 }" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1) ;
		
		String formatStr2 = "{\"cd\": 2 , \"45t\" : 1 }" ;
		JsonObject jf2 = JsonParser.parseString(formatStr2).getAsJsonObject();
		Format format2 = new Format(jf2) ;
		
		Format format3 = new Format(null) ;
		assertThat(format3.getPoids()).isZero();
		
		format3.incrementFormat(format1);
		assertThat(format1.getPoids()).isEqualTo(format3.getPoids());
		
		format3.incrementFormat(format2);
		assertThat(format3.getPoids()).isEqualTo(format1.getPoids() + format2.getPoids());
		
		assertThat(format3.hasError()).isTrue();
		assertThat(format3.hasMediaFiles(ContentNature.AUDIO)).isFalse();
		assertThat(format3.getMediaFiles(ContentNature.AUDIO)).isNull();
		assertThat(format3.hasMediaFiles(ContentNature.VIDEO)).isFalse();
		assertThat(format3.getMediaFiles(ContentNature.VIDEO)).isNull();
		assertThat(format3.getAllMediaFiles()).isEmpty();
		assertThat(format3.hasContentNature(ContentNature.AUDIO)).isTrue();
		assertThat(format3.hasContentNature(ContentNature.VIDEO)).isFalse();
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
				    "type" : "FLAC" }]
				}
				""";
		
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1) ;
		
		assertThat(format1.getPoids()).isEqualTo(2.5);
		assertThat(format1.hasMediaFiles(ContentNature.AUDIO)).isTrue();
		assertThat(format1.hasMediaFiles(ContentNature.VIDEO)).isFalse();
		assertThat(format1.getMediaFiles(ContentNature.VIDEO)).isEmpty();
		
		assertThat(format1.getMediaFiles(ContentNature.AUDIO)).singleElement()
			.satisfies(audio -> {
				assertThat(audio).isNotNull().isInstanceOf(LosslessAudioFile.class);
				
				LosslessAudioFile lossLessAudio = (LosslessAudioFile)audio;
				assertThat(lossLessAudio.getBitDepth()).isEqualTo(32);
				assertThat(lossLessAudio.getSamplingRate()).isEqualTo(192);
				assertThat(lossLessAudio.getType().name()).isEqualTo("FLAC");
				assertThat(lossLessAudio.getSource()).isEqualTo("MOFI Fidelity Sound Lab");
			});
		
		assertThat(format1.getAllMediaFiles()).hasSameElementsAs(format1.getMediaFiles(ContentNature.AUDIO));
	}
	
	@Test
	void test6() {
		
		String formatStr1 = """
				{"cd": 2 , 
				"45t" : 1,
				"audioFiles" : [{
				    "bitDepth": 16 , 
				    "samplingRate" : 44.1, 
				    "source" : "MOFI Fidelity Sound Lab", 
				    "type" : "FLAC" }]
				}
				""";
		
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1) ;
		
		assertThat(format1.getPoids()).isEqualTo(2.5);
		assertThat(format1.hasMediaFiles(ContentNature.AUDIO)).isTrue();
		assertThat(format1.hasMediaFiles(ContentNature.VIDEO)).isFalse();
		assertThat(format1.getMediaFiles(ContentNature.VIDEO)).isEmpty();

		assertThat(format1.getMediaFiles(ContentNature.AUDIO)).singleElement()
			.satisfies(audio -> {
				assertThat(audio).isNotNull().isInstanceOf(LosslessAudioFile.class);
				
				LosslessAudioFile lossLessAudio = (LosslessAudioFile)audio;
				assertThat(lossLessAudio.getBitDepth()).isEqualTo(16);
				assertThat(lossLessAudio.getSamplingRate()).isEqualTo(44.1);
				assertThat(lossLessAudio.getType().name()).isEqualTo("FLAC");
				assertThat(lossLessAudio.getSource()).isEqualTo("MOFI Fidelity Sound Lab");
			});
		
		assertThat(format1.getAllMediaFiles()).hasSameElementsAs(format1.getMediaFiles(ContentNature.AUDIO));
	}
	
	@Test
	void test7() {
		
		String formatStr1 = """
				{"cd": 2 , 
				"45t" : 1,
				"audioFiles" : [{
				    "bitRate": 320 , 
				    "samplingRate" : 44.1, 
				    "source" : "MOFI Fidelity Sound Lab", 
				    "type" : "MP3",
				    "note" : "Mix Bob Smith" },
				    {
				    "bitDepth": 24 , 
				    "samplingRate" : 88, 
				    "source" : "CD", 
				    "type" : "FLAC" }]
				}
				""";
		
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1) ;
		
		assertThat(format1.getPoids()).isEqualTo(2.5);
		assertThat(format1.hasMediaFiles(ContentNature.AUDIO)).isTrue();
		assertThat(format1.hasMediaFiles(ContentNature.VIDEO)).isFalse();
		assertThat(format1.getMediaFiles(ContentNature.VIDEO)).isEmpty();

		assertThat(format1.getMediaFiles(ContentNature.AUDIO)).hasSize(2)
			.anySatisfy(audio -> {
				assertThat(audio).isNotNull().isInstanceOf(LossyAudioFile.class);
				
				LossyAudioFile lossyAudio = (LossyAudioFile)audio;
				assertThat(lossyAudio.getBitRate()).isEqualTo(320);
				assertThat(lossyAudio.getSamplingRate()).isEqualTo(44.1);
				assertThat(lossyAudio.getType().name()).isEqualTo("MP3");
				assertThat(lossyAudio.getSource()).isEqualTo("MOFI Fidelity Sound Lab");
				assertThat(lossyAudio.getNote()).isEqualTo("Mix Bob Smith");
			})
			.anySatisfy(audio -> {
				assertThat(audio).isNotNull().isInstanceOf(LosslessAudioFile.class);
				
				LosslessAudioFile lossLessAudio = (LosslessAudioFile)audio;
				assertThat(lossLessAudio.getBitDepth()).isEqualTo(24);
				assertThat(lossLessAudio.getSamplingRate()).isEqualTo(88);
				assertThat(lossLessAudio.getType().name()).isEqualTo("FLAC");
				assertThat(lossLessAudio.getSource()).isEqualTo("CD");
				assertThat(lossLessAudio.getNote()).isNull();
			});
		
		List<String> csvParts = format1.printAudioFilesCsvParts(";", (af) -> true);
		
		assertThat(csvParts).isNotEmpty().hasSize(2)
			.satisfiesExactly(
					csvAudio -> assertThat(csvAudio).isEqualTo("320.0 kbit/s;44.1 KHz;MP3;MOFI Fidelity Sound Lab;Mix Bob Smith"),
					csvAudio -> assertThat(csvAudio).isEqualTo("24 bits;88.0 KHz;FLAC;CD"));

		assertThat(format1.getAllMediaFiles()).hasSameElementsAs(format1.getMediaFiles(ContentNature.AUDIO));
	}
	
	@Test
	void test8() {
		
		String formatStr1 = """
				{"cd": 2 , 
				"45t" : 1,
				"audioFiles" : [{
				    "bitDepth": 16 , 
				    "samplingRate" : 44.1, 
				    "source" : "MOFI Fidelity Sound Lab", 
				    "type" : "FLAC" }],
				"videoFiles" : [{
					"width": 720, 
					"height" : 480,
					"type" : "MKV", 
					"source" : "DVD",
					"note" : "version noir et blanc" }]
				}
				""";
		
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1) ;
		
		assertThat(format1.getPoids()).isEqualTo(2.5);
		assertThat(format1.hasMediaFiles(ContentNature.AUDIO)).isTrue();
		assertThat(format1.hasMediaFiles(ContentNature.VIDEO)).isTrue();

		assertThat(format1.getMediaFiles(ContentNature.AUDIO)).singleElement()
			.satisfies(audio -> {
				assertThat(audio).isNotNull().isInstanceOf(LosslessAudioFile.class);
				
				LosslessAudioFile lossLessAudio = (LosslessAudioFile)audio;
				assertThat(lossLessAudio.getBitDepth()).isEqualTo(16);
				assertThat(lossLessAudio.getSamplingRate()).isEqualTo(44.1);
				assertThat(lossLessAudio.getType().name()).isEqualTo("FLAC");
				assertThat(lossLessAudio.getSource()).isEqualTo("MOFI Fidelity Sound Lab");
			});
		
		assertThat(format1.getMediaFiles(ContentNature.VIDEO)).singleElement()
			.asInstanceOf(InstanceOfAssertFactories.type(VideoFile.class))
			.satisfies(videoFile -> {
				assertThat(videoFile.getHeight()).isEqualTo(480);
				assertThat(videoFile.getWidth()).isEqualTo(720);
				assertThat(videoFile.getSource()).isEqualTo("DVD");
				assertThat(videoFile.getType()).isEqualTo(VideoFileType.MKV);
				assertThat(videoFile.getNote()).isEqualTo("version noir et blanc");
			});
		
		assertThat(format1.getAllMediaFiles())
			.containsAll(format1.getMediaFiles(ContentNature.VIDEO))
			.containsAll(format1.getMediaFiles(ContentNature.AUDIO));
	}
	
	@Test
	void test9() {
		
		String formatStr1 = """
				{"dvd": 2, 
				"videoFiles" : [{
					"width": 720, 
					"height" : 480,
					"type" : "MP4", 
					"source" : "DVD",
					"note" : "version noir et blanc" },
					{
					"width": 1920, 
					"height" : 1024,
					"type" : "MKV", 
					"source" : "Bluray",
					"note" : "version couleur" }
					]
				}
				""";
		
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1) ;

		assertThat(format1.hasMediaFiles(ContentNature.AUDIO)).isFalse();
		assertThat(format1.hasMediaFiles(ContentNature.VIDEO)).isTrue();
		
		assertThat(format1.getMediaFiles(ContentNature.VIDEO)).hasSize(2)
			.asInstanceOf(InstanceOfAssertFactories.list(VideoFile.class))
			.anySatisfy(videoFile -> {
				assertThat(videoFile.getHeight()).isEqualTo(1024);
				assertThat(videoFile.getWidth()).isEqualTo(1920);
				assertThat(videoFile.getSource()).isEqualTo("Bluray");
				assertThat(videoFile.getType()).isEqualTo(VideoFileType.MKV);
				assertThat(videoFile.getNote()).isEqualTo("version couleur");
			})
			.anySatisfy(videoFile -> {
				assertThat(videoFile.getHeight()).isEqualTo(480);
				assertThat(videoFile.getWidth()).isEqualTo(720);
				assertThat(videoFile.getSource()).isEqualTo("DVD");
				assertThat(videoFile.getType()).isEqualTo(VideoFileType.MP4);
				assertThat(videoFile.getNote()).isEqualTo("version noir et blanc");
			});
		
		assertThat(format1.getAllMediaFiles()).hasSameElementsAs(format1.getMediaFiles(ContentNature.VIDEO));
		
		assertThat(format1.hasContentNature(ContentNature.VIDEO)).isTrue();
		assertThat(format1.hasContentNature(ContentNature.AUDIO)).isFalse();
	}
	
	@Test
	void test10() {
		
		String formatStr1 = "{\"bluray\": 3 }" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1) ;

		assertThat(format1.getPoids()).isZero();
		assertThat(format1.hasMediaFiles(ContentNature.AUDIO)).isFalse();
		assertThat(format1.hasMediaFiles(ContentNature.VIDEO)).isFalse();
	}
	
	@Test
	void test11() {
		
		String formatStr1 = "{\"blueray\": 3 }" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1) ;

		assertThat(format1.getPoids()).isEqualTo(3);
		assertThat(format1.hasMediaFiles(ContentNature.AUDIO)).isFalse();
		assertThat(format1.hasMediaFiles(ContentNature.VIDEO)).isFalse();
		assertThat(format1.hasContentNature(ContentNature.AUDIO)).isFalse();
		assertThat(format1.hasContentNature(ContentNature.VIDEO)).isTrue();
	}
	
	@Test
	void test12() {
		
		String formatStr1 = "{\"bluerayAudio\": 3 }" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1) ;

		assertThat(format1.getPoids()).isEqualTo(3);
		assertThat(format1.hasMediaFiles(ContentNature.AUDIO)).isFalse();
		assertThat(format1.hasMediaFiles(ContentNature.VIDEO)).isFalse();
		assertThat(format1.hasContentNature(ContentNature.AUDIO)).isTrue();
		assertThat(format1.hasContentNature(ContentNature.VIDEO)).isFalse();
	}
}
