/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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

package org.fl.collectionAlbumGui;

import java.awt.Component;
import java.util.EventObject;
import java.util.Objects;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;

public class MediaFilesCellEditor extends MediaFilesPane implements TableCellEditor {

	private static final long serialVersionUID = 1L;

	private static final Logger mLog = Control.getAlbumLog();
	
	private final AlbumsJTable albumsTable;
	
	protected transient ChangeEvent changeEvent;
	
	public MediaFilesCellEditor(AlbumsJTable albumsTable, MediaFilesSearchListener mediaFilesSearchListener) {
		super(mediaFilesSearchListener);
		this.albumsTable = albumsTable;
	}

	@Override 
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (value == null) {
			mLog.severe("Null value in MediaFiles cell. Should be an Album");
		} else if (value instanceof Album) {
			updateValue((Album)value);
		} else {
			mLog.severe("Invalid value type in MediaFiles cell. Should be Album but is " + value.getClass().getName());
		}
		return this;
	}
	
	@Override
	public Object getCellEditorValue() {
		return albumsTable.getSelectedAlbum();
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean stopCellEditing() {
		fireEditingStopped();
	    return true;
	}

	@Override
	public void cancelCellEditing() {
		fireEditingStopped();	
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		listenerList.add(CellEditorListener.class, l);		
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		listenerList.remove(CellEditorListener.class, l);		
	}

	protected void fireEditingStopped() {
	    // Guaranteed to return a non-null array
	    Object[] listeners = listenerList.getListenerList();
	    // Process the listeners last to first, notifying
	    // those that are interested in this event
	    for (int i = listeners.length - 2; i >= 0; i -= 2) {
	      if (listeners[i] == CellEditorListener.class) {
	        // Lazily create the event:
	        if (Objects.isNull(changeEvent)) {
	          changeEvent = new ChangeEvent(this);
	        }
	        ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
	      }
	    }
	  }
}
