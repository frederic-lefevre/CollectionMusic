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

package org.fl.collectionAlbum.json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ParserHelpers {

	private final static Logger albumLog = Logger.getLogger(ParserHelpers.class.getName());
	
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
	
	public static boolean parseBooleanProperty(JsonObject json, String property, boolean mandatory) {
		
		return Optional.ofNullable(json.get(property))
				.map(JsonElement::getAsBoolean)
				.orElseGet(() -> {
					if (mandatory) {
						albumLog.severe("Mandatory property " + property + " not found in " + json);
					}
					return false;
				});
	}
	
	public static List<String> getArrayAttribute(JsonObject json, String jsonMusicProperty) {

		JsonElement jElem = json.get(jsonMusicProperty);
		if (jElem != null) {
			if (jElem.isJsonArray()) {
				List<String> result = new ArrayList<>();
				JsonArray jArray = jElem.getAsJsonArray();
				for (JsonElement e : jArray) {
					result.add(e.getAsString());
				}
				albumLog.finest(() -> "Nombre de " + jsonMusicProperty + " " + result.size());
				return result;
			} else {
				albumLog.warning(jsonMusicProperty + " n'est pas un JsonArray pour l'artefact " + json);
			}
		} else {
			albumLog.info(() -> "No property " + jsonMusicProperty + " for " + json);
		}
		return new ArrayList<String>();
	}
	
	public static Set<String> getArrayAttributeAsSet(JsonObject json, String jsonMusicProperty) {

		JsonElement jElem = json.get(jsonMusicProperty);
		if (jElem != null) {
			if (jElem.isJsonArray()) {
				Set<String> result = new HashSet<>();
				jElem.getAsJsonArray().forEach(e -> result.add(e.getAsString()));
				return result;
			} else {
				albumLog.warning(jsonMusicProperty + " n'est pas un JsonArray pour l'artefact " + json);
			}
		} else {
			albumLog.info(() -> "No property " + jsonMusicProperty + " for " + json);
		}
		return null;
	}
}
