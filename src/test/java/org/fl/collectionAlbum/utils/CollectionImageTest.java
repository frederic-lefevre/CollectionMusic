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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.fl.collectionAlbum.disocgs.DiscogsInterface;
import org.fl.discogsInterface.Image;
import org.fl.discogsInterface.Release;
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
		Path nullPath = null;
		CollectionImage collectionImage = new CollectionImage(nullPath);
		assertThat(collectionImage).isNotNull();
		assertThat(collectionImage.getImageStatus()).isEqualTo(CollectionImage.ImageStatus.NOT_FOUND);
		assertThat(collectionImage.getBufferedImage()).isNotNull();
		
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
	void testNullUrl() {
		
		LogRecordCounter filterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(CollectionImage.class.getName()));	
		
		int width = 100;
		int height = 100;
		URL nullUrl = null;
		CollectionImage collectionImage = new CollectionImage(nullUrl);
		assertThat(collectionImage).isNotNull();
		assertThat(collectionImage.getImageStatus()).isEqualTo(CollectionImage.ImageStatus.NOT_FOUND);
		assertThat(collectionImage.getBufferedImage()).isNotNull();
		
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
	void test404Url() throws MalformedURLException, URISyntaxException {
		
		LogRecordCounter filterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(CollectionImage.class.getName()));	
		
		URL nullUrl = new URI("http://www.google.com/404").toURL();
		CollectionImage collectionImage = new CollectionImage(nullUrl);
		assertThat(collectionImage).isNotNull();
		assertThat(collectionImage.getImageStatus()).isEqualTo(CollectionImage.ImageStatus.IN_ERROR);
		assertThat(collectionImage.getBufferedImage()).isNotNull();
		
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
		assertThat(collectionImage.getBufferedImage()).isNotNull();
		
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
		assertThat(collectionImage.getBufferedImage()).isNotNull();
		
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
		assertThat(collectionImage.getBufferedImage()).isNotNull();
		
		ImageIcon imageIcon = collectionImage.buildAdjustedImageIcon(width, height);
		assertThat(imageIcon).isNotNull();
		assertThat(imageIcon.getIconWidth()).isEqualTo(width);
		assertThat(imageIcon.getIconHeight()).isEqualTo(height);
		assertThat(imageIcon.getDescription()).isNull();
		
		assertThat(filterCounter.getLogRecordCount()).isZero();
		filterCounter.stopLogCountAndFilter();
	}
	
	
	@Test
	void testImageFromDiscogs() throws MalformedURLException, URISyntaxException {
		
		LogRecordCounter filterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger(CollectionImage.class.getName()));
		
		String releaseId = "8706129";
		Release release = DiscogsInterface.release(releaseId);
		assertThat(release).isNotNull();
		List<Image> images = release.images();
		assertThat(images).isNotNull().isNotEmpty();
		
		Image primaryImage = images.stream().filter(i -> i.type().equals("primary")).findFirst().orElse(null);
		assertThat(primaryImage).isNotNull();
		
		String imgUriString = primaryImage.uri();
		assertThat(imgUriString).isNotNull().isNotBlank();
		
		URL imageUrl = new URI(imgUriString).toURL();
		CollectionImage collectionImage = new CollectionImage(imageUrl);
		assertThat(collectionImage).isNotNull();
		assertThat(collectionImage.getImageStatus()).isEqualTo(CollectionImage.ImageStatus.OK);
		assertThat(collectionImage.getBufferedImage()).isNotNull();
		
		assertThat(filterCounter.getLogRecordCount()).isZero();
		filterCounter.stopLogCountAndFilter();
	}
}

