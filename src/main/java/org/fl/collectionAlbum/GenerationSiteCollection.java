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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.LieuConcert;
import org.fl.collectionAlbum.concerts.LieuxDesConcerts;
import org.fl.collectionAlbum.concerts.ListeConcert;
import org.fl.collectionAlbum.gui.ProgressInformation;
import org.fl.collectionAlbum.gui.ProgressInformationPanel;
import org.fl.collectionAlbum.rapportCsv.RapportCsv;
import org.fl.collectionAlbum.rapportHtml.RapportCollection;
import org.fl.collectionAlbum.rapportHtml.RapportConcert;
import org.fl.collectionAlbum.rapportHtml.RapportConcertsDunArtiste;
import org.fl.collectionAlbum.rapportHtml.RapportDesConcerts;
import org.fl.collectionAlbum.rapportHtml.RapportHtml;
import org.fl.collectionAlbum.rapportHtml.RapportListeConcerts;
import org.fl.collectionAlbum.rapportHtml.RapportStructuresAndNames;
import org.fl.collectionAlbum.rapportHtml.RapportHtml.LinkType;
import org.fl.collectionAlbum.rapportHtml.CssStyles;
import org.fl.collectionAlbum.rapportHtml.RapportAlbum;
import org.fl.collectionAlbum.rapportHtml.RapportAlbumsDunArtiste;
import org.fl.collectionAlbum.rapportHtml.RapportBuildInfo;
import org.fl.util.file.FilesUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

public class GenerationSiteCollection  extends SwingWorker<String,ProgressInformation> {

	private final static Logger albumLog = Logger.getLogger(GenerationSiteCollection.class.getName());
	
	private CollectionAlbumContainer collectionAlbumContainer;
	private final ProgressInformationPanel progressPanel;

	// Information prefix
	private final static String ARRET = "Arreté";
	private final static String FIN_GENERATION = "Nouveau site généré";
	private final static String CLEANUP = "Nettoyage de l'ancien site";
	private final static String ECRITURE = "Ecriture du nouveau site";
	private final static String RAPPORT_ALBUM = "Rapport des albums";
	private final static String RAPPORT_ARTISTE = "Rapport des artistes";
	private final static String RAPPORT_CONCERT = "Rapport des concerts";
	private final static String RAPPORT_MEDIA = "Rapport des medias";

	// Status
	private final static String GENERATION = "En cours de génération";

	public GenerationSiteCollection(ProgressInformationPanel pip) {
		progressPanel = pip;
	}

	@Override
	public String doInBackground() {

		try {
			collectionAlbumContainer = CollectionAlbumContainer.getInstance();
			publish(new ProgressInformation(GENERATION, "", ""));

			RapportHtml.withCharset(Control.getCharset());
			RapportStructuresAndNames.renew();

			publish(new ProgressInformation(null, CLEANUP, null));
			cleanRapport();

			publish(new ProgressInformation(null, ECRITURE, null));
			rapportCollection();

			// Sort for display when scanning the collection
			collectionAlbumContainer.getCollectionAlbumsMusiques().sortRangementAlbum();

			// Update collection and concert metrics history
			Control.getCollectionMetricsHsitory().addPresentCollectionMetricsToHistory(System.currentTimeMillis(), collectionAlbumContainer);
			Control.getConcertMetricsHsitory().addPresentConcertMetricsToHistory(System.currentTimeMillis(), collectionAlbumContainer);
			
			albumLog.info("Fin de la génération");

		} catch (Exception e) {
			albumLog.log(Level.SEVERE, "Exception dans la génération du site", e);
		}
		return "";
	}
	
	 private void cleanRapport() {

		 try {
			 Path rDir  = RapportStructuresAndNames.getRapportPath();
			 Path roDir = RapportStructuresAndNames.getOldRapportPath();
			 albumLog.info(() -> "Rapport path=" + rDir + "\nRapport old path=" + roDir);

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
				 albumLog.finest(() -> "Deplacement de la directorie des rapports " + roDir );
				 try {
					 Files.move(rDir, roDir);
				 } catch (Exception e1) {

					 albumLog.log(Level.SEVERE, "Impossible de renommer la directorie des rapports: " + rDir + " Essai de delete", e1);
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
				 Files.createDirectories(rDir);
			 }

			Files.createDirectories(RapportStructuresAndNames.getAbsoluteAlbumDir());
			Files.createDirectories(RapportStructuresAndNames.getAbsoluteConcertDir());
			Files.createDirectories(RapportStructuresAndNames.getAbsoluteArtisteAlbumDir());
			Files.createDirectories(RapportStructuresAndNames.getAbsoluteArtisteConcertDir());
			Files.createDirectories(RapportStructuresAndNames.getAbsoluteLieuDir());
			 
			 albumLog.fine("Ancien rapport effacé");
		 } catch (Exception e) {
			 albumLog.log(Level.SEVERE, "exception dans getPath des directories rapports : ", e);
		 }
	 }
	
	 private void rapportCollection() {

		 Path rapportDir = RapportStructuresAndNames.getRapportPath() ;
		 
		 ListeArtiste reunionArtistes = collectionAlbumContainer.getCollectionArtistes().getReunion(collectionAlbumContainer.getConcertsArtistes());
		 createRapports(reunionArtistes,
				 collectionAlbumContainer.getCollectionAlbumsMusiques(), 
				 collectionAlbumContainer.getConcerts(), 
				 collectionAlbumContainer.getLieuxDesConcerts());
		 
		 
		 rapportsHtml(rapportDir);
		 rapportsConcertHtml(rapportDir);
		 rapportBuildInfo();
		 
		 publish(new ProgressInformation(null, RAPPORT_MEDIA, null));
		 RapportCsv.writeCsvAudioFile(collectionAlbumContainer.getAlbumsWithAudioFile(), (audioFile) -> true, RapportStructuresAndNames.getAbsoluteCsvAudioFiles());
		 RapportCsv.writeCsvAudioFile(collectionAlbumContainer.getAlbumsWithHighResAudio(), (audioFile) -> audioFile.isHighRes(), RapportStructuresAndNames.getAbsoluteCsvHdAudioFiles());
	 }
	   
	 private void createRapports(ListeArtiste listeArtiste, 
			 ListeAlbum listeAlbum, 
			 ListeConcert listeConcert, 
			 LieuxDesConcerts lieuxDesConcerts) {

		 Path rapportPath = RapportStructuresAndNames.getRapportPath();
		 
		 publish(new ProgressInformation(null, RAPPORT_ARTISTE, null));
		 for (Artiste artiste : listeArtiste.getArtistes()) {	
			 if (artiste.getNbAlbum() > 0) {
				 Path albumAbsolutePath   = RapportStructuresAndNames.getArtisteAlbumRapportAbsolutePath(artiste);
				 if (! Files.exists(albumAbsolutePath)) {
					 RapportAlbumsDunArtiste rapportDeSesAlbums = new RapportAlbumsDunArtiste(artiste, getOffset(rapportPath, albumAbsolutePath.getParent()));
					 rapportDeSesAlbums.printReport(albumAbsolutePath, CssStyles.stylesTableauDunArtiste);
				 }
			 }

			 if (artiste.getNbConcert() > 0) {
				 Path concertAbsolutePath = RapportStructuresAndNames.getArtisteConcertRapportAbsolutePath(artiste);
				 if (! Files.exists(concertAbsolutePath)) {
					 RapportConcertsDunArtiste rapportDeSesConcerts = new RapportConcertsDunArtiste(artiste, getOffset(rapportPath, concertAbsolutePath.getParent()));
					 rapportDeSesConcerts.printReport(concertAbsolutePath, CssStyles.stylesTableauDunArtiste);
				 }
			 }
		 }

		 publish(new ProgressInformation(null, RAPPORT_ALBUM, null));
		 for (Album album : listeAlbum.getAlbums()) {
			 if (album.hasAdditionnalInfo()) {
				 Path absolutePath = RapportStructuresAndNames.getAlbumRapportAbsolutePath(album);
				 if (! Files.exists(absolutePath)) {
					 RapportAlbum.createRapportAlbum(album)
					 	.withOffset(getOffset(rapportPath, absolutePath.getParent()))
					 	.printReport(absolutePath, CssStyles.stylesTableauDunArtiste);
				 }
			 }
		 }
		 
		 publish(new ProgressInformation(null, RAPPORT_CONCERT, null));
		 for (Concert concert : listeConcert.getConcerts()) {
			 if (concert.hasAdditionnalInfo()) {
				 Path absolutePath = RapportStructuresAndNames.getConcertRapportAbsolutePath(concert);
				 if (! Files.exists(absolutePath)) {
					 RapportConcert.createRapportConcert(concert)
					 	.withOffset(getOffset(rapportPath, absolutePath.getParent()))
					 	.printReport(absolutePath, CssStyles.ticket);
				 }
			 }
		 }
		 
		 for (LieuConcert lieuConcert : lieuxDesConcerts.getLieuxConcerts()) {
			 Path absolutePath = RapportStructuresAndNames.getLieuRapportAbsolutePath(lieuConcert);
			 if (! Files.exists(absolutePath)) {
				 String offSet = getOffset(rapportPath, absolutePath.getParent());
				 RapportListeConcerts concertDeCeLieu = new RapportListeConcerts(lieuConcert.getConcerts().sortChrono(), lieuConcert.getLieu(), LinkType.LIST);
				 concertDeCeLieu.withOffset(offSet);
				 concertDeCeLieu.printReport(absolutePath, CssStyles.stylesTableauMusicArtefact);
			 }
		 }
	 }
	 
	 private static final String OFFSET_ELEMENT = "../" ;

	 private static String getOffset(Path rootPath, Path targetPath) {

		 int diffPath = targetPath.getNameCount() - rootPath.getNameCount();	
		 if (diffPath <= 0) {
			 return "" ;
		 } else {
			 return getOffset(rootPath, targetPath.getParent()) + OFFSET_ELEMENT;
		 }
	 }

	 private void rapportsHtml(Path rapportDir) {	
	   	
		Path rapportFile = RapportStructuresAndNames.getAbsoluteHomeCollectionFile();
		RapportCollection rapportCollection = new RapportCollection(collectionAlbumContainer, rapportDir, "Collections d'albums");
		rapportCollection.printReport(rapportFile, CssStyles.mainFormat);				
	}
		 
	private void rapportsConcertHtml(Path rapportDir) {

		 Path rapportFile = RapportStructuresAndNames.getAbsoluteHomeConcertFile();
		 RapportDesConcerts rapportConcerts = new RapportDesConcerts(collectionAlbumContainer, rapportDir, "Concerts");
		 rapportConcerts.printReport(rapportFile, CssStyles.main);
	 }
	
	private void rapportBuildInfo() {
		
		Path rapportBuildInfoFile = RapportStructuresAndNames.getAbsoluteBuildInfoFile();
		String buildInfo;
		try {
			buildInfo = Control.getMusicRunningContext().getBuildInformation();
		} catch (JsonProcessingException e) {
			albumLog.log(Level.SEVERE, "Exception getting build information", e);
			buildInfo = e.getMessage();
			
		}
		RapportBuildInfo rapportBuildInfo = new RapportBuildInfo(buildInfo, "Informations sur le programme de génération", LinkType.LIST);
		rapportBuildInfo.printReport(rapportBuildInfoFile, CssStyles.main);
	}
	
	@Override
	public void done() {

		progressPanel.setProgressInformation(new ProgressInformation(FIN_GENERATION, ARRET, ""));
	}

	@Override
	public void process(List<ProgressInformation> progressInformationList) {

    	if (progressInformationList != null) {
    		progressInformationList.forEach(progressInformation -> progressPanel.setProgressInformation(progressInformation));
    	}
	}
}