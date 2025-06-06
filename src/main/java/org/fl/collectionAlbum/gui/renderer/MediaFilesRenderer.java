/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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

package org.fl.collectionAlbum.gui.renderer;

import java.awt.Font;
import java.util.logging.Logger;

import javax.swing.SwingConstants;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.utils.CollectionUtils;
import org.fl.util.swing.CustomTableCellRenderer;

public class MediaFilesRenderer extends CustomTableCellRenderer {

	private static final long serialVersionUID = 1L;

	private static final Logger mLog = Logger.getLogger(MediaFilesRenderer.class.getName());
	
	private static final Font font = new Font("Dialog", Font.BOLD, 12);
	
	public MediaFilesRenderer() {
		super(font, SwingConstants.LEFT);
	}

	@Override
	public void valueProcessor(Object value) {
		
		if (value == null) {
			// This may happen when rescanning the album collection
			mLog.fine("Null value in Auteurs cell. Should be an Album");
			setText("Valeur null");
		} else if (value instanceof Album album) {
			setText(CollectionUtils.getHtmlForMediaFiles(album));	
		} else {
			mLog.severe("Invalid value type in MediaFiles cell. Should be Album but is " + value.getClass().getName());
		}
	}
}
