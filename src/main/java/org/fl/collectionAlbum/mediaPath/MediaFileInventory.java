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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;

public class MediaFileInventory {

	private final static Logger albumLog = Control.getAlbumLog();
	
	private static final String SEPARATOR = " / ";
	
	private static final Set<String> mediaFileExtensions = 
		Set.of("flac", "mp3", "wma", "aiff", "FLAC", "MP3", "m4a",
				"m2ts", "mkv", "mpls", "VOB", "wav", "m4v", "mp4");
	
	private final Map<String,Path> mediaFilePathInventory;
	
	public static final Set<String> extensionSet = new HashSet<>();
	
	protected MediaFileInventory(Path rootPath) {
		
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
    		
    		if ((Files.isRegularFile(file)) &&
    				isMediaFileName(file)) {
    			// It should be a media file, part of an album
    			
    			Path albumAbsolutePath = file.getParent();
    			
    			String artistFolder = albumAbsolutePath.getParent().getFileName().toString();
    			String albumFolder = albumAbsolutePath.getFileName().toString();
    			
    			String inventoryKey = getInventoryKey(artistFolder, albumFolder);
    			
    			if (! mediaFilePathInventory.containsKey(inventoryKey)) {
    				mediaFilePathInventory.put(inventoryKey, albumAbsolutePath);
    			}
    			return FileVisitResult.SKIP_SUBTREE;
    		} else {
    			return FileVisitResult.CONTINUE;
    		}
    	}
	}
	
	private String getInventoryKey(String artist, String albumTitle) {
		return (artist + SEPARATOR + albumTitle).toLowerCase();
	}
	
	public List<Path> getPotentialMediaPath(Album album) {
		
		String title = album.getTitre().toLowerCase();
		List<Artiste> auteurs = album.getAuteurs();
		
		Map<String,Path> potentialMediaPath = mediaFilePathInventory.entrySet().stream()
			.filter(inventoryEntry -> inventoryEntry.getKey().contains(title))
			.filter(inventoryEntry -> 
				(auteurs.isEmpty() ||
				(auteurs.stream()
						.map(auteur -> auteur.getNomComplet().toLowerCase())
						.anyMatch(auteurName -> inventoryEntry.getKey().contains(auteurName)))))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		if (potentialMediaPath.size() > 1) {
			Map<String,Path> potentialMediaPath2 = potentialMediaPath.entrySet().stream()
				.filter(inventoryEntry -> auteurs.stream()
						.map(auteur -> auteur.getNomComplet())
						.anyMatch(auteurName -> inventoryEntry.getKey().contains(getInventoryKey(auteurName, title))))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			
			if (potentialMediaPath2.isEmpty()) {
				return new ArrayList<>(potentialMediaPath.values());
			} else {
				return new ArrayList<>(potentialMediaPath2.values());
			}
		} else {
			return new ArrayList<>(potentialMediaPath.values());
		}
	}
	
	protected static boolean isMediaFileName(Path file) {

		return getFileNameExtension(file)
				.filter(extension -> mediaFileExtensions.contains(extension))
				.isPresent();
	}

	private static Optional<String> getFileNameExtension(Path filename) {
		return Optional.ofNullable(filename)
				.map(f -> f.toString())
				.filter(f -> f.contains("."))
				.map(f -> f.substring(f.lastIndexOf(".") + 1))
				.map(ext -> { extensionSet.add(ext);
				return ext;
					});
				
	}
}
