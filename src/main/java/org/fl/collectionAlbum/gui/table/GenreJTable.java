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

import javax.swing.RowSorter;
import javax.swing.SortOrder;

import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.adapter.GenreParametersMouseAdapter;
import org.fl.collectionAlbum.mediaFile.MediaFileGenres.GenreParameters;

public class GenreJTable extends AbstractCollectionTable<GenreParameters> {

	private static final long serialVersionUID = 1L;
	
	public GenreJTable(GenreTableModel genreTableModel, ContentNature contentNature) {
		super(genreTableModel);
		
		addMouseListener(new GenreParametersMouseAdapter(this, contentNature));
		
		int columnIdx = getColumnNumber(GenreTableModel.MEDIA_FILE_NUMBER);
		if (columnIdx >= 0) {
			getRowSorter().setSortKeys(List.of(new RowSorter.SortKey(columnIdx, SortOrder.DESCENDING)));
		} else {
			throw new IllegalArgumentException("The colum to sort is not a column of this table: " + GenreTableModel.MEDIA_FILE_NUMBER.name());
		}
	}

	public GenreParameters getSelectedGenreParameters() {
		return getSelectedItem();
	}
}
