/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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

package org.fl.collectionAlbum.rapportCsv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.format.AbstractAudioFile;

public class RapportCsv {

	private static final Logger rapportLog = Logger.getLogger(RapportCsv.class.getName());
	
	private static final String CSV_SEPARATOR = ";";
	
	public static void writeCsvAudioFile(ListeAlbum listeAlbum, Predicate<AbstractAudioFile> audioFileFilter, Path filePath) {
		
		Path csvDir = filePath.getParent();
		if (!Files.exists(csvDir)) {
			try {
				Files.createDirectories(csvDir);
			} catch (IOException e) {
				String csvDirString = Optional.ofNullable(csvDir)
						.map(fp -> fp.toString())
						.orElse("csvDir is null");
				rapportLog.log(Level.SEVERE, "IOException creating csv dir " + csvDirString, e);
			}
		}
		
		String filePathString = Optional.ofNullable(filePath)
				.map(fp -> fp.toString())
				.orElse("filePath is null");
		
		try (BufferedWriter outputStream = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
			
			for (String titles : csvTitlesForListeAlbum(listeAlbum, audioFileFilter)) {
				outputStream.write(titles);
				outputStream.write("\n");				
			}
			
			listeAlbum.getAlbums().stream()
				.flatMap(album ->  csvForOneAlbum(album, audioFileFilter).stream())
				.distinct()
				.forEachOrdered(line -> {
					try {
						outputStream.write(line);
						outputStream.write("\n");
					} catch (IOException e) {

						rapportLog.log(Level.SEVERE, "IOException writing csv file " + filePathString + " line " + line, e);
					}
				});
		} catch (IOException e) {
			rapportLog.log(Level.SEVERE, "IOException opening csv file " + filePathString, e);
		}
	}
	
	private static final String AUTEURS_ALBUM_TITLES = "Auteurs" + CSV_SEPARATOR + "Titres" + CSV_SEPARATOR;
	
	private static List<String> csvForOneAlbum(Album album, Predicate<AbstractAudioFile> audioFileFilter) {
		
		String auteurs = album.getAuteurs().stream().map(auteur -> auteur.getNomComplet()).collect(Collectors.joining(" "));
		String chefsOrchestre = album.getChefsOrchestre().stream().map(co -> co.getNomComplet()).collect(Collectors.joining(" "));
		
		String allAuteurs;
		if ((chefsOrchestre != null) && (!chefsOrchestre.isEmpty())) {
			allAuteurs = auteurs + " " + chefsOrchestre;
		} else {
			allAuteurs = auteurs;
		}
		String auteursAlbum = doubleQuoteEnclose(allAuteurs) + CSV_SEPARATOR + doubleQuoteEnclose(album.getTitre()) + CSV_SEPARATOR;
		
		return album.getFormatAlbum().printAudioFilesCsvParts(CSV_SEPARATOR, audioFileFilter).stream()
				.map(audioCsv -> auteursAlbum + audioCsv).toList();
	}
	
	private static String doubleQuoteEnclose(String s) {
		return "\"" + s.replace("\"", "\"\"") + "\"";
	}
	
	private static List<String> csvTitlesForListeAlbum(ListeAlbum listeAlbum, Predicate<AbstractAudioFile> audioFileFilter) {
		
		return listeAlbum.getAlbums().stream()
			.map(Album::getFormatAlbum)
			.flatMap(format -> format.printAudioFilesCsvTitles(CSV_SEPARATOR, audioFileFilter).stream())
			.distinct()
			.map(titles -> AUTEURS_ALBUM_TITLES + titles)
		    .toList();
	}
}
