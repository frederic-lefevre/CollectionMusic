/*
 MIT License

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

package org.fl.collectionAlbum.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JPanel;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumRelease;
import org.fl.collectionAlbum.gui.GenerationPane;

public class DiscogsFormatValidationListener implements ActionListener {

	private static final Logger aLog = Logger.getLogger(DiscogsFormatValidationListener.class.getName());
	
	private final DiscogsAlbumRelease release;
	private final JPanel formatValidationPane;
	private final GenerationPane generationPane;
	
	public DiscogsFormatValidationListener(DiscogsAlbumRelease release, JPanel formatValidationPane, GenerationPane generationPane) {
		
		this.release = release;
		this.formatValidationPane = formatValidationPane;
		this.generationPane = generationPane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Set<Album> albums = release.getAlbumsWithFormatMismatch();
		if ((albums != null) && !albums.isEmpty()) {
			
			albums.forEach(album -> setDiscogsFormatValidation(album));
			formatValidationPane.removeAll();
			formatValidationPane.updateUI();
			generationPane.rescanNeeded();
			
		} else {
			aLog.severe("Trying to set discogs format validation for a discogs release that is not linked to an album or that has no format mismatch problem. Release id=" + release.getInventoryCsvAlbum().getReleaseId());
		}
		
	}

	private void setDiscogsFormatValidation(Album album) {
		
		album.setDiscogsFormatValid(true);
		album.writeJson();
	}
}
