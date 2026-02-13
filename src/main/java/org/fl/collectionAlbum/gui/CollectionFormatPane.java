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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.format.MediaSupportCategories;
import org.fl.collectionAlbum.format.MediaSupports;

public class CollectionFormatPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private static EnumMap<MediaSupportCategories, Set<MediaSupports>> supportCategoriesMap;
	
	static {
		
		supportCategoriesMap = new EnumMap<>(MediaSupportCategories.class);
		
		for (MediaSupportCategories mediaSupportCategory : MediaSupportCategories.values()) {
			supportCategoriesMap.put(mediaSupportCategory, new HashSet<>());
		}
		
		for (MediaSupports mediaSupport : MediaSupports.values()) {
			supportCategoriesMap.get(mediaSupport.getSupportPhysique()).add(mediaSupport);
		}
	}
	
	public CollectionFormatPane(Format format) {
		super();
		
		GridBagLayout layout = new GridBagLayout();
		
		setLayout(layout);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		
		MediaSupports[] mediaSupports = MediaSupports.values();
		for (int colIdx = 0; colIdx < mediaSupports.length; colIdx++) {
			
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = colIdx;
			constraints.gridy = 0;
			constraints.gridheight = 1;
			constraints.gridwidth = 1;
			
			JLabel lbl = new JLabel(mediaSupports[colIdx].toString(), SwingConstants.CENTER);
			lbl.setPreferredSize(new Dimension(120,30));
			lbl.setBorder(BorderFactory.createLineBorder(getForeground()));
			lbl.setOpaque(true);
			lbl.setBackground(Color.LIGHT_GRAY);
			layout.setConstraints(lbl, constraints);
			add(lbl);

			constraints.gridy = 1;
			
			JLabel lbl2 = new JLabel(Format.poidsToString(format.getNb(mediaSupports[colIdx])), SwingConstants.CENTER);
			lbl2.setPreferredSize(new Dimension(120,30));
			lbl2.setBorder(BorderFactory.createLineBorder(getForeground()));
			layout.setConstraints(lbl2, constraints);
			add(lbl2);
		}
		
		MediaSupportCategories[] mediaSupportCategories = MediaSupportCategories.values();
		int colIdx = 0;
		for (int mediaIdx = 0; mediaIdx < mediaSupportCategories.length; mediaIdx++) {
			
			MediaSupportCategories mediaSupportCategory = mediaSupportCategories[mediaIdx];
			int numberMediaSupport = supportCategoriesMap.get(mediaSupportCategory).size();
			
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = colIdx;
			constraints.gridy = 2;
			constraints.gridheight = 1;
			constraints.gridwidth = numberMediaSupport;
			
			JLabel lbl = new JLabel(mediaSupportCategory.toString(), SwingConstants.CENTER);
			lbl.setPreferredSize(new Dimension(120*numberMediaSupport,30));
			lbl.setBorder(BorderFactory.createLineBorder(getForeground()));
			lbl.setOpaque(true);
			lbl.setBackground(Color.LIGHT_GRAY);
			layout.setConstraints(lbl, constraints);
			add(lbl);

			constraints.gridy =3;
			
			JLabel lbl2 = new JLabel(Format.poidsToString(format.getSupportsPhysiquesNumbers().get(mediaSupportCategory)), SwingConstants.CENTER);
			lbl2.setPreferredSize(new Dimension(120*numberMediaSupport,30));
			lbl2.setBorder(BorderFactory.createLineBorder(getForeground()));
			layout.setConstraints(lbl2, constraints);
			add(lbl2);
			
			colIdx = colIdx + numberMediaSupport;
		}
	}
}
