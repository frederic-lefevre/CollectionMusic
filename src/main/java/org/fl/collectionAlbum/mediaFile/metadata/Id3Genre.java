/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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

package org.fl.collectionAlbum.mediaFile.metadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Id3Genre {

	private static final Logger logger = Logger.getLogger(Id3Genre.class.getName());
	
	private static final Map<Integer, String> ID3_V1_GENRES = new HashMap<>();
	
	static {
		   ID3_V1_GENRES.put(0, "Blues");
		    ID3_V1_GENRES.put(1, "Classic Rock");
		    ID3_V1_GENRES.put(2, "Country");
		    ID3_V1_GENRES.put(3, "Dance");
		    ID3_V1_GENRES.put(4, "Disco");
		    ID3_V1_GENRES.put(5, "Funk");
		    ID3_V1_GENRES.put(6, "Grunge");
		    ID3_V1_GENRES.put(7, "Hip-Hop");
		    ID3_V1_GENRES.put(8, "Jazz");
		    ID3_V1_GENRES.put(9, "Metal");
		    ID3_V1_GENRES.put(10, "New Age");
		    ID3_V1_GENRES.put(11, "Oldies");
		    ID3_V1_GENRES.put(12, "Other");
		    ID3_V1_GENRES.put(13, "Pop");
		    ID3_V1_GENRES.put(14, "R&B");
		    ID3_V1_GENRES.put(15, "Rap");
		    ID3_V1_GENRES.put(16, "Reggae");
		    ID3_V1_GENRES.put(17, "Rock");
		    ID3_V1_GENRES.put(18, "Techno");
		    ID3_V1_GENRES.put(19, "Industrial");
		    ID3_V1_GENRES.put(20, "Alternative");
		    ID3_V1_GENRES.put(21, "Ska");
		    ID3_V1_GENRES.put(22, "Death Metal");
		    ID3_V1_GENRES.put(23, "Pranks");
		    ID3_V1_GENRES.put(24, "Soundtrack");
		    ID3_V1_GENRES.put(25, "Euro-Techno");
		    ID3_V1_GENRES.put(26, "Ambient");
		    ID3_V1_GENRES.put(27, "Trip-Hop");
		    ID3_V1_GENRES.put(28, "Vocal");
		    ID3_V1_GENRES.put(29, "Jazz+Funk");
		    ID3_V1_GENRES.put(30, "Fusion");
		    ID3_V1_GENRES.put(31, "Trance");
		    ID3_V1_GENRES.put(32, "Classical");
		    ID3_V1_GENRES.put(33, "Instrumental");
		    ID3_V1_GENRES.put(34, "Acid");
		    ID3_V1_GENRES.put(35, "House");
		    ID3_V1_GENRES.put(36, "Game");
		    ID3_V1_GENRES.put(37, "Sound Clip");
		    ID3_V1_GENRES.put(38, "Gospel");
		    ID3_V1_GENRES.put(39, "Noise");
		    ID3_V1_GENRES.put(40, "AlternRock");
		    ID3_V1_GENRES.put(41, "Bass");
		    ID3_V1_GENRES.put(42, "Soul");
		    ID3_V1_GENRES.put(43, "Punk");
		    ID3_V1_GENRES.put(44, "Space");
		    ID3_V1_GENRES.put(45, "Meditative");
		    ID3_V1_GENRES.put(46, "Instrumental Pop");
		    ID3_V1_GENRES.put(47, "Instrumental Rock");
		    ID3_V1_GENRES.put(48, "Ethnic");
		    ID3_V1_GENRES.put(49, "Gothic");
		    ID3_V1_GENRES.put(50, "Darkwave");
		    ID3_V1_GENRES.put(51, "Techno-Industrial");
		    ID3_V1_GENRES.put(52, "Electronic");
		    ID3_V1_GENRES.put(53, "Pop-Folk");
		    ID3_V1_GENRES.put(54, "Eurodance");
		    ID3_V1_GENRES.put(55, "Dream");
		    ID3_V1_GENRES.put(56, "Southern Rock");
		    ID3_V1_GENRES.put(57, "Comedy");
		    ID3_V1_GENRES.put(58, "Cult");
		    ID3_V1_GENRES.put(59, "Gangsta");
		    ID3_V1_GENRES.put(60, "Top 40");
		    ID3_V1_GENRES.put(61, "Christian Rap");
		    ID3_V1_GENRES.put(62, "Pop/Funk");
		    ID3_V1_GENRES.put(63, "Jungle");
		    ID3_V1_GENRES.put(64, "Native American");
		    ID3_V1_GENRES.put(65, "Cabaret");
		    ID3_V1_GENRES.put(66, "New Wave");
		    ID3_V1_GENRES.put(67, "Psychadelic");
		    ID3_V1_GENRES.put(68, "Rave");
		    ID3_V1_GENRES.put(69, "Showtunes");
		    ID3_V1_GENRES.put(70, "Trailer");
		    ID3_V1_GENRES.put(71, "Lo-Fi");
		    ID3_V1_GENRES.put(72, "Tribal");
		    ID3_V1_GENRES.put(73, "Acid Punk");
		    ID3_V1_GENRES.put(74, "Acid Jazz");
		    ID3_V1_GENRES.put(75, "Polka");
		    ID3_V1_GENRES.put(76, "Retro");
		    ID3_V1_GENRES.put(77, "Musical");
		    ID3_V1_GENRES.put(78, "Rock & Roll");
		    ID3_V1_GENRES.put(79, "Hard Rock");
		    ID3_V1_GENRES.put(80, "Folk");
		    ID3_V1_GENRES.put(81, "Folk-Rock");
		    ID3_V1_GENRES.put(82, "National Folk");
		    ID3_V1_GENRES.put(83, "Swing");
		    ID3_V1_GENRES.put(84, "Fast Fusion");
		    ID3_V1_GENRES.put(85, "Bebob");
		    ID3_V1_GENRES.put(86, "Latin");
		    ID3_V1_GENRES.put(87, "Revival");
		    ID3_V1_GENRES.put(88, "Celtic");
		    ID3_V1_GENRES.put(89, "Bluegrass");
		    ID3_V1_GENRES.put(90, "Avantgarde");
		    ID3_V1_GENRES.put(91, "Gothic Rock");
		    ID3_V1_GENRES.put(92, "Progressive Rock");
		    ID3_V1_GENRES.put(93, "Psychedelic Rock");
		    ID3_V1_GENRES.put(94, "Symphonic Rock");
		    ID3_V1_GENRES.put(95, "Slow Rock");
		    ID3_V1_GENRES.put(96, "Big Band");
		    ID3_V1_GENRES.put(97, "Chorus");
		    ID3_V1_GENRES.put(98, "Easy Listening");
		    ID3_V1_GENRES.put(99, "Acoustic");
		    ID3_V1_GENRES.put(100, "Humour");
		    ID3_V1_GENRES.put(101, "Speech");
		    ID3_V1_GENRES.put(102, "Chanson");
		    ID3_V1_GENRES.put(103, "Opera");
		    ID3_V1_GENRES.put(104, "Chamber Music");
		    ID3_V1_GENRES.put(105, "Sonata");
		    ID3_V1_GENRES.put(106, "Symphony");
		    ID3_V1_GENRES.put(107, "Booty Bass");
		    ID3_V1_GENRES.put(108, "Primus");
		    ID3_V1_GENRES.put(109, "Porn Groove");
		    ID3_V1_GENRES.put(110, "Satire");
		    ID3_V1_GENRES.put(111, "Slow Jam");
		    ID3_V1_GENRES.put(112, "Club");
		    ID3_V1_GENRES.put(113, "Tango");
		    ID3_V1_GENRES.put(114, "Samba");
		    ID3_V1_GENRES.put(115, "Folklore");
		    ID3_V1_GENRES.put(116, "Ballad");
		    ID3_V1_GENRES.put(117, "Power Ballad");
		    ID3_V1_GENRES.put(118, "Rhythmic Soul");
		    ID3_V1_GENRES.put(119, "Freestyle");
		    ID3_V1_GENRES.put(120, "Duet");
		    ID3_V1_GENRES.put(121, "Punk Rock");
		    ID3_V1_GENRES.put(122, "Drum Solo");
		    ID3_V1_GENRES.put(123, "Acapella");
		    ID3_V1_GENRES.put(124, "Euro-House");
		    ID3_V1_GENRES.put(125, "Dance Hall");
		    ID3_V1_GENRES.put(126, "Goa");
		    ID3_V1_GENRES.put(127, "Drum & Bass");
		    ID3_V1_GENRES.put(128, "Club-House");
		    ID3_V1_GENRES.put(129, "Hardcore");
		    ID3_V1_GENRES.put(130, "Terror");
		    ID3_V1_GENRES.put(131, "Indie");
		    ID3_V1_GENRES.put(132, "BritPop");
		    ID3_V1_GENRES.put(133, "Negerpunk"); // to say the least - this name is problematic
		    ID3_V1_GENRES.put(134, "Polsk Punk");
		    ID3_V1_GENRES.put(135, "Beat");
		    ID3_V1_GENRES.put(136, "Christian Gangsta Rap");
		    ID3_V1_GENRES.put(137, "Heavy Metal");
		    ID3_V1_GENRES.put(138, "Black Metal");
		    ID3_V1_GENRES.put(139, "Crossover");
		    ID3_V1_GENRES.put(140, "Contemporary Christian");
		    ID3_V1_GENRES.put(141, "Christian Rock");
		    ID3_V1_GENRES.put(142, "Merengue");
		    ID3_V1_GENRES.put(143, "Salsa");
		    ID3_V1_GENRES.put(144, "Thrash Metal");
		    ID3_V1_GENRES.put(145, "Anime");
		    ID3_V1_GENRES.put(146, "JPop");
		    ID3_V1_GENRES.put(147, "SynthPop");
	}
	
	public static Optional<String> getGenre(int v1Id) {
		return Optional.ofNullable(ID3_V1_GENRES.get(v1Id));
	}
	
	public static String getGenre(String genre) {
		
		if ((genre.indexOf('(') == 0) && (genre.indexOf(')') == genre.length() -1)) {
			try {
				return getGenre(Integer.parseInt(genre.substring(1,genre.length() -1))).orElse(genre);
			} catch (NumberFormatException e) {
				logger.log(Level.WARNING, "Cannot convert ID3 genre", e);
			}
		}
		return genre;
	}

}
