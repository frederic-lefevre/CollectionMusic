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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumRelease;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;

public class DetailedAlbumAndDiscogsInfoPane extends JScrollPane {

	private static final long serialVersionUID = 1L;
	
	public DetailedAlbumAndDiscogsInfoPane(DiscogsAlbumRelease release) {
		
		super();
		setPreferredSize(new Dimension(1650,850));
		JPanel infosPane = new JPanel();
		infosPane.setLayout(new BoxLayout(infosPane, BoxLayout.Y_AXIS));
		
		infosPane.add(releaseInfos(release));
		infosPane.add(albumsInfos(release.getCollectionAlbums()));
		setViewportView(infosPane);
	}

	public DetailedAlbumAndDiscogsInfoPane(Album album) {
		
		super();
		setPreferredSize(new Dimension(1650,850));
		JPanel infosPane = new JPanel();
		infosPane.setLayout(new BoxLayout(infosPane, BoxLayout.Y_AXIS));
		
		infosPane.add(albumsInfos(Set.of(album)));
		
		String discogsReleaseId = album.getDiscogsLink();					
		if (discogsReleaseId != null) {
			
			DiscogsAlbumRelease release = DiscogsInventory.getDiscogsAlbumRelease(discogsReleaseId);
			if (release != null) {
				infosPane.add(releaseInfos(release));
			}
		}
		setViewportView(infosPane);
	}
	
	private JTextArea releaseInfos(DiscogsAlbumRelease release) {
		
		JTextArea infoRelease = new JTextArea(release.getInfo(false));
		infoRelease.setEditable(false);
		infoRelease.setFont(new Font("monospaced", Font.BOLD, 14));
		infoRelease.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
		
		return infoRelease;
	}
	
	private JPanel albumsInfos(Set<Album> albums) {
		
		JPanel albumsPane = new JPanel();
		albumsPane.setLayout(new BoxLayout(albumsPane, BoxLayout.Y_AXIS));
		albums.forEach(album -> albumsPane.add(albumInfo(album)));
		
		return albumsPane;
	}
	
	private JEditorPane albumInfo(Album album) {
		
		JEditorPane infoAlbum = new JEditorPane();
		infoAlbum.setContentType("text/html");
		infoAlbum.setText(album.getSimpleHtml());
		infoAlbum.setEditable(false);
		infoAlbum.setFont(new Font("monospaced", Font.BOLD, 14));
		return infoAlbum;
	}
}
