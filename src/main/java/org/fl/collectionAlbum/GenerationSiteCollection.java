package org.fl.collectionAlbum;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.rapportHtml.RapportCollection;
import org.fl.collectionAlbum.rapportHtml.RapportDesConcerts;
import org.fl.collectionAlbum.rapportHtml.RapportStructuresAndNames;
import org.fl.collectionAlbum.rapportHtml.CssStyles;
import org.fl.collectionAlbumGui.ProgressInformation;
import org.fl.collectionAlbumGui.ProgressInformationPanel;
import org.fl.util.file.FilesUtils;

public class GenerationSiteCollection  extends SwingWorker<String,ProgressInformation> {

	private CollectionAlbumContainer albumsContainer ;
	private final Logger albumLog ;
	private final ProgressInformationPanel progressPanel;
	
	// Information prefix
	private final static String ARRET 			= "Arreté" ;
	private final static String FIN_GENERATION	= "Nouveau site généré" ;
	private final static String CLEANUP 		= "Nettoyage de l'ancien site" ;
	private final static String ECRITURE 		= "Ecriture du nouveau site" ;
	
	// Status
	private final static String GENERATION 	    = "En cours de génération" ;

	public GenerationSiteCollection(ProgressInformationPanel pip, Logger aLog) {
		
		albumLog 		= aLog;
		progressPanel 	= pip ;
	}

	 @Override 
	 public String doInBackground() {
  		
  		albumsContainer = CollectionAlbumContainer.getInstance(albumLog) ;
  		progressPanel.setProcessStatus(GENERATION) ;
  		
		albumLog.info("Nettoyage de l'ancien site") ;
		progressPanel.setStepPrefixInformation(CLEANUP) ;
		cleanRapport() ;
		
		albumLog.info("Ecriture du nouveau site") ;
		progressPanel.setStepPrefixInformation(ECRITURE) ;
		rapportCollection() ;
		
		albumLog.info("Fin de la génération");
		return "" ;	
  	}
	
	 private void cleanRapport() {

		 try {
			 Path rDir  = RapportStructuresAndNames.getRapportPath() ;
			 Path roDir = RapportStructuresAndNames.getOldRapportPath() ;
			 albumLog.info("Rapport path=" + rDir);
			 albumLog.info("Rapport od path=" + roDir);

			 // delete old rapport directory
			 if (Files.exists(roDir)) {
				 try {
					 albumLog.finest("Destruction de la directorie des rapports anciens " + roDir );
					 FilesUtils.deleteDirectoryTree(roDir, false, albumLog);
				 } catch (Exception e) {
					 albumLog.log(Level.SEVERE, "Impossible de détruire la directorie des rapports anciens: " + roDir, e);
				 }
			 } else {
				 albumLog.warning("La directorie des rapports anciens n'existe pas: " + roDir);
			 }

			 // move rapport directory to old rapport
			 if (Files.exists(rDir)) {
				 albumLog.finest("Deplacement de la directorie des rapports " + roDir );
				 try {
					 Files.move(rDir, roDir) ;
				 } catch (Exception e1) {

					 albumLog.log(Level.SEVERE, "Impossible de renommer la directorie des rapports: " + rDir + " Essai de delete", e1) ;
					 try {
						 FilesUtils.deleteDirectoryTree(roDir, false, albumLog);
					 } catch (Exception e) {
						 albumLog.log(Level.SEVERE, "Impossible de détruire la directorie des rapports anciens: " + roDir, e);
					 }
				 }
			 } else {
				 albumLog.warning("La directorie des rapports n'existe pas: " + rDir);
			 }

			 if (! Files.exists(rDir)) {
				 Files.createDirectories(rDir) ;
			 }

			Files.createDirectories(RapportStructuresAndNames.getAbsoluteAlbumDir()) ;
			Files.createDirectories(RapportStructuresAndNames.getAbsoluteConcertDir()) ;
			Files.createDirectories(RapportStructuresAndNames.getAbsoluteArtisteAlbumDir()) ;
			Files.createDirectories(RapportStructuresAndNames.getAbsoluteArtisteConcertDir()) ;
			Files.createDirectories(RapportStructuresAndNames.getAbsoluteLieuDir()) ;
			 
			 albumLog.fine("Ancien rapport effacé");
		 } catch (Exception e) {
			 albumLog.log(Level.SEVERE, "exception dans getPath des directories rapports : ", e);
		 }
	 }
	
	 private void rapportCollection() {

		 Path rapportDir = RapportStructuresAndNames.getRapportPath() ;
		 
		 ListeArtiste reunionArtistes = albumsContainer.getCollectionArtistes().getReunion(albumsContainer.getConcertsArtistes());
		 RapportStructuresAndNames.createRapports(reunionArtistes,
				 albumsContainer.getCollectionAlbumsMusiques(), 
				 albumsContainer.getConcerts(), 
				 albumsContainer.getLieuxDesConcerts());
		 rapportsHtml(rapportDir) ;
		 rapportsConcertHtml(rapportDir) ;
	 }
	   
	 private void rapportsHtml(Path rapportDir) {	
	   	
		Path rapportFile = RapportStructuresAndNames.getAbsoluteHomeCollectionFile() ;
		RapportCollection rapportCollection = new RapportCollection(albumsContainer, rapportDir, "Collections d'albums", albumLog) ;
		rapportCollection.printReport(rapportFile, CssStyles.mainFormat) ;				
	}
		 
	private void rapportsConcertHtml(Path rapportDir) {

		 Path rapportFile = RapportStructuresAndNames.getAbsoluteHomeConcertFile() ;
		 RapportDesConcerts rapportConcerts = new RapportDesConcerts(albumsContainer, rapportDir, "Concerts", albumLog) ;
		 rapportConcerts.printReport(rapportFile, CssStyles.main) ;
	 }
		
	@Override
	public void done() {

		progressPanel.setStepInformation("");
    	progressPanel.setStepPrefixInformation(ARRET);
    	progressPanel.setProcessStatus(FIN_GENERATION);
	}

	@Override
	public void process(List<ProgressInformation> lp) {

		ProgressInformation latestResult = lp.get(lp.size() - 1);
		progressPanel.setStepInformation(latestResult.getInformation());
	}
}