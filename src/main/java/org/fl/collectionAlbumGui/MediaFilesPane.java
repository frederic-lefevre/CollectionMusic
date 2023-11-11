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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fl.collectionAlbum.albums.Album;

public class MediaFilesPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel mediaFilesStatus;
	private JButton mediaFilesSearch;
	
	public MediaFilesPane() {
		
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		mediaFilesStatus = new JLabel("Status unknown");
		add(mediaFilesStatus);
		
		mediaFilesSearch = new JButton("Search potential media files");
	}

	public void updateValue(Album album) {
		if (album.hasMediaFiles()) {
			if (album.hasMediaFilePathNotFound() ||
				album.hasMissingOrInvalidMediaFilePath()) {
				mediaFilesStatus.setText("Invalid or missing media file paths");
				add(mediaFilesSearch);
			} else {
				mediaFilesStatus.setText("Media file paths found");
				remove(mediaFilesSearch);
			}
		} else {
			mediaFilesStatus.setText("No media files");
			add(mediaFilesSearch);
		}
	}
}
