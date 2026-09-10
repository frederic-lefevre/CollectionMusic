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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.disocgs.DiscogsInterface;

public class ApplicationToolsPane extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final Font verdana = new Font("Verdana", Font.BOLD, 16);
	private static final Font monospaced = new Font("monospaced", Font.BOLD, 14);
	
	private static final Dimension RELEASE_ID_TEXT_DIMENSION = new Dimension(100, 25);
	
	private static final String OPTION_LABEL = "Options";
	private static final String DISCOGS_LABEL = "Réponses Discogs brutes";
	private static final String SCAN_METADATA_LABEL = "Lire les meta-données des fichiers media ";
	private static final String YES_TITLE = "Oui";
	private static final String NO_TITLE = "Non";
	
	private final JToggleButton scanMediaMetadataButton;
	private final JTextField releaseIdField;
	private final JEditorPane resultPane;
	
	public ApplicationToolsPane() {
		super();
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JPanel commandPanel = new JPanel();
		commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.Y_AXIS));
		
		JLabel optionTitle = new JLabel(OPTION_LABEL);
		optionTitle.setFont(verdana);
		optionTitle.setBackground(Color.WHITE);
		optionTitle.setOpaque(true);
		optionTitle.setBorder(new CompoundBorder(new MatteBorder(4, 0, 2, 0, Color.BLACK), new EmptyBorder(10, 50, 15, 50)));
		commandPanel.add(optionTitle);
		
		JPanel scanMediaOptionPanel = new JPanel();
		scanMediaOptionPanel.setLayout(new BoxLayout(scanMediaOptionPanel, BoxLayout.X_AXIS));
		scanMediaOptionPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
		
		JLabel scanMediaMetadataLabel = new JLabel(SCAN_METADATA_LABEL);
		scanMediaMetadataButton = new JToggleButton(NO_TITLE);
		scanMediaMetadataButton.setSelected(Control.isReadMediaFileMetadata());
		setButtonAppearence(scanMediaMetadataButton);
		
		scanMediaMetadataButton.addItemListener(new ReadMetadataOptionListener());
		scanMediaOptionPanel.add(scanMediaMetadataLabel);
		scanMediaOptionPanel.add(scanMediaMetadataButton);
		
		commandPanel.add(scanMediaOptionPanel);
		
		JLabel discogsRawResponseLabel = new JLabel(DISCOGS_LABEL);
		discogsRawResponseLabel.setFont(verdana);
		discogsRawResponseLabel.setBackground(Color.WHITE);
		discogsRawResponseLabel.setOpaque(true);
		discogsRawResponseLabel.setBorder(new CompoundBorder(new MatteBorder(4, 0, 2, 0, Color.BLACK), new EmptyBorder(10, 50, 15, 50)));
		commandPanel.add(discogsRawResponseLabel);
		
		JPanel releaseGetPane = new JPanel();
		
		releaseIdField = new JTextField();
		releaseIdField.setPreferredSize(RELEASE_ID_TEXT_DIMENSION);
		releaseGetPane.add(releaseIdField);
		
		JButton releaseGetButton = new JButton("Release");
		releaseGetButton.addActionListener(new ReleaseGetListener());
		releaseGetPane.add(releaseGetButton);
		
		commandPanel.add(releaseGetPane);
		
		add(commandPanel);
		
		resultPane = new JEditorPane();
		resultPane.setEditable(false);
		resultPane.setFont(monospaced);
		
		add(new JScrollPane(resultPane));
	}

	private void setButtonAppearence(JToggleButton toogleButton) {
		
		if (toogleButton.isSelected()) {
			toogleButton.setText(YES_TITLE);
		} else {
			toogleButton.setText(NO_TITLE);
			toogleButton.setBackground(Color.ORANGE);
		}
	}
	
	private class ReadMetadataOptionListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			
			Control.setReadMediaFileMetadata(scanMediaMetadataButton.isSelected());
			setButtonAppearence(scanMediaMetadataButton);	
		}	
	}
	
	private class ReleaseGetListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String releaseId = releaseIdField.getText();
			resultPane.setText(DiscogsInterface.rawRelease(releaseId));
			
		}
	}
}
