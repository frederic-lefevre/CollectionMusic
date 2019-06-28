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
import org.fl.collectionAlbum.rapportHtml.RapportStructuresAndNames;
import org.fl.collectionAlbumGui.ProgressInformation;
import org.fl.collectionAlbumGui.ProgressInformationPanel;

import com.google.gson.JsonObject;
import com.ibm.lge.fl.util.json.JsonUtils;

public class CollectionAlbums extends SwingWorker<CollectionAlbumContainer,ProgressInformation>{
	
	private static int ALBUM = 0 ;
	private static int CONCERT = 1 ;

	private CollectionAlbumContainer albumsContainer ;
	private Logger albumLog ;
	private ProgressInformationPanel progressPanel;
	
	public CollectionAlbums(ProgressInformationPanel pip, Logger aLog) {
		
		albumLog = aLog;
		progressPanel = pip ;
	}
   	
	 @Override 
	 public CollectionAlbumContainer doInBackground() {
		
		albumsContainer = CollectionAlbumContainer.getEmptyInstance(albumLog) ;
		
		albumLog.info("Lecture des données des albums");
		buildAlbumsCollection() ;
		albumLog.info("Lecture des données des concerts");
		buildConcerts() ;	
		albumLog.info("Construction du calendrier") ;
		buildCalendrier() ;
		return albumsContainer;
	}
   	
	
	private void buildAlbumsCollection() {
  	
		Path albumsPath = RapportStructuresAndNames.getCollectionDirectoryName() ;
		try {
			MusicFileVisitor albumsVisitor = new MusicFileVisitor(Control.getMusicfileExtension(), ALBUM) ;
			
			Files.walkFileTree(albumsPath, albumsVisitor) ;
				
		} catch (Exception e) {
			albumLog.log(Level.SEVERE, "Exception scanning album directory " + albumsPath, e);
		}
		
	}
  
	private void buildConcerts() {

		Path concertsPath = RapportStructuresAndNames.getConcertDirectoryName() ;
		try {
			MusicFileVisitor concertsVisitor = new MusicFileVisitor(Control.getMusicfileExtension(), CONCERT) ;
			
			Files.walkFileTree(concertsPath, concertsVisitor) ;
				
		} catch (Exception e) {
			albumLog.log(Level.SEVERE, "Exception scanning concert directory " + concertsPath, e);
		}
	}
	
    private class MusicFileVisitor extends SimpleFileVisitor<Path> {
    	
    	private PathMatcher matcher ;
    	private int			musicFileType;
    	
    	protected MusicFileVisitor(String fileExtension, int mft) {
    		
    		musicFileType = mft ;
    		if ((musicFileType != ALBUM) && (musicFileType != CONCERT)) {
    			albumLog.severe("unkown music file type: " + musicFileType);
    		}
    		matcher = FileSystems.getDefault().getPathMatcher("glob:*." + fileExtension);
    	}
    	
    	@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
    		
    		Path name = file.getFileName() ;
    		if (Files.isRegularFile(file)) {
	    		if (matcher.matches(name)) {
	    			JsonObject arteFactJson = JsonUtils.getJsonObjectFromPath(file, Control.getCharset(), albumLog) ;
	    			if (musicFileType == ALBUM) {
	    				albumsContainer.addAlbum(arteFactJson) ;
	    			} else if (musicFileType == CONCERT) {
	    				albumsContainer.addConcert(arteFactJson) ;
	    			}
	    		}
    		}
    		return FileVisitResult.CONTINUE;
    	}
    	
    	@Override
		public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attr) {
    		 publish(new ProgressInformation("En cours de lecture", "Dossier examiné: " + file.getFileName())) ;
    		 return FileVisitResult.CONTINUE;
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

    	progressPanel.setStepInfos("Arreté");
    	progressPanel.setProcessStatus("Collection chargée");
    }

    @Override
    public void process(List<ProgressInformation> lp) {

    	ProgressInformation latestResult = lp.get(lp.size() - 1);

    	progressPanel.setStepInfos(latestResult.getInformation());
    	progressPanel.setProcessStatus(latestResult.getStatus());
    }
}
