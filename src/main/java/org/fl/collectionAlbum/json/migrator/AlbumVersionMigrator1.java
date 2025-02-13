/*
 * MIT License

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

package org.fl.collectionAlbum.json.migrator;

import org.fl.collectionAlbum.JsonMusicProperties;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class AlbumVersionMigrator1 implements VersionMigrator {
	
	private static final int TARGET_VERSION = 1;
	
	private static AlbumVersionMigrator1 instance;
	
	private AlbumVersionMigrator1() {
	}

	protected static AlbumVersionMigrator1 getInstance() {
		if (instance == null) {
			instance = new AlbumVersionMigrator1();
		}
		return instance;
	}
	
	@Override
	public ObjectNode migrate(ObjectNode json) {
		
		if (checkVersion(json)) {
			json.put(JsonMusicProperties.JSON_VERSION, TARGET_VERSION);
		}
		return json;
	}

	@Override
	public int targetVersion() {
		return TARGET_VERSION;
	}

}
