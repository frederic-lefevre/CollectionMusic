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

package org.fl.collectionAlbum.gui.adapter;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.gui.ArtisteInformationPanel;
import org.fl.collectionAlbum.gui.ArtistesJTable;

public class ArtisteMouseAdapter extends MouseAdapter {

	private final JPopupMenu localJPopupMenu;
	private final JMenuItem showArtisteInfo;
	
	public ArtisteMouseAdapter(ArtistesJTable artistesJTable) {
		
		localJPopupMenu = new JPopupMenu();
		showArtisteInfo = new JMenuItem("Informations sur l'artiste");
		showArtisteInfo.addActionListener(new ArtisteActionListener(artistesJTable));
		localJPopupMenu.add(showArtisteInfo);
	}
	
	@Override
	public void mousePressed(MouseEvent evt) {
		if (evt.isPopupTrigger()) {
			localJPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		if (evt.isPopupTrigger()) {
			localJPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}
	
	private static class ArtisteActionListener implements java.awt.event.ActionListener {

		private final ArtistesJTable artistesJTable;
		
		public ArtisteActionListener(ArtistesJTable artistesJTable) {
			this.artistesJTable = artistesJTable;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			Artiste selectedArtiste = artistesJTable.getSelectedArtiste();
			
			JOptionPane.showMessageDialog(null, new ArtisteInformationPanel(selectedArtiste), selectedArtiste.getNomComplet(), JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
}
