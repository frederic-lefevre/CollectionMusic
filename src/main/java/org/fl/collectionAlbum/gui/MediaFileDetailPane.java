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
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.mediaFile.MediaFile;
import org.fl.collectionAlbum.mediaFile.metadata.MediaFileMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.MetadataElement;

public class MediaFileDetailPane extends JScrollPane {

	private static final long serialVersionUID = 1L;

	private static final Font verdana = new Font("Verdana", Font.BOLD, 14);
	
	public MediaFileDetailPane(MediaFile mediaFile) {
		
		super();
		setPreferredSize(Control.getInfoWindowDimension());
		JPanel infosPane = new JPanel();
		infosPane.setLayout(new BoxLayout(infosPane, BoxLayout.Y_AXIS));

		infosPane.add(descriptionValuePanel("Chemin du fichier", mediaFile.getFilePath().toString()));
		infosPane.add(descriptionValuePanel("Taille du fichier", mediaFile.getSize().map(v -> Long.toString(v)).orElse("Inconnue")));
		infosPane.add(descriptionValuePanel("Fichier media valide", mediaFile.isValidMediaFile().map(v -> Boolean.toString(v)).orElse("Inconnu")));
		infosPane.add(descriptionValuePanel("Image incluse dans le fichier", mediaFile.hasImbeddedPicture().map(v -> Boolean.toString(v)).orElse("Inconnu")));
		
		MediaFileMetadata metadata = mediaFile.getMetadata();
		if (metadata != null) {
			
			infosPane.add(metadataPanel("Metadata du flux", metadata.getStreamMetadata()));
			infosPane.add(metadataPanel("Metadata normalisées du contenu", metadata.getNormalizedTags()));
			infosPane.add(metadataPanel("Metadata supplémentaires du contenu", metadata.getAdditionalTags()));
			infosPane.add(metadataPanel("Metadata spécifiques au format media", metadata.getFormatSpecificMetadata()));
		}
		
		add(infosPane);
		setViewportView(infosPane);
	}
	
	private static JPanel metadataPanel(String titre, Map<String, MetadataElement<?>> metadataMap) {

		JPanel metadataPane = new JPanel();
		metadataPane.setLayout(new BoxLayout(metadataPane, BoxLayout.Y_AXIS));
		metadataPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

		JLabel titreMetadata = new JLabel(titre);
		metadataPane.add(titreMetadata);

		if ((metadataMap == null) ||  metadataMap.isEmpty()) {
			metadataPane.add(new JLabel("Pas de metadata" + titre));
		} else {
			metadataMap.values().stream()
					.forEach(m -> metadataPane.add(descriptionValuePanel(m.name(), m.value().toString())));
		}
		return metadataPane;
	}
	
	private static JPanel descriptionValuePanel(String description, String value) {
		
		JPanel dvPane = new JPanel();
		dvPane.setLayout(new GridLayout(1, 2));
		dvPane.setFont(verdana);	
		JLabel titreLbl = new JLabel(description + ": ");
		titreLbl.setFont(verdana);
		dvPane.add(titreLbl);
		JLabel valueLbl = new JLabel(value);
		valueLbl.setFont(verdana);
		dvPane.add(valueLbl);
		
		return dvPane;
	}
}
