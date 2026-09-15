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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOError;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.fl.collectionAlbum.Control;

public class CollectionImage {

	private static final Logger logger = Logger.getLogger(CollectionImage.class.getName());
	
	private static final String DESCRIPTION_FOR_IMAGE_ERROR = "Image en erreur";
	private static final String DESCRIPTION_FOR_IMAGE_NOT_FOUND = "Image non trouvée";
	private static final String DESCRIPTION_FOR_IMAGE_NOT_LOADED = "Image non chargée";
	private static final String DESCRIPTION_FOR_IMAGE_TOO_MANY_REQUEST = "Trop de demande d'images discogs. Attendre 1 minute.";
	
	public enum ImageStatus {
		OK(null, null), 
		NOT_FOUND(Control.getImageForImageNotFoundPath(), DESCRIPTION_FOR_IMAGE_NOT_FOUND),
		NOT_LOADED(Control.getImageForImageNotLoadedPath(), DESCRIPTION_FOR_IMAGE_NOT_LOADED), 
		IN_ERROR(Control.getImageForErrorPath(), DESCRIPTION_FOR_IMAGE_ERROR), 
		TOO_MANY_REQUEST(Control.getImageForTooManyDiscogsRequestPath(), DESCRIPTION_FOR_IMAGE_TOO_MANY_REQUEST);
		
		private final Path imagePath;
		private final String description;
		private BufferedImage bufferedImage;
		
		private ImageStatus(Path imagePath, String message) {
			this.imagePath = imagePath;
			this.description = message;
			this.bufferedImage = null;
		}
		
		private BufferedImage bufferedImage() {
			if (this.bufferedImage == null) {
				try {
					this.bufferedImage = ImageIO.read(this.imagePath.toFile());				
				} catch (Exception e) {
					logger.log(Level.WARNING, "Exception when creating BufferedImage for error " + this.imagePath, e);
					this.bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_BYTE_GRAY);
				}
			}
			return this.bufferedImage;
		}
		
		private ImageIcon imageIcon(int maxWidth, int maxHeight) {
			return  new ImageIcon(scaleImage(this.bufferedImage, maxWidth, maxHeight), this.description);
		}
	};
	
	public static final CollectionImage IMAGE_NOT_LOADED = new CollectionImage(new ResultImage(ImageStatus.NOT_LOADED.bufferedImage(), ImageStatus.NOT_LOADED));
	public static final CollectionImage IMAGE_IN_ERROR = new CollectionImage(new ResultImage(ImageStatus.IN_ERROR.bufferedImage(), ImageStatus.IN_ERROR));
	public static final CollectionImage IMAGE_NOT_FOUND = new CollectionImage(new ResultImage(ImageStatus.NOT_FOUND.bufferedImage(), ImageStatus.NOT_FOUND));
	public static final CollectionImage IMAGE_TOO_MANY_REQUEST = new CollectionImage(new ResultImage(ImageStatus.TOO_MANY_REQUEST.bufferedImage(), ImageStatus.TOO_MANY_REQUEST));
	
	private final URL imageUrl;
	private final BufferedImage bufferedImage;
	private final ImageStatus imageStatus;
	
	public CollectionImage(Path imagePath) {
		
		if (imagePath == null) {
			logger.warning( "Null image path");
			this.imageUrl = null;
			imageStatus = ImageStatus.NOT_FOUND;
			bufferedImage = ImageStatus.NOT_FOUND.bufferedImage();
		} else {
			
			URL imgUrl;
			try {
				imgUrl = imagePath.toUri().toURL();
			} catch (IOError e) {
				imgUrl = null;
				logger.log(Level.SEVERE, "IOError converting image path to URL " + Objects.toString(imagePath), e);
			} catch (Exception e) {
				imgUrl = null;
				logger.log(Level.SEVERE, "Exception converting image path to URL " + Objects.toString(imagePath), e);
			}
			
			this.imageUrl = imgUrl;;
			ResultImage resultImage = getImage(imageUrl);
			this.bufferedImage = resultImage.bufferedImage();
			this.imageStatus = resultImage.imageStatus();
		}
	}

	public CollectionImage(URL imageUrl) {		
		this.imageUrl = imageUrl;
		ResultImage resultImage = getImage(imageUrl);
		this.bufferedImage = resultImage.bufferedImage();
		this.imageStatus = resultImage.imageStatus();
	}

	// For discogs images, images must not be downloaded directly, but through discogs interface
	public CollectionImage(ResultImage resultImage) {
		
		this.imageUrl = null;
		this.imageStatus = resultImage.imageStatus();
		if (imageStatus == ImageStatus.OK) {
			this.bufferedImage = resultImage.bufferedImage();
		} else {
			this.bufferedImage = imageStatus.bufferedImage();
		}
	}
	
	public record ResultImage(BufferedImage bufferedImage, ImageStatus imageStatus) {};
	
	private ResultImage getImage(URL imageUrl) {
		
		if (imageUrl == null) {
			logger.warning( "Null image url");
			return new ResultImage(ImageStatus.NOT_FOUND.bufferedImage(), ImageStatus.NOT_FOUND);
		} else {

			BufferedImage image;
			ImageStatus status;
			try {
				image = ImageIO.read(imageUrl);
				
				if (image != null) {
					status = ImageStatus.OK;
				} else {
					logger.warning("Image format problem: No image reader found for this image " + Objects.toString(imageUrl));
					image = ImageStatus.IN_ERROR.bufferedImage();
					status = ImageStatus.IN_ERROR;
				}
			} catch (Exception e) {
				logger.log(Level.WARNING, "Exception when creating BufferedImage from URL " + Objects.toString(imageUrl), e);
				image = ImageStatus.IN_ERROR.bufferedImage();
				status = ImageStatus.IN_ERROR;
			}
			return new ResultImage(image, status);
		}
	}
	
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public ImageStatus getImageStatus() {
		return imageStatus;
	}

	public ImageIcon buildAdjustedImageIcon(int maxWidth, int maxHeight) {
		if (bufferedImage != null) {
			return switch (imageStatus) {
			case OK -> new ImageIcon(scaleImage(bufferedImage, maxWidth, maxHeight));
			case NOT_FOUND -> ImageStatus.NOT_FOUND.imageIcon(maxWidth, maxHeight);
			case NOT_LOADED -> ImageStatus.NOT_LOADED.imageIcon(maxWidth, maxHeight);
			case IN_ERROR -> ImageStatus.IN_ERROR.imageIcon(maxWidth, maxHeight);
			case TOO_MANY_REQUEST -> ImageStatus.TOO_MANY_REQUEST.imageIcon(maxWidth, maxHeight);
			};
		} else {
			String message = "Unexpected null image for path " + Objects.toString(imageUrl);
			logger.severe( message);
			throw new RuntimeException(message);
		}
	}
	
	public JLabel getAdjustedImageLabel(int maxWidth, int maxHeight) {

		ImageIcon imageIcon = buildAdjustedImageIcon(maxWidth, maxHeight);
		if (imageIcon != null) {
			return new JLabel(imageIcon);
		} else {
			return new JLabel("Fichier image en erreur");
		}
	}
	
	private static Image scaleImage(BufferedImage bufferedImage, int maxWidth, int maxHeight) {
		
		final int imageWidth = bufferedImage.getWidth();
		final int imageHeight = bufferedImage.getHeight();
		int adjustedImageWidth;
		int adjustedImageHeight;
		if (maxWidth * imageHeight < maxHeight * imageWidth) {
			adjustedImageWidth = maxWidth;
			adjustedImageHeight = (imageHeight * maxWidth) / imageWidth;
		} else {
			adjustedImageWidth = (imageWidth * maxHeight) / imageHeight;
			adjustedImageHeight = maxHeight;
		}
		return bufferedImage.getScaledInstance(adjustedImageWidth, adjustedImageHeight, Image.SCALE_DEFAULT);
	}
}
