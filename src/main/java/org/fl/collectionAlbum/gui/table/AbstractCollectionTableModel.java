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

import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class AbstractCollectionTableModel<T> extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private final GenericTableColumns<T>  genericTableColumns;
	private List<T> itemList;
	
	AbstractCollectionTableModel(GenericTableColumns<T>  genericTableColumns, List<T> itemList) {
		super();
		this.genericTableColumns = genericTableColumns;
		this.itemList = itemList;
	}
	
	protected GenericTableColumns<T> getGenericTableColumns() {
		return genericTableColumns;
	}
	
	@Override
	public int getColumnCount() {
		return genericTableColumns.tableColumnParameters().size();
	}
	
	@Override
	public String getColumnName(int col) {
	    return genericTableColumns.tableColumnParameters().get(col).name();
	}
	
	// AbstractTableModel.getColumnClass is overridden because it interprets numbers as string
	// So they are left aligned instead of right aligned
    @Override
    public Class<?> getColumnClass(int columnIndex) {
    	return genericTableColumns.tableColumnParameters().get(columnIndex).valueClass();
    } 
	
	@Override
	public int getRowCount() {
		return itemList.size();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		if (itemList.size() < rowIndex + 1) {
			// This may happen when triggering a rescan of the collection
			return null;
		} else {
			return genericTableColumns.tableColumnParameters().get(columnIndex).valueGetter().apply(itemList.get(rowIndex));
		}
	}
	
	protected List<T> getItemList() {
		return itemList;
	};
	
	protected void setItemList(List<T> listOfItems) {
		this.itemList = listOfItems;
	}
}
