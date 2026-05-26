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

package org.fl.collectionAlbum.mediaFile.metadata;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AudioStreamMetadataTest {

	@Test
	void testParameterNumber() {
		
		MetadataElement<Integer> m1 = new MetadataElement<>("1", 0);
		MetadataElement<Boolean> m2 = new MetadataElement<>("2", true);
		MetadataElement<Long> m3 = new MetadataElement<>("3", 0L);
		
		AudioStreamMetadata audioStreamMetadata = new AudioStreamMetadata(m2, m3, m1, m3, m1, m3);
		
		assertThat(AudioStreamMetadata.metadataNames.size())
			.isEqualTo(audioStreamMetadata.getMetadataMap().size())
			.isEqualTo(AudioStreamMetadata.class.getRecordComponents().length);
	}
}
