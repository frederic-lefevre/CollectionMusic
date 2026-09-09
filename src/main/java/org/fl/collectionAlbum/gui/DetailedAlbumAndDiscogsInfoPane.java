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
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumRelease;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.disocgs.DiscogsReleaseRequest;
import org.fl.collectionAlbum.gui.adapter.ImageDisplayMouseAdapter;
import org.fl.collectionAlbum.gui.listener.CollectionHyperLinkListener;
import org.fl.collectionAlbum.gui.listener.MediaFilePathActionListener;
import org.fl.collectionAlbum.gui.listener.OsActionListener;
import org.fl.collectionAlbum.gui.table.ArtistesScrollJTablePane;
import org.fl.collectionAlbum.gui.table.ArtistesTableColumns;
import org.fl.collectionAlbum.utils.CollectionImage;
import org.fl.collectionAlbum.utils.CollectionUtils;

public class DetailedAlbumAndDiscogsInfoPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(DetailedAlbumAndDiscogsInfoPane.class.getName());
	
	private static final Font verdana = new Font("Verdana", Font.BOLD, 14);
	private static final Font monospaced = new Font("monospaced", Font.BOLD, 14);
	
	private static final int MAX_COVER_WIDTH = 400;
	private static final int MAX_COVER_HEIGHT = 400;
	

	
	public DetailedAlbumAndDiscogsInfoPane(DiscogsAlbumRelease release, GenerationPane generationPane) {
		
		super();
		setPreferredSize(Control.getInfoWindowDimension());		
		addTab("Discogs release", new ReleasePanel(release));
		addAlbumsTab(release.getCollectionAlbums());
		addArtistesTab(
				release.getCollectionAlbums().stream().map(Album::getAllArtists).flatMap(artistList -> artistList.stream()).toList(), 
				generationPane);
	}

	public DetailedAlbumAndDiscogsInfoPane(Album album, GenerationPane generationPane) {
		
		super();
		setPreferredSize(Control.getInfoWindowDimension());
		addTab("Album", albumsInfos(Set.of(album)));
		
		String discogsReleaseId = album.getDiscogsLink();					
		if (discogsReleaseId != null) {
			DiscogsAlbumRelease release = DiscogsInventory.getDiscogsAlbumRelease(discogsReleaseId);
			if (release != null) {
				addTab("Discogs release", new ReleasePanel(release));
			} else {
				logger.warning("La release discogs référencé dans l'album " + album.getTitre() + " n'est pas dans l'inventaire discogs (csv)");
			}
		}
		addArtistesTab(album.getAllArtists(), generationPane);
	}
	
	private void addAlbumsTab(Set<Album> albums) {
		if (albums.size() > 1) {
			addTab("Albums", albumsInfos(albums));
		} else {
			addTab("Album", albumsInfos(albums));
		}
	}
	
	public static class ReleasePanel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		private static final String WAIT_MESSAGE = "<html><body>Requête envoyée à discogs ....</body></html>";
		
		private final JEditorPane releaseTextInfoFromDiscogs;
		private final JLabel coverImageLabel;
		
		private ReleasePanel(DiscogsAlbumRelease release) {
			super();
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			
			releaseTextInfoFromDiscogs = new JEditorPane();
			releaseTextInfoFromDiscogs.setContentType("text/html");
			releaseTextInfoFromDiscogs.setEditable(false);
			releaseTextInfoFromDiscogs.setFont(monospaced);
			releaseTextInfoFromDiscogs.addHyperlinkListener(new CollectionHyperLinkListener());
			releaseTextInfoFromDiscogs.setText(WAIT_MESSAGE);
			add(new JScrollPane(releaseTextInfoFromDiscogs));
			
			JPanel releasePane = new JPanel();
			releasePane.setLayout(new BoxLayout(releasePane, BoxLayout.Y_AXIS));
			
			coverImageLabel = new JLabel();
			coverImageLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
			releasePane.add(coverImageLabel);
			
			JButton showDiscogsRelease = new JButton("Montrer la release sur le site Discogs"); 
			
			OsActionListener<List<String>> showDiscogsReleasenListener = 
					new OsActionListener<>(List.of(Control.getDiscogsBaseUrlForRelease() + release.inventoryCsvAlbum().getReleaseId()), Control.getDisplayUrlAction());
			
			showDiscogsRelease.addActionListener(showDiscogsReleasenListener);
			
			releasePane.add(showDiscogsRelease);
			add(releasePane);
			
			DiscogsReleaseRequest discogsReleaseRequest = new DiscogsReleaseRequest(release, this);
			discogsReleaseRequest.execute();
		}
		
		public void setReleaseTextInfo(String text) {
			releaseTextInfoFromDiscogs.setText(text);
		}
		
		public void setReleaseCoverImage(CollectionImage coverImage) {
			coverImageLabel.setIcon(coverImage.buildAdjustedImageIcon(MAX_COVER_WIDTH, MAX_COVER_HEIGHT));
		}
	}
	
	private JScrollPane albumsInfos(Set<Album> albums) {
		
		JPanel albumsPane = new JPanel();
		albumsPane.setLayout(new BoxLayout(albumsPane, BoxLayout.Y_AXIS));
		albums.forEach(album -> albumsPane.add(albumInfo(album)));
		
		return new JScrollPane(albumsPane);
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
			
			JLabel titreMediaFiles = new JLabel("Listes fichiers medias:");
			titreMediaFiles.setBorder(new EmptyBorder(10, 0, 10, 0));
			titreMediaFiles.setFont(verdana);
			albumPane.add(titreMediaFiles);
			
			album.getAllMediaFilePaths().stream()
				.map(mediaFilePaths -> mediaFilePaths.getMediaFilePaths())
				.flatMap(Collection::stream)
				.forEachOrdered(mediaFilePath -> {
					JButton showMediaFilesutton = new JButton("Liste des fichiers media de " + mediaFilePath.getPath().getFileName());
					
					MediaFilePathActionListener showMediaFileListener = new MediaFilePathActionListener(mediaFilePath);
					showMediaFilesutton.addActionListener(showMediaFileListener);
					
					albumPane.add(showMediaFilesutton);
				});
			
			JLabel titreMediaPath = new JLabel("Chemins contenant les fichiers medias:");
			titreMediaPath.setBorder(new EmptyBorder(10, 0, 10, 0));
			titreMediaPath.setFont(verdana);
			albumPane.add(titreMediaPath);
			
			album.getAllMediaFilePaths().stream()
					.map(mediaFilePaths -> mediaFilePaths.getMediaFilePaths())
					.flatMap(Collection::stream)
					.map(mediaFilePath -> mediaFilePath.getPath().toString())
					.distinct()
					.forEachOrdered(mediaFolder -> {
						JButton showMediaFolderButton = new JButton(mediaFolder);
						
						OsActionListener<String> showMediaFolderListener = new OsActionListener<>(mediaFolder, Control.getDisplayFolderAction());
						showMediaFolderButton.addActionListener(showMediaFolderListener);
						
						albumPane.add(showMediaFolderButton);
					});						
		}
		
		if (album.hasUrlLinks()) {
			albumPane.add(CollectionUtils.urlLinkPanel(album));
		}
		
		return albumPane;
	}
	
	private JLabel getCoverImage(Album album) {
		CollectionImage sleeveImage = album.getSleeveImage();
		JLabel sleeveImageLabel = sleeveImage.getAdjustedImageLabel(MAX_COVER_WIDTH, MAX_COVER_HEIGHT);
		sleeveImageLabel.addMouseListener(new ImageDisplayMouseAdapter(sleeveImage.getBufferedImage()));
		return sleeveImageLabel;
	}
	
	private void addArtistesTab(List<Artiste> artistes, GenerationPane generationPane) {
		if (artistes.size() == 1) {
			addTab("Artiste", new ArtisteInformationPanel(artistes.get(0), generationPane));
		} else if (artistes.size() > 1) {
			addTab("Artistes", new ArtistesScrollJTablePane(artistes, ArtistesTableColumns.REGULAR_COLUMNS, generationPane));
		}
	}
}
