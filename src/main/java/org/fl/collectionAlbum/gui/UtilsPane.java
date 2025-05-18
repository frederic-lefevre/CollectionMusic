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

package org.fl.collectionAlbum.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.gui.listener.OsActionListener;
import org.fl.collectionAlbum.gui.listener.RandomAlbumsPickListener;
import org.fl.collectionAlbum.rapportHtml.RapportStructuresAndNames;

public class UtilsPane extends JPanel implements ActivableElement {

	private static final long serialVersionUID = 1L;
	
	private final JButton pickRandomAlbumsButton;
	private final JComboBox<Integer> numberOfAlbumBox;
	private final JComboBox<String> methodOfChoiceBox;
	
	private static final int DEFAULT_NUMBER_OF_RANDOM_ALBUMS = 5;
	public static final String EQUI_REPARTI_PAR_ARTISTE = "artiste";
	public static final String EQUI_REPARTI_PAR_ALBUM = "album";
	
	private static final Integer[] numberOfAlbumsChoice = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	private static final String[] albumChoiceMethod = {EQUI_REPARTI_PAR_ARTISTE, EQUI_REPARTI_PAR_ALBUM};
	
	public UtilsPane(GenerationPane generationPane) {
		
		super();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createMatteBorder(10,0,10,0,Color.BLACK));
		
		Font buttonFont = new Font("Verdana", Font.BOLD, 14);
		Font textFont = new Font("Verdana", Font.BOLD, 12);
		
		JButton showCollectionButton = new JButton("Montrer la collection");
		
		showCollectionButton.setFont(buttonFont);
		showCollectionButton.setBackground(Color.GREEN);
		showCollectionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		add(showCollectionButton);
		
		OsActionListener<List<String>> showCollectionListener = 
				new OsActionListener<>(List.of(RapportStructuresAndNames.getAbsoluteHomeCollectionUrl()), Control.getDisplayUrlAction());
		
		showCollectionButton.addActionListener(showCollectionListener);

		JPanel choixAleatoirePane = new JPanel();
		choixAleatoirePane.setLayout(new BoxLayout(choixAleatoirePane, BoxLayout.Y_AXIS));
		choixAleatoirePane.setBorder(new EmptyBorder(60, 0, 20, 40));
		
		JPanel choixNombreAlbumPane = new JPanel();
		choixNombreAlbumPane.setLayout(new BoxLayout(choixNombreAlbumPane, BoxLayout.X_AXIS));
		choixNombreAlbumPane.setBorder(new EmptyBorder(0, 5, 5, 5));
		
		JLabel titreAlbumsAleatoire = new JLabel("Choisir");
		titreAlbumsAleatoire.setFont(textFont);
		titreAlbumsAleatoire.setBorder(new EmptyBorder(0, 0, 0, 5));
		choixNombreAlbumPane.add(titreAlbumsAleatoire);
		
		numberOfAlbumBox = new JComboBox<>(numberOfAlbumsChoice);
		numberOfAlbumBox.setSelectedIndex(DEFAULT_NUMBER_OF_RANDOM_ALBUMS-1);	
		choixNombreAlbumPane.add(numberOfAlbumBox);
		
		choixAleatoirePane.add(choixNombreAlbumPane);
		
		pickRandomAlbumsButton = new JButton("Albums aléatoires");
		pickRandomAlbumsButton.setFont(buttonFont);
		pickRandomAlbumsButton.setBackground(Color.GREEN);
		pickRandomAlbumsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		choixAleatoirePane.add(pickRandomAlbumsButton);
		
		JPanel choixMethodPane = new JPanel();
		choixMethodPane.setLayout(new BoxLayout(choixMethodPane, BoxLayout.X_AXIS));
		choixMethodPane.setBorder(new EmptyBorder(5, 5, 5, 0));
		
		JLabel methodeAleatoire = new JLabel("équi-réparti par");
		methodeAleatoire.setFont(textFont);
		methodeAleatoire.setBorder(new EmptyBorder(0, 0, 0, 5));
		methodOfChoiceBox  = new JComboBox<>(albumChoiceMethod);
		methodOfChoiceBox.setSelectedItem(EQUI_REPARTI_PAR_ARTISTE);
		
		choixMethodPane.add(methodeAleatoire);
		choixMethodPane.add(methodOfChoiceBox);
		
		choixAleatoirePane.add(choixMethodPane);
		
		RandomAlbumsPickListener pickRandomAlbumsListener = new RandomAlbumsPickListener(this, generationPane);
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
	}

	@Override
	public void deactivate() {
		pickRandomAlbumsButton.setEnabled(false);	
	}
}
