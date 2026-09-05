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
import org.fl.discogsInterface.Currency;
import org.fl.discogsInterface.DiscogsApi;
import org.fl.discogsInterface.DiscogsApi.DiscogsApiResponse;
import org.fl.discogsInterface.UserProfile;

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
	private final DiscogsCollectionValue collectionValue;
	private final UserProfile userProfile;
	
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
		userProfile = getUserProfile(discogsApi);
		collectionValue = getCollectionValue(discogsApi, userProfile);
		
		if (userName != null) {
			if (userProfile == null) {
				logger.severe("Null userProfile");
			} else if (! userName.equals(userProfile.username())) {
				logger.severe("The userName in file (" + userName + ") does not match the user name of the profile (" + userProfile.username() + ")");
			}
		}
	}
	
	private UserProfile getUserProfile(DiscogsApi discogsApi) {
		DiscogsApiResponse<UserProfile> userProfileResponse = discogsApi.userProfile();
		if (userProfileResponse == null) {		
			logger.severe("Null response returned by discogsApi.userProfile()");
			return null;
		} else {
			return userProfileResponse.value();
		}
	}
	
	private DiscogsCollectionValue getCollectionValue(DiscogsApi discogsApi, UserProfile userProfile) {
		
		DiscogsApiResponse<CollectionValue> collectionValueResponse = discogsApi.collectionValue();
		if (collectionValueResponse == null) {
			logger.severe("Null response returned by discogsApi.collectionValue()");
			return null;
		} else {
			Currency currency;
			if (userProfile == null) {
				logger.warning("Discogs user profile is null. Collection value is in EURO currency");
				currency = Currency.EUR;
			} else {
				currency = userProfile.currency();
			}
			
			try {
				return DiscogsCollectionValue.convertDiscogsValue(collectionValueResponse.value(), currency);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Exception parsing discogs collection value:\n" + collectionValueResponse.rawResponse());
				return null;
			}
		}
	}
	
	public static void clear() {
		discogsInterfaceInstance = null;
	}
	
	public static DiscogsCollectionValue collectionValue() {
		return getInstance().collectionValue;
	}
	
	public static UserProfile userProfile() {
		return getInstance().userProfile;
	}
}
