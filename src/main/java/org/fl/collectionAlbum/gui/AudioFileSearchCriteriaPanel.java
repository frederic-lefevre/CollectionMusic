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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.table.MediaFileTableModel;
import org.fl.collectionAlbum.mediaFile.AudioFile;
import org.fl.collectionAlbum.mediaFile.MediaFile;

public class AudioFileSearchCriteriaPanel extends MediaFilesSearchCriteriaPanel {

	private static final long serialVersionUID = 1L;
	
	private final JTextField trackTitleSearchedText;
	private final JTextField artistSearchedText;
	private final JTextField albumSearchedText;
	private final JTextField genreSearchedText;
	
	private final JTextField bitDepthSearchedText;
	private final JTextField sampleRateSearchedText;
	private final JTextField bitRateSearchedText;
	private final JTextField channelNumberSearchedText;

	public AudioFileSearchCriteriaPanel(ContentNature contentNature, MediaFileTableModel mediaFileTableModel) {
		super(contentNature, mediaFileTableModel);
		
		trackTitleSearchedText = new JTextField();
		artistSearchedText = new JTextField();
		albumSearchedText = new JTextField();
		genreSearchedText = new JTextField();
		
		bitDepthSearchedText = new JTextField(2);
		sampleRateSearchedText = new JTextField(7);
		bitRateSearchedText  = new JTextField(8);
		channelNumberSearchedText = new JTextField(2);
		
		fillPanel();
	}

	@Override
	protected void addSearchField() {
		
		JPanel searchCriteriaPane = new JPanel();
		searchCriteriaPane.setLayout(new BoxLayout(searchCriteriaPane, BoxLayout.Y_AXIS));
		
		JPanel normalizedTagsSearchPane = new JPanel();
		normalizedTagsSearchPane.setLayout(new BoxLayout(normalizedTagsSearchPane, BoxLayout.X_AXIS));	
		normalizedTagsSearchPane.add(createTextFieldSearchPanel("Titre incluant les caractères", trackTitleSearchedText));
		normalizedTagsSearchPane.add(createTextFieldSearchPanel("Artiste incluant les caractères", artistSearchedText));
		normalizedTagsSearchPane.add(createTextFieldSearchPanel("Album incluant les caractères", albumSearchedText));
		normalizedTagsSearchPane.add(createTextFieldSearchPanel("Genre musical incluant les caractères", genreSearchedText));
		searchCriteriaPane.add(normalizedTagsSearchPane);
		
		JPanel streamInfoSearchPane = new JPanel();
		streamInfoSearchPane.setLayout(new BoxLayout(streamInfoSearchPane, BoxLayout.X_AXIS));
		streamInfoSearchPane.add(createTextFieldSearchPanel("Bits par échantillon", bitDepthSearchedText));
		streamInfoSearchPane.add(createTextFieldSearchPanel("Echantillonnage (Hz)", sampleRateSearchedText));
		streamInfoSearchPane.add(createTextFieldSearchPanel("Débit (Bits/s)", bitRateSearchedText));
		streamInfoSearchPane.add(createTextFieldSearchPanel("Nombre de canaux", channelNumberSearchedText));
		
		searchCriteriaPane.add(streamInfoSearchPane);
		
		add(searchCriteriaPane);
	}

	@Override
	protected List<Predicate<MediaFile>> getMediaFilePredicateList() {
		
		List<Predicate<MediaFile>> mediaFilePredicateList = new ArrayList<>();
		String trackTitleValue = getLowerCaseTextFieldValue(trackTitleSearchedText);
		if (trackTitleValue != null) {
			mediaFilePredicateList.add(
					(m) -> ((AudioFile)m).getAudioMetadata().getNormalizedAudioMetadataTags().trackTitle().value().toLowerCase().contains(trackTitleValue));
		}
		
		String trackArtistValue = getLowerCaseTextFieldValue(artistSearchedText);
		if (trackArtistValue != null) {
			mediaFilePredicateList.add(
					(m) -> ((AudioFile)m).getAudioMetadata().getNormalizedAudioMetadataTags().artist().value().toLowerCase().contains(trackArtistValue) ||
					((AudioFile)m).getAudioMetadata().getNormalizedAudioMetadataTags().composer().value().toLowerCase().contains(trackArtistValue) ||
					((AudioFile)m).getAudioMetadata().getNormalizedAudioMetadataTags().albumArtist().value().toLowerCase().contains(trackArtistValue));
		}
		
		String trackAlbumValue = getLowerCaseTextFieldValue(albumSearchedText);
		if (trackAlbumValue != null) {
			mediaFilePredicateList.add(
					(m) -> ((AudioFile)m).getAudioMetadata().getNormalizedAudioMetadataTags().albumTitle().value().toLowerCase().contains(trackAlbumValue));
		}
		
		String genreAlbumValue = getLowerCaseTextFieldValue(genreSearchedText);
		if (genreAlbumValue != null) {
			mediaFilePredicateList.add(
					(m) -> ((AudioFile)m).getAudioMetadata().getNormalizedAudioMetadataTags().genre().value().toLowerCase().contains(genreAlbumValue));
		}
		
		Integer bitPerSampleValue = getIntegerFieldValue(bitDepthSearchedText);
		if (bitPerSampleValue != null) {
			mediaFilePredicateList.add(
					(m) -> ((AudioFile)m).getAudioMetadata().getAudioStreamMetadata().bitDepth().value().equals(bitPerSampleValue));
		}
		
		Long sampleRateValue = getLongFieldValue(sampleRateSearchedText);
		if (sampleRateValue != null) {
			mediaFilePredicateList.add(
					(m) -> ((AudioFile)m).getAudioMetadata().getAudioStreamMetadata().samplingRate().value().equals(sampleRateValue));
		}
		
		Long bitRateValue = getLongFieldValue(bitRateSearchedText);
		if (bitRateValue != null) {
			mediaFilePredicateList.add(
					(m) -> ((AudioFile)m).getAudioMetadata().getAudioStreamMetadata().bitRate().value().equals(bitRateValue));
		}
		
		Integer channelNumberSampleValue = getIntegerFieldValue(channelNumberSearchedText);
		if (channelNumberSampleValue != null) {
			mediaFilePredicateList.add(
					(m) -> ((AudioFile)m).getAudioMetadata().getAudioStreamMetadata().numberOfChannels().value().equals(channelNumberSampleValue));
		}
		return mediaFilePredicateList;
	}
}
