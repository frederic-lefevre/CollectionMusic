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

import org.fl.collectionAlbum.gui.UpdatableElement;
import org.fl.collectionAlbum.gui.renderer.CollectionNumberRenderer;
import org.fl.collectionAlbum.mediaFile.MediaStreamPatterns.MediaStreamPattern;
import org.fl.collectionAlbum.utils.CollectionUtils;

public class MediaFileStreamTableModel  extends AbstractCollectionTableModel<MediaStreamPattern> implements UpdatableElement {

	private static final long serialVersionUID = 1L;

	private static final TableColumnParameter<MediaStreamPattern> STREAM_PATTERN = 
			new TableColumnParameter<>("Type de flux", null, 800, null, null, String.class, (s) -> s.descriptionKey());
	private static final TableColumnParameter<MediaStreamPattern> MEDIA_FILE_NUMBER = 
			new TableColumnParameter<>("Nombre de morceaux", null, 200, new CollectionNumberRenderer(), 
					new CollectionUtils.IntegerComparator(), Integer.class, (s) -> s.mediaFileList().size());
	
	public static final GenericTableColumns<MediaStreamPattern> REGULAR_COLUMNS = new GenericTableColumns<>(List.of(STREAM_PATTERN, MEDIA_FILE_NUMBER), 0);
	
	public MediaFileStreamTableModel(List<MediaStreamPattern> mediaStreamPatternList, GenericTableColumns<MediaStreamPattern> columns) {
		super(columns, mediaStreamPatternList);
	}
	
	@Override
	public void updateElement() {
		fireTableDataChanged();	
	}
}
