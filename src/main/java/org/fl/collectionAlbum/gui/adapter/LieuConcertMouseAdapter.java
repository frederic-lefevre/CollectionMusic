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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.fl.collectionAlbum.concerts.LieuConcert;
import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.gui.table.ConcertsScrollJTablePane;
import org.fl.collectionAlbum.gui.table.LieuConcertJTable;

public class LieuConcertMouseAdapter extends MouseAdapter {

	private final LieuConcertJTable lieuConcertJTable;
	private final GenerationPane generationPane;
	
	public LieuConcertMouseAdapter(LieuConcertJTable lieuConcertJTable, GenerationPane generationPane) {
		super();
		this.lieuConcertJTable = lieuConcertJTable;
		this.generationPane = generationPane;
	}
	
	@Override
	public void mouseClicked(MouseEvent evt) {
		
		if (SwingUtilities.isLeftMouseButton(evt) && (evt.getClickCount() > 1)) {
			
			LieuConcert lieuConcert = lieuConcertJTable.getSelectedLieuConcert();
			if (lieuConcert != null) {
				
				JOptionPane.showMessageDialog(null,
						new ConcertsScrollJTablePane(lieuConcert.getListeConcert().sortChrono().getConcerts(), generationPane), 
						"Concerts à " + lieuConcert.getLieu(), 
						JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
}
