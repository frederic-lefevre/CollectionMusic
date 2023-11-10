/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;

public class MediaFileInventory {

	private final static Logger albumLog = Control.getAlbumLog();
	
	private final Map<String,Path> mediaFilePathInventory;
	
	public MediaFileInventory(Path rootPath) {
		
		mediaFilePathInventory = new HashMap<>();
		
		try {
			MediaFileVisitor mediaFileVisitor = new MediaFileVisitor();
			Files.walkFileTree(rootPath, mediaFileVisitor);
		} catch (Exception e) {
			albumLog.log(Level.SEVERE, "Exception scanning media directory " + rootPath, e);
		}
	}

	private class MediaFileVisitor extends SimpleFileVisitor<Path> {
		
    	@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
    		
    		if (Files.isRegularFile(file)) {
    			// It should be a media file, part of an album
    			
    			Path albumAbsolutePath = file.getParent();
    			
    			String artistFolder = albumAbsolutePath.getParent().getFileName().toString();
    			String albumFolder = albumAbsolutePath.getFileName().toString();
    			
    			String inventoryKey = (artistFolder + albumFolder).toLowerCase();
    			
    			if (! mediaFilePathInventory.containsKey(inventoryKey)) {
    				mediaFilePathInventory.put(inventoryKey, albumAbsolutePath);
    			}
    			return FileVisitResult.SKIP_SUBTREE;
    		} else {
    			return FileVisitResult.CONTINUE;
    		}
    	}
	}
	
	public List<Path> getPotentialMediaPath(Album album) {
		
		List<Path> potentialMediaPath = new ArrayList<>();
		
		String title = album.getTitre().toLowerCase();
		
		mediaFilePathInventory.keySet().forEach(key -> {
			if (key.contains(title)) {
				
				if (album.getAuteurs().stream()
					.map(auteur -> auteur.getNomComplet().toLowerCase())
					.anyMatch(auteurName -> key.contains(auteurName))) {
					potentialMediaPath.add(mediaFilePathInventory.get(key));
				}
			}
		});
		return potentialMediaPath;
	}
}
