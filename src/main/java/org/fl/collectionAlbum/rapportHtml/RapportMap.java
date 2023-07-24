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


package org.fl.collectionAlbum.rapportHtml;

import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;

public class RapportMap<K> extends HashMap<K, Path> {

	private static final long serialVersionUID = 1L;
	
	private int  id ;
	private final Path absoluteRootPath ;
	private final Path absoluteLocationPath ;	
	private final URI  absoluteRootUri ;
	
	public RapportMap(Path ar, Path al) {
		super();
		 id 			 	  = 0 ;
		 absoluteRootPath	  = ar ;
		 absoluteLocationPath = al ;
		 absoluteRootUri	  = absoluteRootPath.toUri() ;
	}

	public URI getUri(K musicObject) {
		Path relativePath = get(musicObject) ;
		Path absolutePath ;
		if (relativePath == null) {
			absolutePath = absoluteLocationPath.resolve("i" + id + ".html") ;
			id++ ;
			relativePath = absoluteRootPath.relativize(absolutePath) ;
			put(musicObject, relativePath) ;			
		} else {
			absolutePath = absoluteRootPath.resolve(relativePath) ;			
		}
		return absoluteRootUri.relativize(absolutePath.toUri()) ;	
	}
	
}
