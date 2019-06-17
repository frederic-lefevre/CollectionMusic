package org.fl.collectionAlbum;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.rapportHtml.HtmlReportPrintable;
import org.fl.collectionAlbum.rapportHtml.RapportHtml;
import org.fl.collectionAlbumGui.ProgressInformation;
import org.fl.collectionAlbumGui.ProgressInformationPanel;

import com.ibm.lge.fl.util.file.FilesUtils;

public class GenerationSiteCollection  extends SwingWorker<String,ProgressInformation> {

	private CollectionAlbumContainer albumsContainer ;
	private Logger albumLog ;
	private HtmlLinkList accueils ;
	private ProgressInformationPanel progressPanel;
	
	private int rapportIndex ;
	
	public GenerationSiteCollection(ProgressInformationPanel pip, Logger aLog) {
		
		albumLog 		= aLog;
		progressPanel 	= pip ;
		accueils 		= Control.getAccueils() ;
		rapportIndex 	= -1 ;
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
	   	
		albumsContainer.getCollectionAlbumsMusiques().rapportAdditionnalInfo() ;
	   
	   	String styles[] = { RapportHtml.mainStyle, RapportHtml.formatStyle } ;
	     	
		String rapportFileName = new String(rapportDir.getAbsolutePath() + File.separator + Control.getHomecollectionfile()) ;
		File rapportFile = new File(rapportFileName) ;
		RapportHtml rapport = new RapportHtml("Collections d'albums", true, rapportFile, accueils, "", albumLog) ;
			
		rapport.enteteRapport(styles) ;
		rapport.write("<h3>Classement des auteurs, interpretes et chefs d'orchestre (artistes, groupes, ensembles)</h3>\n<ul>\n") ;
		
		generationRapportHtml(rapport, rapportDir, "Classement alphabethique", 						  albumsContainer.getCollectionArtistes(), ListeArtiste.rapportAlpha,  "") ;
		generationRapportHtml(rapport, rapportDir, "Classement par nombre d'unit&eacute;s physiques", albumsContainer.getCollectionArtistes(), ListeArtiste.rapportPoids,  "") ;
		generationRapportHtml(rapport, rapportDir, "Classement chronologique", 						  albumsContainer.getCollectionArtistes(), ListeArtiste.rapportChrono, "") ;
		generationRapportHtml(rapport, rapportDir, "Calendrier", 						  			  albumsContainer.getCalendrierArtistes(), 0, "") ;
		
		rapport.write("</ul>\n<h3>Classement des albums</h3>\n<ul>\n") ;
		generationRapportHtml(rapport, rapportDir, "Classement chronologique (enregistrement)", albumsContainer.getCollectionAlbumsMusiques(), ListeAlbum.rapportChronoEnregistrement,  "") ;
		generationRapportHtml(rapport, rapportDir, "Classement chronologique (composition)", 	albumsContainer.getCollectionAlbumsMusiques(), ListeAlbum.rapportChronoComposition,  "") ;

		rapport.write("</ul>\n<h3>Rangement des albums</h3>\n<ul>\n") ;
		for (Format.RangementSupportPhysique rangement : Format.RangementSupportPhysique.values()) {
			generationRapportHtml(rapport, rapportDir, rangement.getDescription(), albumsContainer.getRangementAlbums(rangement), ListeAlbum.rapportRangement, "") ;
		}

		rapport.write("</ul>\n<h3>Statistiques</h3>\n<ul>\n") ;
		generationRapportHtml(rapport, rapportDir, "Statistiques par année d'enregistrement: Nombre d'unit&eacute;s physiques",  albumsContainer.getStatChronoEnregistrement(), 0, "") ;
		generationRapportHtml(rapport, rapportDir, "Statistiques par décennie de composition: Nombre d'unit&eacute;s physiques", albumsContainer.getStatChronoComposition(), 	0, "") ;

		rapport.write("  <li>Nombre d'albums: " + albumsContainer.getCollectionAlbumsMusiques().getNombreAlbums()) ;
		rapport.write("</li>\n  <li>Nombre d'artistes, de groupes et d'ensemble: " + albumsContainer.getCollectionArtistes().getNombreArtistes()) ;
		rapport.write("</li>\n  <li>Nombre d'unit&eacute;s physiques:\n<table>\n  <tr>\n") ;
		albumsContainer.getCollectionAlbumsMusiques().getFormatListeAlbum().enteteFormat(rapport, "total", 1) ;
		rapport.write("  </tr>\n  <tr>\n") ;
		albumsContainer.getCollectionAlbumsMusiques().getFormatListeAlbum().rowFormat(rapport, "total") ;
		rapport.write("  </tr>\n</table>\n</li>\n</ul>\n") ;
		rapport.finRapport() ;			
	}
	
	 private void generationRapportHtml(RapportHtml rapport, File rapportDir, String titre, HtmlReportPrintable hpr, int typeRapport, String urlOffset) {
		 RapportHtml rapHtml = new RapportHtml(titre, true, getNextRapportFile(rapportDir), accueils,  "", albumLog) ;
		 rapport.write(rapHtml.printReport(hpr, typeRapport, urlOffset)) ;
	 }
	 
	 private File getNextRapportFile(File rapportDir) {
		 rapportIndex++ ;
		 return new File(rapportDir + File.separator +  "r" + rapportIndex + ".html") ;
	 }
	 
	 private void rapportsConcertHtml(File rapportDir) {

		 albumsContainer.getConcerts().rapportAdditionnalInfo() ;

		 String styles[] = { RapportHtml.mainStyle, RapportHtml.formatStyle };

		 String rapportFileName = new String(rapportDir.getAbsolutePath() + File.separator + Control.getHomeconcertfile());
		 File rapportFile = new File(rapportFileName);
		 RapportHtml rapport = new RapportHtml("Concerts", true, rapportFile, accueils,  "", albumLog) ;

		 rapport.enteteRapport(styles);
		 rapport.write("<h3>Classement des auteurs, interpretes et chefs d'orchestre (artistes, groupes, ensembles)</h3>\n<ul>\n");
		 generationRapportHtml(rapport, rapportDir, "Classement alphabethique", 			albumsContainer.getConcertsArtistes(), ListeArtiste.rapportConcertAlpha,  "") ;
		 generationRapportHtml(rapport, rapportDir, "Classement par nombre de concerts", albumsContainer.getConcertsArtistes(), ListeArtiste.rapportConcertPoids,  "") ;

		 rapport.write("</ul>\n<h3>Classement des concerts</h3>\n<ul>\n");
		 generationRapportHtml(rapport, rapportDir, "Classement chronologique", albumsContainer.getConcerts(), 0,  "") ;

		 rapport.write("  <li>Nombre de concerts: " + albumsContainer.getConcerts().getNombreConcerts());
		 rapport.write("</li>\n  <li>Nombre d'artistes, de groupes et d'ensemble: " + albumsContainer.getConcertsArtistes().getNombreArtistes());

		 rapport.write("</li>\n</ul>\n");
		 rapport.finRapport();	
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