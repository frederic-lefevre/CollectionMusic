package org.fl.collectionAlbum.albums;

import java.time.temporal.TemporalAccessor;
import java.util.Comparator;
import java.util.logging.Logger;

import org.fl.util.date.TemporalUtils;

public class AlbumEnregistrementComparator  implements Comparator<Album> {

	private Logger log ;
	
	public AlbumEnregistrementComparator(Logger l) {
		log = l ;
	}
	
	public int compare(Album arg0, Album arg1) {
		
		TemporalAccessor d0 = arg0.getDebutEnregistrement() ;
		TemporalAccessor d1 = arg1.getDebutEnregistrement() ;
		
		int comp = TemporalUtils.compareTemporal(d0, d1, log) ;
		
		if (comp == 0) {
			TemporalAccessor d2 = arg0.getFinEnregistrement() ;
			TemporalAccessor d3 = arg1.getFinEnregistrement() ;
			comp = TemporalUtils.compareTemporal(d2, d3, log) ;
		}		
		return comp ;
	}
}
