package org.fl.collectionAlbum.stat;

public class StatAnnee {
	
	private final int an ;
	private double nombre ;

	public StatAnnee(int aan, double anombre) {
		an 	   = aan ;
		nombre = anombre ;
	}

	public int getAn() {
		return an;
	}

	public String getNombre() {
		
		if (nombre == 0) {
			return "" ;
		} else {
			long poidsArrondi = Math.round(nombre) ;
	
			if (nombre == poidsArrondi) {
				return Long.toString(poidsArrondi) ;
			} else {
				return Double.toString(nombre) ;
			}
		}
	}

	public void incrementNombre(double n) {
		nombre = nombre + n ;
	}
}
