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

package org.fl.collectionAlbumGui.listener;

import java.awt.event.ActionEvent;

import org.fl.collectionAlbum.OsAction;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;
import org.fl.collectionAlbumGui.MediaFilesJTable;

public class MediaFileCommandListener implements java.awt.event.ActionListener {

	private final MediaFilesJTable mediaFileJTable;
	private final OsAction<MediaFilePath> osAction;
	
	public MediaFileCommandListener(MediaFilesJTable mediaFileJTable, OsAction<MediaFilePath> osAction) {
		this.mediaFileJTable = mediaFileJTable;
		this.osAction = osAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		MediaFilePath mediaFilePath = mediaFileJTable.getSelectedMediaFile();
		
		if (mediaFilePath != null) {
			osAction.runOsAction(mediaFilePath);
		}
	}

}
