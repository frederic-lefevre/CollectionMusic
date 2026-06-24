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
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.table.MediaFileTableModel;
import org.fl.collectionAlbum.mediaFile.MediaFile;
import org.fl.collectionAlbum.mediaPath.MediaFileInventory;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;

public abstract class MediaFilesSearchCriteriaPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Font buttonFont = new Font("Verdana", Font.BOLD, 14);
	
	private final ContentNature contentNature;
	private final MediaFileTableModel mediaFileTableModel;	
	private final List<MediaFile> searchedMediaFileList;
	
	public MediaFilesSearchCriteriaPanel(ContentNature contentNature, MediaFileTableModel mediaFileTableModel) {
		super();
		
		this.contentNature = contentNature;
		this.mediaFileTableModel = mediaFileTableModel;
		searchedMediaFileList = mediaFileTableModel.getMediaFileList();
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}
	
	protected void fillPanel() {
		addSearchField();
		
		JButton mediaFilesSearchButton = new JButton("Rechercher");
		mediaFilesSearchButton.setFont(buttonFont);
		mediaFilesSearchButton.setBackground(Color.GREEN);
		mediaFilesSearchButton.addActionListener(new MediaFilesSearchCriteriaListener());
		
		add(mediaFilesSearchButton);
	}
	
	protected abstract void addSearchField();
	protected abstract List<Predicate<MediaFile>> getMediaFilePredicateList();
	
	private class MediaFilesSearchCriteriaListener implements java.awt.event.ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			MediaFileInventory mediaFileInventory = MediaFilesInventories.getMediaFileInventory(contentNature);
			List<MediaFile> mediaFileListFound = mediaFileInventory.getMediaFileListSastisfying(getMediaFilePredicateList());
			searchedMediaFileList.clear();
			searchedMediaFileList.addAll(mediaFileListFound);
			mediaFileTableModel.fireTableDataChanged();
		}		
	}
	
	protected static String getLowerCaseTextFieldValue(JTextField textField) {
		String value = textField.getText();
		if ((value != null) && !value.isBlank()) {
			return value.strip().toLowerCase();
		} else {
			return null;
		}
	}
	
	protected static JPanel createTextFieldSearchPanel(String labelText, JTextField searchField) {
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
		JLabel searchLabel = new JLabel(labelText);
		searchPanel.add(searchLabel);
		searchPanel.add(searchField);
		return searchPanel;
	}
	
	protected static Integer getIntegerFieldValue(JTextField textField) {
		return getNumberFieldValue(textField, Integer::parseInt);

	}
	
	protected static Long getLongFieldValue(JTextField textField) {
		return getNumberFieldValue(textField, Long::parseLong);
	}
	
	private static <T extends Number> T getNumberFieldValue(JTextField textField, Function<String, T> parseFunction) {
		String value = textField.getText();
		if ((value != null) && !value.isBlank()) {
			try {
				textField.setBackground(Color.WHITE);
				return parseFunction.apply(value.strip());
			} catch (NumberFormatException ex) {
				textField.setText("");
				textField.setBackground(Color.PINK);
				return null;
			}
		} else {
			return null;
		}
	}
}
