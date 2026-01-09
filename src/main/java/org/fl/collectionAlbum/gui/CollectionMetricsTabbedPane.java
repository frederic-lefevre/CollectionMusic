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
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.fl.collectionAlbum.metrics.MetricsHistory;
import org.fl.collectionAlbum.metrics.MetricsHistory.MetricAttributes;

public class CollectionMetricsTabbedPane extends AbstractColorableTabbedPane {

	private static final long serialVersionUID = 1L;

	private static final Color historyTabHighLightColor = Color.MAGENTA;
	
	private final List<MetricsHistoryTableModel> metricsHistoryTableModelList;
	
	private final Map<Component, MetricsHistory> componentMap;
	
	public CollectionMetricsTabbedPane(List<MetricsHistory> metricsHistoryList) {
		super();

		this.metricsHistoryTableModelList = new ArrayList<>();
		this.componentMap = new HashMap<>();
		
		for (MetricsHistory metricHistory : metricsHistoryList) {
			MetricsHistoryTableModel metricHistoryTableModel = new MetricsHistoryTableModel(metricHistory);
			metricsHistoryTableModelList.add(metricHistoryTableModel);
			JTable metricsHistoryTable = new JTable(metricHistoryTableModel);
			metricsHistoryTable.getColumnModel().getColumn(MetricsHistoryTableModel.DATE_COL_IDX).setPreferredWidth(200);
			int colIdx = 1;
			for (MetricAttributes metricAttributes : metricHistory.getMetricsAttributes()) {
				metricsHistoryTable.getColumnModel().getColumn(colIdx).setPreferredWidth(metricAttributes.getRepresentationWidth());
				colIdx++;
			}
			
			componentMap.put(
					add(metricHistory.getName(), new JScrollPane(metricsHistoryTable)), 
					metricHistory);
		}
	}
	
	public List<MetricsHistoryTableModel> getTableModels() {
		return metricsHistoryTableModelList;
	}
	
	private MetricsHistory getMetricHistoryAt(int idx) {
		if ((idx < 0) || (idx > getTabCount() -1)) {
			throw new IllegalArgumentException("Invalid tab number for MetricsHistory tabs");
		} else {
			return  componentMap.get(getComponentAt(idx));
		}
	}
	
	@Override
	protected Color getColorFor(int idx) {
		if (getMetricHistoryAt(idx).hasEvolved()) {
			return historyTabHighLightColor;
		} else {
			// Default tab color
			return null;
		}
	}
}
