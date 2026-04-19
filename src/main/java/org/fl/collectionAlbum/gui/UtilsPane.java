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

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.gui.listener.AlbumsSearchListener;
import org.fl.collectionAlbum.gui.listener.OsActionListener;
import org.fl.collectionAlbum.gui.listener.RandomAlbumsPickListener;
import org.fl.collectionAlbum.rapportHtml.RapportStructuresAndNames;

public class UtilsPane extends JPanel implements ActivableElement {

	private static final long serialVersionUID = 1L;
	
	private static final Font buttonFont = new Font("Verdana", Font.BOLD, 14);
	private static final Font textFont = new Font("Verdana", Font.BOLD, 12);
	
	private final JButton albumsSearchButton;
	
	private final JButton pickRandomAlbumsButton;
	private final JComboBox<Integer> numberOfAlbumBox;
	private final JComboBox<String> methodOfChoiceBox;
	
	private static final int DEFAULT_NUMBER_OF_RANDOM_ALBUMS = 10;
	public static final String EQUI_REPARTI_PAR_ARTISTE = "artiste";
	public static final String EQUI_REPARTI_PAR_ALBUM = "album";
	
	private static final Integer[] numberOfAlbumsChoice = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 16, 18, 20};
	private static final String[] albumChoiceMethod = {EQUI_REPARTI_PAR_ARTISTE, EQUI_REPARTI_PAR_ALBUM};
	
	public UtilsPane(GenerationPane generationPane, CollectionAlbumContainer collectionAlbumContainer) {
		
		super();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createMatteBorder(10,0,10,0,Color.BLACK));
		
		JPanel showCollectionPanel = new JPanel();
		
		JButton showCollectionButton = new JButton("Montrer la collection");
		
		showCollectionButton.setFont(buttonFont);
		showCollectionButton.setBackground(Color.GREEN);
		
		showCollectionPanel.add(showCollectionButton);
		showCollectionPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(2, 0, 4, 0)));
		add(showCollectionPanel);
		
		OsActionListener<List<String>> showCollectionListener = 
				new OsActionListener<>(List.of(RapportStructuresAndNames.getAbsoluteHomeCollectionUrl()), Control.getDisplayUrlAction());
		
		showCollectionButton.addActionListener(showCollectionListener);

		JPanel albumsSearchPanel = new JPanel();
		
		albumsSearchButton = new JButton("Rechercher des albums");
		albumsSearchButton.setFont(buttonFont);
		albumsSearchButton.setBackground(Color.GREEN);
		
		albumsSearchPanel.add(albumsSearchButton);
		albumsSearchPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(4, 0, 4, 0)));
		albumsSearchPanel.setBackground(Color.WHITE);
		add(albumsSearchPanel);
		
		JPanel choixAleatoirePane = new JPanel();
		choixAleatoirePane.setLayout(new BoxLayout(choixAleatoirePane, BoxLayout.Y_AXIS));
		choixAleatoirePane.setBorder(new EmptyBorder(4, 0, 4, 0));
		
		JPanel choixNombreAlbumPane = new JPanel();
		choixNombreAlbumPane.setLayout(new BoxLayout(choixNombreAlbumPane, BoxLayout.X_AXIS));
		choixNombreAlbumPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JLabel titreAlbumsAleatoire = new JLabel("Choisir");
		titreAlbumsAleatoire.setFont(textFont);
		titreAlbumsAleatoire.setBorder(new EmptyBorder(0, 0, 0, 5));
		choixNombreAlbumPane.add(titreAlbumsAleatoire);
		
		numberOfAlbumBox = new JComboBox<>(numberOfAlbumsChoice);
		numberOfAlbumBox.setSelectedIndex(DEFAULT_NUMBER_OF_RANDOM_ALBUMS-1);	
		choixNombreAlbumPane.add(numberOfAlbumBox);
		choixNombreAlbumPane.setBorder(new EmptyBorder(4, 5, 5, 100));
		
		choixAleatoirePane.add(choixNombreAlbumPane);
		
		JPanel pickRandomAlbumsPanel = new JPanel();
		pickRandomAlbumsButton = new JButton("Albums aléatoires");
		pickRandomAlbumsButton.setFont(buttonFont);
		pickRandomAlbumsButton.setBackground(Color.GREEN);
		pickRandomAlbumsPanel.add(pickRandomAlbumsButton);
		pickRandomAlbumsPanel.setBorder(new EmptyBorder(0, 0, 0, 80));
		
		albumsSearchButton.addActionListener(new AlbumsSearchListener(generationPane, collectionAlbumContainer));
		choixAleatoirePane.add(pickRandomAlbumsPanel);
		
		JPanel choixMethodPane = new JPanel();
		choixMethodPane.setLayout(new BoxLayout(choixMethodPane, BoxLayout.X_AXIS));
		choixMethodPane.setBorder(new EmptyBorder(4, 5, 5, 100));
		
		JLabel methodeAleatoire = new JLabel("équi-réparti par");
		methodeAleatoire.setFont(textFont);
		methodeAleatoire.setBorder(new EmptyBorder(0, 0, 0, 5));
		methodOfChoiceBox  = new JComboBox<>(albumChoiceMethod);
		methodOfChoiceBox.setSelectedItem(EQUI_REPARTI_PAR_ARTISTE);
		
		choixMethodPane.add(methodeAleatoire);
		choixMethodPane.add(methodOfChoiceBox);
		
		choixAleatoirePane.setBorder(new LineBorder(Color.BLACK));
		choixAleatoirePane.add(choixMethodPane);
		
		RandomAlbumsPickListener pickRandomAlbumsListener = new RandomAlbumsPickListener(this, generationPane, collectionAlbumContainer);
		pickRandomAlbumsButton.addActionListener(pickRandomAlbumsListener);
		
		add(choixAleatoirePane);
	}

	public int getNumberOfAlbums() {
		int idx = numberOfAlbumBox.getSelectedIndex();
		if ((idx >= 0) && (idx < numberOfAlbumsChoice.length)) {
			return numberOfAlbumsChoice[idx];
		} else {
			return DEFAULT_NUMBER_OF_RANDOM_ALBUMS;
		}
	}
	
	public String getMethodForRandomAlbum( ) {
		
		return Optional.ofNullable((String)methodOfChoiceBox.getSelectedItem())
				.map(choiceMethod -> {
					if (choiceMethod.equals(EQUI_REPARTI_PAR_ALBUM) || choiceMethod.equals(EQUI_REPARTI_PAR_ARTISTE)) {
						return choiceMethod;
					} else {
						return EQUI_REPARTI_PAR_ARTISTE;
					}
				})
				.orElse(EQUI_REPARTI_PAR_ARTISTE);
	}
	
	@Override
	public void activate() {
		pickRandomAlbumsButton.setEnabled(true);
		albumsSearchButton.setEnabled(true);
	}

	@Override
	public void deactivate() {
		pickRandomAlbumsButton.setEnabled(false);
		albumsSearchButton.setEnabled(false);
	}
}
