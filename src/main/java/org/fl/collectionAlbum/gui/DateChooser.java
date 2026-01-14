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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.fl.collectionAlbum.utils.DisplayableTemporal;

public class DateChooser extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int PANEL_WIDTH = 240;
	private static final int PANEL_HEIGHT = 30;
	
	private static final  Logger logger = Logger.getLogger(DateChooser.class.getName());
	
	private static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM").localizedBy(Locale.FRENCH);
	private static final DateTimeFormatter dayOfMonthFormatter = DateTimeFormatter.ofPattern("EEEE dd").localizedBy(Locale.FRENCH);
	
	private static final Vector<DisplayableTemporal> monthsVector = 
			new Vector<>(Arrays.stream(Month.values()).map(month -> new DisplayableTemporal(monthFormatter, month)).toList());
	
	private final JLabel infoLabel;
	private final JComboBox<DisplayableTemporal> dayField;
	private final DefaultComboBoxModel<DisplayableTemporal> dayFieldModel;
	private final JComboBox<DisplayableTemporal> monthField;
	private final JTextField yearField;
	private final DateListener dateListener;
	
	private LocalDate choosenDate;
	
	public DateChooser(LocalDate date) {
		super();
		
		choosenDate = date;
		int year = date.getYear();
		int month = date.getMonthValue();
		int day = date.getDayOfMonth();
		Vector<DisplayableTemporal> dayOfThisMonth = getAllDaysOf(YearMonth.of(year, month));
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		infoLabel = new JLabel();
		add(infoLabel);
		
		JPanel dateEntryPanel = new JPanel();
		dateEntryPanel.setLayout(new BoxLayout(dateEntryPanel,  BoxLayout.X_AXIS));
		
		dayFieldModel = new DefaultComboBoxModel<>();
		dayField = new JComboBox<DisplayableTemporal>(dayFieldModel);
		dayFieldModel.addAll(dayOfThisMonth);
		dayField.setSelectedIndex(day - 1);
		dateEntryPanel.add(dayField);
		
		monthField = new JComboBox<DisplayableTemporal>(monthsVector);
		monthField.setSelectedIndex(month - 1);
		dateEntryPanel.add(monthField);
		
		yearField = new JTextField(5);
		yearField.setText(Integer.toString(year));
		dateEntryPanel.add(yearField);
		
		add(dateEntryPanel);
		
		dateListener = new DateListener();
		dayField.addActionListener(dateListener);
		monthField.addActionListener(dateListener);
		yearField.addActionListener(dateListener);
		
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setMaximumSize(getPreferredSize());
		setMinimumSize(getPreferredSize());
	}

	public LocalDate getChoosenDate() {
		return choosenDate;
	}

	private static Vector<DisplayableTemporal> getAllDaysOf(YearMonth yearMonth) {

		return new Vector<>(IntStream.range(1, yearMonth.lengthOfMonth()+1)
				.mapToObj(dayNumber -> new DisplayableTemporal(dayOfMonthFormatter, yearMonth.atDay(dayNumber)))
				.toList());
	}
	
	private class DateListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			try {
				int year = parseNumericTextField(yearField, "une année");
				int month = ((DisplayableTemporal) monthField.getSelectedItem()).getTemporalAccessor().get(ChronoField.MONTH_OF_YEAR);
				int day = ((DisplayableTemporal) dayField.getSelectedItem()).getTemporalAccessor().get(ChronoField.DAY_OF_MONTH);
				
				YearMonth yearMonth = YearMonth.of(year, month);
				if (day > yearMonth.lengthOfMonth()) {
					day = yearMonth.lengthOfMonth();
				}
				
				choosenDate = LocalDate.of(year, month, day);
				
				yearField.setForeground(Color.BLACK);
				infoLabel.setForeground(Color.BLACK);
				infoLabel.setText("");
				
				// Update day field
				dayField.removeActionListener(dateListener);
				dayField.removeAllItems();
				dayFieldModel.addAll(getAllDaysOf(yearMonth));
				dayField.setSelectedIndex(day - 1);
				dayField.addActionListener(dateListener);
				
			} catch(NumberFormatException ex) {
				choosenDate = null;
			} catch (DateTimeException ex) {
				logger.log(Level.FINE, "Invalid date time entered", ex);
				infoLabel.setForeground(Color.RED);
				infoLabel.setText("Rentrez un nombre valide: " + ex.getMessage());
			} catch (Exception ex) {
				logger.log(Level.SEVERE, "Exception parsing time field", ex);
			}
		}
	}
	
	private int parseNumericTextField(JTextField field, String fieldName) {
		try {
			field.setForeground(Color.BLACK);
			return Integer.parseInt(field.getText().strip());
		} catch (NumberFormatException ex) {
			infoLabel.setForeground(Color.RED);
			infoLabel.setText("Rentrez " + fieldName + " valide");
			field.setForeground(Color.RED);
			throw ex;
		}
	}
}
