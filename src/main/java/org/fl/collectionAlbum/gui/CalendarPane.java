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

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.stream.Stream;

import javax.swing.JTabbedPane;

import org.fl.collectionAlbum.CollectionAlbumContainer;

public class CalendarPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	
	public CalendarPane(CollectionAlbumContainer collectionAlbumContainer, GenerationPane generationPane) {
		super();
		
		setTabPlacement(JTabbedPane.LEFT);
		
		Stream.of(Month.values()).forEachOrdered(month -> {
			
			MonthPane monthPane = new MonthPane(collectionAlbumContainer, generationPane);
			addTab(month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.FRANCE), monthPane);
		});
	}
}
