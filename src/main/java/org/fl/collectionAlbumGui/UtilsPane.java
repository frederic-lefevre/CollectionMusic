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

package org.fl.collectionAlbumGui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.rapportHtml.RapportStructuresAndNames;
import org.fl.collectionAlbumGui.listener.OsActionListener;
import org.fl.collectionAlbumGui.listener.RandomAlbumsPickListener;

public class UtilsPane extends JPanel implements ActivableElement {

	private static final long serialVersionUID = 1L;
	
	private final JButton pickRandomAlbumsButton;
	private final JComboBox<Integer> numberOfAlbumBox;
	
	private static final Integer[] numberOfAlbumsChoice = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	
	public UtilsPane(GenerationPane generationPane) {
		
		super();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createMatteBorder(10,0,10,0,Color.BLACK));
		
		Font font = new Font("Verdana", Font.BOLD, 14);
		
		JButton showCollectionButton = new JButton("Montrer la collection");
		
		showCollectionButton.setFont(font);
		showCollectionButton.setBackground(Color.GREEN);
		showCollectionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		add(showCollectionButton);
		
		OsActionListener<List<String>> showCollectionListener = 
				new OsActionListener<>(List.of(RapportStructuresAndNames.getAbsoluteHomeCollectionUrl()), Control.getDisplayUrlAction());
		
		showCollectionButton.addActionListener(showCollectionListener);
		
		JPanel choixAleatoirePane = new JPanel();
		choixAleatoirePane.setLayout(new BoxLayout(choixAleatoirePane, BoxLayout.X_AXIS));
		
		JLabel titreAlbumsAleatoire = new JLabel("Choisir");
		titreAlbumsAleatoire.setBorder(new EmptyBorder(0, 5, 0, 5));
		choixAleatoirePane.add(titreAlbumsAleatoire);
		
		numberOfAlbumBox = new JComboBox<>(numberOfAlbumsChoice);
		numberOfAlbumBox.setSelectedIndex(4);
		
		choixAleatoirePane.add(numberOfAlbumBox);
		
		add(choixAleatoirePane);
		
		pickRandomAlbumsButton = new JButton("Albums aléatoires");
		pickRandomAlbumsButton.setFont(font);
		pickRandomAlbumsButton.setBackground(Color.GREEN);
		pickRandomAlbumsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		add(pickRandomAlbumsButton);
		
		RandomAlbumsPickListener pickRandomAlbumsListener = new RandomAlbumsPickListener(this, generationPane);
		pickRandomAlbumsButton.addActionListener(pickRandomAlbumsListener);
	}

	public int getNumberOfAlbums() {
		int idx = numberOfAlbumBox.getSelectedIndex();
		return numberOfAlbumsChoice[idx];
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
