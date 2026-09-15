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

package org.fl.collectionAlbum.gui;

import java.net.URI;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.disocgs.DiscogsImageReleaseRequester;
import org.fl.collectionAlbum.utils.CollectionImage;

public class ImageBrowserPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int IMAGE_WIDTH = 85;
	private static final int IMAGE_HEIGHT = 85;
	
	public static class LabelImage {
		
		private final JLabel jLabel;
		private CollectionImage collectionImage;
		
		private LabelImage(JLabel jLabel, CollectionImage collectionImage) {
			this.jLabel = jLabel;
			setCollectionImage(collectionImage);
		}
		
		public JLabel jLabel() {
			return jLabel;
		}

		public CollectionImage getCollectionImage() {
			return collectionImage;
		}

		public void setCollectionImage(CollectionImage collectionImage) {
			this.collectionImage = collectionImage;
			jLabel.setIcon(collectionImage.buildAdjustedImageIcon(IMAGE_WIDTH, IMAGE_HEIGHT));
		}
	};
	
	private final JPanel currentImagePanel;
	
	public ImageBrowserPanel(List<URI> imageUriList) {
		super();
		setPreferredSize(Control.getInfoWindowDimension());
		setLayout(new BoxLayout(this,  BoxLayout.Y_AXIS));
		
		currentImagePanel = new JPanel();
		add(currentImagePanel);
		
		JPanel allImagesPanel = new JPanel();
		allImagesPanel.setLayout(new BoxLayout(allImagesPanel, BoxLayout.X_AXIS));
		
		if ((imageUriList != null) && !imageUriList.isEmpty()) {
			imageUriList.forEach(uri -> {
				
				JLabel label = new JLabel();
				LabelImage labelImage = new LabelImage(label, CollectionImage.IMAGE_NOT_LOADED);
				label.setBorder(new EmptyBorder(5, 5, 5, 5));
				allImagesPanel.add(label);
				DiscogsImageReleaseRequester discogsImageReleaseRequester = new DiscogsImageReleaseRequester(uri, labelImage);
				discogsImageReleaseRequester.execute();
			});
		}
		add(new JScrollPane(allImagesPanel));
	}
}
