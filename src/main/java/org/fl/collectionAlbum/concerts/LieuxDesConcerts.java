package org.fl.collectionAlbum.concerts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LieuxDesConcerts {

	private static Map<String, LieuConcert> lieuxConcerts ;
	
	public LieuxDesConcerts() {
		lieuxConcerts = new HashMap<String, LieuConcert>() ;
	}
	
	public LieuConcert addLieuDunConcert(String lieu, Logger log) {
		LieuConcert lieuConcert = lieuxConcerts.get(lieu) ;
		if (lieuConcert == null) {
			lieuConcert = new LieuConcert(lieu, log) ;
			lieuxConcerts.put(lieu, lieuConcert) ;
		}
		return lieuConcert ;
	}
	
	public List<LieuConcert> getLieuxConcerts() {
		LieuxPoidsComparator lieuxComparator = new LieuxPoidsComparator() ;
		List<LieuConcert> lieux = new ArrayList<LieuConcert>(lieuxConcerts.values()) ;
		Collections.sort(lieux, lieuxComparator) ;
		return  lieux ;
	}
}
