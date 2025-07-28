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

package org.fl.collectionAlbum;

import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;

import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.gui.ProgressInformation;
import org.fl.collectionAlbum.gui.ProgressInformationPanel;
import org.fl.collectionAlbum.json.migrator.MusicArtefactMigrator;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;
import org.fl.util.json.JsonUtils;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class CollectionAlbums extends SwingWorker<CollectionAlbumContainer,ProgressInformation> {
	
	private static final Logger albumLog = Logger.getLogger(CollectionAlbums.class.getName());
	
	private CollectionAlbumContainer albumsContainer;
	private final ProgressInformationPanel progressPanel;
	private final List<AbstractTableModel> tableModels;
	
	// Information prefix
	private static final String ARRET = "Arrêté";
	private static final String EN_EXAMEN = "Dossier examiné: ";

	// Status
	private static final String MEDIA_INVENTORY = "Inventaire media";
	private static final String INIT_MEDIA_INVENTORY = "Initialisation des inventaires des fichiers media";
	private static final String MEDIA_INVENTORY_PRORESS = "Inventaire des fichiers media en cours";
	private static final String DISCOGS_INVENTORY = "Inventaire discogs";
	private static final String DISCOGS_INVENTORY_PROGRESS = "Inventaire des releases discogs en cours";
	private static final String LECTURE_ALBUM = "Lecture des albums";
	private static final String LECTURE_CONCERT = "Lecture des concerts";
	private static final String CALENDARS = "Construction des calendriers";
	private static final String FIN_LECTURE = "Collection chargée";
	
	public CollectionAlbums(List<AbstractTableModel> tableModels, ProgressInformationPanel pip) {

		progressPanel = pip;
		this.tableModels = tableModels;
	}
	
	@Override 
	public CollectionAlbumContainer doInBackground() {

		publish(new ProgressInformation(MEDIA_INVENTORY, INIT_MEDIA_INVENTORY, ""));
		MediaFilesInventories.clearInventories();
		
		publish(new ProgressInformation(DISCOGS_INVENTORY, DISCOGS_INVENTORY_PROGRESS, ""));
		DiscogsInventory.buildDiscogsInventory();
		
		publish(new ProgressInformation(LECTURE_ALBUM, EN_EXAMEN, ""));
		albumsContainer = CollectionAlbumContainer.getEmptyInstance();
		buildAlbumsCollection();
		
		publish(new ProgressInformation(LECTURE_CONCERT, EN_EXAMEN, ""));
		buildConcerts();	
		
		publish(new ProgressInformation(CALENDARS, "", ""));
		buildCalendriers();
		
		publish(new ProgressInformation(MEDIA_INVENTORY, MEDIA_INVENTORY_PRORESS, ""));
		MediaFilesInventories.scanMediaFilePaths();
		
		// Sort for display when scanning the collection
		albumsContainer.getCollectionAlbumsMusiques().sortRangementAlbum();
				
		return albumsContainer;
	}
   	
	private void buildAlbumsCollection() {
  	
		Path albumsPath = Control.getCollectionDirectoryName();
		try {
			MusicFileVisitor albumsVisitor = new AlbumFileVisitor(Control.getMusicfileExtension());
			
			Files.walkFileTree(albumsPath, albumsVisitor);
				
		} catch (Exception e) {
			albumLog.log(Level.SEVERE, "Exception scanning album directory " + albumsPath, e);
		}
		
	}
  
	private void buildConcerts() {

		Path concertsPath = Control.getConcertDirectoryName();
		try {
			MusicFileVisitor concertsVisitor = new ConcertFileVisitor(Control.getMusicfileExtension());
			
			Files.walkFileTree(concertsPath, concertsVisitor);
			
		} catch (Exception e) {
			albumLog.log(Level.SEVERE, "Exception scanning concert directory " + concertsPath, e);
		}
	}
	
    private abstract class MusicFileVisitor extends SimpleFileVisitor<Path> {
    	
    	private PathMatcher matcher ;
    	
    	protected MusicFileVisitor(String fileExtension) {
    		matcher = FileSystems.getDefault().getPathMatcher("glob:*." + fileExtension);
    	}
    	
    	@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
    		
    		Path name = file.getFileName();
    		if ((Files.isRegularFile(file)) &&
	    		(matcher.matches(name))) {
    			
    			ObjectNode arteFactJson = (ObjectNode) JsonUtils.getJsonObjectFromPath(file, Control.getCharset(), albumLog);
	    		if (arteFactJson != null) {
	    			addMusicArtefact(arteFactJson, file);
	    		} else {
	    			albumLog.warning("Impossible de lire le fichier json " + file);
	    		}

    		}
    		return FileVisitResult.CONTINUE;
    	}
    	
    	@Override
		public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attr) {
    		 publish(new ProgressInformation(null, null, file.getFileName().toString()));
    		 return FileVisitResult.CONTINUE;
    	}

    	public abstract void addMusicArtefact(ObjectNode artefactJson, Path jsonFile);
    }
    
	private class AlbumFileVisitor extends MusicFileVisitor {
		protected AlbumFileVisitor(String fileExtension) {
			super(fileExtension);
		}

		@Override
		public void addMusicArtefact(ObjectNode artefactJson, Path jsonFile) {

			// Migration if needed
			ObjectNode migratedJson = MusicArtefactMigrator.getMigrator().migrateAlbum(artefactJson, jsonFile);

			albumsContainer.addAlbum(migratedJson, jsonFile);
		}
	}
    
	private class ConcertFileVisitor extends MusicFileVisitor {
		protected ConcertFileVisitor(String fileExtension) {
			super(fileExtension);
		}

		@Override
		public void addMusicArtefact(ObjectNode artefactJson, Path jsonFile) {
			albumsContainer.addConcert(artefactJson, jsonFile);
		}
	}

    private void buildCalendriers() {

    	albumsContainer.buildCalendriers();
    }

    @Override
    public void done() {

    	progressPanel.setProgressInformation(new ProgressInformation(FIN_LECTURE, ARRET, ""));
    	tableModels.forEach(AbstractTableModel::fireTableDataChanged);
    }

    @Override
    public void process(List<ProgressInformation> progressInformationList) {

    	if (progressInformationList != null) {
    		progressInformationList.forEach(progressInformation -> progressPanel.setProgressInformation(progressInformation));
    	}
    }
}
