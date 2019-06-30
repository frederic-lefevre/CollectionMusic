package org.fl.collectionAlbum.concerts;

public class LieuConcert {

	private final String lieu ;
	private int nombreConcert ;

	public LieuConcert(String l) {
		lieu 		  = l ;
		nombreConcert = 0 ;
	}

	public String getLieu() {
		return lieu;
	}
	
	public int getNombreConcert() {
		return nombreConcert;
	}

	public void incrementNombreConcert() {
		nombreConcert++ ;
	}
}
