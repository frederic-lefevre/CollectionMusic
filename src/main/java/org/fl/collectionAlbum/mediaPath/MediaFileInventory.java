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
