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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.AuteurComparator;
import org.fl.collectionAlbum.artistes.AuteurDateComparator;
import org.fl.collectionAlbum.artistes.AuteurDateDecesComparator;
import org.fl.collectionAlbum.format.MediaSupportCategories;
import org.fl.collectionAlbum.gui.renderer.AuteurDateRenderer;
import org.fl.collectionAlbum.gui.renderer.AuteurRenderer;
import org.fl.collectionAlbum.gui.renderer.CollectionNumberRenderer;
import org.fl.collectionAlbum.utils.CollectionUtils;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class ArtistesTableColumns {

	private static final AuteurComparator AUTEUR_COMPARATOR = new AuteurComparator();
	private static final AuteurDateComparator AUTEUR_DATE_COMPARATOR = new AuteurDateComparator();
	private static final AuteurDateDecesComparator AUTEUR_DECES_COMPARATOR = new AuteurDateDecesComparator();
	private static final CollectionUtils.IntegerComparator INTEGER_COMPARATOR = new CollectionUtils.IntegerComparator();
	private static final CollectionUtils.DoubleComparator DOUBLE_COMPARATOR = new CollectionUtils.DoubleComparator();
	
	private static final Function<Artiste, TemporalAccessor> artisteNaissanceGetter = a -> a.getNaissance();
	private static final Function<Artiste, TemporalAccessor> artisteDecesGetter = a -> a.getMort();
	private static final Function<TemporalAccessor, String> dateFormatterFunction = t -> TemporalUtils.formatDate((TemporalAccessor)t);
	
	private static final TableColumnParameter<Artiste> NOM = 
			new TableColumnParameter<>("Noms", null, 400, new AuteurRenderer(), AUTEUR_COMPARATOR, Artiste.class, (a) -> a);
	private static final TableColumnParameter<Artiste> NAISSANCE =
			new TableColumnParameter<>("Naissance", null, 150, new AuteurDateRenderer(artisteNaissanceGetter, dateFormatterFunction), AUTEUR_DATE_COMPARATOR,  Artiste.class, (a) -> a);
	private static final TableColumnParameter<Artiste> DECES =
			new TableColumnParameter<>("Décès", null, 150, new AuteurDateRenderer(artisteDecesGetter, dateFormatterFunction), AUTEUR_DECES_COMPARATOR,  Artiste.class, (a) -> a);
	private static final TableColumnParameter<Artiste> NB_CONCERT =
			new TableColumnParameter<>("Concerts", null, 100, new CollectionNumberRenderer(), INTEGER_COMPARATOR, Number.class, (a) -> a.getNbConcert());
	private static final TableColumnParameter<Artiste> NB_ALBUMS =
			new TableColumnParameter<>("Albums", null, 100, new CollectionNumberRenderer(), INTEGER_COMPARATOR, Number.class, (a) -> a.getNbAlbum());
	private static final TableColumnParameter<Artiste> POIDS =
			new TableColumnParameter<>("Poids", null, 100, new CollectionNumberRenderer(), DOUBLE_COMPARATOR, Number.class, (a) -> a.getAlbumsFormat().getPoids());
	
	private static final List<TableColumnParameter<Artiste>> SUPPORT_PHYSIQUES_COLUMNS_LIST =
			Stream.of(MediaSupportCategories.values())
				.map(msc ->  new TableColumnParameter<Artiste>(msc.getNom(), msc.getDescription(), 80,
								new CollectionNumberRenderer(), DOUBLE_COMPARATOR, Number.class, 
								(a) -> a.getAlbumsFormat().getSupportsPhysiquesNumbers().get(msc))).toList();
	
	private static List<TableColumnParameter<Artiste>> REGULAR_COLUMNS_LIST = new ArrayList<>(List.of(NOM, NAISSANCE, DECES, NB_CONCERT, NB_ALBUMS, POIDS));
	
	static {
		REGULAR_COLUMNS_LIST.addAll(SUPPORT_PHYSIQUES_COLUMNS_LIST);
	}
	
	public static final GenericTableColumns<Artiste> MINIMAL_COLUMNS = new GenericTableColumns<>(List.of(NOM, NAISSANCE, DECES), 0);
	
	public static final GenericTableColumns<Artiste> REGULAR_COLUMNS = new GenericTableColumns<>(REGULAR_COLUMNS_LIST, 0);
}
