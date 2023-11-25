package org.fl.collectionAlbumGui;

import java.awt.Component;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;

public class AuteursRenderer extends JLabel implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	private static final Logger mLog = Control.getAlbumLog();
	
	private final static String AUTEURS_SEPARATOR = ", ";
	
	public AuteursRenderer() {
		super();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		if (value == null) {
			// This may happen when rescanning the album collection
			mLog.fine("Null value in MediaFiles cell. Should be an Album");
		} else if (value instanceof Album) {
			setText(((Album)value).getAuteurs().stream()
					.map(auteur -> auteur.getNomComplet())
					.collect(Collectors.joining(AUTEURS_SEPARATOR)));
		} else {
			mLog.severe("Invalid value type in Auteurs cell. Should be Album but is " + value.getClass().getName());
		}
		return this;
	}

}
