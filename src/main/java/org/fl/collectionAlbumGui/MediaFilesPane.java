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
import org.fl.collectionAlbumGui.listener.MediaFileValidationListener;
import org.fl.collectionAlbumGui.listener.MediaFilesSearchListener;

public class MediaFilesPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private Map<ContentNature, JLabel> mediaFilesStatuses;
	private Map<ContentNature, JButton> mediaFilesSearches;
	private Map<ContentNature, JButton> mediaFilesValidations;
	private Map<ContentNature,JScrollPane> statusScrollPanels;
	private Map<ContentNature,JPanel> mediaFilesPanels;
	
	private static final int SINGLE_ROW_HEIGHT = 25;
	
	public MediaFilesPane(
			Map<ContentNature, MediaFilesSearchListener>  mediaFilesSearchListeners, 
			Map<ContentNature, MediaFileValidationListener> mediaFilesValidationListeners) {
		
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		mediaFilesStatuses = new HashMap<>();
		mediaFilesSearches = new HashMap<>();
		mediaFilesValidations = new HashMap<>();
		statusScrollPanels = new HashMap<>();
		mediaFilesPanels = new HashMap<>();
		
		
		// Search and validation media files button
		Stream.of(ContentNature.values()).forEachOrdered(contentNature -> {
			
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
			
			add(mediaFilePanel);
		});
	}

	public int updateValue(Album album) {
		
		Stream.of(ContentNature.values())
			.forEachOrdered(contentNature -> 
				mediaFilesPanels.get(contentNature).remove(mediaFilesValidations.get(contentNature)));
		
		return Stream.of(ContentNature.values())
			.map(contentNature -> {
				JPanel contentPane = mediaFilesPanels.get(contentNature);
				if (!album.hasContentNature(contentNature)) {
					mediaFilesStatuses.get(contentNature).setText("Pas de contenu " + contentNature.getNom());
					contentPane.setBackground(Color.GRAY);
					statusScrollPanels.get(contentNature).getViewport().setBackground(Color.GRAY);
					contentPane.remove(mediaFilesSearches.get(contentNature));
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
								contentPane.add(mediaFilesSearches.get(contentNature));
								contentPane.setBackground(Color.RED);
								statusScrollPanels.get(contentNature).getViewport().setBackground(Color.RED);
						
							} else if (potentialMediaFilesPaths.isEmpty()) {
								mediaFilesStatuses.get(contentNature).setText("Aucun chemin " + contentNature.getNom() + " potentiel trouvé");
								contentPane.remove(mediaFilesSearches.get(contentNature));
								setBackground(Color.ORANGE);
								statusScrollPanels.get(contentNature).getViewport().setBackground(Color.ORANGE);
							} else {
								mediaFilesStatuses.get(contentNature).setText(potentialMediaFilesList(potentialMediaFilesPaths, contentNature));
								contentPane.remove(mediaFilesSearches.get(contentNature));
								contentPane.setBackground(Color.PINK);
								statusScrollPanels.get(contentNature).getViewport().setBackground(Color.PINK);
								
								if ((potentialMediaFilesPaths.size() == 1) &&
										album.hasMediaFiles(contentNature) &&
										(album.getFormatAlbum().getMediaFiles(contentNature).size() == 1)) {
									// Media file paths can be validated if and only if there is a single potential link
									// and a single media file defined in the album
									contentPane.add(mediaFilesValidations.get(contentNature));
								}
								return (potentialMediaFilesPaths.size() + 2)*SINGLE_ROW_HEIGHT;
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
				return SINGLE_ROW_HEIGHT;
			})
			.mapToInt(Integer::valueOf)
		    .sum();
		
		
	}
	
	private String potentialMediaFilesList(List<Path> potentialMediaFilesPaths, ContentNature contentNature) {

		StringBuilder htmlString = new StringBuilder("<html><body>Chemin potentiels des fichiers " + contentNature.getNom() + ":<br/>");

		htmlString
			.append(potentialMediaFilesPaths.stream().map(path -> path.toString()).collect(Collectors.joining("<br/>")))
			.append("</body></html>");
		
		return htmlString.toString();
	}
}
