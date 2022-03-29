package org.fl.collectionAlbum;

import java.time.temporal.TemporalAccessor;
import java.util.List;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.util.date.AnniversaryCalendar;

public class ChronoArtistes {

	private AnniversaryCalendar<Artiste> anniversaires ;

	public ChronoArtistes() {
		anniversaires = new AnniversaryCalendar<Artiste>() ;
	}
	
	public void add(Artiste a) {
		TemporalAccessor naissance = a.getNaissance() ;
		TemporalAccessor mort = a.getMort() ;
		if (naissance != null) {
			anniversaires.addAnniversary(a, naissance) ;
		}
		if (mort != null) {
			anniversaires.addAnniversary(a, mort) ;
		}
	}
	
	public List<Artiste> getChronoArtistes(TemporalAccessor date) {
		return anniversaires.getAnniversaries(date) ;
	}
}
