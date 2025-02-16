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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
import org.fl.collectionAlbum.utils.AlbumUtils;
import org.fl.collectionAlbumGui.listener.OsActionListener;

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
		infoRelease.setFont(new Font("monospaced", Font.BOLD, 14));
		infoRelease.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
		
		releasePane.add(infoRelease);
		
		Font font = new Font("Verdana", Font.BOLD, 14);
		
		JButton showDiscogsRelease = new JButton("Montrer la release sur le site Discogs"); 
		showDiscogsRelease.setFont(font);
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
		infoAlbum.setText(AlbumUtils.getSimpleHtml(album));
		infoAlbum.setEditable(false);
		infoAlbum.setFont(new Font("monospaced", Font.BOLD, 14));
		return infoAlbum;
	}
	
	private static final int MAX_COVER_WIDTH = 400;
	private static final int MAX_COVER_HEIGHT = 400;
	
	private JPanel albumOtherInfo(Album album) {
		
		JPanel albumPane = new JPanel();
		albumPane.setLayout(new BoxLayout(albumPane, BoxLayout.Y_AXIS));
		
		albumPane.add(getCoverImage(album));
		
		if (album.hasMediaFiles()) {
			
			JLabel titreMedia = new JLabel("Folders contenant les medias:");
			titreMedia.setBorder(new EmptyBorder(20, 0, 20, 0));
			titreMedia.setFont(new Font("Verdana", Font.BOLD, 14));
			albumPane.add(titreMedia);
			
			album.getAllMediaFiles().stream()
					.map(mediaFile -> mediaFile.getMediaFilePaths())
					.flatMap(Collection::stream)
					.map(mediaFilePath -> mediaFilePath.getPath().toString())
					.distinct()
					.forEach(mediaFolder -> {
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
		
		if (album.getCoverImage() != null) {
			try {
				ImageIcon cover = new ImageIcon(ImageIO.read(album.getCoverImage().toFile()));
				final int coverWidth = cover.getIconWidth();
				final int coverHeight = cover.getIconHeight();
				int adjustedCoverWidth;
				int adjustedCoverHeight;
				if (coverWidth > coverHeight) {
					adjustedCoverWidth = MAX_COVER_WIDTH;
					adjustedCoverHeight = (coverHeight * MAX_COVER_WIDTH)/coverWidth;
				} else {
					adjustedCoverHeight = MAX_COVER_HEIGHT;
					adjustedCoverWidth = (coverWidth* MAX_COVER_HEIGHT)/coverHeight;
				}
				ImageIcon adjusteCover = new ImageIcon(cover.getImage().getScaledInstance(adjustedCoverWidth, adjustedCoverHeight, Image.SCALE_SMOOTH));

				return new JLabel(adjusteCover);
			} catch (IOException e) {
				return new JLabel("Fichier couverture non trouvé: " + album.getCoverImage().toString());
			}
		} else {
			return new  JLabel("Couverture de l'album non disponible");
		}
	}
}
