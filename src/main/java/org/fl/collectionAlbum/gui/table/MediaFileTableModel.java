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

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.table.AbstractTableModel;

import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.UpdatableElement;
import org.fl.collectionAlbum.mediaFile.MediaFile;
import org.fl.collectionAlbum.mediaFile.metadata.MediaFileMetadata;

public class MediaFileTableModel extends AbstractTableModel implements UpdatableElement {

	private static final long serialVersionUID = 1L;

	public static final int FILE_COL_IDX = 0;
	
	private static final List<String> baseEntetes = List.of("Fichiers");
	
	private final List<String> streamInfoEntetes;
	private final List<String> normalizedTagsEntetes;
	private final List<String> entetes;
	
	private final List<MediaFile> mediaFiles;
	
	public MediaFileTableModel(List<MediaFile> mediaFiles, ContentNature contentNature) {
		super();
		this.mediaFiles = mediaFiles;
		this.streamInfoEntetes = contentNature.getStreamMetadataNames();
		this.normalizedTagsEntetes = contentNature.getNormalizedMetadataNames();		
		this.entetes = Stream.of(baseEntetes, streamInfoEntetes, normalizedTagsEntetes).flatMap(Collection::stream).toList();
	}
	
	@Override
	public int getRowCount() {
		if (mediaFiles == null) {
			return 0;
		} else {
			return mediaFiles.size();
		}
	}

	@Override
	public int getColumnCount() {
		return entetes.size();
	}

	@Override
	public String getColumnName(int col) {
	    return entetes.get(col);
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if (rowIndex < mediaFiles.size()) {
			MediaFile mediaFile = mediaFiles.get(rowIndex);
			if (columnIndex == FILE_COL_IDX) {
				return mediaFile.getFileName();
			} else {

				MediaFileMetadata mediaFileMetadata = mediaFile.getMetadata();
				if (mediaFileMetadata == null) {
					return null;
				} else if ((columnIndex > FILE_COL_IDX) && (columnIndex < streamInfoEntetes.size() + 1)) {

					return mediaFileMetadata.getStreamMetadata().get(streamInfoEntetes.get(columnIndex - 1)).value();

				} else if ((columnIndex > streamInfoEntetes.size()) && (columnIndex < entetes.size() + 1)) {

					return mediaFileMetadata.getNormalizedTags().get(normalizedTagsEntetes.get(columnIndex - (streamInfoEntetes.size() + 1))).value();
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}

	@Override
	public void updateElement() {
		fireTableDataChanged();
	}
}
