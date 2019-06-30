package org.fl.collectionAlbum.concerts;

import java.util.Comparator;

public class LieuxPoidsComparator  implements Comparator<LieuConcert> {

	@Override
	public int compare(LieuConcert o1, LieuConcert o2) {
		
		int nbConcert1 = o1.getNombreConcert() ;
		int nbConcert2 = o2.getNombreConcert() ;
		
		if (nbConcert1 < nbConcert2) {
			return 1 ;
		} else if (nbConcert1 > nbConcert2) {
			return -1 ;
		} else {
			return 0;
		}
	}

}
