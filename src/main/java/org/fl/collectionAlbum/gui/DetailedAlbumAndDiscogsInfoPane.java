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

package org.fl.collectionAlbum.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumRelease;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.gui.listener.OsActionListener;
import org.fl.collectionAlbum.utils.CollectionUtils;

public class DetailedAlbumAndDiscogsInfoPane extends JScrollPane {

	private static final long serialVersionUID = 1L;
	
	private static final Font verdana = new Font("Verdana", Font.BOLD, 14);
	private static final Font monospaced = new Font("monospaced", Font.BOLD, 14);
	
	private static final int PREFERRED_WIDTH = 1700;
	private static final int PREFERRED_HEIGHT = 900;
	
	private static final int MAX_COVER_WIDTH = 400;
	private static final int MAX_COVER_HEIGHT = 400;
	
	private static final int RELEASE_INFO_PREFERRED_WIDTH = PREFERRED_WIDTH - 450;
	private static final int RELEASE_INFO_PREFERRED_HEIGHT = PREFERRED_HEIGHT - 575;
	
	public DetailedAlbumAndDiscogsInfoPane(DiscogsAlbumRelease release) {
		
		super();
		setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
		JPanel infosPane = new JPanel();
		infosPane.setLayout(new BoxLayout(infosPane, BoxLayout.Y_AXIS));
		
		infosPane.add(releaseInfos(release));
		infosPane.add(albumsInfos(release.getCollectionAlbums()));
		setViewportView(infosPane);
	}

	public DetailedAlbumAndDiscogsInfoPane(Album album) {
		
		super();
		setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
		JPanel infosPane = new JPanel();
		infosPane.setLayout(new BoxLayout(infosPane, BoxLayout.Y_AXIS));
		
		infosPane.add(albumsInfos(Set.of(album)));
		
		String discogsReleaseId = album.getDiscogsLink();					
		if (discogsReleaseId != null) {
			infosPane.add(releaseInfos(DiscogsInventory.getDiscogsAlbumRelease(discogsReleaseId)));			
		}
		setViewportView(infosPane);
	}
	
	private JPanel releaseInfos(DiscogsAlbumRelease release) {
		
		String releaseInfo;
		if (release == null) {
			releaseInfo = "Release inconnue dans l'inventaire des releases Discogs";
		} else {
			releaseInfo = release.getInfo(false);
		}
		
		JPanel releasePane = new JPanel();
		releasePane.setLayout(new BoxLayout(releasePane, BoxLayout.X_AXIS));
		
		JTextArea infoRelease = new JTextArea(releaseInfo);
		infoRelease.setEditable(false);
		infoRelease.setFont(monospaced);
		infoRelease.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
		
		JScrollPane discogsReleasesScrollPane = new JScrollPane(infoRelease);
		discogsReleasesScrollPane.setPreferredSize(new Dimension(RELEASE_INFO_PREFERRED_WIDTH,RELEASE_INFO_PREFERRED_HEIGHT));
		
		releasePane.add(discogsReleasesScrollPane);
		
		JButton showDiscogsRelease = new JButton("Montrer la release sur le site Discogs"); 
		showDiscogsRelease.setFont(verdana);
		showDiscogsRelease.setBackground(Color.GREEN);
		showDiscogsRelease.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		OsActionListener<List<String>> showDiscogsReleasenListener = 
				new OsActionListener<>(List.of(Control.getDiscogsBaseUrlForRelease() + release.getInventoryCsvAlbum().getReleaseId()), Control.getDisplayUrlAction());
		
		showDiscogsRelease.addActionListener(showDiscogsReleasenListener);
		
		releasePane.add(showDiscogsRelease);
		
		return releasePane;
	}
	
	private JPanel albumsInfos(Set<Album> albums) {
		
		JPanel albumsPane = new JPanel();
		albumsPane.setLayout(new BoxLayout(albumsPane, BoxLayout.Y_AXIS));
		albums.forEach(album -> albumsPane.add(albumInfo(album)));
		
		return albumsPane;
	}
	
	private JPanel albumInfo(Album album) {
		
		JPanel albumPane = new JPanel();
		albumPane.setLayout(new BoxLayout(albumPane, BoxLayout.X_AXIS));
		albumPane.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
		
		albumPane.add(albumTextInfo(album));
		albumPane.add(albumOtherInfo(album));
		return albumPane;
	}
	
	private JEditorPane albumTextInfo(Album album) {
		
		JEditorPane infoAlbum = new JEditorPane();
		infoAlbum.setContentType("text/html");
		infoAlbum.setText(CollectionUtils.getSimpleHtml(album));
		infoAlbum.setEditable(false);
		infoAlbum.setFont(monospaced);
		return infoAlbum;
	}
	
	private JPanel albumOtherInfo(Album album) {
		
		JPanel albumPane = new JPanel();
		albumPane.setLayout(new BoxLayout(albumPane, BoxLayout.Y_AXIS));
		
		albumPane.add(getCoverImage(album));
		
		if (album.hasMediaFiles()) {
			
			JLabel titreMedia = new JLabel("Folders contenant les medias:");
			titreMedia.setBorder(new EmptyBorder(20, 0, 20, 0));
			titreMedia.setFont(verdana);
			albumPane.add(titreMedia);
			
			album.getAllMediaFiles().stream()
					.map(mediaFile -> mediaFile.getMediaFilePaths())
					.flatMap(Collection::stream)
					.map(mediaFilePath -> mediaFilePath.getPath().toString())
					.distinct()
					.forEachOrdered(mediaFolder -> {
						JButton showMediaFolderButton = new JButton(mediaFolder);
						showMediaFolderButton.setBorder(new EmptyBorder(20, 0, 20, 0));
						
						OsActionListener<String> showMediaFolderListener = new OsActionListener<>(mediaFolder, Control.getDisplayFolderAction());
						showMediaFolderButton.addActionListener(showMediaFolderListener);
						
						albumPane.add(showMediaFolderButton);
					});						
		}
			
		return albumPane;
	}
	
	private JLabel getCoverImage(Album album) {
		
		Path coverImagePath = album.getCoverImage();
		if (coverImagePath != null) {
			return CollectionUtils.getAdjustedImageLabel(coverImagePath, MAX_COVER_WIDTH, MAX_COVER_HEIGHT);
		} else {
			return new JLabel("Couverture de l'album non disponible");
		}
	}
}
