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
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.fl.collectionAlbum.disocgs.DiscogsInventory.DiscogsAlbumRelease;

public class DiscogsReleaseCustomActionListener implements java.awt.event.ActionListener {

	private static final Logger aLog = Logger.getLogger(DiscogsReleaseCustomActionListener.class.getName());
	
	public enum CustomAction { SHOW_INFO };
	
	private final DiscogsReleaseJTable discogsReleaseJTable;
	private final CustomAction customAction;
	
	public DiscogsReleaseCustomActionListener(DiscogsReleaseJTable discogsReleaseJTable, CustomAction customAction) {
		
		this.discogsReleaseJTable = discogsReleaseJTable;
		this.customAction = customAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		DiscogsAlbumRelease release = discogsReleaseJTable.getSelectedDisocgsRelease();
		
		if (release != null) {
			
			switch (customAction) {
			
				case SHOW_INFO:
					
					// Show informations in popup message
					JTextArea infoRelease = new JTextArea(40, 200);
					infoRelease.setEditable(false);
					
					infoRelease.setText(release.getInfo());
					infoRelease.setFont(new Font("monospaced", Font.BOLD, 14));
					JScrollPane infoFilesScroll = new JScrollPane(infoRelease) ;
					JOptionPane.showMessageDialog(null, infoFilesScroll, "Informations", JOptionPane.INFORMATION_MESSAGE);
				
					break;
					
				default:
					aLog.severe("Unkown custom action triggered for discogs release: " + customAction);
			}
		}
	}

}
