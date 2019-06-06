package org.fl.collectionAlbum.stat;

import java.util.Comparator;

public class StatAnComparator implements Comparator<StatAnnee> {
	
	public int compare(StatAnnee arg0, StatAnnee arg1) {
		int a0 = arg0.getAn() ;
		int a1 = arg1.getAn() ;
		if ( a0 < a1) {
			return -1 ;
		} else if (a0 > a1) {
			return 1 ;
		} else {
			return 0 ;
		}
	}

}
