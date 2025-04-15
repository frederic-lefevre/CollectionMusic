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

package org.fl.collectionAlbumGui.renderer;

import java.awt.Component;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;

public class AlbumsRenderer extends JLabel implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	private static final Logger mLog = Logger.getLogger(AlbumsRenderer.class.getName());
	
	private static final String ALBUMS_SEPARATOR = ", ";
	
	public AlbumsRenderer() {
		super();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (value == null) {
			// This may happen when rescanning the album collection
			mLog.fine("Null value in MediaFiles cell. Should be an MediaFilePath");
			setText("Valeur null");
		} else if (value instanceof MediaFilePath mediaFilePath) {
			setText(mediaFilePath.getAlbumSet().stream()
					.map(Album::getTitre)
					.collect(Collectors.joining(ALBUMS_SEPARATOR)));
		} else {
			mLog.severe("Invalid value type in MediaFile cell. Should be MediaFilePath but is " + value.getClass().getName());
		}
		return this;
	}
}
