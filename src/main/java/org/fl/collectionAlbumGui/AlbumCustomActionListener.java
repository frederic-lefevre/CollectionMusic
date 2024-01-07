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

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher.ReleaseMatchResult;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.disocgs.DiscogsInventory.DiscogsAlbumRelease;

public class AlbumCustomActionListener implements java.awt.event.ActionListener {

	private static final Logger aLog = Control.getAlbumLog();
	
	private static final Predicate<Album> isLinkedToDiscogsRelease = (album) -> (album.getDiscogsLink() != null) && !album.getDiscogsLink().isEmpty();
	
	public enum CustomAction {
		
		DISCOGS_RELEASE_DISPLAY("Afficher la release discogs", isLinkedToDiscogsRelease), 
		DISCOGS_RELEASE_SEARCH("Chercher la release discogs", isLinkedToDiscogsRelease.negate()) ;
		
		private final String actionTitle;
		private final Predicate<Album> displayable;
		
		private CustomAction(String actionTitle, Predicate<Album> displayable) {
			this.actionTitle = actionTitle;
			this.displayable = displayable;
		}

		public String getActionTitle() {
			return actionTitle;
		}

		public Predicate<Album> getDisplayable() {
			return displayable;
		}		
	};
	
	private final AlbumsJTable albumsJTable;
	private final CustomAction customAction;
	
	public AlbumCustomActionListener(AlbumsJTable ajt, CustomAction ca) {
		
		albumsJTable = ajt;
		customAction = ca;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Album selectedAlbum = albumsJTable.getSelectedAlbum();
		
		if (selectedAlbum != null) {
			
			switch (customAction) {
			
				case DISCOGS_RELEASE_DISPLAY:
					
					String discogsReleaseId = selectedAlbum.getDiscogsLink();					
					if (discogsReleaseId != null) {
						
						DiscogsAlbumRelease release = DiscogsInventory.getDiscogsAlbumRelease(discogsReleaseId);
						if (release != null) {
							
							// Show informations in popup message
							JTextArea infoRelease = new JTextArea(40, 200);	
							
							infoRelease.setText(release.getInfo());
							infoRelease.setFont(new Font("monospaced", Font.BOLD, 14));
							JScrollPane infoFilesScroll = new JScrollPane(infoRelease) ;
							JOptionPane.showMessageDialog(null, infoFilesScroll, "Informations de la release discogs", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					break;
					
				case DISCOGS_RELEASE_SEARCH:
					
					// Show informations in popup message
					JPanel potentialReleasesPane = new JPanel();
					potentialReleasesPane.setLayout(new BoxLayout(potentialReleasesPane, BoxLayout.Y_AXIS));
					
					JTextArea infoPotentialRelease = new JTextArea(0, 200);
					infoPotentialRelease.setPreferredSize(new Dimension(1600,50));
					infoPotentialRelease.setMaximumSize(new Dimension(1600,50));
					
					ReleaseMatchResult releaseMatchResult = selectedAlbum.searchPotentialDiscogsReleases();
					
					Set<DiscogsAlbumRelease> potentialReleases = releaseMatchResult.getMatchingReleases();
					
					switch (releaseMatchResult.getMatchResultType()) {
					case MATCH:
						infoPotentialRelease.setText("Releases discogs potentielles trouvées:\n\n");
						break;
						
					case NO_FORMAT_MATCH:
						infoPotentialRelease.setText("Pas de release discogs potentielle trouvée\nRelease potentielle avec le même titre et des auteurs communs:\n\n");
						break;
						
					case NO_MATCH:
						infoPotentialRelease.setText("Pas de release discogs potentielle trouvée");
						break;
						
					}
					infoPotentialRelease.setFont(new Font("monospaced", Font.BOLD, 14));
					potentialReleasesPane.add(infoPotentialRelease);
					potentialReleases.forEach(release -> potentialReleasesPane.add(discogsPotentialReleasePane(release)));
					
					JScrollPane infoReleaseScroll = new JScrollPane(potentialReleasesPane);
					infoReleaseScroll.setPreferredSize(new Dimension(1650,850));
					JOptionPane.showMessageDialog(null, infoReleaseScroll, "Recherche de release discogs", JOptionPane.INFORMATION_MESSAGE);
					
					break;
				default:
					aLog.severe("Unkown custom action triggered for discogs release: " + customAction);
			}
		}
		
	}
	
	private JPanel discogsPotentialReleasePane(DiscogsAlbumRelease release) {
		
		JPanel potentialReleasePane = new JPanel();
		JTextArea infoPotentialRelease = new JTextArea(0, 200);
		infoPotentialRelease.setText(release.getInfo());
		infoPotentialRelease.setFont(new Font("monospaced", Font.BOLD, 14));
		potentialReleasePane.add(infoPotentialRelease);
		return potentialReleasePane;
	}

}
