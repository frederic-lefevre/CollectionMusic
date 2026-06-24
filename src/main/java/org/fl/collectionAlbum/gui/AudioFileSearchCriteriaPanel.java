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

	public AudioFileSearchCriteriaPanel(ContentNature contentNature, MediaFileTableModel mediaFileTableModel) {
		super(contentNature, mediaFileTableModel);
		
		trackTitleSearchedText = new JTextField();
		artistSearchedText = new JTextField();
		albumSearchedText = new JTextField();
		
		fillPanel();
	}

	@Override
	protected void addSearchField() {
		add(createTextFieldSearchPanel("Titre incluant les caractères", trackTitleSearchedText));
		add(createTextFieldSearchPanel("Artiste incluant les caractères", artistSearchedText));
		add(createTextFieldSearchPanel("Album incluant les caractères", albumSearchedText));	
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
					((AudioFile)m).getAudioMetadata().getNormalizedAudioMetadataTags().albumArtist().value().toLowerCase().contains(trackArtistValue));
		}
		
		String trackAlbumValue = getLowerCaseTextFieldValue(albumSearchedText);
		if (trackAlbumValue != null) {
			mediaFilePredicateList.add(
					(m) -> ((AudioFile)m).getAudioMetadata().getNormalizedAudioMetadataTags().albumTitle().value().toLowerCase().contains(trackAlbumValue));
		}
		return mediaFilePredicateList;
	}
}
