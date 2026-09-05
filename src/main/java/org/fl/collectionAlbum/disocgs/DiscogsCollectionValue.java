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

package org.fl.collectionAlbum.disocgs;

import java.text.ParseException;

import org.fl.discogsInterface.CollectionValue;
import org.fl.discogsInterface.Currency;

public record DiscogsCollectionValue(double maxValue, double medianValue, double minValue) {

	public static DiscogsCollectionValue convertDiscogsValue(CollectionValue collectionValue, Currency currency) throws ParseException {
		return new DiscogsCollectionValue(
				Currency.priceWithCurrencyToDouble(collectionValue.maximum(), currency),
				Currency.priceWithCurrencyToDouble(collectionValue.median(), currency),
				Currency.priceWithCurrencyToDouble(collectionValue.minimum(), currency)
				);
	}
}
