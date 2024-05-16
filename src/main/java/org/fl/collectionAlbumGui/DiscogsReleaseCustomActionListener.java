/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher.AlbumMatchResult;
import org.fl.collectionAlbum.disocgs.DiscogsInventory.DiscogsAlbumRelease;

public class DiscogsReleaseCustomActionListener implements java.awt.event.ActionListener {

	private static final Logger aLog = Logger.getLogger(DiscogsReleaseCustomActionListener.class.getName());
	
	private static final Predicate<DiscogsAlbumRelease> isNotLinkedToAlbum = (release) -> (release != null) && !release.isLinkedToAlbum();
	
	public enum CustomAction {
		
		SHOW_INFO("Afficher les informations", (release) -> release != null),
		ALBUM_SEARCH("Chercher les albums", isNotLinkedToAlbum);
		
		private final String actionTitle;
		private final Predicate<DiscogsAlbumRelease> displayable;
		
		private CustomAction(String actionTitle, Predicate<DiscogsAlbumRelease> displayable) {
			this.actionTitle = actionTitle;
			this.displayable = displayable;
		}

		public String getActionTitle() {
			return actionTitle;
		}

		public Predicate<DiscogsAlbumRelease> getDisplayable() {
			return displayable;
		}
		
	};
	
	private final DiscogsReleaseJTable discogsReleaseJTable;
	private final CustomAction customAction;
	private final CollectionAlbumContainer albumsContainer;
	
	public DiscogsReleaseCustomActionListener(DiscogsReleaseJTable discogsReleaseJTable, CustomAction customAction, CollectionAlbumContainer albumsContainer) {
		
		this.discogsReleaseJTable = discogsReleaseJTable;
		this.customAction = customAction;
		this.albumsContainer = albumsContainer;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		DiscogsAlbumRelease release = discogsReleaseJTable.getSelectedDisocgsRelease();
		
		if (release != null) {
			
			switch (customAction) {
			
				case SHOW_INFO:
					
					// Show informations in popup message
					JTextArea infoRelease = new JTextArea(40, 200);
					infoRelease.setEditable(false);
					
					infoRelease.setText(release.getInfo());
					infoRelease.setFont(new Font("monospaced", Font.BOLD, 14));
					JScrollPane infoFilesScroll = new JScrollPane(infoRelease) ;
					JOptionPane.showMessageDialog(null, infoFilesScroll, "Informations", JOptionPane.INFORMATION_MESSAGE);
				
					break;
					
				case ALBUM_SEARCH:
					
					JPanel potentialAlbumsPane = new JPanel();
					potentialAlbumsPane.setLayout(new BoxLayout(potentialAlbumsPane, BoxLayout.Y_AXIS));
					
					JTextArea infoPotentialAlbums = new JTextArea(0, 200);
					infoPotentialAlbums.setPreferredSize(new Dimension(1600,50));
					infoPotentialAlbums.setMaximumSize(new Dimension(1600,50));
					infoPotentialAlbums.setEditable(false);
					
					AlbumMatchResult albumMatchResult = DiscogsAlbumReleaseMatcher.getPotentialAlbumMatch(release, albumsContainer.getAlbumsMissingDiscogsRelease().getAlbums());
					
					Set<Album> potentialAlbums = albumMatchResult.getMatchingAlbums();
					
					infoPotentialAlbums.setText(
						switch (albumMatchResult.getMatchResultType()) {
						case MATCH -> "Albums potentiels trouvés:\n\n";
						case NO_FORMAT_MATCH -> "Pas d'album potentiel trouvé\nAlbum potentiel avec le même titre et des auteurs communs:\n\n";
						case NO_MATCH -> "Pas d'album potentiel trouvé";
						});

					infoPotentialAlbums.setFont(new Font("monospaced", Font.BOLD, 14));
					JScrollPane searchResultScroll = new JScrollPane(infoPotentialAlbums);
					JOptionPane.showMessageDialog(null, searchResultScroll, "Recherche d'albums", JOptionPane.INFORMATION_MESSAGE);
					
					break;
				default:
					aLog.severe("Unkown custom action triggered for discogs release: " + customAction);
			}
		}
	}

}
