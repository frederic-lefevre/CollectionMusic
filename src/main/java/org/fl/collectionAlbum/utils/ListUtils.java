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

package org.fl.collectionAlbum.utils;

import java.util.List;
import java.util.SplittableRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListUtils {

	private static final SplittableRandom random = new SplittableRandom();
	
	// Randomly return one element of a list
	public static <T> T pickRandomElement(List<T> l) {
		
		if ((l == null) || l.isEmpty()) {
			throw new IllegalArgumentException("Null or empty list");
		}
		
		if (l.size() == 1) {
			return l.get(0);
		} else {
			return l.get(random.nextInt(l.size() - 1));
		}
	}
	
	// Randomly remove and return one element of a list
	public static <T> T pickRemoveRandomElement(List<T> l) {
		
		if ((l == null) || l.isEmpty()) {
			throw new IllegalArgumentException("Null or empty list");
		}
		
		if (l.size() == 1) {
			return l.remove(0);
		} else {
			return l.remove(random.nextInt(l.size() - 1));
		}
	}
	
	// Randomly return nbPick distinct element of a list
	public static <T> List<T> pickRandomDistinctElements(List<T> l, int nbPick) {

		if (l.isEmpty()) {
			throw new IllegalArgumentException("Empty list");
		} else if (nbPick < 1) {
			throw new IllegalArgumentException("Invalid number of elements requested: " + nbPick);
		} 

		List<T> lWithDistinctElements = l.stream().distinct().collect(Collectors.toList());
		
		if (lWithDistinctElements.size() <= nbPick) {
			return lWithDistinctElements;
		} else {
			return Stream.generate(() -> pickRemoveRandomElement(lWithDistinctElements))
					.limit(nbPick)
					.collect(Collectors.toList());
		}
	}
}
