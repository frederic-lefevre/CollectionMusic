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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;

public class MediaFilesSearchPane extends JScrollPane {

	private static final long serialVersionUID = 1L;
	
	private static final ContentNature[] CONTENT_NATURES = ContentNature.values();
	
	private static final int PREFERRED_WIDTH = 1000;
	private static final int PREFERRED_HEIGHT = 50*CONTENT_NATURES.length;
	
	private Map<ContentNature, JLabel> mediaFilesStatuses;
	private Map<ContentNature, JButton> mediaFilesSearches;
	private Map<ContentNature, JButton> mediaFilesValidations;
	private Map<ContentNature,JScrollPane> statusScrollPanels;
	private Map<ContentNature,JPanel> mediaFilesPanels;
	
	public MediaFilesSearchPane(Album album, GenerationPane generationPane) {
		
		super();
		
		Map<ContentNature, MediaFilesSearchListener> mediaFilesSearchListeners = new HashMap<>();
		Map<ContentNature, MediaFileValidationListener> mediaFilesValidationListeners = new HashMap<>();
		
		mediaFilesStatuses = new HashMap<>();
		mediaFilesSearches = new HashMap<>();
		mediaFilesValidations = new HashMap<>();
		statusScrollPanels = new HashMap<>();
		mediaFilesPanels = new HashMap<>();
		
		Stream.of(CONTENT_NATURES).forEachOrdered(contentNature -> {
			mediaFilesSearchListeners.put(contentNature, new MediaFilesSearchListener(album, contentNature));
			mediaFilesValidationListeners.put(contentNature, new MediaFileValidationListener(album, contentNature, generationPane));
		});
		
		
		setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
		
		JPanel mediaFilesSearchPane = new JPanel();
		mediaFilesSearchPane.setLayout(new BoxLayout(mediaFilesSearchPane, BoxLayout.Y_AXIS));
		
		// Search and validation media files button
		Stream.of(CONTENT_NATURES).forEachOrdered(contentNature -> {
			
			JPanel mediaFilePanel = new JPanel();
			mediaFilePanel.setLayout(new BoxLayout(mediaFilePanel, BoxLayout.X_AXIS));
			mediaFilesPanels.put(contentNature, mediaFilePanel);
			
			JButton searchButton = new JButton("Chercher " + contentNature.getNom());
			searchButton.addActionListener(mediaFilesSearchListeners.get(contentNature));
			mediaFilesSearches.put(contentNature, searchButton); 
			
			JButton validationButton = new JButton("Valider " + contentNature.getNom());
			validationButton.addActionListener(mediaFilesValidationListeners.get(contentNature));
			mediaFilesValidations.put(contentNature, validationButton);
			
			JLabel mediaFilesStatus = new JLabel("Etat inconnu " + contentNature.getNom());
			mediaFilesStatuses.put(contentNature, mediaFilesStatus);
			
			JScrollPane statusScrollPane = new JScrollPane(mediaFilesStatus);	
			statusScrollPanels.put(contentNature, statusScrollPane);
			mediaFilePanel.add(statusScrollPane);
			mediaFilePanel.add(searchButton);
			mediaFilePanel.add(validationButton);
			
			mediaFilesSearchPane.add(mediaFilePanel);
		});
		setViewportView(mediaFilesSearchPane);
		
		updateValue(album);
	}
	
	private void updateValue(Album album) {
		
		Stream.of(CONTENT_NATURES)
			.forEachOrdered(contentNature -> 
				mediaFilesValidations.get(contentNature).setVisible(false));
		
		Stream.of(CONTENT_NATURES)
			.forEach(contentNature -> {
				JPanel contentPane = mediaFilesPanels.get(contentNature);
				if (!album.hasContentNature(contentNature)) {
					mediaFilesStatuses.get(contentNature).setText("Pas de contenu " + contentNature.getNom());
					contentPane.setBackground(Color.GRAY);
					statusScrollPanels.get(contentNature).getViewport().setBackground(Color.GRAY);
					mediaFilesSearches.get(contentNature).setVisible(false);
				} else if (album.hasMediaFiles(contentNature)) {
					if (! MediaFilesInventories.getMediaFileInventory(contentNature).isConnected()) {
						mediaFilesStatuses.get(contentNature).setText("Répertoire des fichiers " + contentNature.getNom() + " non connecté");
						contentPane.setBackground(Color.RED);
						statusScrollPanels.get(contentNature).getViewport().setBackground(Color.RED);
					} else if (album.hasMediaFilePathNotFound(contentNature) ||
						album.hasMissingOrInvalidMediaFilePath(contentNature)) {
							List<Path> potentialMediaFilesPaths = album.getPotentialMediaFilesPaths(contentNature);
							if (potentialMediaFilesPaths == null) {
								mediaFilesStatuses.get(contentNature).setText("Chemin " + contentNature.getNom() + " manquant ou invalides");
								mediaFilesSearches.get(contentNature).setVisible(true);
								contentPane.setBackground(Color.RED);
								statusScrollPanels.get(contentNature).getViewport().setBackground(Color.RED);
						
							} else if (potentialMediaFilesPaths.isEmpty()) {
								mediaFilesStatuses.get(contentNature).setText("Aucun chemin " + contentNature.getNom() + " potentiel trouvé");
								mediaFilesSearches.get(contentNature).setVisible(false);
								setBackground(Color.ORANGE);
								statusScrollPanels.get(contentNature).getViewport().setBackground(Color.ORANGE);
							} else {
								mediaFilesStatuses.get(contentNature).setText(potentialMediaFilesList(potentialMediaFilesPaths, contentNature));
								mediaFilesSearches.get(contentNature).setVisible(false);
								contentPane.setBackground(Color.PINK);
								statusScrollPanels.get(contentNature).getViewport().setBackground(Color.PINK);
								
								if ((potentialMediaFilesPaths.size() == 1) &&
										album.hasMediaFiles(contentNature) &&
										(album.getFormatAlbum().getMediaFiles(contentNature).size() == 1)) {
									// Media file paths can be validated if and only if there is a single potential link
									// and a single media file defined in the album
									mediaFilesValidations.get(contentNature).setVisible(true);
								}
							}
						} else {
							statusScrollPanels.get(contentNature).getViewport().setBackground(Color.GREEN);
							contentPane.setBackground(Color.GREEN);
							mediaFilesStatuses.get(contentNature).setText("Chemin " + contentNature.getNom() + " trouvé");
							contentPane.remove(mediaFilesSearches.get(contentNature));
						}
				} else {
					mediaFilesStatuses.get(contentNature).setText("Pas de fichier " + contentNature.getNom());
					contentPane.add(mediaFilesSearches.get(contentNature));
					contentPane.setBackground(Color.MAGENTA);
					statusScrollPanels.get(contentNature).getViewport().setBackground(Color.MAGENTA);
					contentPane.remove(mediaFilesSearches.get(contentNature));
				}
			});			
	}
	
	private String potentialMediaFilesList(List<Path> potentialMediaFilesPaths, ContentNature contentNature) {

		StringBuilder htmlString = new StringBuilder();
		htmlString
			.append("<html><body>Chemin potentiels des fichiers ")
			.append(contentNature.getNom())
			.append(":<br/>")
			.append(potentialMediaFilesPaths.stream().map(path -> path.toString()).collect(Collectors.joining("<br/>")))
			.append("</body></html>");
		
		return htmlString.toString();
	}
	
	private class MediaFilesSearchListener implements ActionListener {

		private final Album album;
		private final ContentNature contentNature;		
		public MediaFilesSearchListener(Album album, ContentNature contentNature) {
			this.album = album;
			this.contentNature = contentNature;
		}

		@Override
		public void actionPerformed(ActionEvent e) {			
			album.searchPotentialMediaFilesPaths(contentNature);
			updateValue(album);			
		}
	}
	
	private class MediaFileValidationListener implements ActionListener {

		private final Album album;
		private final ContentNature contentNature;
		private final GenerationPane generationPane;
		
		public MediaFileValidationListener(Album album, ContentNature contentNature, GenerationPane generationPane) {
			this.album = album;
			this.contentNature = contentNature;
			this.generationPane = generationPane;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean success = album.validatePotentialMediaFilePath(contentNature);
			if (success) {
				// Write json into file
				album.writeJson();
				updateValue(album);
				generationPane.rescanNeeded();
			}	
		}
	}
}
