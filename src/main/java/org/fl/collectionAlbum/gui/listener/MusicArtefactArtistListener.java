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

package org.fl.collectionAlbum.gui.listener;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.gui.ArtisteInformationPanel;
import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.gui.table.MusicArtefactTable;

public class MusicArtefactArtistListener<T extends MusicArtefact> implements java.awt.event.ActionListener {

	private final MusicArtefactTable<T> musicArtefactTable;
	private final GenerationPane generationPane;
	
	public MusicArtefactArtistListener(MusicArtefactTable<T> musicArtefactTable, GenerationPane generationPane) {
		this.musicArtefactTable = musicArtefactTable;
		this.generationPane = generationPane;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		T selectedMusicArtefact = musicArtefactTable.getSelectedMusicArtefact();
		
		if (selectedMusicArtefact != null) {
			List<Artiste> artistes = selectedMusicArtefact.getAllArtists();
			if (! artistes.isEmpty()) {
				if (artistes.size() == 1) {
					Artiste selectedArtiste = artistes.get(0);
					JOptionPane.showMessageDialog(null, new ArtisteInformationPanel(selectedArtiste, generationPane), selectedArtiste.getNomComplet(), JOptionPane.PLAIN_MESSAGE);
				}
			}
		}
		
	}

}
