/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

package org.fl.collectionAlbum.mediaPath;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.format.ContentNature;

public class MediaFilesInventories {

	private static MediaFilesInventories instance;
	
	private final Map<ContentNature,MediaFileInventory> mediaFilesInventories;

	private MediaFilesInventories() {
   		
		mediaFilesInventories = new LinkedHashMap<>();
		Stream.of(ContentNature.values()).forEach(contentNature -> {
			mediaFilesInventories.put(
					contentNature, 
					new MediaFileInventory(Control.getMediaFileRootPath(contentNature), contentNature, contentNature.strictCheckings()));
		});
	}

	public static MediaFileInventory getMediaFileInventory(ContentNature contentNature) {
		if (instance == null) {
			instance = new MediaFilesInventories();
		}
		return instance.mediaFilesInventories.get(contentNature);
	}

	public static void buildInventories() {
		if (instance != null) {
			instance.mediaFilesInventories.values().forEach(MediaFileInventory::buildInventory);
		}
	}
}
