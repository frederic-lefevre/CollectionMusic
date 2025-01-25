/*
 MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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

package org.fl.collectionAlbum.osAction;

import java.nio.file.Files;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.mediaPath.MediaFilePath;

public enum MediaFilePathCommandParameter implements OsActionCommandParameter<MediaFilePath> {
	
	ALBUMS_JSON(
		(mediaFile) -> mediaFile.getAlbumSet().stream()
			.map(album -> album.getJsonFilePath().toAbsolutePath().toString())
			.collect(Collectors.toList()),
		(mediaFile) -> ((mediaFile.getAlbumSet() != null) && !mediaFile.getAlbumSet().isEmpty())	
			),
	MEDIA_FILE_PATH(
			(mediaFile) -> List.of(mediaFile.getPath().toAbsolutePath().toString()),
			(mediaFile) -> ((mediaFile.getPath() != null) && Files.exists(mediaFile.getPath()) && Files.isDirectory(mediaFile.getPath()))
			);

	private final Function<MediaFilePath,List<String>> parametersGetter;
	private final Predicate<MediaFilePath> actionValidityPredicate;
	
	private MediaFilePathCommandParameter(Function<MediaFilePath, List<String>> pg, Predicate<MediaFilePath> vp) {
		this.parametersGetter = pg;
		this.actionValidityPredicate = vp;
	}

	@Override
	public Function<MediaFilePath, List<String>> getParametersGetter() {
		return parametersGetter;
	}

	@Override
	public Predicate<MediaFilePath> getActionValidityPredicate() {
		return actionValidityPredicate;
	}



}
