package org.fl.collectionAlbum.artistes;

import java.util.Comparator;

public class AuteurComparator implements Comparator<Artiste> {

	public int compare(Artiste arg0, Artiste arg1) {
		
		String nom0    = arg0.getNom() ;
		String prenom0 = arg0.getPrenoms() ;
		String nom1    = arg1.getNom() ;
		String prenom1 = arg1.getPrenoms() ;
		
		int ordreNom = nom0.compareToIgnoreCase(nom1) ;
		if (ordreNom == 0) {
			return prenom0.compareToIgnoreCase(prenom1) ;
		} else {
			return ordreNom ;
		}
	}
}
