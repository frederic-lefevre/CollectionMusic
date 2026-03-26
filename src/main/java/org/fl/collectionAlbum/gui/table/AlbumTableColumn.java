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

import java.time.temporal.TemporalAccessor;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.table.TableCellRenderer;

import org.fl.collectionAlbum.RangementComparator;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.AlbumCompositionComparator;
import org.fl.collectionAlbum.albums.AlbumEnregistrementComparator;
import org.fl.collectionAlbum.albums.AlbumMediaFilesStatusComparator;
import org.fl.collectionAlbum.gui.renderer.AuteurListRenderer;
import org.fl.collectionAlbum.gui.renderer.CollectionBooleanRenderer;
import org.fl.collectionAlbum.gui.renderer.CollectionNumberRenderer;
import org.fl.collectionAlbum.gui.renderer.DatesAlbumRenderer;
import org.fl.collectionAlbum.gui.renderer.MediaFilesRenderer;
import org.fl.collectionAlbum.gui.renderer.StringToHtmlRenderer;
import org.fl.collectionAlbum.utils.CollectionUtils;
import org.fl.collectionAlbum.utils.TemporalUtils;

public enum AlbumTableColumn {

	TITRE("Titres", 250, new StringToHtmlRenderer(), null, String.class, Album::getTitre),
	AUTEURS("Auteurs", 550, new AuteurListRenderer(), new RangementComparator(), Album.class, (a) -> a),
	FORMAT("Formats", 100, null, null, String.class,
			(a) -> a.getFormatAlbum()
				.getSupportsPhysiques().stream()
				.map(f -> f.getNom())
				.collect(Collectors.joining(","))),
	MEDIA_FILES("Fichiers media", 140, new MediaFilesRenderer(), new AlbumMediaFilesStatusComparator(),  Album.class, (a) -> a),
	PROBLEM("Problème", 70, new CollectionBooleanRenderer(), null, Boolean.class, Album::hasProblem),
	DISCOGS("Discogs release", 110, null, null, String.class, (a) -> Optional.ofNullable(a.getDiscogsLink()).orElse("")),
	POIDS("Poids", 50, new CollectionNumberRenderer(), new CollectionUtils.DoubleComparator(), Double.class, (a) -> a.getFormatAlbum().getPoids()),
	ENREGISTREMENT("Enregistrement", 260, 
			new DatesAlbumRenderer(Album::getDebutEnregistrement, Album::getFinEnregistrement, t -> TemporalUtils.formatDate((TemporalAccessor)t)),
			new AlbumEnregistrementComparator(),
			Album.class,
			(a) -> a),
	COMPOSITION("Composition", 260,
			new DatesAlbumRenderer(Album::getDebutComposition, Album::getFinComposition, t -> TemporalUtils.formatDate((TemporalAccessor)t)),
			new AlbumCompositionComparator(),
			Album.class,
			(a) -> a);
		
	private final String name;
	private final int width;
	private final TableCellRenderer cellRenderer;
	private final Comparator<?> comparator;
	private final Class<?> valueClass;
	private final Function<Album, Object> valueGetter;
	
	private AlbumTableColumn(String name, int width, TableCellRenderer cellRenderer, Comparator<?> comparator, Class<?> valueClass, Function<Album, Object> valueGetter) {
		this.name = name;
		this.width = width;
		this.cellRenderer = cellRenderer;
		this.comparator = comparator;
		this.valueClass = valueClass;
		this.valueGetter = valueGetter;
	}

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public TableCellRenderer getCellRenderer() {
		return cellRenderer;
	}

	public Comparator<?> getComparator() {
		return comparator;
	}

	public Class<?> getValueClass() {
		return valueClass;
	}

	public Function<Album, Object> getValueGetter() {
		return valueGetter;
	}
}
