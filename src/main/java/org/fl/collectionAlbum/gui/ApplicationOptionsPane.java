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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.fl.collectionAlbum.Control;

public class ApplicationOptionsPane extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final String SCAN_METADATA_LABEL = "Lire les meta-données des fichiers media ";
	private static final String YES_TITLE = "Oui";
	private static final String NO_TITLE = "Non";
	
	JToggleButton scanMediaMetadataButton;
	
	public ApplicationOptionsPane() {
		super();
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JLabel scanMediaMetadataLabel = new JLabel(SCAN_METADATA_LABEL);
		scanMediaMetadataButton = new JToggleButton(NO_TITLE);
		scanMediaMetadataButton.setSelected(Control.isReadMediaFileMetadata());
		setButtonAppearence(scanMediaMetadataButton);
		
		scanMediaMetadataButton.addItemListener(new ReadMetadataOptionListener());
		add(scanMediaMetadataLabel);
		add(scanMediaMetadataButton);
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
}
