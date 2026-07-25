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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.format.MediaSupportCategories;
import org.fl.collectionAlbum.format.MediaSupports;
import org.fl.collectionAlbum.utils.CollectionUtils;
import org.fl.collectionAlbum.utils.JLabelBuilder;

public class CollectionFormatPane extends JScrollPane {

	private static final long serialVersionUID = 1L;

	private static final int COLUMN_NUMBER = MediaSupports.values().length;
	private static final int ROW_NUMBER = 5;
	private static final int ROW_HEIGHT = 30;
	private static final int ROW_WIDTH = 120;
	
	private static final Dimension SIMPLE_CELL_DIMENSION = new Dimension(ROW_WIDTH,ROW_HEIGHT);
	private static final Dimension DOUBLE_CELL_DIMENSION = new Dimension(ROW_WIDTH,ROW_HEIGHT*2);
	private static final Dimension FORMAT_PANE_DIMENSION = new Dimension(COLUMN_NUMBER*ROW_WIDTH + 20,ROW_NUMBER*(ROW_HEIGHT + 2) + 20);
	
	private static final EnumMap<MediaSupportCategories, Set<MediaSupports>> supportCategoriesMap = new EnumMap<>(MediaSupportCategories.class);
	
	static {
		
		for (MediaSupportCategories mediaSupportCategory : MediaSupportCategories.values()) {
			supportCategoriesMap.put(mediaSupportCategory, new HashSet<>());
		}
		
		for (MediaSupports mediaSupport : MediaSupports.values()) {
			supportCategoriesMap.get(mediaSupport.getSupportPhysique()).add(mediaSupport);
		}
	}
	
	public CollectionFormatPane(Format format) {
		super();
		
		setPreferredSize(FORMAT_PANE_DIMENSION);
		
		JPanel formatPane = new JPanel();
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		
		formatPane.setLayout(layout);
		formatPane.setBackground(Color.WHITE);
		formatPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		
		MediaSupports[] mediaSupports = MediaSupports.values();
		for (int colIdx = 0; colIdx < mediaSupports.length; colIdx++) {
			
			constraints.gridx = colIdx;
			constraints.gridy = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 2;

			formatPane.add(CollectionUtils.createGridCellLabel(layout, constraints, 
					JLabelBuilder.builder()
						.text(CollectionUtils.getHtmlForString(mediaSupports[colIdx].getDescription()))
						.preferredSize(DOUBLE_CELL_DIMENSION)
						.backgroundColor(Color.LIGHT_GRAY)
					));

			constraints.gridy = 2;
			constraints.gridheight = 1;
			
			formatPane.add(CollectionUtils.createGridCellLabel(layout, constraints, 
					JLabelBuilder.builder()
						.text(Format.poidsToString(format.getNb(mediaSupports[colIdx])))
						.preferredSize(SIMPLE_CELL_DIMENSION)
					));
		}
		
		MediaSupportCategories[] mediaSupportCategories = MediaSupportCategories.values();
		int colIdx = 0;
		for (int mediaIdx = 0; mediaIdx < mediaSupportCategories.length; mediaIdx++) {
			
			MediaSupportCategories mediaSupportCategory = mediaSupportCategories[mediaIdx];
			
			constraints.gridx = colIdx;
			constraints.gridy = 3;
			constraints.gridwidth = supportCategoriesMap.get(mediaSupportCategory).size();
			Dimension cellDimension = new Dimension(120*constraints.gridwidth,30);

			formatPane.add(CollectionUtils.createGridCellLabel(layout, constraints, 
					JLabelBuilder.builder()
						.text(mediaSupportCategory.getDescription())
						.preferredSize(cellDimension)
						.backgroundColor(Color.LIGHT_GRAY)
					));

			constraints.gridy = 4;
			
			formatPane.add(CollectionUtils.createGridCellLabel(layout, constraints, 
					JLabelBuilder.builder()
						.text(Format.poidsToString(format.getSupportsPhysiquesNumbers().get(mediaSupportCategory)))
						.preferredSize(cellDimension)
					));
			
			colIdx = colIdx + constraints.gridwidth;
		}
		setViewportView(formatPane);
	}	
}
