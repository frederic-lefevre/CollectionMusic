package org.fl.collectionAlbum.concerts;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.RapportHtml;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.utils.TemporalUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

public class Concert extends MusicArtefact {

    private TemporalAccessor dateConcert ;   
    private final String lieuConcert;    
    private final String urlInfos;
    
    private List<String> titres;
    
    private List<String> ticketImages;
    
	public Concert(Path srcFile, ListeArtiste listeArtistes, Logger cl) {
	       
		super(srcFile, listeArtistes, cl) ;
		
		JsonElement jElem ;
       
        // Traitement de la date
        jElem = arteFactJson.get(JsonMusicProperties.DATE) ;
        if (jElem == null) {
        	dateConcert = null ;
        	artefactLog.warning("Pas de date dans le concert " + arteFactJson);
        } else {
        	 try {
        		 dateConcert = TemporalUtils.parseDate(jElem.getAsString()) ;
             } catch (Exception e) {
            	 artefactLog.log(Level.SEVERE, "Erreur dans les dates du concert " + arteFactJson, e) ;
             }
        }
       
        // traitement du lieu
        jElem = arteFactJson.get(JsonMusicProperties.LIEU) ;
        if (jElem == null) {
        	lieuConcert = null ;
        	artefactLog.warning("Pas de lieu dans le concert " + arteFactJson);
        } else {
        	lieuConcert = jElem.getAsString() ;
        }
        
        // traitement de l'url d'information
        jElem = arteFactJson.get(JsonMusicProperties.URL_INFOS) ;
        if (jElem == null) {
        	urlInfos = null ;
        } else {
        	urlInfos = jElem.getAsString() ;
        }
        
        Type listType = new TypeToken<List<String>>() {}.getType();
        
        // Traitement des titres
		 jElem = arteFactJson.get(JsonMusicProperties.MORCEAUX) ;
		 if (jElem != null) {
			if (jElem.isJsonArray()) {				
				JsonArray jTitres = jElem.getAsJsonArray() ;			
				titres = new Gson().fromJson(jTitres, listType);
			} else {
				artefactLog.warning(JsonMusicProperties.MORCEAUX + " n'est pas un JsonArray pour le concert " + arteFactJson) ;
			}
		 }
       
		 // Traitement des images des tickets
		 jElem = arteFactJson.get(JsonMusicProperties.TICKET_IMG) ;
		 if (jElem == null) {
			 ticketImages = null ;
			 artefactLog.info("Pas de ticket pour le concert " + arteFactJson) ;
		 } else {
			 if (jElem.isJsonArray()) {
				 JsonArray jTickets = jElem.getAsJsonArray() ; 
				 ticketImages =  new Gson().fromJson(jTickets,  listType);
			 } else {
				 artefactLog.warning(JsonMusicProperties.TICKET_IMG + " n'est pas un JsonArray pour le concert " + arteFactJson) ;
			 }
		 }  
	}
	
    
    public void generateHtml() {
    	
    	if (additionnalInfo()) {
    				
        	File htmlFile = new File(Control.getAbsoluteConcertDir() + artefactHtml) ; ;	
        	
        	if (! htmlFile.exists()) {
        		
        		RapportHtml rapport = new RapportHtml("", false, htmlFile, null,  "../", artefactLog);
				
				rapport.enteteRapport(new String[]{RapportHtml.albumStyle}) ;
		
				if (notes != null) {
					for (String note : notes) {
						rapport.write("<p>").write(note).write("</p>") ;
					}
				}
				
				if ((titres != null) && (titres.size() >0)) {
					rapport.write("<h3>Titres</h3>") ;
					rapport.write("<ol>") ;
					for (String titre : titres) {
						rapport.write("<li>").write(titre).write("</li>") ;
					}
					rapport.write("</ol>") ;
				}
				if (urlInfos != null) {
					rapport.write("<h3><a href=\"").write(urlInfos).write("\">Informations</a></h3>") ;
				}
				if (ticketImages != null) {
					try {
					for (int i=0; i < ticketImages.size(); i++) {
						String imgUrl = Control.getConcertTicketImgPath() + ticketImages.get(i) ;
						// check that the file exists
						if (! (new File(new URI(imgUrl))).exists()) {
							artefactLog.warning("Le fichier ticket image suivant n'existe pas: " + imgUrl + " Date du concert: " + dateConcert.toString()) ;
						}
						rapport.write("<a href=\"").write(imgUrl ).write("\">") ;
						rapport.write("<img class=\"ticket\" src=\"").write(imgUrl).write("\"/></a>") ;
					}
					} catch (URISyntaxException e) {
						artefactLog.log(Level.SEVERE, "Exception en traitant les images de concert", e) ;
					}
				}
				rapport.finRapport() ; 			
    		}
    	}    	
    }
    
    public boolean additionnalInfo() {
    
    	boolean res = false ;
    	if ((notes != null) && (notes.size() > 0)) {
    		res = true ;
    	} else if ((ticketImages != null) && (ticketImages.size() >0)){
    		res = true ;
    	}
    	return res ;
    }
    
	public String getLieuConcert() {
		return lieuConcert;
	}
	
	public TemporalAccessor getDateConcert() {
		return dateConcert ;
	}
	
	public String getFullPathHtmlFileName() {
		return "file://" + Control.getAbsoluteConcertDir() + artefactHtml ;
	}	
}