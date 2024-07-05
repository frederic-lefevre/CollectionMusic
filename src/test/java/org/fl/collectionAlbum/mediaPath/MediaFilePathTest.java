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

package org.fl.collectionAlbum.mediaPath;

import static org.assertj.core.api.Assertions.*;

import java.nio.file.Paths;

import org.fl.collectionAlbum.format.ContentNature;
import org.junit.jupiter.api.Test;

class MediaFilePathTest {

	@Test
	void shouldSelectMediaFileExtension() {
		
		assertThat(MediaFilePath.isMediaFileName(Paths.get("toto.flac"), ContentNature.AUDIO)).isTrue();
		assertThat(MediaFilePath.isMediaFileName(Paths.get("toto.mp3"), ContentNature.AUDIO)).isTrue();
		assertThat(MediaFilePath.isMediaFileName(Paths.get("toto.wma"), ContentNature.AUDIO)).isTrue();
		assertThat(MediaFilePath.isMediaFileName(Paths.get("toto.txt"), ContentNature.AUDIO)).isFalse();
		assertThat(MediaFilePath.isMediaFileName(Paths.get("toto.jpg"), ContentNature.AUDIO)).isFalse();
		assertThat(MediaFilePath.isMediaFileName(Paths.get("toto"), ContentNature.AUDIO)).isFalse();
		assertThat(MediaFilePath.isMediaFileName(Paths.get(""), ContentNature.AUDIO)).isFalse();
		assertThat(MediaFilePath.isMediaFileName(null, ContentNature.AUDIO)).isFalse();
		assertThat(MediaFilePath.isMediaFileName(Paths.get("toto.mkv"), ContentNature.AUDIO)).isFalse();
		
		assertThat(MediaFilePath.isMediaFileName(Paths.get("toto.flac"), ContentNature.VIDEO)).isFalse();
		assertThat(MediaFilePath.isMediaFileName(Paths.get("toto.mkv"), ContentNature.VIDEO)).isTrue();
		
		//To print all encountered extension
		//MediaFilePath.extensionSet.forEach(extension -> System.out.println(extension));
	}

}
