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

import java.awt.Font;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.gui.table.AlbumsScrollJTablePane;
import org.fl.collectionAlbum.gui.table.ConcertsScrollJTablePane;

public class ArtisteInformationPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Font fontNom = new Font("Verdana", Font.BOLD, 18);
	private static final Font fontDates = new Font("Verdana", Font.BOLD, 14);
	
	public ArtisteInformationPanel(Artiste artiste, GenerationPane generationPane) {
		
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel nomEtDatePanel = new JPanel();
		nomEtDatePanel.setLayout(new BoxLayout(nomEtDatePanel, BoxLayout.X_AXIS));
		nomEtDatePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
		
		JLabel nomLabel = new JLabel();
		nomLabel.setFont(fontNom);
		nomLabel.setText(artiste.getNomComplet());
		nomEtDatePanel.add(nomLabel);
		
		JLabel dateLabel = new JLabel();
		dateLabel.setFont(fontDates);
		dateLabel.setText(artiste.getDates());
		nomEtDatePanel.add(dateLabel);
		
		add(nomEtDatePanel);
		
		JScrollPane albumsScrollTable = getAlbumsScrollPane(artiste.getAlbums().getAlbums(), generationPane);
		JScrollPane concertScrollTable = getConcertsScrollPane(artiste.getConcerts().getConcerts());
		if (concertScrollTable == null) {
			add(albumsScrollTable);
		} else if (albumsScrollTable == null) {
			add(concertScrollTable);
		} else {
			JTabbedPane albumsEtConcertsTabs = new JTabbedPane();
			albumsEtConcertsTabs.add(albumsScrollTable, "Albums");
			albumsEtConcertsTabs.add(concertScrollTable, "Concerts");
			
			add(albumsEtConcertsTabs);
		}
	}
	
	private JScrollPane getAlbumsScrollPane(List<Album> albums, GenerationPane generationPane) {
		
		if (albums.isEmpty()) {
			return null;
		} else {
			return new AlbumsScrollJTablePane(albums, generationPane);
		}
	}
	
	private JScrollPane getConcertsScrollPane(List<Concert> concerts) {
		
		if (concerts.isEmpty()) {
			return null;
		} else {			
			return new ConcertsScrollJTablePane(concerts);
		}
	}
}
