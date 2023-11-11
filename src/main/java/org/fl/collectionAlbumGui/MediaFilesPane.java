package org.fl.collectionAlbumGui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fl.collectionAlbum.albums.Album;

public class MediaFilesPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel mediaFilesStatus;
	
	public MediaFilesPane() {
		
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		mediaFilesStatus = new JLabel("Status unknown");
		add(mediaFilesStatus);
	}

	public void updateValue(Album album) {
		if (album.hasMediaFiles()) {
			if (album.hasMediaFilePathNotFound() ||
				album.hasMissingOrInvalidMediaFilePath()) {
				mediaFilesStatus.setText("Invalid or missing media file paths");
			} else {
				mediaFilesStatus.setText("Media file paths found");
			}
		} else {
			mediaFilesStatus.setText("No media files");
		}
	}
}
