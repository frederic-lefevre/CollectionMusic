package org.fl.collectionAlbum.concerts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
	
	public List<LieuConcert> getLieuxConcerts() {
		LieuxPoidsComparator lieuxComparator = new LieuxPoidsComparator() ;
		List<LieuConcert> lieux = new ArrayList<LieuConcert>(lieuxConcerts.values()) ;
		Collections.sort(lieux, lieuxComparator) ;
		return  lieux ;
	}
}
