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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.gui.table.AlbumsScrollJTablePane;
import org.fl.collectionAlbum.gui.table.AlbumsTableModel;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class AlbumsSearchPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Font buttonFont = new Font("Verdana", Font.BOLD, 14);
	
	private static final Dimension SEARCH_LABEL_DIMENSION = new Dimension(400, 80);
	private static final Dimension SEARCH_TEXT_DIMENSION = new Dimension(400, 30);
	
	private final CollectionAlbumContainer collectionAlbumContainer;
	private final DateRangeChooser dateEnregistrement;
	private final DateRangeChooser dateComposition;
	private final List<Album> searchResultAlbums;
	private final AlbumsTableModel albumsTableModel;
	
	private final JTextField titreAlbumSearchedText;
	private final JTextField auteursAlbumSearchedText;
	
	private final LocalDate albumOldestRecordingDate;
	private final LocalDate albumMostRecentRecordingDate;
	private final LocalDate albumOldestCompositionDate;
	private final LocalDate albumMostRecentCompositionDate;
	
	public AlbumsSearchPanel(GenerationPane generationPane, CollectionAlbumContainer collectionAlbumContainer) {
		super();
		
		this.collectionAlbumContainer = collectionAlbumContainer;
		searchResultAlbums = new ArrayList<>();
		
		this.albumOldestRecordingDate = TemporalUtils.getRoundedLocalDate(collectionAlbumContainer.getAlbumOldestRecordingDate());
		this.albumMostRecentRecordingDate = TemporalUtils.getRoundedLocalDate(collectionAlbumContainer.getAlbumMostRecentRecordingDate());
		this.albumOldestCompositionDate = TemporalUtils.getRoundedLocalDate(collectionAlbumContainer.getAlbumOldestCompositionDate());
		this.albumMostRecentCompositionDate = TemporalUtils.getRoundedLocalDate(collectionAlbumContainer.getAlbumMostRecentCompositionDate());
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel searchCriteriaPanel = new JPanel();
		searchCriteriaPanel.setLayout(new BoxLayout(searchCriteriaPanel, BoxLayout.X_AXIS));
		
		JPanel titreAlbumSearchPanel = new JPanel();
		titreAlbumSearchPanel.setLayout(new BoxLayout(titreAlbumSearchPanel, BoxLayout.Y_AXIS));
		
		JLabel titreAlbumSearchLabel = new JLabel("Titre incluant les caractères");
		titreAlbumSearchLabel.setPreferredSize(SEARCH_LABEL_DIMENSION);
		titreAlbumSearchedText = new JTextField();
		titreAlbumSearchedText.setPreferredSize(SEARCH_TEXT_DIMENSION);
		
		titreAlbumSearchPanel.add(titreAlbumSearchLabel);
		titreAlbumSearchPanel.add(titreAlbumSearchedText);
		titreAlbumSearchPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		searchCriteriaPanel.add(titreAlbumSearchPanel);
		
		JPanel auteursAlbumSearchPanel = new JPanel();
		auteursAlbumSearchPanel.setLayout(new BoxLayout(auteursAlbumSearchPanel, BoxLayout.Y_AXIS));
		
		JLabel auteursAlbumSearchLabel = new JLabel("Auteurs incluant les caractères");
		auteursAlbumSearchLabel.setPreferredSize(SEARCH_LABEL_DIMENSION);
		auteursAlbumSearchedText = new JTextField();
		auteursAlbumSearchedText.setPreferredSize(SEARCH_TEXT_DIMENSION);
		
		auteursAlbumSearchPanel.add(auteursAlbumSearchLabel);
		auteursAlbumSearchPanel.add(auteursAlbumSearchedText);
		auteursAlbumSearchPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		searchCriteriaPanel.add(auteursAlbumSearchPanel);
		
		dateEnregistrement = new DateRangeChooser("Dates d'enregistrement", albumOldestRecordingDate, albumMostRecentRecordingDate);
		dateEnregistrement.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		dateComposition = new DateRangeChooser("Dates de composition", albumOldestCompositionDate, albumMostRecentCompositionDate);
		dateComposition.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		searchCriteriaPanel.add(dateEnregistrement);
		searchCriteriaPanel.add(dateComposition);
		
		JButton albumsSearchButton = new JButton("Rechercher");
		
		searchCriteriaPanel.add(albumsSearchButton);
		
		albumsSearchButton.setFont(buttonFont);
		albumsSearchButton.setBackground(Color.GREEN);
		
		add(searchCriteriaPanel);
		
		// Table to display the result albums
		AlbumsScrollJTablePane albumsScrollJTablePane = new AlbumsScrollJTablePane(searchResultAlbums, generationPane);
		albumsTableModel = albumsScrollJTablePane.getAlbumsTableModel();
		
		add(albumsScrollJTablePane);
		
		albumsSearchButton.addActionListener(new AlbumSearchListener());
		titreAlbumSearchedText.addActionListener(new AlbumTextFieldListener());
		auteursAlbumSearchedText.addActionListener(new AlbumTextFieldListener());
	}
	
	private class AlbumSearchListener implements java.awt.event.ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			ListeAlbum listeAlbumsFound = collectionAlbumContainer.getAlbumsSastisfying(getAlbumsPredicates());
			
			searchResultAlbums.clear();
			searchResultAlbums.addAll(listeAlbumsFound.getAlbums());
			albumsTableModel.fireTableDataChanged();
		}
		
		private List<Predicate<Album>> getAlbumsPredicates() {
			
			String titreSearchTextEntry = titreAlbumSearchedText.getText();
			String auteursTextEntry = auteursAlbumSearchedText.getText();
			
			LocalDate minRecordingDate = dateEnregistrement.getMinChoosenDate();
			LocalDate maxRecordingDate = dateEnregistrement.getMaxChoosenDate();
			
			LocalDate minCompositionDate = dateComposition.getMinChoosenDate();
			LocalDate maxCompositionDate = dateComposition.getMaxChoosenDate();
			
			List<Predicate<Album>> predicates = new ArrayList<>();
			
			if ((titreSearchTextEntry != null) && !titreSearchTextEntry.isBlank()) {
				String titreSearchText = titreSearchTextEntry.strip().toLowerCase();
				predicates.add(album -> album.getTitre().toLowerCase().contains(titreSearchText));
			}

			if ((auteursTextEntry != null) && !auteursTextEntry.isBlank()) {
				String auteursText = auteursTextEntry.strip().toLowerCase();
				predicates.add(album -> album.getAuteurs().stream().anyMatch(artiste -> artiste.getNomComplet().toLowerCase().contains(auteursText)));
			}
			
			if ((minRecordingDate != null) && (maxRecordingDate != null) && 
					(minCompositionDate != null) && (maxCompositionDate != null)) {
				if (TemporalUtils.compareTemporal(minRecordingDate, albumOldestRecordingDate) > 0) {
					predicates.add(album -> (TemporalUtils.compareTemporal(minRecordingDate, album.getDebutEnregistrement()) <= 0));
				}
				if (TemporalUtils.compareTemporal(maxRecordingDate, albumMostRecentRecordingDate) < 0) {
					predicates.add(album -> (TemporalUtils.compareTemporal(maxRecordingDate, album.getFinEnregistrement()) >= 0));
				}
				if (TemporalUtils.compareTemporal(minCompositionDate, albumOldestCompositionDate) > 0) {
					predicates.add(album -> (TemporalUtils.compareTemporal(minCompositionDate, album.getDebutComposition()) <= 0));
				}
				if (TemporalUtils.compareTemporal(maxCompositionDate, albumMostRecentCompositionDate) < 0) {
					predicates.add(album -> (TemporalUtils.compareTemporal(maxCompositionDate, album.getFinComposition()) >= 0));
				}
			} else {
				predicates.add(album -> false);
			}
			
			return predicates;
		}
	}
	
	private class AlbumTextFieldListener implements java.awt.event.ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// just to capture the ENTER char and avoid the closing of the JOptionPane containing this panel
		}	
	}
}
