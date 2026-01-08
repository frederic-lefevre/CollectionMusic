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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.table.AbstractTableModel;

import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.metrics.Metrics;
import org.fl.collectionAlbum.metrics.MetricsHistory;
import org.fl.collectionAlbum.metrics.MetricsHistory.MetricAttributesList;

public class MetricsHistoryTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	public static final int DATE_COL_IDX = 0;
	
	private static final String dateFrancePattern = "EEEE dd MMMM uuuu à HH:mm";
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFrancePattern, Locale.FRANCE);
	
	private static final String DATE_COLUMN_HEADER = "Date";
	
	private final MetricsHistory metricsHistory;
	private final MetricAttributesList metricsKeysAttributes;
	
	public MetricsHistoryTableModel(MetricsHistory metricsHistory) {
		super();
		this.metricsHistory = metricsHistory;
		metricsKeysAttributes = metricsHistory.getMetricsAttributes();
	}
	
	@Override
	public int getRowCount() {
		if (metricsHistory.hasEvolved()) {
			return metricsHistory.getMetricsHistory().size() + 1;
		} else {
			return metricsHistory.getMetricsHistory().size();
		}
	}

	@Override
	public int getColumnCount() {
		return metricsKeysAttributes.size() + 1;
	}

	@Override
	public String getColumnName(int col) {
		if (col == DATE_COL_IDX) {
			// First column is Date column
			return DATE_COLUMN_HEADER;
		} else {
			return metricsKeysAttributes.get(col - 1).getMetricName();
		}
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Metrics metrics;
		if (! metricsHistory.hasEvolved()) {
			metrics = metricsHistory.getMetricsHistory().get(rowIndex);
		} else if (rowIndex > 0) {
			metrics = metricsHistory.getMetricsHistory().get(rowIndex - 1);
		} else {
			metrics = metricsHistory.getPresentMetrics();
		}
		
		if (columnIndex == DATE_COL_IDX) {
			// First column is Date column
			return dateTimeFormatter.format(
					LocalDateTime.ofInstant(Instant.ofEpochMilli(metrics.getMetricTimeStamp()), ZoneId.systemDefault()));

		} else {
			// Following columns are metric numbers
			String metricKey = metricsKeysAttributes.get(columnIndex - 1).getMetricKey();
			return Format.poidsToString(metrics.getMetrics().get(metricKey));
		}
	}

}
