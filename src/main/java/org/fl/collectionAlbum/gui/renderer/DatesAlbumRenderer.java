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

package org.fl.collectionAlbum.gui.renderer;

import java.awt.Font;
import java.time.temporal.TemporalAccessor;
import java.util.function.Function;
import java.util.logging.Logger;

import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.utils.CollectionUtils;
import org.fl.util.swing.CustomTableCellRenderer;

public class DatesAlbumRenderer extends CustomTableCellRenderer {

	private static final long serialVersionUID = 1L;

	private static final Logger mLog = Logger.getLogger(DatesAlbumRenderer.class.getName());
	
	private static final Font font = new Font("Dialog", Font.PLAIN, 12);
	private static final AbstractBorder BORDER = new EmptyBorder(0, 10, 0, 0);
	
	private final Function<TemporalAccessor, String> dateFormatter;
	private final Function<Album, TemporalAccessor> beginDateGetter;
	private final Function<Album, TemporalAccessor> endDateGetter;
	
	public DatesAlbumRenderer(Function<Album, TemporalAccessor> beginDateGetter, Function<Album, TemporalAccessor> endDateGetter, Function<TemporalAccessor, String> dateFormatter) {
		super(font, SwingConstants.LEFT);
		this.beginDateGetter = beginDateGetter;
		this.endDateGetter = endDateGetter;
		this.dateFormatter = dateFormatter;
	}

	@Override
	public void valueProcessor(Object value) {
		setBorder(BORDER);
		if (value == null) {
			// This may happen when rescanning the album collection
			mLog.fine("Null value in Dates Album cell. Should be an Album");
			setText("Valeur null");
		} else if (value instanceof Album album) {
			if ((beginDateGetter != null) && (endDateGetter != null)) {
				setText(CollectionUtils.getHtmlForInterval(
						dateFormatter.compose(beginDateGetter).apply(album), 
						dateFormatter.compose(endDateGetter).apply(album)));
			} else if (beginDateGetter != null) {
				setText(dateFormatter.compose(beginDateGetter).apply(album));
			} else {
				mLog.severe("beginDateGetter must not be null inn DatesAlbumRenderer");
			}
		} else {
			mLog.severe("Invalid value type in Dates Album cell. Should be Album but is " + value.getClass().getName());
		}		
	}

}
