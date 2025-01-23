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

package org.fl.collectionAlbumGui;

import java.awt.Font;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumRelease;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;

public class DetailedAlbumAndDiscogsInfoPane extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public DetailedAlbumAndDiscogsInfoPane(DiscogsAlbumRelease release) {
		
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		releaseInfos(release);
		albumsInfos(release.getCollectionAlbums());
	}

	public DetailedAlbumAndDiscogsInfoPane(Album album) {
		
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		albumsInfos(Set.of(album));
		
		String discogsReleaseId = album.getDiscogsLink();					
		if (discogsReleaseId != null) {
			
			DiscogsAlbumRelease release = DiscogsInventory.getDiscogsAlbumRelease(discogsReleaseId);
			if (release != null) {
				releaseInfos(release);
			}
		}
	}
	
	private void releaseInfos(DiscogsAlbumRelease release) {
		
		JTextArea infoRelease = new JTextArea(20, 200);
		infoRelease.setEditable(false);
		
		infoRelease.setText(release.getInfo(false));
		infoRelease.setFont(new Font("monospaced", Font.BOLD, 14));
		JScrollPane infoFilesScroll = new JScrollPane(infoRelease);
		add(infoFilesScroll);
	}
	
	private void albumsInfos(Set<Album> albums) {
		
		StringBuilder info = new StringBuilder();
		JTextArea infoAlbums = new JTextArea(20, 200);
		infoAlbums.setEditable(false);
				
		albums.forEach(album -> 
			info.append(album.getJsonString())
				.append("\n-------------------------------------\n"));
			
		infoAlbums.setText(info.toString());
		infoAlbums.setFont(new Font("monospaced", Font.BOLD, 14));
		JScrollPane infoFilesScroll = new JScrollPane(infoAlbums);
		
		add(infoFilesScroll);
	}
}
