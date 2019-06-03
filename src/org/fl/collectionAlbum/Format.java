package org.fl.collectionAlbum;

import java.util.EnumMap;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


/**
 * @author Frédéric Lefèvre
 *
 *  Format d'un album (ou d'une liste d'albums)
 *  un album peut comprendre plusieurs supports physiques (par exemple 1 vinyl 33T, 1 blueray, 5 CD)
 *  Il a un poids total calculé en fonction de ses supports physiques
 *  Il peut n'occuper qu'une fraction d'un support physique (par exemple 0.5 CD)
 *   
 */
public class Format {

	// Définition des différents physiques
	private enum SupportPhysique {
		CD(		 "CD",  	 "xnbcd",  	   JsonMusicProperties.CD, 		 1) ,
		K7(		 "K7",  	 "xnbk7", 	   JsonMusicProperties.K7, 		 1) ,
		Vinyl33T("33T", 	 "xnb33T", 	   JsonMusicProperties._33T, 	 1) ,
		Vinyl45T("45T", 	 "xnb45T", 	   JsonMusicProperties._45T,  	 0.5) ,
		MiniCD(	 "Mini CD",  "xnbminicd",  JsonMusicProperties.MINI_CD,  0.5) ,
		MiniDVD( "Mini DVD", "xnbminidvd", JsonMusicProperties.MINI_DVD, 0.5) ,
		Mini33T( "Mini 33T", "xnbmini33T", JsonMusicProperties.MINI_33T, 0.5) ,
		Maxi45T( "Maxi 45T", "xnbmaxi45T", JsonMusicProperties.MAXI_45T, 0.5) ,
		VHS(	 "VHS",		 "xnbvhs",	   JsonMusicProperties.VHS, 	 1) ,
		DVD(	 "DVD",		 "xnbdvd", 	   JsonMusicProperties.DVD, 	 1) ,
		BlueRay( "Blue Ray", "xnbblueray", JsonMusicProperties.BLUERAY,  1) ;
		
		private final String nom ;
		private final String cssClass ; 
		private final String jsonPropertyName ;
		private final double poidsSupport ;
		
		private SupportPhysique(String n, String cssCl, String jp, double ps) {
			nom 	 	 	 = n ;
			cssClass 	 	 = cssCl ;
			jsonPropertyName = jp ;
			poidsSupport 	 = ps ;
		}
		
		private String getJsonPropertyName() {	return jsonPropertyName ;}
		private String getCssClass() 		 {	return cssClass 		;}		
		private String getNom() 			 {	return nom 				;}	
		private double getPoidsSupport() 	 {	return poidsSupport 	;}
	}
	
	// Définition des rangements des supports physiques
	public enum RangementSupportPhysique {
		RangementCD("Ordre de rangement des CD"), 
		RangementVinyl("Ordre de rangement des vinyls"), 
		RangementK7("Ordre de rangement des K7"), 
		RangementVHS("Ordre de rangement des DVD, VHS et Blue Ray") ;
		
		private final String description ;
		private RangementSupportPhysique(String n) {
			description = n ;
		}
		public String getDescription() {
			return description ;
		}
	}
	
	// Supports physiques de l'album et leur nombre correspondant
	private EnumMap<SupportPhysique, Double> tableFormat ;
	
	// Create a format
	public Format(JsonObject formatJson, Logger fl) {
		
		tableFormat = new EnumMap<SupportPhysique, Double>(SupportPhysique.class) ;
		if (formatJson != null) {
			for (SupportPhysique sPhys : SupportPhysique.values()) {
				JsonElement elemFormat = formatJson.get(sPhys.getJsonPropertyName()) ;
				if (elemFormat != null) {
					tableFormat.put(sPhys, new Double(elemFormat.getAsDouble())) ;
				}
			}
		}
	}

    /**
     * Get rangement for this format
     * @return Rangement (Vinyl, CD, VHS, K7)
     */
    public RangementSupportPhysique getRangement() {
    	RangementSupportPhysique typeRangement = null ;
		if ((getNb(SupportPhysique.Vinyl33T) > 0) || (getNb(SupportPhysique.Vinyl45T) > 0) || (getNb(SupportPhysique.Mini33T) > 0) || (getNb(SupportPhysique.Maxi45T) > 0)) {
		// à ranger dans la collection Vinyl
			typeRangement = RangementSupportPhysique.RangementVinyl ;
		} else if ((getNb(SupportPhysique.CD) > 0) || (getNb(SupportPhysique.MiniCD) > 0) ||(getNb(SupportPhysique.MiniDVD) > 0) ) {
		// à ranger dans la collection CD
			typeRangement = RangementSupportPhysique.RangementCD ;
		} else if (getNb(SupportPhysique.K7) > 0) {
		// à ranger dans la collection K7
			typeRangement = RangementSupportPhysique.RangementK7 ;
		} else if ((getNb(SupportPhysique.VHS) > 0) || (getNb(SupportPhysique.DVD) > 0) || (getNb(SupportPhysique.BlueRay) > 0)) {
		// à ranger dans la collection VHS
			typeRangement = RangementSupportPhysique.RangementVHS ;
		}
		return typeRangement ;
	}

	private double getNb(SupportPhysique phys) {
		Double nb = tableFormat.get(phys) ;
		if (nb == null) {
			return 0 ;
		} else {
			return nb.doubleValue() ;
		}
	}
	
	// Get the poids of the format
	public double getPoids() {
		double res = 0 ;
		Set<SupportPhysique> supportPhysiques = tableFormat.keySet() ;
		for (SupportPhysique sPhys : supportPhysiques) {
			res = res + sPhys.getPoidsSupport()*getNb(sPhys) ;
		}
		return res ;
	}
	
	/**
	 * Increment format
	 * @param addFormat
	 */
	public void incrementFormat(Format addFormat) {
		for (SupportPhysique sPhys : SupportPhysique.values()) {
			tableFormat.put(sPhys, new Double(this.getNb(sPhys) + addFormat.getNb(sPhys))) ;
		}
	}
	
	private final static String F_ROW1 = "    <td rowspan=\"" ;
	private final static String F_ROW2 = "\" class=\"total\">Total</td>\n" ;
	private final static String F_ROW3 = "\" class=\"" ;
	private final static String F_ROW4 = "\">" ;
	private final static String F_ROW5 = "</td>\n" ;
	private final static String F_ROW6 = "\"><span class=\"" ;
	private final static String F_ROW7 = "</span></td>\n" ;
	
	public void enteteFormat(RapportHtml rapport, boolean avecTotal, int rows) {
		
		if (avecTotal) {
			rapport.write(F_ROW1).write(rows).write(F_ROW2) ;
		}
		for (SupportPhysique sPhys : SupportPhysique.values()) {
			rapport.write(F_ROW1).write(rows).write(F_ROW3).write(sPhys.getCssClass()).write(F_ROW4).write(sPhys.getNom()).write(F_ROW5) ;
		}
	}
	

	public void rowFormat(RapportHtml rapport, boolean avecTotal) {
		rowFormat(rapport, avecTotal, "total") ;
	}
	
	public void rowFormat(RapportHtml rapport, boolean avecTotal, String cssTotal) {
		
		if (avecTotal) {
			rapport.write(F_ROW1).write(1).write(F_ROW3).write(cssTotal).write(F_ROW6).write(cssTotal).write(F_ROW4).write(displayDouble(getPoids())).write(F_ROW7) ;
		}
		for (SupportPhysique sPhys : SupportPhysique.values()) {
			rapport.write(F_ROW1).write(1).write(F_ROW3).write(sPhys.getCssClass()).write(F_ROW4).write(displayDouble(getNb(sPhys))).write(F_ROW5) ;			 
		}		
	}
	
	private String displayDouble(double d) {
		if (d == 0) {
			return "" ;
		} else {
			long dArrondi = Math.round(d) ;
			
			if (d == dArrondi) {
				return Long.toString(dArrondi);
			} else {
				return Double.toString(d) ;
			}
		}
	}
}