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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.gui.DateRangeChooser;
import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class AlbumsSearchListener implements java.awt.event.ActionListener {

	private static final Font buttonFont = new Font("Verdana", Font.BOLD, 14);
	
	private final GenerationPane generationPane;
	private final CollectionAlbumContainer collectionAlbumContainer;
	
	public AlbumsSearchListener(GenerationPane generationPane, CollectionAlbumContainer collectionAlbumContainer) {
		super();
		this.generationPane = generationPane;
		this.collectionAlbumContainer = collectionAlbumContainer;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
		
		JPanel searchCriteriaPanel = new JPanel();
		searchCriteriaPanel.setLayout(new BoxLayout(searchCriteriaPanel, BoxLayout.X_AXIS));
		
		DateRangeChooser dateEnregistrement = new DateRangeChooser(
				"Dates d'enregistrement", 
				TemporalUtils.getRoundedLocalDate(collectionAlbumContainer.getAlbumOldestRecordingDate()), 
				LocalDate.now());
		searchCriteriaPanel.add(dateEnregistrement);
		
		DateRangeChooser dateComposition = new DateRangeChooser("Dates de composition", LocalDate.now(), LocalDate.now());
		searchCriteriaPanel.add(dateComposition);
		
		JButton albumsSearchButton = new JButton("Rechercher");
		
		searchCriteriaPanel.add(albumsSearchButton);
		
		albumsSearchButton.setFont(buttonFont);
		albumsSearchButton.setBackground(Color.GREEN);
		
		mainPane.add(searchCriteriaPanel);
		
		JScrollPane albumsScrollTable = new JScrollPane();
		albumsScrollTable.setPreferredSize(new Dimension(1800, 800));
		
		mainPane.add(albumsScrollTable);
		
		JOptionPane.showMessageDialog(null, mainPane, "Recherche d'albums", JOptionPane.INFORMATION_MESSAGE);
	}

}
