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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Optional;
import java.util.function.Function;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.stat.StatChrono;
import org.fl.collectionAlbum.stat.StatistiquesView;
import org.fl.collectionAlbum.utils.CollectionUtils;
import org.fl.collectionAlbum.utils.JLabelBuilder;

public class StatisticsScrollPane extends JScrollPane implements UpdatableElement {

	private static final long serialVersionUID = 1L;
	
	private static final Dimension CELL_DIMENSION = new Dimension(80,30);
	private static final Dimension CURRENT_STAT_DIMENSION = new Dimension(350,250);
	
	private static final Font PERIOD_FONT = new Font("Verdana", Font.BOLD, 64);
	private static final Font STAT_FONT = new Font("Verdana", Font.BOLD, 64);
	
	private static final Function<Double, String> statToStringFunction = (d) ->  Optional.ofNullable(d).map(poids -> Format.poidsToString(poids)).orElse("0");
	
	private final StatChrono statChrono;
	private final JPanel statistiquesPanel;
	private final JPanel currentStatistiquePanel;
	private final JLabel currentPeriodLabel;
	private final JLabel currentStatLabel;
	private final JPanel statistiquesTablePanel;
	
	public StatisticsScrollPane(StatChrono statChrono) {
		
		super();
		this.statChrono = statChrono;
		this.statistiquesPanel =  new JPanel();
		statistiquesPanel.setLayout(new BoxLayout(statistiquesPanel, BoxLayout.X_AXIS));
		
		this.statistiquesTablePanel = new JPanel();
		
		statistiquesPanel.add(statistiquesTablePanel);
		
		currentStatistiquePanel = new JPanel();
		currentStatistiquePanel.setLayout(new BoxLayout(currentStatistiquePanel, BoxLayout.Y_AXIS));
		
		currentPeriodLabel = new JLabel();
		currentPeriodLabel.setFont(PERIOD_FONT);
		currentPeriodLabel.setPreferredSize(CURRENT_STAT_DIMENSION);
		currentPeriodLabel.setForeground(new Color(0xFF, 0xA0, 0xA0));
		
		currentStatLabel = new JLabel();
		currentStatLabel.setFont(STAT_FONT);
		currentStatLabel.setPreferredSize(CURRENT_STAT_DIMENSION);
		currentStatLabel.setForeground(new Color(0xA0, 0xA0, 0xFF));
		
		currentStatistiquePanel.add(currentPeriodLabel);
		currentStatistiquePanel.add(currentStatLabel);
		
		statistiquesPanel.add(currentStatistiquePanel);
		
		setViewportView(statistiquesPanel);
		
		fillPanel();
	}

	private void fillPanel() {
		
		statistiquesTablePanel.removeAll();
		StatistiquesView statistiquesView = new StatistiquesView(statChrono, 200, statToStringFunction);
		int pas = statistiquesView.getPas();
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		
		statistiquesTablePanel.setLayout(layout);
		
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		statistiquesTablePanel.add(CollectionUtils.createGridCellLabel(layout, constraints,
				JLabelBuilder.builder()
					.text(statistiquesView.getSubdivisionName())
					.preferredSize(CELL_DIMENSION)
					.backgroundColor(Color.BLACK)
					.foregroundColor(Color.WHITE)
				));
		
		statistiquesTablePanel.add(CollectionUtils.createGridCellLabel(layout, setGridx(constraints, 1),
				JLabelBuilder.builder()
					.text("Total")
					.preferredSize(CELL_DIMENSION)
					.backgroundColor(Color.BLACK)
					.foregroundColor(Color.WHITE)
				));
		
		for (int i = 0; i < 10; i++) {
			statistiquesTablePanel.add(CollectionUtils.createGridCellLabel(layout, setGridx(constraints, i+2), 
					JLabelBuilder.builder()
						.text(Integer.toString(i*pas))
						.preferredSize(CELL_DIMENSION)
						.backgroundColor(Color.BLACK)
						.foregroundColor(Color.WHITE)
					));
		}
		
		int rowNumber = statistiquesView.getLineNumber() + 1;
		for (int i = 0; i < rowNumber -1; i++) {
			
			constraints.gridy = i + 1;
			int subdivisionYear = statistiquesView.getYearForLine(i);
			
			statistiquesTablePanel.add(
					CollectionUtils.createGridCellLabel(layout, setGridx(constraints, 0), 
							JLabelBuilder.builder()
							.text(Integer.toString(subdivisionYear))
							.preferredSize(CELL_DIMENSION)
							.backgroundColor(Color.BLACK)
							.foregroundColor(Color.WHITE)
						));
			
			statistiquesTablePanel.add(
					CollectionUtils.createGridCellLabel(layout, setGridx(constraints, 1),
							JLabelBuilder.builder()
								.text(statistiquesView.getAccumulationStatFor(subdivisionYear))
								.preferredSize(CELL_DIMENSION)
								.backgroundColor(Color.LIGHT_GRAY)
							));
			
			for (int j = 0; j < 10; j++) {
				int year = subdivisionYear + j*pas;
				String period = Integer.toString(year);
				String poids = statistiquesView.getStatFor(year);
				statistiquesTablePanel.add(
						CollectionUtils.createGridCellLabel(layout, setGridx(constraints, j+2), 
								JLabelBuilder.builder()
									.text(poids)
									.preferredSize(CELL_DIMENSION)
									.mouseMotionListener(new CurrentStatMouseMotionAdapter(period, poids))
								));
			}
		}
	}

	private GridBagConstraints setGridx(GridBagConstraints constraints, int x) {
		constraints.gridx = x;
		return constraints;
	}
	
	@Override
	public void updateElement() {
		fillPanel();
	}
	
	private class CurrentStatMouseMotionAdapter extends MouseMotionAdapter {
		
		private final String period;
		private final String poids;
		
		public CurrentStatMouseMotionAdapter(String period, String poids) {
			super();
			this.period = period;
			this.poids = poids;
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			currentPeriodLabel.setText(period);
			currentStatLabel.setText(poids);
		}
	}
}
