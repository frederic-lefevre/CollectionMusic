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
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.concerts.Concert;

public class ArtisteInformationPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public ArtisteInformationPanel(Artiste artiste, GenerationPane generationPane) {
		
		List<Album> albums = artiste.getAlbums().getAlbums();
		List<Concert> concerts = artiste.getConcerts().getConcerts();
		
		JTabbedPane albumsEtConcertsTabs = new JTabbedPane();
		
		AlbumsTableModel albumsTableModel = new AlbumsTableModel(albums);
		ConcertTableModel concertTableModel = new ConcertTableModel(concerts);
		
		AlbumsJTable albumsJTable = new AlbumsJTable(albumsTableModel, generationPane);
		ConcertsJTable concertsJTable = new ConcertsJTable(concertTableModel);
		
		JScrollPane albumsScrollTable = new JScrollPane(albumsJTable);
		albumsScrollTable.setPreferredSize(new Dimension(1800, 800));
		
		JScrollPane concertScrollTable = new JScrollPane(concertsJTable);
		concertScrollTable.setPreferredSize(new Dimension(1800, 800));
		
		albumsEtConcertsTabs.add(albumsScrollTable, "Albums");
		albumsEtConcertsTabs.add(concertScrollTable, "Concerts");
		
		add(albumsEtConcertsTabs);
	}
}
