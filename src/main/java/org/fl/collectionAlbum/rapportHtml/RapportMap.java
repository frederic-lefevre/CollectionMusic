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
