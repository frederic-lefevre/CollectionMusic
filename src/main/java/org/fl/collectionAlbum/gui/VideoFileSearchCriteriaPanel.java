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
import org.fl.collectionAlbum.mediaFile.MediaFile;

public class VideoFileSearchCriteriaPanel extends MediaFilesSearchCriteriaPanel {

	private static final long serialVersionUID = 1L;
	
	private final JTextField fileNameSearchedText;
	
	public VideoFileSearchCriteriaPanel(ContentNature contentNature, MediaFileTableModel mediaFileTableModel) {
		super(contentNature, mediaFileTableModel);
		
		fileNameSearchedText = new JTextField();
		
		fillPanel();
	}

	@Override
	protected void addSearchField() {
		add(createTextFieldSearchPanel("Nom de fichier incluant les caractères", fileNameSearchedText));
	}

	@Override
	protected List<Predicate<MediaFile>> getMediaFilePredicateList() {
		
		List<Predicate<MediaFile>> mediaFilePredicateList = new ArrayList<>();
		String fileNameValue = getLowerCaseTextFieldValue(fileNameSearchedText);
		if (fileNameValue != null) {
			mediaFilePredicateList.add((m) -> m.getFileName().toString().toLowerCase().contains(fileNameValue));
		}
		
		return mediaFilePredicateList;
	}
}
