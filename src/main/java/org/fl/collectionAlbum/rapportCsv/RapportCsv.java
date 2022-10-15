package org.fl.collectionAlbum.rapportCsv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.LosslessAudioFile;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;

public class RapportCsv {

	private static final String CSV_SEPARATOR = ";";
	
	public static void writeCsvFile(ListeAlbum listeAlbum, Path filePath, Logger logger) {
		
		Path csvDir = filePath.getParent();
		if (!Files.exists(csvDir)) {
			try {
				Files.createDirectories(csvDir);
			} catch (IOException e) {
				String csvDirString = Optional.ofNullable(csvDir)
						.map(fp -> fp.toString())
						.orElse("csvDir is null");
				logger.log(Level.SEVERE, "IOException creating csv dir " + csvDirString, e);
			}
		}
		
		String filePathString = Optional.ofNullable(filePath)
				.map(fp -> fp.toString())
				.orElse("filePath is null");
		
		try (BufferedWriter outputStream = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
			
			outputStream.write(getLosslessAudioFileEntete(CSV_SEPARATOR));
			outputStream.write("\n");
			
			listeAlbum.getAlbums().stream()
				.flatMap(album ->  csvForOneAlbum(album).stream())
				.forEach(line -> {
					try {
						outputStream.write(line);
						outputStream.write("\n");
					} catch (IOException e) {

						logger.log(Level.SEVERE, "IOException writing csv file " + filePathString + " line " + line, e);
					}
				});
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException opening csv file " + filePathString, e);
		}
	}
	
	private static String getLosslessAudioFileEntete(String csvSeparator) {
		return "Auteurs" + CSV_SEPARATOR + "Titres" + CSV_SEPARATOR + LosslessAudioFile.getAudioFilePropertyTitles(CSV_SEPARATOR);
	}
	
	private static List<String> csvForOneAlbum(Album album) {
		
		String auteurs = album.getAuteurs().stream().map(auteur -> auteur.getNomComplet()).collect(Collectors.joining(" "));
		String chefsOrchestre = album.getChefsOrchestre().stream().map(co -> co.getNomComplet()).collect(Collectors.joining(" "));
		
		String allAuteurs;
		if ((chefsOrchestre != null) && (!chefsOrchestre.isEmpty())) {
			allAuteurs = auteurs + " " + chefsOrchestre;
		} else {
			allAuteurs = auteurs;
		}
		String auteursAlbum = doubleQuoteEnclose(allAuteurs) + CSV_SEPARATOR + doubleQuoteEnclose(album.getTitre()) + CSV_SEPARATOR;
		
		return album.getFormatAlbum().printAudioFilesCsvParts(CSV_SEPARATOR).stream()
				.map(audioCsv -> auteursAlbum + audioCsv).toList();
	}
	
	private static String doubleQuoteEnclose(String s) {
		return "\"" + s + "\"";
	}
}
