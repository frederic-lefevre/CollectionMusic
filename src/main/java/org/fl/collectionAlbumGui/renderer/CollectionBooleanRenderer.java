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

import java.awt.Color;
import java.awt.Font;
import java.util.logging.Logger;

import javax.swing.SwingConstants;

import org.fl.util.swing.CustomTableCellRenderer;

public class CollectionBooleanRenderer extends CustomTableCellRenderer {

	private static final long serialVersionUID = 1L;
	
	private static final Logger mLog = Logger.getLogger(CollectionBooleanRenderer.class.getName());
	
	private static final Font font = new Font("Dialog", Font.BOLD, 12);
	
	public CollectionBooleanRenderer() {
		super(font, SwingConstants.CENTER);
	}

	@Override
	public void valueProcessor(Object value) {
		if (value == null) {
			// This may happen when rescanning the album collection
			mLog.fine("Null value in MediaFiles cell. Should be non null Boolean");
			setText("Valeur null");
			setBackground(Color.RED);
		} else if (value instanceof Boolean booleanValue) {
			if (booleanValue) {
				setText("Oui");
				setBackground(Color.ORANGE);
			} else {
				setText("Non");
				setBackground(Color.WHITE);
			}
		} else {
			mLog.severe("Invalid value type in cell. Should be Boolean but is " + value.getClass().getName());
		}
	}

}
