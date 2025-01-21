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

package org.fl.collectionAlbum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ListUtilsTest {

	@Test
	void testRandomListPick() {
		
		int valueMin = 0;
		int valueMax = 2500;
		int numrberOfTests = 1000;
		
		List<Integer> aList = new ArrayList<>();		
		IntStream.range(valueMin,valueMax).forEach(aList::add);
		
		assertThat(aList).hasSize(valueMax);
		
		Stream.generate(() -> ListUtils.pickRandomElement(aList))
			.limit(numrberOfTests)
			.forEach(elem -> assertThat(elem).isBetween(valueMin, valueMax));

	}
	
	@Test
	void testRandomListPick2() {
		
		int valueMin = 0;
		int valueMax = 2500;
		int numrberOfTests = 1000;
		
		// List of even int from valueMin to valueMax, each int being repeated twice
		List<Integer> aList = new ArrayList<>();		
		IntStream.range(valueMin,valueMax).map(i -> { if (i % 2 == 0) return i; else return i-1;}).forEach(aList::add);
		
		assertThat(aList).hasSize(valueMax);
		assertThat(aList.stream().distinct().collect(Collectors.toList())).hasSize(valueMax/2);
		
		Stream.generate(() -> ListUtils.pickRandomDistinctElements(aList, 3))
			.limit(numrberOfTests)
			.forEach(pickList -> 
				// All pickList are composed of 3 distinct element, each between valueMin and valueMax
				assertThat(pickList.stream().distinct().collect(Collectors.toList()))
					.hasSize(3)
					.allSatisfy(elem -> assertThat(elem).isBetween(valueMin, valueMax)));

	}
	
	@Test
	void nullListShouldRaiseNullPointException() {
		assertThatNullPointerException().isThrownBy(() -> ListUtils.pickRandomElement(null));
	}
	
	@Test
	void emptyListShouldRaiseIllegalArgumentException() {
		assertThatIllegalArgumentException().isThrownBy(() -> ListUtils.pickRandomElement(List.of()));
	}
	
	@Test
	void testSingleElement() {
		
		String value = "une valeur";
		assertThat(ListUtils.pickRandomElement(List.of(value))).isEqualTo(value);
	}
	
	@Test
	void nullListShouldRaiseNullPointException2() {
		assertThatNullPointerException().isThrownBy(() -> ListUtils.pickRandomDistinctElements(null, 5));
	}
	
	@Test
	void emptyListShouldRaiseIllegalArgumentException2() {
		assertThatIllegalArgumentException().isThrownBy(() -> ListUtils.pickRandomDistinctElements(List.of(), 3));
	}
	
	@Test
	void zeroRequestedShouldRaiseIllegalArgumentException() {
		assertThatIllegalArgumentException().isThrownBy(() -> ListUtils.pickRandomDistinctElements(List.of("une string"), 0));
	}
	
	@Test
	void testSingleElement2() {
		
		String value = "une valeur";
		assertThat(ListUtils.pickRandomDistinctElements(List.of(value), 2)).singleElement().isEqualTo(value);
	}
	
	@Test
	void testSingleElement3() {
		
		String value = "une valeur";
		assertThat(ListUtils.pickRandomDistinctElements(List.of(value), 1)).singleElement().isEqualTo(value);
	}
	
	@Test
	void testListOfTwoIdenticElements() {
		
		String value = "une valeur";
		assertThat(ListUtils.pickRandomDistinctElements(List.of(value, value), 3)).singleElement().isEqualTo(value);
	}
	
	@Test
	void testListOfTwoIdenticElements2() {
		
		String value = "une valeur";
		assertThat(ListUtils.pickRandomDistinctElements(List.of(value, value), 2)).singleElement().isEqualTo(value);
	}
	
	@Test
	void testListOfTwoElements() {
		
		String v1 = "v1";
		String v2 = "v2";

		assertThat(ListUtils.pickRandomDistinctElements(List.of(v1, v2), 2)).hasSize(2).containsExactlyInAnyOrder(v1, v2);
	}
	
	@Test
	void testListOfThreeElements() {
		
		String v1 = "v1";
		String v2 = "v2";
		String v3 = "v3";
		assertThat(ListUtils.pickRandomDistinctElements(List.of(v1, v2, v3), 2)).hasSize(2);
	}
}
