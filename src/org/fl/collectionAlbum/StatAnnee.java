package org.fl.collectionAlbum;

public class StatAnnee {
	
	private int an ;
	private double nombre ;

	/**
	 * @param aan
	 * @param anombre
	 * 
	 */
	public StatAnnee(int aan, double anombre) {
		an = aan ;
		nombre = anombre ;
	}

	/**
	 * @return An
	 */
	public int getAn() {
		return an;
	}

	/**
	 * @return number as a string
	 */
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

	/**
	 * Increment the stat
	 * @param n
	 */
	public void incrementNombre(double n) {
		nombre = nombre + n ;
	}

}
