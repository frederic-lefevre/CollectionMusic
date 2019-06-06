package org.fl.collectionAlbum.albums;

import java.time.temporal.TemporalAccessor;
import java.util.Comparator;
import java.util.logging.Logger;

import com.ibm.lge.fl.util.date.TemporalUtils;

public class AlbumCompositionComparator  implements Comparator<Album> {

	private Logger log ;
	
	public AlbumCompositionComparator(Logger l) {
		log = l ;
	}
	
	public int compare(Album arg0, Album arg1) {
		
		TemporalAccessor d0 = arg0.getDebutComposition() ;
		TemporalAccessor d1 = arg1.getDebutComposition() ;
		
		int comp = TemporalUtils.compareTemporal(d0, d1, log) ;
		
		if (comp == 0) {
			TemporalAccessor d2 = arg0.getFinComposition() ;
			TemporalAccessor d3 = arg1.getFinComposition() ;
			comp = TemporalUtils.compareTemporal(d2, d3, log) ;
		}

		if (comp == 0) {
		    AlbumEnregistrementComparator compEnr = new AlbumEnregistrementComparator(log) ;
		    comp = compEnr.compare(arg0, arg1) ;
		} 
		
		return comp ;
	}
}
