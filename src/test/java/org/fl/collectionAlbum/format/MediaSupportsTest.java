/*
 MIT License

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

package org.fl.collectionAlbum.format;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MediaSupportsTest {

	@Test
	void allMediaSupportCategoriesHaveAtLeastOneMediaSupport() {
		
		assertThat(MediaSupportCategories.values()).allSatisfy(cat -> {
			assertThat(MediaSupports.values()).anyMatch(mediaSupport -> mediaSupport.getSupportPhysique().equals(cat));
		});
	}
	
	@Test
	void basicFieldsTest() {
		
		assertThat(MediaSupports.values()).allSatisfy(mediaSupport -> {
			assertThat(mediaSupport.getDescription()).isNotBlank();
			assertThat(mediaSupport.getContentNatures()).isNotEmpty();
			assertThat(mediaSupport.getJsonPropertyName()).isNotBlank();
			assertThat(mediaSupport.getSupportMaterial()).isNotNull();
			assertThat(mediaSupport.getSupportPhysique()).isNotNull();
		});
	}
	
	@Test
	void testOrder() {
		
		MediaSupports[] mediaSupports = MediaSupports.values();
		for (int idx = 1; idx < mediaSupports.length; idx++) {
			
			int categoryIndex0 = mediaSupports[idx-1].getSupportPhysique().ordinal();
			int categoryIndex1 = mediaSupports[idx].getSupportPhysique().ordinal();
			
			assertThat(categoryIndex1 - categoryIndex0).isBetween(0, 1);
		}
	}
}
