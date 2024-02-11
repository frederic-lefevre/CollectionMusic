/*
 MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

package org.fl.collectionAlbum.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fl.collectionAlbum.Control;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Frédéric Lefèvre
 *
 *  Format d'un album (ou d'une liste d'albums)
 *  un album peut comprendre plusieurs supports physiques (par exemple 1 vinyl 33T, 1 blu-ray, 5 CD)
 *  Il a un poids total calculé en fonction de ses supports physiques
 *  Il peut n'occuper qu'une fraction d'un support physique (par exemple 0.5 CD)
 *   
 */
public class Format {

	private final static Logger albumLog = Control.getAlbumLog();
	
	private static final String ORDRE_PREFIX = "Ordre de rangement des ";
	
	// Définition des rangements des supports physiques
	public enum RangementSupportPhysique {
		
		RangementCD("Albums au format CD", "cd"),
		RangementCDBox("Albums au format CD box", "cd box"),
		RangementVinyl("Albums au format vinyl 33 tours", "33T"), 
		RangementK7("Albums au format K7", "K7"), 
		RangementVHS("Albums au format DVD, VHS et Blu-ray", "DVD");
		
		private final String description;
		private final String jsonPropertyName;

		private RangementSupportPhysique(String n, String j) {
			description = n;
			jsonPropertyName = j;
		}

		public String getOrdreDescription() {
			return ORDRE_PREFIX + description;
		}


		public String getDescription() {
			return description;
		}
		
		public String getJsonPropertyName() {
			return jsonPropertyName;
		}

		public static RangementSupportPhysique getRangement(String rangement) {
			return Stream.of(RangementSupportPhysique.values()).filter(r -> r.getJsonPropertyName().equals(rangement))
					.findFirst().orElse(null);
		}
		
	}
	
	// Supports de l'album et leur nombre correspondant
	private final EnumMap<MediaSupports, Double> tableFormat ;
	
	private Map<ContentNature, List<AbstractMediaFile>> mediaFiles;
	
	private boolean hasError;

	// Create a format
	public Format(JsonObject formatJson) {
		
		tableFormat = new EnumMap<MediaSupports, Double>(MediaSupports.class);
		
		mediaFiles = new HashMap<>();
		if (formatJson != null) {
			try {
					
				hasError = false;
				for (MediaSupports support : MediaSupports.values()) {
					JsonElement elemFormat = formatJson.get(support.getJsonPropertyName());
					if (elemFormat != null) {
						tableFormat.put(support, Double.valueOf(elemFormat.getAsDouble()));
					}
				}
				if (tableFormat.isEmpty()) {
					hasError = true;
					albumLog.warning("Pas de support media connu pour le format " + formatJson.toString());
				}
	
				Stream.of(ContentNature.values()).forEach(contentNature -> {
					
					JsonArray mediaFileArray = formatJson.getAsJsonArray(contentNature.getJsonProperty());
					List<AbstractMediaFile> mediaFileList = new ArrayList<>();
					if (mediaFileArray != null) {
						mediaFileArray.forEach(mediaFileJson -> {
							AbstractMediaFile mediaFile = contentNature.getMediaFileParser().parseMediaFile(mediaFileJson.getAsJsonObject());
							if (mediaFile != null) {
								mediaFileList.add(mediaFile);
							} else {
								hasError = true ;
							}
						});
					}
					mediaFiles.put(contentNature, mediaFileList);
				});
				
			} catch (Exception e) {
				hasError = true;
				albumLog.log(Level.SEVERE, "Exception when parsing album format", e);
			}
			
		} else {
			hasError = true;
			Stream.of(ContentNature.values()).forEach(contentNature -> {
				mediaFiles.put(contentNature, null);
			});
		}
	}

    /**
     * Infer rangement for this format : Depending on the physical supports included in the format, infer the rangement
     * @return Rangement (Vinyl, CD, VHS, K7)
     */
    public RangementSupportPhysique inferRangement() {
    	RangementSupportPhysique typeRangement = null ;
		if ((supportPhysiquePresent(MediaSupportCategories.VinylLP)) || 
			(supportPhysiquePresent(MediaSupportCategories.MiniVinyl))) {
		// à ranger dans la collection Vinyl
			typeRangement = RangementSupportPhysique.RangementVinyl ;
		} else if ((supportPhysiquePresent(MediaSupportCategories.CD)) || 
					(supportPhysiquePresent(MediaSupportCategories.MiniCD)) ||
					(supportPhysiquePresent(MediaSupportCategories.MiniDVD)) ) {
		// à ranger dans la collection CD
			typeRangement = RangementSupportPhysique.RangementCD ;
		} else if (supportPhysiquePresent(MediaSupportCategories.K7)) {
		// à ranger dans la collection K7
			typeRangement = RangementSupportPhysique.RangementK7 ;
		} else if ((supportPhysiquePresent(MediaSupportCategories.VHS)) || 
					(supportPhysiquePresent(MediaSupportCategories.DVD)) || 
					(supportPhysiquePresent(MediaSupportCategories.BluRay))) {
		// à ranger dans la collection VHS
			typeRangement = RangementSupportPhysique.RangementVHS ;
		} else {
			albumLog.severe("Rangement du support physiques non trouvé");
		}
		return typeRangement ;
	}

    public Set<MediaSupportCategories> getSupportsPhysiques() {
    	
    	return tableFormat.keySet().stream()
    			.map(MediaSupports::getSupportPhysique)
    			.collect(Collectors.toSet());
    }
    
	private double getNb(MediaSupports support) {
		Double nb = tableFormat.get(support) ;
		if (nb == null) {
			return 0 ;
		} else {
			return nb.doubleValue() ;
		}
	}
	
	private double getNbSupportPhysique(MediaSupportCategories supportPhysique) {
		return tableFormat.entrySet().stream()
				.filter(entry -> entry.getKey().getSupportPhysique().equals(supportPhysique))
				.mapToDouble(entry -> entry.getValue())			
				.sum();
	}
	
	private boolean supportPhysiquePresent(MediaSupportCategories supportPhysique) {
		return tableFormat.keySet().stream()
			.map(MediaSupports::getSupportPhysique)
			.filter(sp -> sp.equals(supportPhysique))
			.findFirst()
			.isPresent();
	}
	
	// Get the poids of the format
	public double getPoids() {

		return tableFormat.keySet().stream()
			.mapToDouble(support -> support.getSupportPhysique().getPoidsSupport()*getNb(support))
			.sum();
	}
	
	public List<? extends AbstractMediaFile> getMediaFiles(ContentNature contentNature) {
		return mediaFiles.get(contentNature);
	}
	
	public boolean hasMediaFiles() {
		return Stream.of(ContentNature.values()).anyMatch(contentNature -> hasMediaFiles(contentNature));
	}
	
	public boolean hasMediaFiles(ContentNature contentNature) {
		return hasMediaFile(getMediaFiles(contentNature));

	}
	
	public boolean hasOnlyLossLessAudio() {
		return 	hasMediaFiles(ContentNature.AUDIO) &&
				getMediaFiles(ContentNature.AUDIO).stream()
					.map(mediaFile -> (AbstractAudioFile)mediaFile)
					.allMatch(audioFile -> audioFile.isLossLess());
	}
	
	public boolean hasHighResAudio() {
		return hasMediaFiles(ContentNature.AUDIO) && 
				getMediaFiles(ContentNature.AUDIO).stream()
				.map(mediaFile -> (AbstractAudioFile)mediaFile)
			   	.anyMatch(audioFile -> audioFile.isHighRes());
	}
	
	public boolean hasMissingOrInvalidMediaFilePath(ContentNature contentNature) {
		return hasMissingOrInvalidMediaFilePath(getMediaFiles(contentNature));
	}
	
	public boolean hasMediaFilePathNotFound(ContentNature contentNature) {
		return hasMediaFilePathNotFound(getMediaFiles(contentNature));
	}
	
	private <T extends AbstractMediaFile> boolean hasMediaFilePathNotFound(List<T> mediaFiles) {
		return (hasMediaFile(mediaFiles) &&
				mediaFiles.stream()
					.anyMatch(mediaFile -> mediaFile.hasMediaFilePathNotFound()));
	}
	
	private <T extends AbstractMediaFile> boolean hasMissingOrInvalidMediaFilePath(List<T> mediaFiles) {
		return (hasMediaFile(mediaFiles) &&
				mediaFiles.stream()
					.anyMatch(mediaFile -> mediaFile.hasMissingOrInvalidMediaFilePath()));
	}
	
	public boolean hasMediaSupport(MediaSupports mediaSupport) {
		return (getNb(mediaSupport) > 0);
	}
	
	private <T extends AbstractMediaFile> boolean hasMediaFile(List<T> mediaFiles) {
		return (mediaFiles != null) && (!mediaFiles.isEmpty());
	}
	
	public boolean hasContentNature(ContentNature cn) {
		return Arrays.asList(MediaSupports.values()).stream()
				.filter(s -> s.getContentNatures().contains(cn))
				.map(s -> tableFormat.get(s))
				.filter(Objects::nonNull)
				.anyMatch(p -> p > 0);
	}
	
	public Set<ContentNature> getContentNatures() {
		return tableFormat.entrySet()
			.stream()
			.filter(entry -> Objects.nonNull(entry.getValue()) && entry.getValue() > 0)
			.flatMap(entry -> entry.getKey().getContentNatures().stream())
			.collect(Collectors.toSet());			
	}
	
	public boolean hasError() {
		return hasError;
	}
	
	/**
	 * Increment format
	 * @param addFormat
	 */
	public void incrementFormat(Format addFormat) {
		for (MediaSupports support : MediaSupports.values()) {
			tableFormat.put(support, Double.valueOf(this.getNb(support) + addFormat.getNb(support))) ;
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
	
	private final static String SUPPORT_CLASS = "support";
	private final static String INFO_SUPPORT_CLASS = "infosupport";
	
	public static void enteteFormat(StringBuilder rapport, String cssTotal, int rows, boolean putAudioFile) {
		
		if (cssTotal != null) {
			rapport.append(F_ROW1).append(rows).append(F_ROW3).append(cssTotal).append(F_ROW2);
		}
		for (MediaSupportCategories sPhys : MediaSupportCategories.values()) {
			rapport.append(F_ROW1).append(rows).append(F_ROW3).append(sPhys.getCssClass()).append(" ").append(SUPPORT_CLASS).append(F_ROW4).append(sPhys.getNom())
			.append(F_ROW8).append(INFO_SUPPORT_CLASS).append(F_ROW4).append(sPhys.getDescription()).append(F_ROW7) ;
		}
		
		if (putAudioFile) {
			rapport.append(F_ROW1).append(rows).append(F_ROW3).append(AUDIO_FILE_CLASS).append(F_ROW4).append(AUDIO_FILE_TITLE).append(F_ROW5) ;
		}
	}
	
	public String csvEnteteAudioFormat(String csvSeparator) {
		
		StringBuilder csvRapport = new StringBuilder();
		for (MediaSupportCategories sPhys : MediaSupportCategories.values()) {
			csvRapport.append(sPhys.getNom()).append(csvSeparator);
		}
		csvRapport.append(LosslessAudioFile.getAudioFilePropertyTitles(csvSeparator)).append(csvSeparator);
		return csvRapport.toString();
	}
	
	public void rowFormat(StringBuilder rapport, String cssTotal, boolean putMediaFile) {
		
		if (cssTotal != null) {
			rapport.append(F_ROW0).append(cssTotal).append(F_ROW6).append(cssTotal).append(F_ROW4).append(displayPoids(getPoids())).append(F_ROW7) ;
		}
		for (MediaSupportCategories sPhys : MediaSupportCategories.values()) {
			rapport.append(F_ROW0).append(sPhys.getCssClass()).append(F_ROW4).append(displayPoids(getNbSupportPhysique(sPhys))).append(F_ROW5) ;			 
		}

		if (putMediaFile) {
			if (hasMediaFiles()) {
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
		return displayMediaFileInformation(getAllMediaFiles(), (af) -> af.displayMediaFileDetail("<br/>"), "<br/>---<br/>");
	}
	
	private String displayMediaFilesSummary() {
		return displayMediaFileInformation(getAllMediaFiles(), (af) -> af.displayMediaFileSummary(), "<br/>");
		
	}
	
	public List<AbstractMediaFile> getAllMediaFiles() {
		List<AbstractMediaFile> allMediaFiles = new ArrayList<>();
		Stream.of(ContentNature.values())
			.map(contentNature -> mediaFiles.get(contentNature))
			.filter(Objects::nonNull)
			.forEach(mediaFileList -> allMediaFiles.addAll(mediaFileList));
		
		return allMediaFiles;
	}
	
	public List<String> printAudioFilesCsvParts(String csvSeparator, Predicate<AbstractAudioFile> audioFileFilter) {
		if (hasMediaFiles(ContentNature.AUDIO)) {
			return getMediaFiles(ContentNature.AUDIO).stream()
					.map(mediaFile -> (AbstractAudioFile)mediaFile)
					.filter(audioFileFilter)
					.map(af -> af.displayMediaFileDetail(csvSeparator))
					.collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}
	
}