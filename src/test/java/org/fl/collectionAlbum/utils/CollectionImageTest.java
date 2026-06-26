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

package org.fl.collectionAlbum.utils;

import static org.assertj.core.api.Assertions.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.fl.util.FilterCounter;
import org.fl.util.FilterCounter.LogRecordCounter;
import org.fl.util.file.FilesUtils;
import org.junit.jupiter.api.Test;

class CollectionImageTest {

	@Test
	void testNullPathImageIcon() {
		
		LogRecordCounter filterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(CollectionImage.class.getName()));	
		
		int width = 100;
		int height = 100;
		CollectionImage collectionImage = new CollectionImage(null);
		assertThat(collectionImage).isNotNull();
		assertThat(collectionImage.getImageStatus()).isEqualTo(CollectionImage.ImageStatus.NOT_FOUND);
		assertThat(collectionImage.getImagePath()).isNull();
		
		ImageIcon imageIcon = collectionImage.buildAdjustedImageIcon(width, height);
		assertThat(imageIcon).isNotNull();
		assertThat(imageIcon.getIconWidth()).isEqualTo(width);
		assertThat(imageIcon.getIconHeight()).isEqualTo(height);
		assertThat(imageIcon.getDescription()).isEqualTo("Image non trouvée");
		
		assertThat(filterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(filterCounter.getLogRecordCount(Level.WARNING)).isEqualTo(1);
		filterCounter.stopLogCountAndFilter();
	}
	
	@Test
	void testUnexistantPathImageIcon() {
		
		LogRecordCounter filterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(CollectionImage.class.getName()));	
		
		int width = 100;
		int height = 100;
		CollectionImage collectionImage = new CollectionImage(Path.of("dummyPath"));
		assertThat(collectionImage).isNotNull();
		assertThat(collectionImage.getImageStatus()).isEqualTo(CollectionImage.ImageStatus.IN_ERROR);
		
		ImageIcon imageIcon = collectionImage.buildAdjustedImageIcon(width, height);
		assertThat(imageIcon).isNotNull();
		assertThat(imageIcon.getIconWidth()).isEqualTo(width);
		assertThat(imageIcon.getIconHeight()).isEqualTo(height);
		assertThat(imageIcon.getDescription()).isEqualTo("Image en erreur");
		
		assertThat(filterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(filterCounter.getLogRecordCount(Level.WARNING)).isEqualTo(1);
		filterCounter.stopLogCountAndFilter();
	}
	
	@Test
	void testWrongImageFormat() throws URISyntaxException {
		
		Path imgFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/img_bad.jpg");		
		assertThat(imgFilePath).exists();
		
		LogRecordCounter filterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(CollectionImage.class.getName()));	
		
		int width = 100;
		int height = 100;
		CollectionImage collectionImage = new CollectionImage(imgFilePath);
		assertThat(collectionImage).isNotNull();
		assertThat(collectionImage.getImageStatus()).isEqualTo(CollectionImage.ImageStatus.IN_ERROR);
		assertThat(collectionImage.getImagePath().toString()).isEqualTo("C:\\ForTests\\CollectionMusique\\img_bad.jpg");
		
		ImageIcon imageIcon = collectionImage.buildAdjustedImageIcon(width, height);
		assertThat(imageIcon).isNotNull();
		assertThat(imageIcon.getIconWidth()).isEqualTo(width);
		assertThat(imageIcon.getIconHeight()).isEqualTo(height);
		assertThat(imageIcon.getDescription()).isEqualTo("Image en erreur");
		
		assertThat(filterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(filterCounter.getLogRecordCount(Level.WARNING)).isEqualTo(1);
		filterCounter.stopLogCountAndFilter();
	}
	
	@Test
	void testGoodImageFormat() throws URISyntaxException {
		
		Path imgFilePath = FilesUtils.uriStringToAbsolutePath("file:///ForTests/CollectionMusique/cover.jpg");		
		assertThat(imgFilePath).exists();
		
		LogRecordCounter filterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(CollectionImage.class.getName()));	
		
		int width = 100;
		int height = 100;
		CollectionImage collectionImage = new CollectionImage(imgFilePath);
		assertThat(collectionImage).isNotNull();
		assertThat(collectionImage.getImageStatus()).isEqualTo(CollectionImage.ImageStatus.OK);
		assertThat(collectionImage.getImagePath().toString()).isEqualTo("C:\\ForTests\\CollectionMusique\\cover.jpg");
		
		ImageIcon imageIcon = collectionImage.buildAdjustedImageIcon(width, height);
		assertThat(imageIcon).isNotNull();
		assertThat(imageIcon.getIconWidth()).isEqualTo(width);
		assertThat(imageIcon.getIconHeight()).isEqualTo(height);
		assertThat(imageIcon.getDescription()).isNull();
		
		assertThat(filterCounter.getLogRecordCount()).isZero();
		filterCounter.stopLogCountAndFilter();
	}
}

