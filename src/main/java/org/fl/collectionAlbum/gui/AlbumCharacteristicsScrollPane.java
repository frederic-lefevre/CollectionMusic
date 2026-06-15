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
import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.listener.ListAlbumListener;

public class AlbumCharacteristicsScrollPane extends JScrollPane implements UpdatableElement  {

	private static final long serialVersionUID = 1L;

	private static final Font TITLE_FONT = new Font("Verdana", Font.BOLD, 18);
	private static final Font FONT = new Font("Verdana", Font.BOLD, 16);
	
	private static final String AUDIO_CHARACTERISTICS_TITLE = "Caractéristiques des numérisations des albums";
	private static final String CONTENT_NATURE_CHARACTERISTICS_TITLE = "Albums par nature de contenu";
	
	private static final Dimension AUDIO_CHARACTERISTICS_DIMENSION = new Dimension(800,500);
	private static final Dimension CONTENT_NATURE_DIMENSION = new Dimension(800,320);
	
	private final CollectionAlbumContainer collectionAlbumContainer;
	private final GenerationPane generationPane;
	private final JPanel albumsCaracteristicsPanel;
	
	public AlbumCharacteristicsScrollPane(CollectionAlbumContainer collectionAlbumContainer, GenerationPane generationPane) {
		super();
		
		this.collectionAlbumContainer = collectionAlbumContainer;
		this.generationPane = generationPane;
		
		albumsCaracteristicsPanel = new JPanel();
		albumsCaracteristicsPanel.setLayout(new BoxLayout(albumsCaracteristicsPanel, BoxLayout.X_AXIS));
		
		fillPanel();
		
		setViewportView(albumsCaracteristicsPanel);
	}
	
	private void fillPanel() {
		
		albumsCaracteristicsPanel.removeAll();
		
		JPanel albumsAudioPanel = new JPanel();
		albumsAudioPanel.setLayout(new BoxLayout(albumsAudioPanel, BoxLayout.Y_AXIS));
		albumsAudioPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED), 
				BorderFactory.createEmptyBorder(40, 30, 40, 30)));
		albumsAudioPanel.setPreferredSize(AUDIO_CHARACTERISTICS_DIMENSION);
		
		JPanel pLabel = new JPanel();
		JLabel albumsAudioCharacteristicsLabel = new JLabel(AUDIO_CHARACTERISTICS_TITLE);
		albumsAudioCharacteristicsLabel.setFont(TITLE_FONT);
		pLabel.add(albumsAudioCharacteristicsLabel);
		
		JPanel albumsAudioCharacteristicsPanel = new JPanel();
		albumsAudioCharacteristicsPanel.setLayout(new GridLayout(0, 2, 50, 60));
		
		ListeAlbum albumsWithAudio = collectionAlbumContainer.getAlbumsWithAudioFile().sortRangementAlbum();
		addCharacteristic("Albums avec fichiers audio", albumsWithAudio, albumsAudioCharacteristicsPanel);
		ListeAlbum highResAudioAlbums = collectionAlbumContainer.getAlbumsWithHighResAudio().sortRangementAlbum();
		addCharacteristic("Albums avec fichiers audio haute résolution", highResAudioAlbums, albumsAudioCharacteristicsPanel);
		ListeAlbum lowResAudioAlbums = collectionAlbumContainer.getAlbumsWithLowResAudio().sortRangementAlbum();
		addCharacteristic("Albums avec fichiers audio avec perte (basse qualité)", lowResAudioAlbums, albumsAudioCharacteristicsPanel);
		ListeAlbum missingAudioAlbums = collectionAlbumContainer.getAlbumsMissingAudioFile().sortRangementAlbum();
		addCharacteristic("Albums manquant de fichier audio", missingAudioAlbums, albumsAudioCharacteristicsPanel);
		ListeAlbum albumsWithVideoFile = collectionAlbumContainer.getAlbumsWithVideoFile().sortRangementAlbum();
		addCharacteristic("Albums avec fichiers vidéo", albumsWithVideoFile, albumsAudioCharacteristicsPanel);
		ListeAlbum albumsMissingVideoFile = collectionAlbumContainer.getAlbumsMissingVideoFile().sortRangementAlbum();
		addCharacteristic("Albums manquant de fichier vidéo", albumsMissingVideoFile, albumsAudioCharacteristicsPanel);
		
		albumsAudioPanel.add(pLabel);
		albumsAudioPanel.add(albumsAudioCharacteristicsPanel);
		
		JPanel albumContentNaturePanel = new JPanel();
		albumContentNaturePanel.setLayout(new BoxLayout(albumContentNaturePanel, BoxLayout.Y_AXIS));
		albumContentNaturePanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED), 
				BorderFactory.createEmptyBorder(40, 30, 40, 30)));
		albumContentNaturePanel.setPreferredSize(CONTENT_NATURE_DIMENSION);
		
		JPanel pLabel2 = new JPanel();
		JLabel albumsContentNatureLabel = new JLabel(CONTENT_NATURE_CHARACTERISTICS_TITLE);
		albumsContentNatureLabel.setFont(TITLE_FONT);
		pLabel2.add(albumsContentNatureLabel);
		
		JPanel albumsContentNatureCharacteristicsPanel = new JPanel();
		albumsContentNatureCharacteristicsPanel.setLayout(new GridLayout(0, 2, 50, 150));
		
		Arrays.stream(ContentNature.values()).forEachOrdered(contentNature -> {
			ListeAlbum albumsWithOnlyContentNature = collectionAlbumContainer.getAlbumsWithOnlyContentNature(contentNature).sortRangementAlbum();
			addCharacteristic("Albums avec seulement du contenu " + contentNature.getNom(), albumsWithOnlyContentNature, albumsContentNatureCharacteristicsPanel);
		});
		ListeAlbum albumsWithMixedContentNature = collectionAlbumContainer.getAlbumsWithMixedContentNature().sortRangementAlbum();
		addCharacteristic("Albums avec plusieurs types de contenus", albumsWithMixedContentNature, albumsContentNatureCharacteristicsPanel);
		
		albumContentNaturePanel.add(pLabel2);
		albumContentNaturePanel.add(albumsContentNatureCharacteristicsPanel);
		
		albumsCaracteristicsPanel.add(albumsAudioPanel);
		albumsCaracteristicsPanel.add(albumContentNaturePanel);
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
