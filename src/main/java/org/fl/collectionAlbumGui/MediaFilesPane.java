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

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fl.collectionAlbum.albums.Album;

public class MediaFilesPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel mediaFilesStatus;
	private JButton mediaFilesSearch;
	private JButton mediaFileValidation;
	
	private static final int SINGLE_ROW_HEIGHT = 30;
	
	public MediaFilesPane(MediaFilesSearchListener mediaFilesSearchListener, MediaFileValidationListener mediaFilesValidationListener) {
		
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		mediaFilesStatus = new JLabel("Etat inconnu");
		add(mediaFilesStatus);
		
		mediaFilesSearch = new JButton("Chercher");
		mediaFilesSearch.addActionListener(mediaFilesSearchListener);
		
		mediaFileValidation = new JButton("Valider");
		mediaFileValidation.addActionListener(mediaFilesValidationListener);
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
			
				} else if (potentialAudioFilesPaths.isEmpty()) {
					mediaFilesStatus.setText("Aucun chemin potentiel trouvé");
					remove(mediaFilesSearch);
				} else {
					mediaFilesStatus.setText(potentialMediaFilesList(potentialAudioFilesPaths));
					remove(mediaFilesSearch);

					if ((potentialAudioFilesPaths.size() == 1) &&
							album.hasAudioFiles() &&
							(album.getFormatAlbum().getAudioFiles().size() == 1)) {
						// Media file paths can be validated if and only if there is a single potential link
						// and a single media file defined in the album
						add(mediaFileValidation);
					}
					return (potentialAudioFilesPaths.size() + 1)*SINGLE_ROW_HEIGHT;
				}
			} else {
				mediaFilesStatus.setText("Trouvé");
				remove(mediaFilesSearch);
			}
		} else {
			mediaFilesStatus.setText("Pas de fichier media");
			add(mediaFilesSearch);
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
