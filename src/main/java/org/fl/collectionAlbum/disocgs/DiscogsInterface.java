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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.discogsInterface.CollectionValue;
import org.fl.discogsInterface.DiscogsApi;
import org.fl.discogsInterface.DiscogsApi.DiscogsApiResponse;

public class DiscogsInterface {

	private static final Logger logger = Logger.getLogger(DiscogsInterface.class.getName());
	
	private static DiscogsInterface discogsInterfaceInstance;
	
	private static DiscogsInterface getInstance() {
		if (discogsInterfaceInstance == null) {
			discogsInterfaceInstance = new DiscogsInterface();
		}
		return discogsInterfaceInstance;
	}
	
	private final DiscogsApi discogsApi;
	private DiscogsCollectionValue collectionValue;
	
	private DiscogsInterface() {
		
		String userName = Control.getDiscogsUserName();
		String userToken = Control.getDiscogsUserToken();
		
		DiscogsApi.Builder discogsApiBuilder = DiscogsApi.builder();
		if ((userName == null) || userName.isEmpty()) {
			logger.severe("Discogs user name is empty. Discogs interface will function in degraded mode");
		} else {
			discogsApiBuilder.userName(userName);
		}
		if ((userToken == null) || userToken.isEmpty()) {
			logger.severe("Discogs token is empty. Discogs interface will function in degraded mode");
		} else {
			discogsApiBuilder.token(userToken);
		}
		discogsApi = discogsApiBuilder.build();
		
		DiscogsApiResponse<CollectionValue> collectionValueResponse = discogsApi.collectionValue();
		if (collectionValueResponse == null) {
			logger.severe("Null response returned by discogsApi.collectionValue()");
		} else {
			try {
				collectionValue = DiscogsCollectionValue.convertDiscogsValue(collectionValueResponse.value());
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Exception parsing discogs collection value:\n" + collectionValueResponse.rawResponse());
			}
		}
	}
	
	public static void clear() {
		discogsInterfaceInstance = null;
	}
	
	public static DiscogsCollectionValue collectionValue() {
		return getInstance().collectionValue;
	}
}
