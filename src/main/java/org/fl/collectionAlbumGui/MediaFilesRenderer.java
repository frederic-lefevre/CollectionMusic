/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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

package org.fl.collectionAlbumGui;

import java.awt.Component;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;

public class MediaFilesRenderer extends MediaFilesPane implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	private static final Logger mLog = Control.getAlbumLog();
	
	public MediaFilesRenderer() {
		super();
	}

	@Override 
	public Component getTableCellRendererComponent(
		      JTable table, Object value, boolean isSelected, boolean hasFocus,
		      int row, int column) {
		if (value == null) {
			mLog.severe("Null value in MediaFiles cell. Should be an Album");
		} else if (value instanceof Album) {
			updateValue((Album)value);
		} else {
			mLog.severe("Invalid value type in MediaFiles cell. Should be Album but is " + value.getClass().getName());
		}
		return this;
	}
}
