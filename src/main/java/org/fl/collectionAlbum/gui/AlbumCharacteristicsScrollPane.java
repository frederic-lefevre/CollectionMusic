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
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.gui.listener.ListAlbumListener;

public class AlbumCharacteristicsScrollPane extends JScrollPane implements UpdatableElement  {

	private static final long serialVersionUID = 1L;

	private static final Font TITLE_FONT = new Font("Verdana", Font.BOLD, 18);
	private static final Font FONT = new Font("Verdana", Font.BOLD, 14);
	
	private static final String AUDIO_CHARACTERISTICS_TITLE = "Caractéristiques des numérisations des albums";
	
	private final CollectionAlbumContainer collectionAlbumContainer;
	private final GenerationPane generationPane;
	private final JPanel albumsCaracteristicsPanel;
	
	public AlbumCharacteristicsScrollPane(CollectionAlbumContainer collectionAlbumContainer, GenerationPane generationPane) {
		super();
		
		this.collectionAlbumContainer = collectionAlbumContainer;
		this.generationPane = generationPane;
		
		albumsCaracteristicsPanel = new JPanel();
		albumsCaracteristicsPanel.setLayout(new BoxLayout(albumsCaracteristicsPanel, BoxLayout.Y_AXIS));
		
		fillPanel();
		
		setViewportView(albumsCaracteristicsPanel);
	}
	
	private void fillPanel() {
		
		albumsCaracteristicsPanel.removeAll();
		JLabel albumsAudioCharacteristicsLabel = new JLabel(AUDIO_CHARACTERISTICS_TITLE);
		albumsAudioCharacteristicsLabel.setFont(TITLE_FONT);
		
		JPanel albumsAudioCharacteristicsPanel = new JPanel();
		albumsAudioCharacteristicsPanel.setLayout(new GridLayout(0, 2));
		
		ListeAlbum highResAudioAlbums = collectionAlbumContainer.getAlbumsWithHighResAudio().sortRangementAlbum();
		addCharacteristic("Albums avec fichier audio haute résolution", highResAudioAlbums, albumsAudioCharacteristicsPanel);
		ListeAlbum lowResAudioAlbums = collectionAlbumContainer.getAlbumsWithLowResAudio().sortRangementAlbum();
		addCharacteristic("Albums avec fichier audio avec perte (basse qualité)", lowResAudioAlbums, albumsAudioCharacteristicsPanel);
		
		albumsCaracteristicsPanel.add(albumsAudioCharacteristicsLabel);
		albumsCaracteristicsPanel.add(albumsAudioCharacteristicsPanel);
	}
	
	private void addCharacteristic(String characteristicTitle, ListeAlbum albums, JPanel panel) {
		JButton buttonTitle = new JButton(characteristicTitle);
		buttonTitle.setFont(FONT);
		buttonTitle.addActionListener(new ListAlbumListener(characteristicTitle, albums.getAlbums(), generationPane));
		panel.add(buttonTitle);
		JLabel valueLabel = new JLabel(Integer.toString(albums.getNombreAlbums()));
		valueLabel.setFont(FONT);
		panel.add(valueLabel);
	}
	
	@Override
	public void updateElement() {
		fillPanel();
		
	}

}
