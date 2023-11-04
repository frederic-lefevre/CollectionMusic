/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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

package org.fl.collectionAlbum.jsonParsers;

import java.util.Optional;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ParserHelpers {

	private final static Logger albumLog = Control.getAlbumLog();
	
	private ParserHelpers() {
	}

	public static String parseStringProperty(JsonObject json, String property, boolean mandatory) {
		
		return Optional.ofNullable(json.get(property))
				.map(JsonElement::getAsString)
				.orElseGet(() -> {
					if (mandatory) {
						albumLog.severe("Mandatory property " + property + " not found in " + json);
					}
					return null;
				});
	}
}
