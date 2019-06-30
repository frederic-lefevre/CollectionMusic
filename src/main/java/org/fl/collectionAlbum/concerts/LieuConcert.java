package org.fl.collectionAlbum.concerts;

import java.util.logging.Logger;

public class LieuConcert {

	private final String lieu ;
	private ListeConcert concerts ;

	public LieuConcert(String l, Logger log) {
		lieu 		  = l ;
		concerts  	 = new ListeConcert(log) ;
	}

	public String getLieu() {
		return lieu;
	}
	
	public int getNombreConcert() {
		return concerts.getNombreConcerts();
	}

	public void addConcert(Concert concert) {
		concerts.addConcert(concert) ;
	}
}
