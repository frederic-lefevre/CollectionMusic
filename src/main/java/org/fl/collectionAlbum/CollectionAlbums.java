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

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.json.migrator.MusicArtefactMigrator;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;
import org.fl.collectionAlbumGui.AlbumsTableModel;
import org.fl.collectionAlbumGui.ProgressInformation;
import org.fl.collectionAlbumGui.ProgressInformationPanel;
import org.fl.util.json.JsonUtils;

import com.google.gson.JsonObject;

public class CollectionAlbums extends SwingWorker<CollectionAlbumContainer,ProgressInformation>{
	
	private final static Logger albumLog = Control.getAlbumLog();
	
	private CollectionAlbumContainer albumsContainer ;
	private final ProgressInformationPanel progressPanel;
	private final AlbumsTableModel albumsTableModel;
	
	// Information prefix
	private final static String ARRET 			= "Arreté" ;
	private final static String EN_EXAMEN		= "Dossier examiné: " ;
	
	// Status
	private final static String LECTURE_ALBUM 	= "Lecture des albums" ;
	private final static String LECTURE_CONCERT = "Lecture des  concerts" ;
	private final static String CALENDARS 		= "Construction des calendriers" ;
	private final static String FIN_LECTURE		= "Collection chargée" ;
	
	public CollectionAlbums(AlbumsTableModel albumsTableModel, ProgressInformationPanel pip) {

		progressPanel = pip;
		this.albumsTableModel = albumsTableModel;
	}
   	
	@Override 
	public CollectionAlbumContainer doInBackground() {

		progressPanel.setStepInformation("");

		MediaFilesInventories.rebuildInventories();
		DiscogsInventory.rebuildDiscogsInventory();
		
		albumsContainer = CollectionAlbumContainer.getEmptyInstance() ;
		progressPanel.setStepPrefixInformation(EN_EXAMEN);

		albumLog.info("Lecture des données des albums") ;		
		progressPanel.setProcessStatus(LECTURE_ALBUM) ;
		buildAlbumsCollection() ;
		
		albumLog.info("Lecture des données des concerts");
		progressPanel.setProcessStatus(LECTURE_CONCERT);
		buildConcerts() ;	
		
		albumLog.info("Construction du calendrier") ;
		progressPanel.setProcessStatus(CALENDARS);
		buildCalendrier() ;
		
		// Sort for display when scanning the collection
		albumsContainer.getCollectionAlbumsMusiques().sortRangementAlbum();
				
		return albumsContainer;
	}
   	
	
	private void buildAlbumsCollection() {
  	
		Path albumsPath = Control.getCollectionDirectoryName() ;
		try {
			MusicFileVisitor albumsVisitor = new AlbumFileVisitor(Control.getMusicfileExtension()) ;
			
			Files.walkFileTree(albumsPath, albumsVisitor) ;
				
		} catch (Exception e) {
			albumLog.log(Level.SEVERE, "Exception scanning album directory " + albumsPath, e);
		}
		
	}
  
	private void buildConcerts() {

		Path concertsPath = Control.getConcertDirectoryName() ;
		try {
			MusicFileVisitor concertsVisitor = new ConcertFileVisitor(Control.getMusicfileExtension()) ;
			
			Files.walkFileTree(concertsPath, concertsVisitor) ;
			
		} catch (Exception e) {
			albumLog.log(Level.SEVERE, "Exception scanning concert directory " + concertsPath, e);
		}
	}
	
    private abstract class MusicFileVisitor extends SimpleFileVisitor<Path> {
    	
    	private PathMatcher matcher ;
    	
    	protected MusicFileVisitor(String fileExtension) {
    		matcher = FileSystems.getDefault().getPathMatcher("glob:*." + fileExtension) ;
    	}
    	
    	@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
    		
    		Path name = file.getFileName() ;
    		if ((Files.isRegularFile(file)) &&
	    		(matcher.matches(name))) {
    			
	    		JsonObject arteFactJson = JsonUtils.getJsonObjectFromPath(file, Control.getCharset(), albumLog) ;
	    		if (arteFactJson != null) {
	    			addMusicArtefact(arteFactJson, file) ;
	    		} else {
	    			albumLog.warning("Impossible de lire le fichier json " + file);
	    		}

    		}
    		return FileVisitResult.CONTINUE;
    	}
    	
    	@Override
		public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attr) {
    		 publish(new ProgressInformation(file.getFileName().toString())) ;
    		 return FileVisitResult.CONTINUE;
    	}

    	public abstract void addMusicArtefact(JsonObject artefactJson, Path jsonFile) ;
    }
    
    private class AlbumFileVisitor extends MusicFileVisitor {
		protected AlbumFileVisitor(String fileExtension) {	super(fileExtension); }

		@Override
		public void addMusicArtefact(JsonObject artefactJson, Path jsonFile) {
			
			// Migration if needed
			JsonObject migratedJson = MusicArtefactMigrator.getMigrator().migrateAlbum(artefactJson, jsonFile);
			
			albumsContainer.addAlbum(migratedJson, jsonFile) ;			
		}    	
    }
    
    private class ConcertFileVisitor extends MusicFileVisitor {
		protected ConcertFileVisitor(String fileExtension) {	super(fileExtension); }

		@Override
		public void addMusicArtefact(JsonObject artefactJson, Path jsonFile) {		
			albumsContainer.addConcert(artefactJson, jsonFile) ;			
		}    	
    }
    
    private void buildCalendrier() {

    	try {
    		ListeArtiste allArtistes =  albumsContainer.getCollectionArtistes().getReunion(albumsContainer.getConcertsArtistes())  ;
    		for (Artiste a : allArtistes.getArtistes()) {
    			albumsContainer.getCalendrierArtistes().add(a) ;
    		}
    	} catch (Exception e) {
    		albumLog.log(Level.SEVERE, "Exception in build calendrier ", e);
    	}
    }

    @Override
    public void done() {

    	progressPanel.setStepInformation("");
    	progressPanel.setStepPrefixInformation(ARRET);
    	progressPanel.setProcessStatus(FIN_LECTURE);
    	albumsTableModel.fireTableDataChanged();
    }

    @Override
    public void process(List<ProgressInformation> lp) {

    	ProgressInformation latestResult = lp.get(lp.size() - 1);
    	progressPanel.setStepInformation(latestResult.getInformation());
    }
}
