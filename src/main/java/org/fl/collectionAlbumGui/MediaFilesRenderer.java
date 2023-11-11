package org.fl.collectionAlbumGui;

import java.awt.Component;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;

public class MediaFilesRenderer extends MediaFilesPane implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	private static final Logger mLog = Control.getAlbumLog();
	
	public MediaFilesRenderer() {
		super();
	}

	@Override 
	public Component getTableCellRendererComponent(
		      JTable table, Object value, boolean isSelected, boolean hasFocus,
		      int row, int column) {
		if (value == null) {
			mLog.severe("Null value in MediaFiles cell. Should be an Album");
		} else if (value instanceof Album) {
			updateValue((Album)value);
		} else {
			mLog.severe("Invalid value type in MediaFiles cell. Should be Album but is " + value.getClass().getName());
		}
		return this;
	}
}
