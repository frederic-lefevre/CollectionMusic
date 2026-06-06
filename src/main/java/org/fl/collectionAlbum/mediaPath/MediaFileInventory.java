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

package org.fl.collectionAlbum.mediaPath;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.mediaFile.MediaFile;
import org.fl.collectionAlbum.mediaFile.MediaFileBuilder;

public class MediaFileInventory {

	private static final Logger albumLog = Logger.getLogger(MediaFileInventory.class.getName());
	
	private static final String SEPARATOR = File.separator;
	
	private final Path rootPath;
	
	private final LinkedHashMap<Path,MediaFilePath> mediaFilePathMap;
	
	// MediaFilePath and MediaFile values maintained as List to be displayed in a JTable
	private final List<MediaFilePath> mediaFilePathList;
	private final List<MediaFile> mediaFileList;
	
	private final ContentNature contentNature;
	private final Level multiFolderLoggingLevel;
	private boolean isConnected;
	
	protected MediaFileInventory(Path rootPath, ContentNature contentNature, boolean isSingleLevelMediaPath) {
		
		this.rootPath = rootPath;
		this.contentNature = contentNature;
		multiFolderLoggingLevel = isSingleLevelMediaPath ? Level.WARNING : Level.INFO;
		mediaFilePathMap = new LinkedHashMap<>();
		mediaFilePathList = new ArrayList<>();
		mediaFileList = new ArrayList<>();
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
		mediaFileList.clear();
		mediaFilePathMap.clear();
		isConnected = Files.exists(rootPath);
	}
	
	private class MediaFileVisitor extends SimpleFileVisitor<Path> {
		
		@Override
		public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attr) {
			if (mediaFilePathMap.containsKey(file)) {
    			// already in inventory
    			
    			return FileVisitResult.SKIP_SUBTREE;
    			
    		} else {
    			return FileVisitResult.CONTINUE;
    		}
		}
		
    	@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
    		
    		if ((attr.isRegularFile()) && isMediaFileName(file, contentNature)) {
    			// It should be a media file, part of an album

    			validateMediaFilePath(file.getParent());
    			return FileVisitResult.SKIP_SIBLINGS;
    		} else {
    			return FileVisitResult.CONTINUE;
    		}
    	}
	}
	
	private MediaFilePath addMediaFilePathToInventory(Path mediaFolderAbsolutePath, List<MediaFile> mediaFiles, Path coverPath, String mediaFileExtension) {
		
		if (! mediaFilePathMap.containsKey(mediaFolderAbsolutePath)) {
			MediaFilePath newMediaFilePath = new MediaFilePath(mediaFolderAbsolutePath, contentNature, mediaFiles, coverPath, mediaFileExtension);
			mediaFilePathMap.put(mediaFolderAbsolutePath, newMediaFilePath);
			mediaFilePathList.add(newMediaFilePath);
			mediaFileList.addAll(newMediaFilePath.getMediaFiles());
		}
		return mediaFilePathMap.get(mediaFolderAbsolutePath);
	}
	
	private boolean checkInventoryKey(Path inventoryKey, String artist, String albumTitle) {
		return inventoryKey.toString().toLowerCase().contains((artist + SEPARATOR + albumTitle).toLowerCase());
	}
	
	private boolean checkInventoryKey(Path inventoryKey, String pathPart) {
		return inventoryKey.toString().toLowerCase().contains(pathPart.toLowerCase());
	}
	
	public Set<MediaFilePath> getPotentialMediaPath(Album album) {
		
		String title = album.getTitre();
		List<Artiste> auteurs = album.getAuteurs();
		
		Map<Path,MediaFilePath> potentialMediaPath = mediaFilePathMap.entrySet().stream()
			.filter(inventoryEntry -> checkInventoryKey(inventoryEntry.getKey(), title))
			.filter(inventoryEntry -> 
				(auteurs.isEmpty() ||
				(auteurs.stream()
						.map(auteur -> auteur.getNomComplet())
						.anyMatch(auteurName -> checkInventoryKey(inventoryEntry.getKey(), auteurName)))))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		if (potentialMediaPath.size() > 1) {
			Set<MediaFilePath> potentialMediaPath2 = potentialMediaPath.entrySet().stream()
				.filter(inventoryEntry -> auteurs.stream()
						.map(auteur -> auteur.getNomComplet())
						.anyMatch(auteurName -> checkInventoryKey(inventoryEntry.getKey(), auteurName, title)))
				.map(inventoryEntry -> inventoryEntry.getValue())
				.collect(Collectors.toSet());
			
			if (potentialMediaPath2.isEmpty()) {
				return new HashSet<>(potentialMediaPath.values());
			} else {
				return potentialMediaPath2;
			}
		} else {
			return new HashSet<>(potentialMediaPath.values());
		}
	}
	
	public int getNbMediaPath() {
		return mediaFilePathMap.size();
	}
	
	public List<MediaFilePath> getMediaFilePathList() {
		return mediaFilePathList;
	}
	
	public List<MediaFile> getMediaFileList() {
		return mediaFileList;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public int getMediaFileNumber() {
		return mediaFileList.size();
	}
	
	public MediaFilePath validateMediaFilePath(Path path) {
		
		if (! isConnected()) {
			return null;
		}
		
		if (Files.exists(path)) {
			
			if (path.startsWith(rootPath)) {

				MediaPathValidatorVisitor mediaPathValidatorVisitor = new MediaPathValidatorVisitor(multiFolderLoggingLevel);

				try {
					Files.walkFileTree(path, mediaPathValidatorVisitor);

					if (mediaPathValidatorVisitor.isMediaFilePresent()) {
						
						Set<String> mediaFileExtensions = mediaPathValidatorVisitor.getMediaFileExtensions();
						
						// Check media files extension (should all be the same)
						final String mediaFileExtension;
						
						if (mediaFileExtensions.isEmpty()) {
							mediaFileExtension = "";
							albumLog.log(multiFolderLoggingLevel, () -> "No media file found directly under " + path);
						} else if (mediaFileExtensions.size() == 1) {
							mediaFileExtension = mediaFileExtensions.iterator().next();
						} else {
							albumLog.log(multiFolderLoggingLevel, () -> "More than 1 media file type found in " + path);
							mediaFileExtension = mediaFileExtensions.toString();
						}
						
						return addMediaFilePathToInventory(path, mediaPathValidatorVisitor.getMediaFiles(), mediaPathValidatorVisitor.getCoverPath(), mediaFileExtension);
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
	
	
	private static final String COVER_START_NAME = "cover.";
	private static final Set<String> coverExtensions = Set.of("jpg", "png");
	private static final Set<String> infoFileExtensions = Set.of("pdf","crt", "bup", "ifo", "idx2", "clpi", "xml", "png", "bdjo", "jpg", "grn", "txt", "jar", "srt", "map");
	
	private class MediaPathValidatorVisitor extends SimpleFileVisitor<Path> {
		
		private final List<MediaFile> mediaFiles;
		private final Set<String> mediaFileExtensions;
		private Path coverPath;
		private final Level multiFolderLoggingLevel;
		private boolean topLevelVisited;
		
		MediaPathValidatorVisitor(Level multiFolderLoggingLevel) {
			super();
			this.mediaFiles = new ArrayList<MediaFile>();
			this.mediaFileExtensions = new HashSet<>();
			this.multiFolderLoggingLevel = multiFolderLoggingLevel;
			coverPath = null;
			topLevelVisited = false;
		}
		
		boolean isMediaFilePresent() {
			return mediaFiles.size() > 0;
		}
		
    	@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
    		
    		if (attr.isRegularFile()) {
    			
    			String extension = getFileNameExtension(path);			
				if (extension == null) {
					albumLog.warning("Unexpected file with no extension in media path : " + path);
				} else {
					
					MediaFile mediaFile = MediaFileBuilder.builder(contentNature, path, extension).build();
					if (mediaFile != null) {
						// It should be a media file, part of an album
						
						mediaFileExtensions.add(extension);
						
						if (Control.isReadMediaFileMetadata()) {
							// Media file metadata parsing
							mediaFile.getMetadata();
						}
						mediaFiles.add(mediaFile);
					} else if (path.getFileName().toString().toLowerCase().startsWith(COVER_START_NAME) &&
							coverExtensions.contains(extension.toLowerCase())) {
						coverPath = path;
					} else if (infoFileExtensions.contains(extension.toLowerCase())) {
						albumLog.fine("Information file found in media path " + path);
					} else {
						albumLog.warning("Unexpected file in media path : " + path);
					}
				}
    		}
    		return FileVisitResult.CONTINUE;
    	}

		@Override
		public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attr) {
			
			if (topLevelVisited) {
				albumLog.log(multiFolderLoggingLevel, () -> "Sub folder found in media path " + file);
			} else {
				topLevelVisited = true;
			}
    		return FileVisitResult.CONTINUE;
		}
		
		public List<MediaFile> getMediaFiles() {
			return mediaFiles;
		}

		public Set<String> getMediaFileExtensions() {
			return mediaFileExtensions;
		}

		public Path getCoverPath() {
			return coverPath;
		}
	}
	
	static boolean isMediaFileName(Path file, ContentNature mediaContentNature) {

		String extension = getFileNameExtension(file);
		if (extension != null) {
			return mediaContentNature.getFileExtensions().contains(extension.toLowerCase());
		} else {
			return false;
		}
	}
	
	private static String getFileNameExtension(Path path) {
		
		String fileName = path.toString();
	    int dotIndex = fileName.lastIndexOf(".");
	    if (dotIndex >= 0) {
	        return fileName.substring(dotIndex + 1);
	    } else {
	    	return null;
	    }		
	}
}
