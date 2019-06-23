package org.fl.collectionAlbum.concerts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class ListeConcert {

	private List<Concert> concerts ;
	
	private Logger listConcertLog;
	
	public ListeConcert(Logger cl) {
		
		listConcertLog = cl ;
		concerts = new ArrayList<Concert>() ;
	}
	
	public void addConcert(Concert a) {
		concerts.add(a) ;
	}
	
	public int getNombreConcerts() {
		return concerts.size() ;
	}
	
	public ListeConcert sortChrono() {
		ConcertChronoComparator compConcert = new ConcertChronoComparator(listConcertLog);
		Collections.sort(concerts, compConcert) ;
		return this;
	}

	public List<Concert> getConcerts() {
		return concerts;
	}
	
}
