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

package org.fl.collectionAlbum;

import org.fl.collectionAlbum.albums.AlbumCommandParameter;

public class OsAction {

	private final String actionTitle;
	private final String actionCommand;
	private final AlbumCommandParameter albumCommandParameter;
	
	public OsAction(String t, String c, AlbumCommandParameter a) {
		actionTitle   = t;
		actionCommand = c;
		albumCommandParameter = a;
	}

	public String getActionTitle() {
		return actionTitle;
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public AlbumCommandParameter getAlbumCommandParameter() {
		return albumCommandParameter;
	}

}
