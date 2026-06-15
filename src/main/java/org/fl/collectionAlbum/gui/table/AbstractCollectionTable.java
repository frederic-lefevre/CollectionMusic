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

package org.fl.collectionAlbum.gui.table;

import java.util.Comparator;
import java.util.logging.Logger;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

public abstract class AbstractCollectionTable<T> extends JTable {

	private static final long serialVersionUID = 1L;
	
	private static final Logger tLog = Logger.getLogger(AbstractCollectionTable.class.getName());
	
	private final GenericTableColumns<T> tableColumns;
	private final AbstractCollectionTableModel<T> collectionTableModel;
	
	public AbstractCollectionTable(AbstractCollectionTableModel<T> collectionTableModel) {
		super(collectionTableModel);
		
		this.collectionTableModel = collectionTableModel;
		this.tableColumns = collectionTableModel.getGenericTableColumns();
		
		setFillsViewportHeight(true);
		
		setRowHeight(tableColumns.rowHeight());
		
		// Row sorter
		TableRowSorter<AbstractCollectionTableModel<T>> sorter = new TableRowSorter<>(collectionTableModel);
		
		for (int colIdx = 0; colIdx < tableColumns.tableColumnParameters().size(); colIdx++) {
			TableCellRenderer renderer =  tableColumns.tableColumnParameters().get(colIdx).cellRenderer();
			if (renderer != null) {
				 getColumnModel().getColumn(colIdx).setCellRenderer(renderer);
			}
			getColumnModel().getColumn(colIdx).setPreferredWidth(tableColumns.tableColumnParameters().get(colIdx).width());
			
			Comparator<?> comparator =  tableColumns.tableColumnParameters().get(colIdx).comparator();
			if (comparator != null) {
				sorter.setComparator(colIdx, comparator);
			}
		}
		
		// Allow single row selection only
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel(listSelectionModel);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		setRowSorter(sorter);
	}
	
	protected boolean isColumnSelected(TableColumnParameter<T> column) {
		
		int selectedIndex = getSelectedColumn();
		if (selectedIndex < 0) {
			return false;
		} else {
			return tableColumns.tableColumnParameters().get(selectedIndex) == column;
		}
	}
	
	protected int getColumnNumber(TableColumnParameter<T> tableColumn) {
		
		for (int i=0; i < tableColumns.tableColumnParameters().size(); i++) {
			if (tableColumns.tableColumnParameters().get(i) == tableColumn) {
				return i;
			}
		}
		return -1;	
	}
	
	public T getSelectedItem() {
		
		int[] rowIdxs = getSelectedRows();
		if (rowIdxs.length == 0) {
			return null;
		} else if (rowIdxs.length > 1) {
			tLog.severe("Found several selected rows for " + this.getClass().getName() + ". Number of selected rows: " + rowIdxs.length);
		}
		
		return collectionTableModel.getItemList().get(convertRowIndexToModel(rowIdxs[0]));
	}
	
	@Override
	protected JTableHeader createDefaultTableHeader() {
		return new JTableHeaderWithSpecificToolTips(columnModel, columnIndex -> getColumnToolTip(columnIndex));
    }
	
	private String getColumnToolTip(int columnIndex) {
		String toolTipText =  tableColumns.tableColumnParameters().get(columnIndex).toolTipText();
		if ((toolTipText != null) && !toolTipText.isBlank()) {
			return toolTipText;
		} else {
			return null;
		}
	}
}
