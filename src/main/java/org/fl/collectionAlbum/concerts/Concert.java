package org.fl.collectionAlbum.concerts;

import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.jsonParsers.ConcertParser;

import com.google.gson.JsonObject;

public class Concert extends MusicArtefact {

    private TemporalAccessor dateConcert ;   
    private LieuConcert		 lieuConcert;     
    private List<String> 	 titres;    
    private List<String> 	 ticketImages;
    
    public Concert(JsonObject concertJson, List<ListeArtiste> knownArtistes, LieuxDesConcerts lieuxDesConcerts, Logger aLog) {
    	super(concertJson, knownArtistes, aLog) ;
    	
    	dateConcert  = ConcertParser.getConcertDate(concertJson, aLog) ;
    	lieuConcert  = lieuxDesConcerts.addLieuDunConcert(ConcertParser.getConcertLieu(concertJson, aLog), aLog) ;
    	titres		 = ConcertParser.getConcertMorceaux(concertJson, aLog) ;
    	ticketImages = ConcertParser.getConcertTickets(concertJson, aLog) ;
    }
    
    @Override
    public boolean additionnalInfo() {
    
    	boolean res = false ;
    	if ((ticketImages != null) && (ticketImages.size() > 0)) {
    		res = true ;
    	} else {
    		res = super.additionnalInfo() ;
    	}
    	return res ;
    }
    
	public LieuConcert 		getLieuConcert()  { return lieuConcert	; }	 
	public TemporalAccessor getDateConcert()  {	return dateConcert 	; }
	public List<String> 	getTitres() 	  {	return titres		; }
	public List<String> 	getTicketImages() { return ticketImages ; }	
	
}