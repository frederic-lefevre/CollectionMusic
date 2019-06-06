package org.fl.collectionAlbum;

import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbumGui.ProgressInformation;
import org.fl.collectionAlbumGui.ProgressInformationPanel;

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
		
		albumsContainer = Control.getCollectionContainer() ;
		albumsContainer.reset() ;
		
		albumLog.info("Lecture des données des albums");
		buildAlbumsCollection() ;
		albumLog.info("Lecture des données des concerts");
		buildConcerts() ;	
		albumLog.info("Construction du calendrier") ;
		buildCalendrier() ;
		return albumsContainer;
	}
   	
	
	private void buildAlbumsCollection() {
  	
		String dirName = Control.getAcDirectoryName() ;
		try {
			Path albumsPath = Paths.get(dirName) ;
			MusicFileVisitor albumsVisitor = new MusicFileVisitor(Control.getMusicfileExtension(), ALBUM) ;
			
			Files.walkFileTree(albumsPath, albumsVisitor) ;
				
		} catch (Exception e) {
			albumLog.log(Level.SEVERE, "Exception scanning album directory " + Control.getAcDirectoryName(), e);
		}
		
	}
  
	private void buildConcerts() {

		String dirName = Control.getConcertDirectoryName() ;
		try {
			Path concertsPath = Paths.get(dirName) ;
			MusicFileVisitor concertsVisitor = new MusicFileVisitor(Control.getMusicfileExtension(), CONCERT) ;
			
			Files.walkFileTree(concertsPath, concertsVisitor) ;
				
		} catch (Exception e) {
			albumLog.log(Level.SEVERE, "Exception scanning concert directory " + dirName, e);
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
	    			if (musicFileType == ALBUM) {
	    				albumsContainer.addAlbum(new Album(file, albumsContainer.getCollectionArtistes(), albumLog)) ;
	    			} else if (musicFileType == CONCERT) {
	    				albumsContainer.addConcert(new Concert(file, albumsContainer.getConcertsArtistes(), albumLog)) ;
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
		
		ListeArtiste allArtistes =  albumsContainer.getCollectionArtistes().getReunion(albumsContainer.getConcertsArtistes())  ;
		for (Artiste a : allArtistes.getArtistes()) {
			albumsContainer.getCalendrierArtistes().add(a) ;
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
