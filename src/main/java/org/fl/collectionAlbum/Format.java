/*
 MIT License

Copyright (c) 2017, 2022 Frederic Lefevre

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package org.fl.collectionAlbum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fl.collectionAlbum.jsonParsers.AudioFileParser;
import org.fl.collectionAlbum.jsonParsers.VideoFileParser;

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
		RangementCD("Ordre de rangement des albums au format CD", "cd"),
		RangementCDBox("Ordre de rangement des albums au format CD box", "cd box"),
		RangementVinyl("Ordre de rangement des albums au format vinyl 33 tours", "33T"), 
		RangementK7("Ordre de rangement des albums au format K7", "K7"), 
		RangementVHS("Ordre de rangement des albums au format DVD, VHS et Blue Ray", "DVD") ;
		
		private final String description ;
		private final String jsonPropertyName ;
		private RangementSupportPhysique(String n, String j) {
			description = n ;
			jsonPropertyName = j ;
		}
		public String getDescription() {
			return description ;
		}
		public String getJsonPropertyName() {
			return jsonPropertyName;
		}
		
		public static RangementSupportPhysique getRangement(String rangement) {
			return Stream.of(RangementSupportPhysique.values())
					.filter(r -> r.getJsonPropertyName().equals(rangement))
					.findFirst()
					.orElse(null);
		}
		
	}
	
	private final Logger logger;
	
	// Supports physiques de l'album et leur nombre correspondant
	private final EnumMap<SupportPhysique, Double> tableFormat ;
	
	private final List<AbstractAudioFile> audioFiles;
	
	private final List<VideoFile> videoFiles;
	
	private boolean hasError;
	
	// Create a format
	public Format(JsonObject formatJson, Logger fl) {
		
		logger = fl;
		tableFormat = new EnumMap<SupportPhysique, Double>(SupportPhysique.class) ;
		if (formatJson != null) {
			hasError = false;
			for (SupportPhysique sPhys : SupportPhysique.values()) {
				JsonElement elemFormat = formatJson.get(sPhys.getJsonPropertyName()) ;
				if (elemFormat != null) {
					tableFormat.put(sPhys, Double.valueOf(elemFormat.getAsDouble())) ;
				}
			}

			audioFiles = Optional.ofNullable(formatJson.getAsJsonArray(JsonMusicProperties.AUDIO_FILE))
					.map(ja -> {
						List<AbstractAudioFile> audioFileList = new ArrayList<>();
						ja.forEach(jsonAudioFile -> {
							AbstractAudioFile audioFile = AudioFileParser.parseAudioFile(jsonAudioFile.getAsJsonObject(), logger);
							if (audioFile != null) {
								audioFileList.add(audioFile);
							} else {
								hasError = true ;
							}
						});
						return audioFileList;
					})
					.orElse(null);
			
			videoFiles = Optional.ofNullable(formatJson.getAsJsonArray(JsonMusicProperties.VIDEO_FILE))
					.map(jv -> {
						List<VideoFile> videoFileList = new ArrayList<>();
						jv.forEach(jsonVideoFile -> {
							VideoFile videoFile = VideoFileParser.parseVideoFile(jsonVideoFile.getAsJsonObject(), logger);
							if (videoFile != null) {
								videoFileList.add(videoFile);
							} else {
								hasError = true ;
							}
						});
						return videoFileList;
					})
					.orElse(null);
			
		} else {
			hasError = true;
			audioFiles = null;
			videoFiles = null;
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
	
	public List<AbstractAudioFile> getAudioFiles() {
		return audioFiles;
	}

	public List<VideoFile> getVideoFiles() {
		return videoFiles;
	}
	
	public boolean hasAudioFiles() {
		return hasMediaFile(audioFiles);
	}
	
	public boolean hasVideoFiles() {
		return hasMediaFile(videoFiles);
	}
	
	private <T extends AbstractMediaFile> boolean hasMediaFile(List<T> mediaFiles) {
		return (mediaFiles != null) && (!mediaFiles.isEmpty());
	}
	
	public boolean hasError() {
		return hasError;
	}
	
	/**
	 * Increment format
	 * @param addFormat
	 */
	public void incrementFormat(Format addFormat) {
		for (SupportPhysique sPhys : SupportPhysique.values()) {
			tableFormat.put(sPhys, Double.valueOf(this.getNb(sPhys) + addFormat.getNb(sPhys))) ;
		}
	}
	
	private final static String F_ROW0 = "    <td class=\"" ;
	private final static String F_ROW1 = "    <td rowspan=\"" ;
	private final static String F_ROW2 = "\">Total</td>\n" ;
	private final static String F_ROW3 = "\" class=\"" ;
	private final static String F_ROW4 = "\">" ;
	private final static String F_ROW5 = "</td>\n" ;
	private final static String F_ROW6 = "\"><span class=\"" ;
	private final static String F_ROW7 = "</span></td>\n" ;
	private final static String F_ROW8 = "<span class=\"" ;
	
	private final static String AUDIO_FILE_CLASS = "audiofe";
	private final static String AUDIO_FILE_TITLE = "Media file";
	private final static String AUDIO_FILE_OK_CLASS = "audiook";
	private final static String AUDIO_FILE_DETAIL_CLASS = "audiodetail";
	
	public void enteteFormat(StringBuilder rapport, String cssTotal, int rows, boolean putAudioFile) {
		
		if (cssTotal != null) {
			rapport.append(F_ROW1).append(rows).append(F_ROW3).append(cssTotal).append(F_ROW2) ;
		}
		for (SupportPhysique sPhys : SupportPhysique.values()) {
			rapport.append(F_ROW1).append(rows).append(F_ROW3).append(sPhys.getCssClass()).append(F_ROW4).append(sPhys.getNom()).append(F_ROW5) ;
		}
		
		if (putAudioFile) {
			rapport.append(F_ROW1).append(rows).append(F_ROW3).append(AUDIO_FILE_CLASS).append(F_ROW4).append(AUDIO_FILE_TITLE).append(F_ROW5) ;
		}
	}
	
	public void rowFormat(StringBuilder rapport, String cssTotal, boolean putMediaFile) {
		
		if (cssTotal != null) {
			rapport.append(F_ROW0).append(cssTotal).append(F_ROW6).append(cssTotal).append(F_ROW4).append(displayPoids(getPoids())).append(F_ROW7) ;
		}
		for (SupportPhysique sPhys : SupportPhysique.values()) {
			rapport.append(F_ROW0).append(sPhys.getCssClass()).append(F_ROW4).append(displayPoids(getNb(sPhys))).append(F_ROW5) ;			 
		}

		if (putMediaFile) {
			if (hasAudioFiles() || hasVideoFiles()) {
				rapport.append(F_ROW0).append(AUDIO_FILE_OK_CLASS).append(F_ROW4).append(displayMediaFilesSummary())
					.append(F_ROW8).append(AUDIO_FILE_DETAIL_CLASS).append(F_ROW4).append(displayAudioFilesDetail()).append(F_ROW7) ;
			} else {
				rapport.append(F_ROW0).append(AUDIO_FILE_CLASS).append(F_ROW4).append(F_ROW5) ;
			}
		}
	}
	
	private String displayPoids(double d) {
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
	
	public String displayPoidsTotal() {
		return displayPoids(getPoids()) ;
	}
	
	private <T extends AbstractMediaFile> String displayMediaFileInformation(
			List<T> mediaFiles,
			Function<T, String> getMediaInfos,
			String infoSeparator) {
		
		if (hasMediaFile(mediaFiles)) {

			return mediaFiles.stream()
					.map(mediaFile -> getMediaInfos.apply(mediaFile))
					.collect(Collectors.joining(infoSeparator));
		} else {
			return "";
		}
	}
	
	private String displayAudioFilesDetail() {
		return displayMediaFileInformation(audioFiles, (af) -> af.displayMediaFileDetail("<br/>"), "<br/>---<br/>");
	}
	
	private String displayMediaFilesSummary() {
		
		StringBuilder mediaFilesInfo = new StringBuilder("");
		mediaFilesInfo.append(displayMediaFileInformation(audioFiles, (af) -> af.displayMediaFileSummary(), "<br/>"));
		mediaFilesInfo.append(displayMediaFileInformation(videoFiles, (vf) -> vf.displayMediaFileSummary(), "<br/>"));
		return mediaFilesInfo.toString();
	}
	
	public List<String> printAudioFilesCsvParts() {
		if (hasAudioFiles()) {
			return audioFiles.stream().map(af -> af.displayMediaFileDetail(";")).collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}
	
}