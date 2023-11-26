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

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.util.os.OScommand;

public class AlbumCommandListener implements java.awt.event.ActionListener {

	private static final Logger aLog = Control.getAlbumLog();
	
	private final String command;
	private final AlbumsJTable albumsJTable;
	
	public AlbumCommandListener(AlbumsJTable ajt, String command) {
		
		this.albumsJTable = ajt;
		this.command = command;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Album selectedAlbum = albumsJTable.getSelectedAlbum();
		
		if (selectedAlbum != null) {
			
			StringBuilder fullCommand = new StringBuilder(command);
			
			fullCommand.append(" ")
				.append(selectedAlbum.getJsonFilePath().toAbsolutePath().toString());
			
			OScommand osCommand = new OScommand(fullCommand.toString(), false, aLog) ;
			osCommand.run();
		}
		
	}

}
