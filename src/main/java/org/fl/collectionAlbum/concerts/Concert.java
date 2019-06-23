package org.fl.collectionAlbum.concerts;

import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.MusicArtefact;

import com.google.gson.JsonObject;

public class Concert extends MusicArtefact {

    private TemporalAccessor dateConcert ;   
    private String 			 lieuConcert;    
    private String 			 urlInfos;   
    private List<String> 	 titres;    
    private List<String> 	 ticketImages;
    
    public Concert(JsonObject concertJson, Logger aLog) {
    	super(concertJson, aLog) ;
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
    
	public String 			getLieuConcert()  { return lieuConcert	; }	 
	public TemporalAccessor getDateConcert()  {	return dateConcert 	; }
	public String 			getUrlInfos() 	  {	return urlInfos		; }
	public List<String> 	getTitres() 	  {	return titres		; }
	public List<String> 	getTicketImages() { return ticketImages ; }	
	
	public String getFullPathHtmlFileName() {
		return "file://" + Control.getAbsoluteConcertDir() + artefactHtmlName ;
	}

	public void setDateConcert(TemporalAccessor dateConcert) {
		this.dateConcert = dateConcert;
	}

	public void setTitres(List<String> lt) 		 { titres 		= lt ; }
	public void setTicketImages(List<String> ti) { ticketImages = ti ; }
	public void setLieuConcert(String lc) 		 { lieuConcert  = lc ; }
	public void setUrlInfos(String ui) 			 { urlInfos 	= ui ; }
	
}