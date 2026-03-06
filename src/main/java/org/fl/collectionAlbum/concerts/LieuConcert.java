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

package org.fl.collectionAlbum.concerts;

import java.util.Objects;
import java.util.logging.Logger;

public class LieuConcert {

	private static final Logger logger = Logger.getLogger(LieuConcert.class.getName());
	
	private final String lieu;
	private final ListeConcert listeConcert;

	public LieuConcert(String lieu) {
	
		if ((lieu == null) || lieu.isBlank()) {
			String cause = "Invalid lieu parameter (null or blank) " + Objects.toString(lieu);
			logger.severe(cause);
			throw new IllegalArgumentException(cause);
		}
		this.lieu = lieu;
		listeConcert = ListeConcert.Builder.getBuilder().build();
	}

	public String getLieu() {
		return lieu;
	}

	public int getNombreConcert() {
		return listeConcert.getNombreConcerts();
	}

	public void addConcert(Concert concert) {
		if (concert == null) {
			String cause = "Invalid null concert parameter";
			logger.severe(cause);
			throw new IllegalArgumentException(cause);
		}
		listeConcert.addConcert(concert);
	}

	public ListeConcert getListeConcert() {
		return listeConcert;
	}
}
