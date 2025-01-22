/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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

package org.fl.collectionAlbumGui.listener;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbumGui.AlbumsJTable;
import org.fl.collectionAlbumGui.AlbumsTableModel;
import org.fl.collectionAlbumGui.GenerationPane;

public class RandomAlbumsPickListener implements java.awt.event.ActionListener {

	private final GenerationPane generationPane;
	
	public RandomAlbumsPickListener(GenerationPane generationPane) {
		this.generationPane = generationPane;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		List<Album> randomAlbums = CollectionAlbumContainer.getInstance().pickRandomAlbums(5);
		
		// Table to display the chosen albums
		AlbumsTableModel albumsTableModel = new AlbumsTableModel(randomAlbums);
		AlbumsJTable albumsJTable = new AlbumsJTable(albumsTableModel, generationPane);
		
		JScrollPane albumsScrollTable = new JScrollPane(albumsJTable);
		albumsScrollTable.setPreferredSize(new Dimension(1800,700));
		
		JOptionPane.showMessageDialog(null, albumsScrollTable, "Informations", JOptionPane.INFORMATION_MESSAGE);
	}
	

}
