package org.fl.collectionAlbum.concerts;

import java.time.temporal.TemporalAccessor;
import java.util.Comparator;
import java.util.logging.Logger;

import com.ibm.lge.fl.util.date.TemporalUtils;

public class ConcertChronoComparator  implements Comparator<Concert> {
	
	private Logger log ;
	
	public ConcertChronoComparator(Logger l) {
		log = l ;
	}
	
	public int compare(Concert arg0, Concert arg1) {
		TemporalAccessor d0 = arg0.getDateConcert() ;
		TemporalAccessor d1 = arg1.getDateConcert() ;
		return TemporalUtils.compareTemporal(d0, d1, log) ;
	}
}
