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

package org.fl.collectionAlbum.gui.listener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumRelease;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher.ReleaseMatchResult;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.DetailedAlbumAndDiscogsInfoPane;
import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.gui.MediaFilesSearchPane;
import org.fl.collectionAlbum.gui.MusicArtefactTable;

public class AlbumCustomActionListener implements java.awt.event.ActionListener {

	private static final Logger aLog = Logger.getLogger(AlbumCustomActionListener.class.getName());
	
	private static final Font verdana = new Font("Verdana", Font.BOLD, 12);
	private static final Font monospaced = new Font("monospaced", Font.BOLD, 14);
	
	private static final Predicate<Album> isLinkedToDiscogsRelease = (album) -> (album.getDiscogsLink() != null) && !album.getDiscogsLink().isEmpty();
	private static final Predicate<Album> hasMissingOrInvalidMediaFiles = 
			(album) -> Stream.of(ContentNature.values())
				.anyMatch(contentNature -> album.hasMediaFilePathNotFound(contentNature) || album.hasMissingOrInvalidMediaFilePath(contentNature)); 
	
	public enum CustomAction {
		
		DETAILED_INFO_DISPLAY("Informations détaillées", Objects::nonNull), 
		DISCOGS_RELEASE_SEARCH("Chercher la release discogs", isLinkedToDiscogsRelease.negate()),
		MISSING_MEDIA_FILES_SEARCH("Chercher les fichiers media manquants ou invalides", hasMissingOrInvalidMediaFiles);
		
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
	
	private final MusicArtefactTable<Album> albumsJTable;
	private final CustomAction customAction;
	private final GenerationPane generationPane;
	
	public AlbumCustomActionListener(MusicArtefactTable<Album> ajt, CustomAction ca, GenerationPane generationPane) {
		
		albumsJTable = ajt;
		customAction = ca;
		this.generationPane = generationPane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Album selectedAlbum = albumsJTable.getSelectedMusicArtefact();
		
		if (selectedAlbum != null) {
			
			switch (customAction) {
			
				case DETAILED_INFO_DISPLAY:
					
					JOptionPane.showMessageDialog(null, new DetailedAlbumAndDiscogsInfoPane(selectedAlbum), customAction.getActionTitle(), JOptionPane.INFORMATION_MESSAGE);
					break;
					
				case DISCOGS_RELEASE_SEARCH:
					
					// Show informations in popup message
					JPanel potentialReleasesPane = new JPanel();
					potentialReleasesPane.setLayout(new BoxLayout(potentialReleasesPane, BoxLayout.Y_AXIS));
					
					JTextArea infoPotentialRelease = new JTextArea(0, 200);
					infoPotentialRelease.setPreferredSize(new Dimension(1600,50));
					infoPotentialRelease.setMaximumSize(new Dimension(1600,50));
					infoPotentialRelease.setEditable(false);
					
					ReleaseMatchResult releaseMatchResult = selectedAlbum.searchPotentialDiscogsReleases();
					
					Set<DiscogsAlbumRelease> potentialReleases = releaseMatchResult.getMatchingReleases();
					
					infoPotentialRelease.setText(
							switch (releaseMatchResult.getMatchResultType()) {
							case MATCH -> "Releases discogs potentielles trouvées:\n\n";
							case NO_FORMAT_MATCH -> "Pas de release discogs potentielle trouvée (différence de format)\nRelease potentielle avec le même titre et des auteurs communs:\n\n";
							case NO_MATCH -> "Pas de release discogs potentielle trouvée";
							});
					
					infoPotentialRelease.setFont(monospaced);
					potentialReleasesPane.add(infoPotentialRelease);
					potentialReleases.forEach(release -> potentialReleasesPane.add(discogsPotentialReleasePane(release, selectedAlbum, potentialReleasesPane)));
					
					JScrollPane infoReleaseScroll = new JScrollPane(potentialReleasesPane);
					infoReleaseScroll.setPreferredSize(new Dimension(1650,850));
					JOptionPane.showMessageDialog(null, infoReleaseScroll, customAction.getActionTitle(), JOptionPane.INFORMATION_MESSAGE);
					
					break;
				case MISSING_MEDIA_FILES_SEARCH:
					
					JOptionPane.showMessageDialog(null, new MediaFilesSearchPane(selectedAlbum, generationPane), customAction.getActionTitle(), JOptionPane.INFORMATION_MESSAGE);
					break;
				default:
					aLog.severe("Unkown custom action triggered for discogs release: " + customAction);
			}
		}
		
	}
	
	private JPanel discogsPotentialReleasePane(DiscogsAlbumRelease release, Album album, JPanel potentialReleasesPane) {
		
		JPanel potentialReleasePane = new JPanel();
		potentialReleasePane.setLayout(new BoxLayout(potentialReleasePane, BoxLayout.X_AXIS));
		potentialReleasePane.setBorder(BorderFactory.createLineBorder(Color.BLACK,2,true)) ;
	
		JTextArea infoPotentialRelease = new JTextArea(0, 150);
		infoPotentialRelease.setEditable(false);
		infoPotentialRelease.setText(release.getInfo(true));
		infoPotentialRelease.setFont(monospaced);
		potentialReleasePane.add(infoPotentialRelease);
		
		JButton releaseValidate = new JButton("Valider cette release");
		releaseValidate.setBackground(Color.GREEN);
		releaseValidate.setFont(verdana);
		releaseValidate.addActionListener(new ReleaseValidationListener(release, album, potentialReleasesPane, generationPane));
		potentialReleasePane.add(releaseValidate);
		
		return potentialReleasePane;
	}

}
