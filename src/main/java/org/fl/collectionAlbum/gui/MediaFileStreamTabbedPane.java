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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.table.MediaFileStreamTableModel;
import org.fl.collectionAlbum.gui.table.MediaStreamJTable;
import org.fl.collectionAlbum.mediaPath.MediaFileInventory;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;

public class MediaFileStreamTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	private final List<UpdatableElement> updatableElements;
	
	public MediaFileStreamTabbedPane() {
		
		super();
		setTabPlacement(JTabbedPane.LEFT);

		updatableElements = new ArrayList<>();
		
		Stream.of(ContentNature.values()).forEachOrdered(contentNature -> {

			MediaFileInventory mediaFileInventory = MediaFilesInventories.getMediaFileInventory(contentNature);
			
			MediaFileStreamTableModel mediaFileStreamTableModel = new MediaFileStreamTableModel(
					mediaFileInventory.getMediaStreamPatterns().getMediaStreamPatternList(), 
					MediaFileStreamTableModel.REGULAR_COLUMNS);
			updatableElements.add(mediaFileStreamTableModel);
			
			MediaStreamJTable mediaStreamJTable = new MediaStreamJTable(mediaFileStreamTableModel, contentNature);
			JScrollPane streamPatternsScrollPane = new JScrollPane(mediaStreamJTable);
			streamPatternsScrollPane.setPreferredSize(Control.getMainSubPaneDimension());
			
			addTab(contentNature.getNom(), streamPatternsScrollPane);
		});
	}
	
	public List<UpdatableElement> getUpdatableElements() {
		return updatableElements;
	}
}
