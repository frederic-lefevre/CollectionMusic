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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Month;
import java.time.MonthDay;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.fl.collectionAlbum.ChronoArtistes;
import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.gui.table.ArtistesScrollJTablePane;
import org.fl.collectionAlbum.utils.CollectionUtils;
import org.fl.collectionAlbum.utils.JLabelBuilder;

public class MonthPane extends JPanel implements UpdatableElement {

	private static final long serialVersionUID = 1L;
	
	private static final int CELL_WIDTH = 80;
	private static final int CELL_HEIGHT = 80;
	private static final Dimension GRID_PANE_DIMENSION = new Dimension(CELL_WIDTH*7 + 20, CELL_HEIGHT*5 + 20);
	private static final Dimension CELL_DIMENSION = new Dimension(CELL_WIDTH, CELL_HEIGHT);
	private static final Font MONTH_FONT = new Font("Verdana", Font.BOLD, 24);
	private static final Font NO_ARTIST_FONT = new Font("Verdana", Font.ITALIC, 20);
	
	private final Month month;
	private final GenerationPane generationPane;
	private final ChronoArtistes calendrierAllArtistes;
	private final JPanel monthGridPane;
	private ArtistesScrollJTablePane artistesPane;
	private final List<Artiste> currentArtisteList;
	private boolean artistesPaneNotFixed;
	private final JLabel dayMonthLabel;
	private final String monthName;
	
	public MonthPane(Month month, CollectionAlbumContainer collectionAlbumContainer, GenerationPane generationPane) {
		super();

		this.month = month;
		this.generationPane = generationPane;
		this.calendrierAllArtistes = collectionAlbumContainer.getCalendrierAllArtistes();
		this.currentArtisteList = new ArrayList<>();
		artistesPaneNotFixed = true;
		monthName = month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.FRANCE);
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JPanel monthDisplayPane = new JPanel();
		monthDisplayPane.setLayout(new BoxLayout(monthDisplayPane, BoxLayout.Y_AXIS));
		monthDisplayPane.setAlignmentY(TOP_ALIGNMENT);
		
		JLabel monthNameLabel = new JLabel(monthName);
		monthNameLabel.setFont(MONTH_FONT);
		monthNameLabel.setAlignmentY(TOP_ALIGNMENT);
		monthNameLabel.setAlignmentX(CENTER_ALIGNMENT);
		monthDisplayPane.add(monthNameLabel);
		
		monthGridPane = new JPanel();
		fillMonthGridPane();

		monthDisplayPane.add(monthGridPane);
		add(monthDisplayPane);
		
		JPanel artistesOfTheDayPane = new JPanel();
		artistesOfTheDayPane.setLayout(new BoxLayout(artistesOfTheDayPane, BoxLayout.Y_AXIS));
		artistesOfTheDayPane.setAlignmentY(TOP_ALIGNMENT);
		
		dayMonthLabel =  new JLabel(month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.FRANCE));
		dayMonthLabel.setFont(MONTH_FONT);
		artistesOfTheDayPane.add(dayMonthLabel);
		
		artistesPane = new ArtistesScrollJTablePane(currentArtisteList, generationPane, false);
		artistesPane.setAlignmentY(TOP_ALIGNMENT);
		
		artistesOfTheDayPane.add(artistesPane);
		add(artistesOfTheDayPane);
	}

	private void fillMonthGridPane() {
		
		monthGridPane.removeAll();
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		monthGridPane.setLayout(layout);
		monthGridPane.setPreferredSize(GRID_PANE_DIMENSION);
		monthGridPane.setMaximumSize(GRID_PANE_DIMENSION);
		monthGridPane.setAlignmentY(TOP_ALIGNMENT);

		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		
		for (int dayOfMonth = 1; dayOfMonth <= month.length(true); dayOfMonth++) {
			
			constraints.gridy = (dayOfMonth-1) / 7;
			constraints.gridx = (dayOfMonth % 7) - 1;
			List<Artiste> artistesOfThatDay = calendrierAllArtistes.getChronoArtistes(MonthDay.of(month, dayOfMonth));
			String dayMonth = dayOfMonth + " " + monthName;
			
			Font dayFont;
			if ((artistesOfThatDay == null) || artistesOfThatDay.isEmpty()) {
				dayFont = NO_ARTIST_FONT;
			} else {
				dayFont = MONTH_FONT;
			}
			monthGridPane.add(CollectionUtils.createGridCellLabel(layout, constraints,
				JLabelBuilder.builder()
					.text(Integer.toString(dayOfMonth))
					.font(dayFont)
					.preferredSize(CELL_DIMENSION)
					.mouseListener(new CurrentDayMouseAdapter(artistesOfThatDay, dayMonth))));
		}
	}
	
	@Override
	public void updateElement() {
		fillMonthGridPane();
	}
	
	private class CurrentDayMouseAdapter extends MouseAdapter {
		
		private final List<Artiste> artistesOfThatDay;
		private final String dayString;
		
		public CurrentDayMouseAdapter(List<Artiste> artistesOfThatDay, String dayString) {		
			this.artistesOfThatDay = artistesOfThatDay;
			this.dayString = dayString;
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			if (artistesPaneNotFixed) {
				if ((artistesOfThatDay != null) && !artistesOfThatDay.isEmpty()) {
					currentArtisteList.clear();
					currentArtisteList.addAll(artistesOfThatDay);
				} else {
					currentArtisteList.clear();
				}
				dayMonthLabel.setText(dayString);
				artistesPane.getArtistesTableModel().fireTableDataChanged();
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if ((artistesOfThatDay != null) && !artistesOfThatDay.isEmpty()) {
				if (SwingUtilities.isLeftMouseButton(e) && (e.getClickCount() > 1)) {
					CollectionUtils.displayArtistesTable(artistesOfThatDay, generationPane);
				} else {
					currentArtisteList.clear();
					currentArtisteList.addAll(artistesOfThatDay);
					artistesPane.getArtistesTableModel().fireTableDataChanged();
					artistesPaneNotFixed = false;
					dayMonthLabel.setText(dayString);
				}
			}
		}
	}
}
