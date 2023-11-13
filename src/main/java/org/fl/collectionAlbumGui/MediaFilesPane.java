/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fl.collectionAlbum.albums.Album;

public class MediaFilesPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel mediaFilesStatus;
	private JButton mediaFilesSearch;
	private JButton mediaFileValidation;
	private JScrollPane statusScrollPane;
	
	private static final int SINGLE_ROW_HEIGHT = 20;
	
	public MediaFilesPane(MediaFilesSearchListener mediaFilesSearchListener, MediaFileValidationListener mediaFilesValidationListener) {
		
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		mediaFilesSearch = new JButton("Chercher");
		mediaFilesSearch.addActionListener(mediaFilesSearchListener);
		
		mediaFileValidation = new JButton("Valider");
		mediaFileValidation.addActionListener(mediaFilesValidationListener);
		
		mediaFilesStatus = new JLabel("Etat inconnu");
		statusScrollPane = new JScrollPane(mediaFilesStatus);
		add(statusScrollPane);
	}

	public int updateValue(Album album) {
		
		remove(mediaFileValidation);
		if (album.hasMediaFiles()) {
			if (album.hasMediaFilePathNotFound() ||
				album.hasMissingOrInvalidMediaFilePath()) {
				List<Path> potentialAudioFilesPaths = album.getPotentialAudioFilesPaths();
				if (potentialAudioFilesPaths == null) {
					mediaFilesStatus.setText("Manquant ou invalides");
					add(mediaFilesSearch);
					setBackground(Color.RED);
					statusScrollPane.getViewport().setBackground(Color.RED);
			
				} else if (potentialAudioFilesPaths.isEmpty()) {
					mediaFilesStatus.setText("Aucun chemin potentiel trouvé");
					remove(mediaFilesSearch);
					setBackground(Color.ORANGE);
					statusScrollPane.getViewport().setBackground(Color.ORANGE);
				} else {
					mediaFilesStatus.setText(potentialMediaFilesList(potentialAudioFilesPaths));
					remove(mediaFilesSearch);
					setBackground(Color.PINK);
					statusScrollPane.getViewport().setBackground(Color.PINK);
					
					if ((potentialAudioFilesPaths.size() == 1) &&
							album.hasAudioFiles() &&
							(album.getFormatAlbum().getAudioFiles().size() == 1)) {
						// Media file paths can be validated if and only if there is a single potential link
						// and a single media file defined in the album
						add(mediaFileValidation);
					}
					return (potentialAudioFilesPaths.size() + 2)*SINGLE_ROW_HEIGHT;
				}
			} else {
				statusScrollPane.getViewport().setBackground(Color.GREEN);
				setBackground(Color.GREEN);
				mediaFilesStatus.setText("Trouvé");
				remove(mediaFilesSearch);
			}
		} else {
			mediaFilesStatus.setText("Pas de fichier media");
			add(mediaFilesSearch);
			setBackground(Color.MAGENTA);
			statusScrollPane.getViewport().setBackground(Color.MAGENTA);
		}
		return SINGLE_ROW_HEIGHT;
	}
	
	private String potentialMediaFilesList(List<Path> potentialAudioFilesPaths) {

		StringBuilder htmlString = new StringBuilder("<html><body>Chemin potentiels des fichiers media:<br/>");

		htmlString
			.append(potentialAudioFilesPaths.stream().map(path -> path.toString()).collect(Collectors.joining("<br/>")))
			.append("</body></html>");
		
		return htmlString.toString();
	}
}
