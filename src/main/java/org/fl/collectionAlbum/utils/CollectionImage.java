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
	
	private static BufferedImage IMAGE_FOR_ERROR = null;
	private static final String DESCRIPTION_FOR_IMAGE_ERROR = "Image en erreur";
	private static BufferedImage IMAGE_FOR_IMAGE_NOT_FOUND = null;
	private static final String DESCRIPTION_FOR_IMAGE_NOT_FOUND = "Image non trouvée";
	
	public enum ImageStatus {OK, NOT_FOUND, IN_ERROR};
	
	private final Path imagePath;
	private final BufferedImage bufferedImage;
	private final ImageStatus imageStatus;
	
	public CollectionImage(Path imagePath) {
		
		this.imagePath = imagePath;
		if (imagePath == null) {
			logger.warning( "Null image path");
			imageStatus = ImageStatus.NOT_FOUND;
			bufferedImage = getImageForImageNotFound();
		} else {
			BufferedImage image;
			ImageStatus status;
			try {
				image = ImageIO.read(imagePath.toFile());
			
				if (image != null) {
					status = ImageStatus.OK;
				} else {
					logger.warning("Image format problem: No image reader found for this image " + Objects.toString(imagePath));
					image = getImageForError();
					status = ImageStatus.IN_ERROR;
				}
			} catch (Exception e) {
				logger.log(Level.WARNING, "Exception when creating BufferedImage from file " + Objects.toString(imagePath), e);
				image = getImageForError();
				status = ImageStatus.IN_ERROR;
			}
			bufferedImage = image;
			imageStatus = status;
		}
	}

	
	public Path getImagePath() {
		return imagePath;
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
			case OK -> new ImageIcon(scaleImage(maxWidth, maxHeight));
			case NOT_FOUND -> new ImageIcon(scaleImage(maxWidth, maxHeight), DESCRIPTION_FOR_IMAGE_NOT_FOUND);
			case IN_ERROR -> new ImageIcon(scaleImage(maxWidth, maxHeight), DESCRIPTION_FOR_IMAGE_ERROR);
			};
		} else {
			String message = "Unexpected null image for path " + Objects.toString(imagePath);
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
	
	private Image scaleImage(int maxWidth, int maxHeight) {
		
		final int imageWidth = bufferedImage.getWidth();
		final int imageHeight = bufferedImage.getHeight();
		int adjustedImageWidth;
		int adjustedImageHeight;
		if (imageWidth > imageHeight) {
			adjustedImageWidth = maxWidth;
			adjustedImageHeight = (imageHeight * maxWidth)/imageWidth;
		} else {
			adjustedImageHeight = maxHeight;
			adjustedImageWidth = (imageWidth* maxHeight)/imageHeight;
		}
		return bufferedImage.getScaledInstance(adjustedImageWidth, adjustedImageHeight, Image.SCALE_DEFAULT);
	}
	
	private static BufferedImage getImageForError() {		
		if (IMAGE_FOR_ERROR == null) {
			try {
				IMAGE_FOR_ERROR = ImageIO.read(Control.getImageForErrorPath().toFile());				
			} catch (Exception e) {
				logger.log(Level.WARNING, "Exception when creating BufferedImage for error " + Objects.toString(Control.getImageForErrorPath()), e);
				IMAGE_FOR_ERROR = new BufferedImage(400, 400, BufferedImage.TYPE_BYTE_GRAY);
			}
		}
		return IMAGE_FOR_ERROR;
	}
	
	private static BufferedImage getImageForImageNotFound() {	
		if (IMAGE_FOR_IMAGE_NOT_FOUND == null) {
			try {
				IMAGE_FOR_IMAGE_NOT_FOUND = ImageIO.read(Control.getImageForImageNotFoundPath().toFile());				
			} catch (Exception e) {
				logger.log(Level.WARNING, "Exception when creating BufferedImage for error " + Objects.toString(Control.getImageForErrorPath()), e);
				IMAGE_FOR_IMAGE_NOT_FOUND = new BufferedImage(400, 400, BufferedImage.TYPE_BYTE_GRAY);
			}
		}
		return IMAGE_FOR_IMAGE_NOT_FOUND;
	}
}
