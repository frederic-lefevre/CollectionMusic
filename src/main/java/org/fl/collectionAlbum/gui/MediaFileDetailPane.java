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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fl.collectionAlbum.mediaFile.MediaFile;
import org.fl.collectionAlbum.mediaFile.metadata.MediaFileMetadata;
import org.fl.collectionAlbum.mediaFile.metadata.MetadataElement;

public class MediaFileDetailPane extends JScrollPane {

	private static final long serialVersionUID = 1L;

	private static final Font verdana = new Font("Verdana", Font.BOLD, 14);
	private static final Dimension metadataWindowDimension = new Dimension(1200, 900);
	
	public MediaFileDetailPane(MediaFile mediaFile) {
		
		super();
		setPreferredSize(metadataWindowDimension);
		JPanel infosPane = new JPanel();
		infosPane.setLayout(new BoxLayout(infosPane, BoxLayout.Y_AXIS));
		infosPane.setAlignmentX(LEFT_ALIGNMENT);

		JPanel generalInfoPane = new JPanel();
		generalInfoPane.setLayout(new GridBagLayout());
		
		GridBagConstraints gridConstraints = new GridBagConstraints();
		gridConstraints.insets =  new Insets(3, 3, 3, 3);
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 0;
		gridConstraints.anchor = GridBagConstraints.LINE_START;
		
		descriptionValuePanel(generalInfoPane, gridConstraints, "Chemin du fichier", mediaFile.getFilePath().toString());
		gridConstraints.gridx = 0;
		gridConstraints.gridy++;
		descriptionValuePanel(generalInfoPane, gridConstraints, "Taille du fichier", mediaFile.getSize().map(v -> Long.toString(v)).orElse("Inconnue"));
		gridConstraints.gridx = 0;
		gridConstraints.gridy++;
		descriptionValuePanel(generalInfoPane, gridConstraints, "Fichier media valide", mediaFile.isValidMediaFile().map(v -> Boolean.toString(v)).orElse("Inconnu"));
		gridConstraints.gridx = 0;
		gridConstraints.gridy++;
		descriptionValuePanel(generalInfoPane, gridConstraints, "Image incluse dans le fichier", mediaFile.hasImbeddedPicture().map(v -> Boolean.toString(v)).orElse("Inconnu"));
		
		infosPane.add(generalInfoPane);
		
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
		metadataPane.setLayout(new GridBagLayout());
		metadataPane.setAlignmentX(RIGHT_ALIGNMENT);
		
		metadataPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

		GridBagConstraints gridConstraints = new GridBagConstraints();
		gridConstraints.insets =  new Insets(3, 10, 3, 20);
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 0;
		gridConstraints.gridheight = 1;
		gridConstraints.gridwidth = 3;
		gridConstraints.anchor = GridBagConstraints.LINE_START;
		
		JLabel titreMetadata = new JLabel(titre);
		titreMetadata.setFont(verdana);
		
		if ((metadataMap == null) ||  metadataMap.isEmpty()) {
			titreMetadata.setText("Pas de " + titre);
			metadataPane.add(titreMetadata, gridConstraints);
		} else {
			metadataPane.add(titreMetadata, gridConstraints);
			metadataMap.values().stream()
					.forEachOrdered(m -> {
						gridConstraints.gridx = 0;
						gridConstraints.gridy++;
						descriptionValuePanel(metadataPane, gridConstraints, m.name(), m.value().toString());
					});
		}
		return metadataPane;
	}
	
	private static void descriptionValuePanel(JPanel pane, GridBagConstraints gridConstraints, String description, String value) {
	
		gridConstraints.gridheight = 1;
		JLabel titreLbl = new JLabel(description + ": ");
		titreLbl.setFont(verdana);
		
		gridConstraints.gridwidth = 1;
		gridConstraints.weightx =0;
		pane.add(titreLbl, gridConstraints);
		JLabel valueLbl = new JLabel(value);
		valueLbl.setFont(verdana);
		gridConstraints.gridx++;
		gridConstraints.gridwidth = 2;
		gridConstraints.weightx =1;
		pane.add(valueLbl, gridConstraints);
	}
}
