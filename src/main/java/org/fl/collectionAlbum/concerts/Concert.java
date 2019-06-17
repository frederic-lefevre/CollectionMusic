package org.fl.collectionAlbum.concerts;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.rapportHtml.RapportHtml;

import com.google.gson.JsonObject;

public class Concert extends MusicArtefact {

    private TemporalAccessor dateConcert ;   
    private String lieuConcert;    
    private String urlInfos;
    
    private List<String> titres;
    
    private List<String> ticketImages;
    
    public Concert(JsonObject concertJson, Logger aLog) {
    	super(concertJson, aLog) ;
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

	public void setDateConcert(TemporalAccessor dateConcert) {
		this.dateConcert = dateConcert;
	}

	public void setTitres(List<String> titres) {
		this.titres = titres;
	}

	public void setTicketImages(List<String> ticketImages) {
		this.ticketImages = ticketImages;
	}

	public void setLieuConcert(String lieuConcert) {
		this.lieuConcert = lieuConcert;
	}

	public void setUrlInfos(String urlInfos) {
		this.urlInfos = urlInfos;
	}	
}