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
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.table.MediaFileTableModel;
import org.fl.collectionAlbum.mediaFile.AudioFile;
import org.fl.collectionAlbum.mediaFile.MediaFile;
import org.fl.collectionAlbum.mediaPath.MediaFileInventory;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;

public class MediaFilesSearchCriteriaPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Font buttonFont = new Font("Verdana", Font.BOLD, 14);
	
	private final ContentNature contentNature;
	private final MediaFileTableModel mediaFileTableModel;
	
	private final List<MediaFile> searchedMediaFileList;
	
	private final JTextField trackTitleSearchedText;
	private final JTextField artistSearchedText;
	private final JTextField albumSearchedText;
	
	private final JTextField fileNameSearchedText;
	
	public MediaFilesSearchCriteriaPanel(ContentNature contentNature, MediaFileTableModel mediaFileTableModel) {
		super();
		
		this.contentNature = contentNature;
		this.mediaFileTableModel = mediaFileTableModel;
		searchedMediaFileList = mediaFileTableModel.getMediaFileList();
		
		trackTitleSearchedText = new JTextField();
		artistSearchedText = new JTextField();
		albumSearchedText = new JTextField();
		
		fileNameSearchedText = new JTextField();
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		if (contentNature == ContentNature.AUDIO) {
		
			add(createTextFieldSearchPanel("Titre incluant les caractères", trackTitleSearchedText));
			add(createTextFieldSearchPanel("Artiste incluant les caractères", artistSearchedText));
			add(createTextFieldSearchPanel("Album incluant les caractères", albumSearchedText));

		} else {
			add(createTextFieldSearchPanel("Nom de fichier incluant les caractères", fileNameSearchedText));
		}
		
		JButton mediaFilesSearchButton = new JButton("Rechercher");
		mediaFilesSearchButton.setFont(buttonFont);
		mediaFilesSearchButton.setBackground(Color.GREEN);
		mediaFilesSearchButton.addActionListener(new MediaFilesSearchCriteriaListener());
		
		add(mediaFilesSearchButton);
	}
	
	private class MediaFilesSearchCriteriaListener implements java.awt.event.ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			List<Predicate<MediaFile>> mediaFilePredicateList = new ArrayList<>();
			
			if (contentNature == ContentNature.AUDIO) {
				
				String trackTitleValue = getLowerCaseTextFieldValue(trackTitleSearchedText);
				if (trackTitleValue != null) {
					mediaFilePredicateList.add(
							(m) -> ((AudioFile)m).getAudioMetadata().getNormalizedAudioMetadataTags().trackTitle().value().toLowerCase().contains(trackTitleValue));
				}
				
				String trackArtistValue = getLowerCaseTextFieldValue(artistSearchedText);
				if (trackArtistValue != null) {
					mediaFilePredicateList.add(
							(m) -> ((AudioFile)m).getAudioMetadata().getNormalizedAudioMetadataTags().artist().value().toLowerCase().contains(trackArtistValue) ||
							((AudioFile)m).getAudioMetadata().getNormalizedAudioMetadataTags().albumArtist().value().toLowerCase().contains(trackArtistValue));
				}
				
				String trackAlbumValue = getLowerCaseTextFieldValue(albumSearchedText);
				if (trackAlbumValue != null) {
					mediaFilePredicateList.add(
							(m) -> ((AudioFile)m).getAudioMetadata().getNormalizedAudioMetadataTags().albumTitle().value().toLowerCase().contains(trackAlbumValue));
				}
			} else {
				String fileNameValue = getLowerCaseTextFieldValue(fileNameSearchedText);
				if (fileNameValue != null) {
					mediaFilePredicateList.add((m) -> m.getFileName().toString().toLowerCase().contains(fileNameValue));
				}
			}
			
			MediaFileInventory mediaFileInventory = MediaFilesInventories.getMediaFileInventory(contentNature);
			List<MediaFile> mediaFileListFound = mediaFileInventory.getMediaFileListSastisfying(mediaFilePredicateList);
			searchedMediaFileList.clear();
			searchedMediaFileList.addAll(mediaFileListFound);
			mediaFileTableModel.fireTableDataChanged();
		}		
	}
	
	private static String getLowerCaseTextFieldValue(JTextField textField) {
		String value = textField.getText();
		if ((value != null) && !value.isBlank()) {
			return value.strip().toLowerCase();
		} else {
			return null;
		}
	}
	
	private static JPanel createTextFieldSearchPanel(String labelText, JTextField searchField) {
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
		JLabel searchLabel = new JLabel(labelText);
		searchPanel.add(searchLabel);
		searchPanel.add(searchField);
		return searchPanel;
	}
}
