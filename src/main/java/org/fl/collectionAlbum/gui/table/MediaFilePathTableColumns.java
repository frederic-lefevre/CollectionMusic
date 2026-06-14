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
import java.util.Optional;

import org.fl.collectionAlbum.gui.renderer.AlbumsRenderer;
import org.fl.collectionAlbum.gui.renderer.CollectionBooleanRenderer;
import org.fl.collectionAlbum.gui.renderer.CollectionOptionalBooleanRenderer;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;
import org.fl.collectionAlbum.mediaPath.MediaFilePathAlbumComparator;
import org.fl.collectionAlbum.utils.CollectionUtils;

public class MediaFilePathTableColumns {

	private static final TableColumnParameter<MediaFilePath> PATH = 
			new TableColumnParameter<>("<html>Chemins des fichiers<br>media</html>", null, 700, null, null, String.class, m -> m.getPath().toString());
	
	public static final TableColumnParameter<MediaFilePath> ALBUMS = 
			new TableColumnParameter<>("Albums", null, 680, new AlbumsRenderer(), new MediaFilePathAlbumComparator(), MediaFilePath.class, (m) -> m);
	
	private static final TableColumnParameter<MediaFilePath> FILE_NUMBER = 
			new TableColumnParameter<>("<html>Nombre de<br>medias</html>", null, 100, null, new CollectionUtils.IntegerComparator(), Integer.class, MediaFilePath::getMediaFileNumber);
	
	private static final TableColumnParameter<MediaFilePath> PRESENCE_POCHETTE = 
			new TableColumnParameter<>("<html>Image de la<br>pochette</html>", null, 100, new CollectionBooleanRenderer(), null, Boolean.class, MediaFilePath::hasCover);
	
	private static final TableColumnParameter<MediaFilePath> MEDIA_TYPE = 
			new TableColumnParameter<>("Type de media", null, 100, null, null, String.class, MediaFilePath::getMediaFileExtension);
	
	private static final TableColumnParameter<MediaFilePath> METADATA_CHECK = 
			new TableColumnParameter<>("<html>Metadata<br>equivalentes</html>", 
					"""
			<html>Tous les morceaux ont des metadata équivalentes:<br/> 
			<ul>
			<li>Type de fichier (flac, mp3...)
			<li>Avec ou sans perte
			<li>Nombre de canaux
			<li>Débits (échantillons ou bit/secondes)
			<li>Nombre de bits des échantillons
			</ul></html>
			""", 
					100, 
					new CollectionOptionalBooleanRenderer(), null, Optional.class, MediaFilePath::hasEquivalentStreamMetadata);
	
	private static final List<TableColumnParameter<MediaFilePath>> REGULAR_COLUMNS_LIST = List.of(
			PATH, ALBUMS, FILE_NUMBER, PRESENCE_POCHETTE, MEDIA_TYPE, METADATA_CHECK
			);
	
	public static final GenericTableColumns<MediaFilePath> REGULAR_COLUMNS = new GenericTableColumns<>(REGULAR_COLUMNS_LIST, 25);
}
