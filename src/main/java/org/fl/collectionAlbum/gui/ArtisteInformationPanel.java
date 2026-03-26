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

import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.gui.table.AbstractAlbumsTableModel;
import org.fl.collectionAlbum.gui.table.AlbumsScrollJTablePane;
import org.fl.collectionAlbum.gui.table.ConcertsScrollJTablePane;

public class ArtisteInformationPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Font fontNom = new Font("Verdana", Font.BOLD, 18);
	private static final Font fontDatesEtChiffres = new Font("Verdana", Font.BOLD, 14);
	
	private static final Dimension ARTIST_ALL_HEADERS_DIMENSION = new Dimension(1800, 40);
	private static final Dimension SCROLL_TABLE_DIMENSION = new Dimension(1820, 650);
	
	public ArtisteInformationPanel(Artiste artiste, GenerationPane generationPane) {
		
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel artistHeaderPanel = new JPanel();
		artistHeaderPanel.setLayout(new BoxLayout(artistHeaderPanel, BoxLayout.X_AXIS));
		artistHeaderPanel.setMaximumSize(ARTIST_ALL_HEADERS_DIMENSION);
		artistHeaderPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
		
		JLabel nomLabel = new JLabel(artiste.getNomComplet(), SwingConstants.LEFT);
		nomLabel.setFont(fontNom);
		artistHeaderPanel.add(nomLabel);
		
		JLabel dateLabel = new JLabel(artiste.getDates(), SwingConstants.LEFT);
		dateLabel.setFont(fontDatesEtChiffres);
		artistHeaderPanel.add(dateLabel);
		
		JLabel nbAlbumsLabel = new JLabel(getArtistFigures(artiste), SwingConstants.RIGHT);
		nbAlbumsLabel.setFont(fontDatesEtChiffres);
		nbAlbumsLabel.setMaximumSize(ARTIST_ALL_HEADERS_DIMENSION);
		artistHeaderPanel.add(nbAlbumsLabel);
		
		add(artistHeaderPanel);
		
		add(new CollectionFormatPane(artiste.getAlbumsFormat()));
		
		JScrollPane albumsScrollTable = getAlbumsScrollPane(artiste.getAlbums().getAlbums(), generationPane);
		JScrollPane concertScrollTable = getConcertsScrollPane(artiste.getConcerts().getConcerts(), generationPane);
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
	
	private String getArtistFigures(Artiste artiste) {
		
		int nbAlbums = artiste.getNbAlbum();
		int nbConcerts = artiste.getNbConcert();
		double poids = artiste.getAlbumsFormat().getPoids();
		StringBuilder chiffres = new StringBuilder();
		if (poids > 0) {
			chiffres.append("Poids total: ").append(Format.poidsToString(poids));
		}
		if (nbAlbums > 0) {
			chiffres.append("  -  Nombre d'albums: ").append(nbAlbums);
		}
		if (nbConcerts > 0) {
			if (chiffres.length() > 0) {
				chiffres.append("  -  ");
			}
			chiffres.append("Nombre de concerts: ").append(nbConcerts);
		}
		return chiffres.toString();
	}
	
	private JScrollPane getAlbumsScrollPane(List<Album> albums, GenerationPane generationPane) {
		
		if (albums.isEmpty()) {
			return null;
		} else {
			JScrollPane albumsScrollJTablePane = new AlbumsScrollJTablePane(albums, 
					AbstractAlbumsTableModel.REGULAR_COLUMN_LIST,
					generationPane);
			albumsScrollJTablePane.setPreferredSize(SCROLL_TABLE_DIMENSION);
			return albumsScrollJTablePane;
		}
	}
	
	private JScrollPane getConcertsScrollPane(List<Concert> concerts, GenerationPane generationPane) {
		
		if (concerts.isEmpty()) {
			return null;
		} else {
			JScrollPane concertScrollTable = new ConcertsScrollJTablePane(concerts, generationPane);
			concertScrollTable.setPreferredSize(SCROLL_TABLE_DIMENSION);
			return concertScrollTable;
		}
	}
}
