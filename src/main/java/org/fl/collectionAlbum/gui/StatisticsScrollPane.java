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

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fl.collectionAlbum.stat.StatChrono;
import org.fl.collectionAlbum.stat.StatistiquesView;

public class StatisticsScrollPane extends JScrollPane implements UpdatableElement {

	private static final long serialVersionUID = 1L;
	
	private final StatChrono statChrono;
	private final JPanel statistiquesTablePanel;
	
	public StatisticsScrollPane(StatChrono statChrono) {
		super();
		this.statChrono = statChrono;
		this.statistiquesTablePanel = new JPanel();
		setViewportView(statistiquesTablePanel);
		
		fillPanel();
	}

	private void fillPanel() {
		
		statistiquesTablePanel.removeAll();
		StatistiquesView statistiquesView = new StatistiquesView(statChrono, 200);
		int pas = statistiquesView.getPas();
		
		int columnNumber = 12;
		int rowNumber = statistiquesView.getLineNumber() + 1;
		
		statistiquesTablePanel.setLayout(new GridLayout(rowNumber,columnNumber));
		
		statistiquesTablePanel.add(new JLabel(statistiquesView.getSubdivisionName()));
		statistiquesTablePanel.add(new JLabel("Total"));
		for (int i = 0; i < 10; i++) {
			statistiquesTablePanel.add(new JLabel(Integer.toString(i*pas)));
		}
		
		for (int i = 0; i < rowNumber -1; i++) {
			int subdivisionYear = statistiquesView.getYearForLine(i);
			statistiquesTablePanel.add(new JLabel(Integer.toString(subdivisionYear)));
			statistiquesTablePanel.add(new JLabel(statistiquesView.getAccumulationStatFor(subdivisionYear)));
			for (int j = 0; j < 10; j++) {
				statistiquesTablePanel.add(new JLabel(statistiquesView.getStatFor(subdivisionYear + j*pas)));
			}
		}
	}

	@Override
	public void updateElement() {
		fillPanel();
	}
}
