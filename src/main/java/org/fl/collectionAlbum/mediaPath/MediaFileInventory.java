/*
 * MIT License

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

package org.fl.collectionAlbum.mediaPath;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.format.ContentNature;

public class MediaFileInventory {

	private static final Logger albumLog = Logger.getLogger(MediaFileInventory.class.getName());
	
	private static final String SEPARATOR = File.separator;
	
	private final Path rootPath;
	
	private final LinkedHashMap<Path,MediaFilePath> mediaFilePathInventory;
	
	// MediaFilePath values maintained as List to be displayed in a JTable
	private final List<MediaFilePath> mediaFilePathList;
	
	private final ContentNature contentNature;
	private final boolean logWarnings;
	private boolean isConnected;
	
	protected MediaFileInventory(Path rootPath, ContentNature contentNature, boolean logWarnings) {
		
		this.rootPath = rootPath;
		this.contentNature = contentNature;
		this.logWarnings = logWarnings;
		mediaFilePathInventory = new LinkedHashMap<>();
		mediaFilePathList = new ArrayList<>();
		isConnected = Files.exists(rootPath);
	}

	public void scanMediaFilePaths() {
		
		if (isConnected()) {
			try {
				MediaFileVisitor mediaFileVisitor = new MediaFileVisitor();
				Files.walkFileTree(rootPath, mediaFileVisitor);
			} catch (Exception e) {
				albumLog.log(Level.SEVERE, "Exception scanning media directory " + rootPath, e);
			}
		} else {
			albumLog.severe("The media file inventory root path does not exist: " + rootPath);
		}
	}
	
	public void clearInventory() {
		mediaFilePathList.clear();
		mediaFilePathInventory.clear();
		isConnected = Files.exists(rootPath);
	}
	
	private class MediaFileVisitor extends SimpleFileVisitor<Path> {
		
		@Override
		public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attr) {
			if (mediaFilePathInventory.containsKey(file)) {
    			// already in inventory
    			
    			return FileVisitResult.SKIP_SUBTREE;
    			
    		} else {
    			return FileVisitResult.CONTINUE;
    		}
		}
		
    	@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
    		
    		if ((Files.isRegularFile(file)) &&
    				MediaFilePath.isMediaFileName(file, contentNature)) {
    			// It should be a media file, part of an album
    			
    			addMediaFilePathToInventory(file.getParent());
    		 	
    		} 
    		return FileVisitResult.CONTINUE;
    	}
	}
	
	private MediaFilePath addMediaFilePathToInventory(Path albumAbsolutePath) {
		
		if (! mediaFilePathInventory.containsKey(albumAbsolutePath)) {
			MediaFilePath newMediaFilePath = new MediaFilePath(albumAbsolutePath, contentNature, logWarnings);
			mediaFilePathInventory.put(albumAbsolutePath, newMediaFilePath);
			mediaFilePathList.add(newMediaFilePath);
		}
		return mediaFilePathInventory.get(albumAbsolutePath);
	}
	
	private boolean checkInventoryKey(Path inventoryKey, String artist, String albumTitle) {
		return inventoryKey.toString().toLowerCase().contains((artist + SEPARATOR + albumTitle).toLowerCase());
	}
	
	private boolean checkInventoryKey(Path inventoryKey, String pathPart) {
		return inventoryKey.toString().toLowerCase().contains(pathPart.toLowerCase());
	}
	
	public List<MediaFilePath> getPotentialMediaPath(Album album) {
		
		String title = album.getTitre();
		List<Artiste> auteurs = album.getAuteurs();
		
		Map<Path,MediaFilePath> potentialMediaPath = mediaFilePathInventory.entrySet().stream()
			.filter(inventoryEntry -> checkInventoryKey(inventoryEntry.getKey(), title))
			.filter(inventoryEntry -> 
				(auteurs.isEmpty() ||
				(auteurs.stream()
						.map(auteur -> auteur.getNomComplet())
						.anyMatch(auteurName -> checkInventoryKey(inventoryEntry.getKey(), auteurName)))))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		if (potentialMediaPath.size() > 1) {
			List<MediaFilePath> potentialMediaPath2 = potentialMediaPath.entrySet().stream()
				.filter(inventoryEntry -> auteurs.stream()
						.map(auteur -> auteur.getNomComplet())
						.anyMatch(auteurName -> checkInventoryKey(inventoryEntry.getKey(), auteurName, title)))
				.map(inventoryEntry -> inventoryEntry.getValue())
				.collect(Collectors.toList());
			
			if (potentialMediaPath2.isEmpty()) {
				return new ArrayList<>(potentialMediaPath.values());
			} else {
				return potentialMediaPath2;
			}
		} else {
			return new ArrayList<>(potentialMediaPath.values());
		}
	}
	
	public int getNbMediaPath() {
		return mediaFilePathInventory.size();
	}
	
	public List<MediaFilePath> getMediaFilePathList() {
		return mediaFilePathList;
	}
	
	public boolean isConnected() {
		return isConnected;
	}

	public MediaFilePath validateMediaFilePath(Path path) {
		
		if (! isConnected()) {
			return null;
		}
		
		if (Files.exists(path)) {
			
			if (path.startsWith(rootPath)) {

				MediaPathValidatorVisitor mediaPathValidatorVisitor = new MediaPathValidatorVisitor();

				try {
					Files.walkFileTree(path, mediaPathValidatorVisitor);

					if (mediaPathValidatorVisitor.isMediaFilePresent()) {
						return addMediaFilePathToInventory(path);
					} else {
						albumLog.severe("No media file found inside: " + path);
						return null;
					}
				} catch (IOException e) {
					albumLog.log(Level.SEVERE, "IOException listing media file inside " + path, e);
					return null;
				}
			} else {
				albumLog.severe("Media file path " + path + " is not in the root folder " + rootPath);
				return null;
			}
			
		} else {
			albumLog.severe("Media file path not found : " + path);
			return null;
		}
	
	}
	
	private class MediaPathValidatorVisitor extends SimpleFileVisitor<Path> {
		
		private boolean mediaFilePresent;
		
		MediaPathValidatorVisitor() {
			super();
			mediaFilePresent = false;
		}
		
		boolean isMediaFilePresent() {
			return mediaFilePresent;
		}
		
    	@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
    		
    		if ((Files.isRegularFile(file)) &&
    				MediaFilePath.isMediaFileName(file, contentNature)) {
    			// It should be a media file, part of an album
    			
    			mediaFilePresent = true;
    			return FileVisitResult.TERMINATE;
    		} else {
    			return FileVisitResult.CONTINUE;
    		}
    	}
	}
	
}
