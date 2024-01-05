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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
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
							JOptionPane.showMessageDialog(null, infoFilesScroll, "Informations", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					break;
					
				case DISCOGS_RELEASE_SEARCH:
					break;
				default:
					aLog.severe("Unkown custom action triggered for discogs release: " + customAction);
			}
		}
		
	}

}
