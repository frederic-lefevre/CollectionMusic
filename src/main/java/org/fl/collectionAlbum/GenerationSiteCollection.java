package org.fl.collectionAlbum;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import org.fl.collectionAlbum.rapportHtml.HtmlLinkList;
import org.fl.collectionAlbum.rapportHtml.RapportCollection;
import org.fl.collectionAlbum.rapportHtml.RapportDesConcerts;
import org.fl.collectionAlbum.rapportHtml.RapportHtml;
import org.fl.collectionAlbumGui.ProgressInformation;
import org.fl.collectionAlbumGui.ProgressInformationPanel;

import com.ibm.lge.fl.util.file.FilesUtils;

public class GenerationSiteCollection  extends SwingWorker<String,ProgressInformation> {

	private CollectionAlbumContainer albumsContainer ;
	private Logger albumLog ;
	private HtmlLinkList accueils ;
	private ProgressInformationPanel progressPanel;
	
	public GenerationSiteCollection(ProgressInformationPanel pip, Logger aLog) {
		
		albumLog 		= aLog;
		progressPanel 	= pip ;
		accueils 		= Control.getAccueils() ;
	}

	 @Override 
	 public String doInBackground() {
  		
  		albumsContainer = CollectionAlbumContainer.getInstance(albumLog) ;
  		
		albumLog.info("Nettoyage de l'ancien site");
		publish(new ProgressInformation("En cours de génération", "Nettoyage de l'ancien site")) ;
		cleanRapport() ;
		albumLog.info("Ecriture du nouveau site") ;
		publish(new ProgressInformation("En cours de génération", "Ecriture du nouveau site")) ;
		rapportCollection() ;
		albumLog.info("Fin de la génération");
		return "" ;	
  	}
	
	 private void cleanRapport() {

		 try {
			 Path rDir  = Paths.get(Control.getRapportPath());
			 Path roDir = Paths.get(Control.getOldRapportPath());	
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

			 Path raDir  = Paths.get(Control.getAbsoluteAlbumDir());
			 if (! Files.exists(raDir)) {
				 Files.createDirectories(raDir) ;
			 }

			 Path rcDir  = Paths.get(Control.getAbsoluteConcertDir());
			 if (! Files.exists(rcDir)) {
				 Files.createDirectories(rcDir) ;
			 }
			 
			 albumLog.fine("Ancien rapport effacé");
		 } catch (Exception e) {
			 albumLog.log(Level.SEVERE, "exception dans getPath des directories rapports : ", e);
		 }
	 }
	
	 private void rapportCollection() {

		 File rDir = new File(Control.getRapportPath());

		 albumsContainer.setHtmlIds() ;
		 rapportsHtml(rDir) ;
		 rapportsConcertHtml(rDir) ;
	 }
	   
	 private void rapportsHtml(File rapportDir) {	
	   	
	   	String styles[] = { RapportHtml.mainStyle, RapportHtml.formatStyle } ;
	     	
		String rapportFileName = new String(rapportDir.getAbsolutePath() + File.separator + Control.getHomecollectionfile()) ;
		File rapportFile = new File(rapportFileName) ;
		RapportCollection rapportCollection = new RapportCollection(albumsContainer, rapportDir, "Collections d'albums", accueils, "", albumLog) ;
		rapportCollection.printReport(rapportFile, styles) ;				
	}
		 
	private void rapportsConcertHtml(File rapportDir) {

		 String styles[] = { RapportHtml.mainStyle, RapportHtml.formatStyle };

		 String rapportFileName = new String(rapportDir.getAbsolutePath() + File.separator + Control.getHomeconcertfile());
		 File rapportFile = new File(rapportFileName) ;
		 RapportDesConcerts rapportConcerts = new RapportDesConcerts(albumsContainer, rapportDir, "Concerts",  accueils, "", albumLog) ;
		 rapportConcerts.printReport(rapportFile, styles) ;
	 }
		
	 @Override
     public void done() {

	        progressPanel.setStepInfos("Arreté");
	        progressPanel.setProcessStatus("Nouveau site généré");
	 }
	 
	 @Override
	 public void process(List<ProgressInformation> lp) {
		 
		 ProgressInformation latestResult = lp.get(lp.size() - 1);
		 
	        progressPanel.setStepInfos(latestResult.getInformation());
	        progressPanel.setProcessStatus(latestResult.getStatus());
	 }
}