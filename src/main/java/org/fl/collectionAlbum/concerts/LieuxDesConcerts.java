package org.fl.collectionAlbum.concerts;

import java.util.HashMap;

public class LieuxDesConcerts {

	private static HashMap<String, LieuConcert> lieuxConcerts ;
	
	public LieuxDesConcerts() {
		lieuxConcerts = new HashMap<String, LieuConcert>() ;
	}
	
	public LieuConcert addLieuDunConcert(String lieu) {
		LieuConcert lieuConcert = lieuxConcerts.get(lieu) ;
		if (lieuConcert == null) {
			lieuConcert = new LieuConcert(lieu) ;
			lieuxConcerts.put(lieu, lieuConcert) ;
		}
		lieuConcert.incrementNombreConcert() ;
		return lieuConcert ;
	}
}
