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
import org.fl.collectionAlbum.gui.renderer.CollectionBooleanRenderer;
import org.fl.collectionAlbum.gui.renderer.CollectionNumberRenderer;
import org.fl.collectionAlbum.gui.renderer.DurationRenderer;
import org.fl.collectionAlbum.mediaFile.MediaFileGenres.GenreParameters;
import org.fl.collectionAlbum.utils.CollectionUtils;

public class GenreTableModel extends AbstractCollectionTableModel<GenreParameters> implements UpdatableElement {

	private static final long serialVersionUID = 1L;

	private static final TableColumnParameter<GenreParameters> GENRE = 
			new TableColumnParameter<>("Genre", null, 600, null, null, String.class, (g) -> g.genre());
	public static final TableColumnParameter<GenreParameters> MEDIA_FILE_NUMBER = 
			new TableColumnParameter<>("Nombre de morceaux", null, 200, new CollectionNumberRenderer(), 
					new CollectionUtils.IntegerComparator(), Integer.class, (g) -> g.mediaFiles().size());
	private static final TableColumnParameter<GenreParameters> TOTAL_DURATION = 
			new TableColumnParameter<>("Durée totale", null, 200, new DurationRenderer(), 
					new CollectionUtils.LongComparator(), Long.class, (g) -> g.duration());
	private static final TableColumnParameter<GenreParameters> IS_NORMALIZED =
			new TableColumnParameter<>("Genre normalisé", null, 150, new CollectionBooleanRenderer(),
					null, Boolean.class, (g) -> g.iNormalizedGenre());
			
	public static final GenericTableColumns<GenreParameters> REGULAR_COLUMN = new GenericTableColumns<>(List.of(GENRE, MEDIA_FILE_NUMBER, TOTAL_DURATION, IS_NORMALIZED), 0);
	
	public GenreTableModel(List<GenreParameters> genreParametersList, GenericTableColumns<GenreParameters> genericTableColumns) {
		super(genericTableColumns, genreParametersList);
	}

	@Override
	public void updateElement() {
		fireTableDataChanged();	
	}
}
