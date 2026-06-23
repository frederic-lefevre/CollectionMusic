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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.table.MediaFileTableModel;
import org.fl.collectionAlbum.mediaFile.MediaFile;
import org.fl.collectionAlbum.mediaPath.MediaFileInventory;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;

public class MediaFilesSearchCriteriaPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Font buttonFont = new Font("Verdana", Font.BOLD, 14);
	
	private final MediaFileTableModel mediaFileTableModel;
	
	public MediaFilesSearchCriteriaPanel(ContentNature contentNature, MediaFileTableModel mediaFileTableModel) {
		super();
		
		this.mediaFileTableModel = mediaFileTableModel;
		
		MediaFileInventory mediaFileInventory = MediaFilesInventories.getMediaFileInventory(contentNature);
		
		mediaFileTableModel.getMediaFileList();
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JButton mediaFilesSearchButton = new JButton("Rechercher");
		mediaFilesSearchButton.setFont(buttonFont);
		mediaFilesSearchButton.setBackground(Color.GREEN);
		mediaFilesSearchButton.addActionListener(new MediaFilesSearchCriteriaListener());
		
		add(mediaFilesSearchButton);
	}
	
	private class MediaFilesSearchCriteriaListener implements java.awt.event.ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			List<MediaFile> mediaFileListResult = mediaFileTableModel.getMediaFileList();
			mediaFileListResult.clear();
			
			mediaFileTableModel.fireTableDataChanged();
		}
		
	}
}
