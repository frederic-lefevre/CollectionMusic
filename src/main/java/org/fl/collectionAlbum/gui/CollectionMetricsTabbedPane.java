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
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SortOrder;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.gui.table.AbstractAlbumsTableModel;
import org.fl.collectionAlbum.gui.table.AlbumTableColumn;
import org.fl.collectionAlbum.gui.table.AlbumsJTable.AlbumColumnSort;
import org.fl.collectionAlbum.gui.table.AlbumsScrollJTablePane;
import org.fl.collectionAlbum.gui.table.MetricsHistoryTableModel;
import org.fl.collectionAlbum.metrics.MetricsHistory;
import org.fl.collectionAlbum.metrics.MetricsHistory.MetricAttributes;

public class CollectionMetricsTabbedPane extends AbstractColorableTabbedPane {

	private static final long serialVersionUID = 1L;

	public static final Color METRICS_TAB_BACKGROUND_COLOR_HIGHLIGHT = Color.CYAN;
	public static final Color METRICS_TAB_FOREGROUND_COLOR_HIGHLIGHT = Color.RED;
	
	private static final Dimension SCROLL_TABLE_DIMENSION = new Dimension(1820, 650);
	
	private final List<MetricsHistory> metricsHistoryList;
	private final List<UpdatableElement> tableModelList;
	
	private final Map<Component, MetricsHistory> componentMap;
	
	public CollectionMetricsTabbedPane(List<MetricsHistory> metricsHistoryList, CollectionAlbumContainer collectionAlbumContainer, GenerationPane generationPane) {
		super();

		this.metricsHistoryList = metricsHistoryList;
		this.tableModelList = new ArrayList<>();
		this.componentMap = new HashMap<>();
		
		for (MetricsHistory metricHistory : metricsHistoryList) {
			MetricsHistoryTableModel metricHistoryTableModel = new MetricsHistoryTableModel(metricHistory);
			tableModelList.add(metricHistoryTableModel);
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
		
		AlbumsScrollJTablePane albumsScrollJTablePane = 
				new AlbumsScrollJTablePane(
						collectionAlbumContainer.getCollectionAlbumsMusiques().getAlbums(),
						AbstractAlbumsTableModel.ACQUISITION_COLUMN_LIST,
						generationPane,
						new AlbumColumnSort(AlbumTableColumn.ACQUISITION, SortOrder.DESCENDING));
		albumsScrollJTablePane.setPreferredSize(SCROLL_TABLE_DIMENSION);
		tableModelList.add(albumsScrollJTablePane.getAlbumsTableModel());
		add("Albums par date d'acquisition", albumsScrollJTablePane);
	}
	
	public List<UpdatableElement> getTableModels() {
		return tableModelList;
	}
	
	private MetricsHistory getMetricHistoryAt(int idx) {
		if ((idx < 0) || (idx > getTabCount() -1)) {
			throw new IllegalArgumentException("Invalid tab number for MetricsHistory tabs");
		} else {
			return  componentMap.get(getComponentAt(idx));
		}
	}
	
	public boolean metricsHasEvolved() {
		return metricsHistoryList.stream().anyMatch(MetricsHistory::hasEvolved);
	}
	
	@Override
	protected Color getBackgroundColorFor(int idx) {
		MetricsHistory metricsHistory = getMetricHistoryAt(idx);
		if ((metricsHistory != null) &&  getMetricHistoryAt(idx).hasEvolved()) {
			return METRICS_TAB_BACKGROUND_COLOR_HIGHLIGHT;
		} else {
			// Default tab color
			return null;
		}
	}
	@Override
	protected Color getForegroundColorFor(int idx) {
		MetricsHistory metricsHistory = getMetricHistoryAt(idx);
		if ((metricsHistory != null) &&  metricsHistory.hasEvolved()) {
			return METRICS_TAB_FOREGROUND_COLOR_HIGHLIGHT;
		} else {
			// Default tab color
			return null;
		}
	}
}
