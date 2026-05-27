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
import org.fl.collectionAlbum.albums.AlbumAcquisitionDateComparator;
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

	TITRE(new TableColumnParameter<>("Titres", 250, new StringToHtmlRenderer(), null, String.class, Album::getTitre)),
	AUTEURS(new TableColumnParameter<>("Auteurs", 550, new AuteurListRenderer(), new RangementComparator(), Album.class, (a) -> a)),
	FORMAT(new TableColumnParameter<>("Formats", 100, null, null, String.class,
			(a) -> a.getFormatAlbum()
				.getSupportsPhysiques().stream()
				.map(f -> f.getNom())
				.collect(Collectors.joining(",")))),
	MEDIA_FILES(new TableColumnParameter<>("Fichiers media", 140, new MediaFilesRenderer(), new AlbumMediaFilesStatusComparator(),  Album.class, (a) -> a)),
	PROBLEM(new TableColumnParameter<>("Problème", 70, new CollectionBooleanRenderer(), null, Boolean.class, Album::hasProblem)),
	DISCOGS(new TableColumnParameter<>("Discogs release", 110, null, null, String.class, (a) -> Optional.ofNullable(a.getDiscogsLink()).orElse(""))),
	POIDS(new TableColumnParameter<>("Poids", 50, new CollectionNumberRenderer(), new CollectionUtils.DoubleComparator(), Double.class, (a) -> a.getFormatAlbum().getPoids())),
	ENREGISTREMENT(new TableColumnParameter<>("Enregistrement", 260, 
			new DatesAlbumRenderer(Album::getDebutEnregistrement, Album::getFinEnregistrement, t -> TemporalUtils.formatDate((TemporalAccessor)t)),
			new AlbumEnregistrementComparator(),
			Album.class,
			(a) -> a)),
	COMPOSITION(new TableColumnParameter<>("Composition", 260,
			new DatesAlbumRenderer(Album::getDebutComposition, Album::getFinComposition, t -> TemporalUtils.formatDate((TemporalAccessor)t)),
			new AlbumCompositionComparator(),
			Album.class,
			(a) -> a)),
	ACQUISITION(new TableColumnParameter<>("Date d'acquisition", 140, 
			new DatesAlbumRenderer(Album::getAcquisitionDate, null, t -> TemporalUtils.formatDate((TemporalAccessor)t)), 
			new AlbumAcquisitionDateComparator(), 
			Album.class, 
			(a) -> a));
		
	private final TableColumnParameter<Album> parameters;
	
	private AlbumTableColumn(TableColumnParameter<Album> parameters) {
		this.parameters = parameters;
	}

	public String getName() {
		return parameters.name();
	}

	public int getWidth() {
		return parameters.width();
	}

	public TableCellRenderer getCellRenderer() {
		return parameters.cellRenderer();
	}

	public Comparator<?> getComparator() {
		return parameters.comparator();
	}

	public Class<?> getValueClass() {
		return parameters.valueClass();
	}

	public Function<Album, Object> getValueGetter() {
		return parameters.valueGetter();
	}
}
