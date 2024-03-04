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

import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.json.AudioFileParser;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;
import org.fl.util.FilterCounter;
import org.fl.util.FilterCounter.LogRecordCounter;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class AudioFileTest {
	
	@Test
	void shouldHaveAllValues() {
		
		LogRecordCounter audioFileParserFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.AudioFileParser"));
		LogRecordCounter parserHelpersFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.ParserHelpers"));
		
		String audioFileStr1 = "{}" ;
		JsonObject jf1 = JsonParser.parseString(audioFileStr1).getAsJsonObject();
		
		AudioFileParser audioFileParser = new AudioFileParser();
		AbstractAudioFile audio = audioFileParser.parseMediaFile(jf1);
		assertThat(audio).isNull();
		
		assertThat(audioFileParserFilterCounter.getLogRecordCount()).isEqualTo(2);
		assertThat(audioFileParserFilterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(2);
		
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
		
		LogRecordCounter filterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.AudioFileParser"));
		
		AudioFileParser audioFileParser = new AudioFileParser();
		AbstractAudioFile audio = audioFileParser.parseMediaFile(null);
		assertThat(audio).isNull();
		
		assertThat(filterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(filterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
	}
	
	@Test
	void shouldNotDeserializeToAudioFile() {
		
		LogRecordCounter audioFileParserFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.AudioFileParser"));
		LogRecordCounter parserHelpersFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.ParserHelpers"));
		
		String audioFileStr1 = """
			{"bitDepth": 24 , 
			 "samplingRate" : 96, 
			 "source" : "SACD" }
				""" ;
		JsonObject jf1 = JsonParser.parseString(audioFileStr1).getAsJsonObject();
		
		AudioFileParser audioFileParser = new AudioFileParser();
		AbstractAudioFile audio = audioFileParser.parseMediaFile(jf1);
		assertThat(audio).isNull();

		assertThat(audioFileParserFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(audioFileParserFilterCounter.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
		
		if (parserHelpersFilterCounter.isLoggable(Level.INFO)) {
			assertThat(parserHelpersFilterCounter.getLogRecordCount()).isEqualTo(1);
			assertThat(parserHelpersFilterCounter.getLogRecordCount(Level.INFO)).isEqualTo(1);
		} else {
			assertThat(parserHelpersFilterCounter.getLogRecordCount()).isEqualTo(0);
		}
	}
	
	@Test
	void shouldDeserializeToAudioFile2() {
		
		LogRecordCounter parserHelpersFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.ParserHelpers"));
		
		String audioFileStr1 = """
				{"bitDepth": 32 , 
				 "samplingRate" : 192, 
				 "source" : "MOFI Fidelity Sound Lab", 
				 "type" : "FLAC",
				 "note" : "Remaster Ocean view" }
				""" ;
		JsonObject jf1 = JsonParser.parseString(audioFileStr1).getAsJsonObject();
		
		AudioFileParser audioFileParser = new AudioFileParser();
		AbstractAudioFile audio = audioFileParser.parseMediaFile(jf1);
		assertThat(audio).isInstanceOf(LosslessAudioFile.class);

		LosslessAudioFile losslessAudio = (LosslessAudioFile)audio;
		
		assertThat(losslessAudio.getBitDepth()).isEqualTo(32);
		assertThat(losslessAudio.getSamplingRate()).isEqualTo(192);
		assertThat(losslessAudio.getType().name()).isEqualTo("FLAC");
		assertThat(losslessAudio.getSource()).isEqualTo("MOFI Fidelity Sound Lab");
		assertThat(losslessAudio.getNote()).isEqualTo("Remaster Ocean view");
		
		assertThat(losslessAudio.isHighRes()).isTrue();
		assertThat(losslessAudio.isLossLess()).isTrue();
		assertThat(losslessAudio.hasMissingOrInvalidMediaFilePath()).isTrue();
		assertThat(losslessAudio.hasMediaFilePathNotFound()).isTrue();
		
		assertThat(losslessAudio.getMediaFilePaths()).isNull();
		
		losslessAudio.addMediaFilePath(new MediaFilePath(Paths.get("E:/Musique/a/John Abercrombie/M [24-96]/"), ContentNature.AUDIO, true));
		
		assertThat(losslessAudio.hasMissingOrInvalidMediaFilePath()).isFalse();
		assertThat(losslessAudio.hasMediaFilePathNotFound()).isFalse();
		
		assertThat(losslessAudio.getMediaFilePaths())
			.isNotNull()
			.singleElement()
			.satisfies(audioPath -> assertThat(audioPath.getPath()).hasToString("E:\\Musique\\a\\John Abercrombie\\M [24-96]"));
		
		assertThat(losslessAudio.displayMediaFileDetailTitles(";")).isEqualTo("Bit depth;Sampling Rate;Type;Source;Note");
		
		if (parserHelpersFilterCounter.isLoggable(Level.INFO)) {
			assertThat(parserHelpersFilterCounter.getLogRecordCount()).isEqualTo(1);
			assertThat(parserHelpersFilterCounter.getLogRecordCount(Level.INFO)).isEqualTo(1);
		}
	}
	
	@Test
	void shouldDeserializeToAudioFileWithInvalidPath() {
		
		LogRecordCounter parserHelpersFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.ParserHelpers"));
		
		String audioFileStr1 = """
				{"bitDepth": 32 , 
				 "samplingRate" : 192, 
				 "source" : "MOFI Fidelity Sound Lab", 
				 "type" : "FLAC",
				 "note" : "Remaster Ocean view",
				 "location" : "invalid path" }
				""" ;
		JsonObject jf1 = JsonParser.parseString(audioFileStr1).getAsJsonObject();
		
		AudioFileParser audioFileParser = new AudioFileParser();
		AbstractAudioFile audio = audioFileParser.parseMediaFile(jf1);
		assertThat(audio).isInstanceOf(LosslessAudioFile.class);

		LosslessAudioFile losslessAudio = (LosslessAudioFile)audio;
		
		assertThat(losslessAudio.getBitDepth()).isEqualTo(32);
		assertThat(losslessAudio.getSamplingRate()).isEqualTo(192);
		assertThat(losslessAudio.getType().name()).isEqualTo("FLAC");
		assertThat(losslessAudio.getSource()).isEqualTo("MOFI Fidelity Sound Lab");
		assertThat(losslessAudio.getNote()).isEqualTo("Remaster Ocean view");
		
		assertThat(losslessAudio.isHighRes()).isTrue();
		assertThat(losslessAudio.isLossLess()).isTrue();
		assertThat(losslessAudio.hasMissingOrInvalidMediaFilePath()).isTrue();
		assertThat(losslessAudio.hasMediaFilePathNotFound()).isTrue();
		
		assertThat(losslessAudio.getMediaFilePaths()).isNull();
		
		assertThat(parserHelpersFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(parserHelpersFilterCounter.getLogRecordCount(Level.WARNING)).isEqualTo(1);
	}
	
	@Test
	void shouldDeserializeToAudioFileWithPathNotFound() {
		
		LogRecordCounter filterCounter1 = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.json.AbstractMediaFileParser"));
		LogRecordCounter filterCounter2 = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.mediaPath.MediaFileInventory"));
		
		String audioFileStr1 = """
				{"bitDepth": 32 , 
				 "samplingRate" : 192, 
				 "source" : "MOFI Fidelity Sound Lab", 
				 "type" : "FLAC",
				 "note" : "Remaster Ocean view",
				 "location" : ["E:/Musique/a/John Abercrombie/notFound/"] }
				""" ;
		JsonObject jf1 = JsonParser.parseString(audioFileStr1).getAsJsonObject();
		
		AudioFileParser audioFileParser = new AudioFileParser();
		AbstractAudioFile audio = audioFileParser.parseMediaFile(jf1);
		assertThat(audio).isInstanceOf(LosslessAudioFile.class);

		LosslessAudioFile losslessAudio = (LosslessAudioFile)audio;
		
		assertThat(losslessAudio.getBitDepth()).isEqualTo(32);
		assertThat(losslessAudio.getSamplingRate()).isEqualTo(192);
		assertThat(losslessAudio.getType().name()).isEqualTo("FLAC");
		assertThat(losslessAudio.getSource()).isEqualTo("MOFI Fidelity Sound Lab");
		assertThat(losslessAudio.getNote()).isEqualTo("Remaster Ocean view");
		
		assertThat(losslessAudio.isHighRes()).isTrue();
		assertThat(losslessAudio.isLossLess()).isTrue();
		assertThat(losslessAudio.hasMissingOrInvalidMediaFilePath()).isTrue();
		assertThat(losslessAudio.hasMediaFilePathNotFound()).isTrue();
		
		assertThat(losslessAudio.getMediaFilePaths())
			.isNotNull()
			.isEmpty();
		
		assertThat(filterCounter1.getLogRecordCount()).isEqualTo(1);
		assertThat(filterCounter1.getLogRecordCount(Level.WARNING)).isEqualTo(1);
		assertThat(filterCounter2.getLogRecordCount()).isEqualTo(1);
		assertThat(filterCounter2.getLogRecordCount(Level.SEVERE)).isEqualTo(1);
	}
}
