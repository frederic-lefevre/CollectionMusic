/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javax.swing.table.AbstractTableModel;

import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.metrics.Metrics;
import org.fl.collectionAlbum.metrics.MetricsHistory;

public class MetricsHistoryTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	public static final int DATE_COL_IDX = 0;
	
	private static final String dateFrancePattern = "EEEE dd MMMM uuuu à HH:mm";
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFrancePattern, Locale.FRANCE);
	
	private static final String DATE_COLUMN_HEADER = "Date";
	
	private final MetricsHistory metricsHistory;
	private final List<String> metricsKeyList;
	
	public MetricsHistoryTableModel(MetricsHistory metricsHistory) {
		super();
		this.metricsHistory = metricsHistory;
		metricsKeyList = metricsHistory.getMetricsKeys();
	}
	
	@Override
	public int getRowCount() {
		return metricsHistory.getMetricsHistory().size();
	}

	@Override
	public int getColumnCount() {
		return metricsKeyList.size() + 1;
	}

	@Override
	public String getColumnName(int col) {
		if (col == DATE_COL_IDX) {
			// First column is Date column
			return DATE_COLUMN_HEADER;
		} else {
			String metricKey = metricsKeyList.get(col - 1);
			return metricsHistory.getMetricsNamesMap().get(metricKey);
		}
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Metrics metrics = metricsHistory.getMetricsHistory().get(rowIndex);
		if (columnIndex == DATE_COL_IDX) {
			// First column is Date column
			return dateTimeFormatter.format(
					LocalDateTime.ofInstant(Instant.ofEpochMilli(metrics.getMetricTimeStamp()), ZoneId.systemDefault()));

		} else {
			// Following columns are metric numbers
			String metricKey = metricsKeyList.get(columnIndex - 1);
			return Format.poidsToString(metrics.getMetrics().get(metricKey));
		}
	}

}
