package org.fl.collectionAlbum.artistes;

import java.time.temporal.TemporalAccessor;
import java.util.Comparator;
import java.util.logging.Logger;

import com.ibm.lge.fl.util.date.TemporalUtils;

public class AuteurDateComparator  implements Comparator<Artiste> {
	
	private Logger log ;
	
	public AuteurDateComparator(Logger l) {
		log = l ;
	}
	
	public int compare(Artiste arg0, Artiste arg1) {
		TemporalAccessor d0 = arg0.getNaissance() ;
		TemporalAccessor d1 = arg1.getNaissance() ;
		return TemporalUtils.compareTemporal(d0, d1, log) ;
	}
}
