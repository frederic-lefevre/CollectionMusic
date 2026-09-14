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

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.utils.CollectionImage.ImageStatus;
import org.fl.collectionAlbum.utils.CollectionImage.ResultImage;
import org.fl.discogsInterface.CollectionValue;
import org.fl.discogsInterface.Currency;
import org.fl.discogsInterface.DiscogsApi;
import org.fl.discogsInterface.DiscogsApi.DiscogsApiResponse;
import org.fl.discogsInterface.DiscogsApi.DiscogsImageResponse;
import org.fl.discogsInterface.Image;
import org.fl.discogsInterface.Release;
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
		userProfile = getUserProfile();
		collectionValue = getCollectionValue(userProfile);
		
		if (userName != null) {
			if (userProfile == null) {
				logger.severe("Null userProfile");
			} else if (! userName.equals(userProfile.username())) {
				logger.severe("The userName in file (" + userName + ") does not match the user name of the profile (" + userProfile.username() + ")");
			}
		}
	}
	
	private UserProfile getUserProfile() {
		
		DiscogsApiResponse<UserProfile> userProfileResponse = discogsApi.userProfile();
		if (checkDiscogsApiResponse(userProfileResponse, "discogsApi.userProfile()", "")) {
			return userProfileResponse.value();
		} else {
			return null;
		}
	}
	
	private String getRawUserProfile() {
		
		DiscogsApiResponse<UserProfile> userProfileResponse = discogsApi.userProfile();
		if (checkDiscogsApiResponse(userProfileResponse, "discogsApi.userProfile()", "")) {
			return userProfileResponse.rawResponse();
		} else {
			return null;
		}
	}
	
	private DiscogsCollectionValue getCollectionValue(UserProfile userProfile) {
		
		DiscogsApiResponse<CollectionValue> collectionValueResponse = discogsApi.collectionValue();
		if (checkDiscogsApiResponse(collectionValueResponse, "discogsApi.collectionValue()", "")) {
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
				logger.log(Level.SEVERE, "Exception parsing discogs collection value:\n" + collectionValueResponse.rawResponse(), e);
				return null;
			}
		} else {
			return null;
		}
	}
	
	private String getRawCollectionValue() {
		
		DiscogsApiResponse<CollectionValue> collectionValueResponse = discogsApi.collectionValue();
		if (checkDiscogsApiResponse(collectionValueResponse, "discogsApi.collectionValue()", "")) {
			return collectionValueResponse.rawResponse();
		} else {
			return null;
		}
	}
	
	private static final String PRIMARY = "primary";
	
	private static boolean isPrimary(Image image) {
		return (image != null) && PRIMARY.equals(image.type());
	}
	
	private static class ReleaseImageComparator implements Comparator<Image> {

		@Override
		public int compare(Image i1, Image i2) {
			
			boolean o1Primary = isPrimary(i1);
			boolean o2Primary = isPrimary(i2);
			if (o1Primary && o2Primary) {
				return 0;
			} else if (o1Primary) {
				return -1;
			} else if (o2Primary) {
				return 1;
			} else {
				return 0;
			}
		}		
	}
	
	private static final ReleaseImageComparator releaseImageComparator = new ReleaseImageComparator();
	
 	private Release getRelease(String releaseId) {

		DiscogsApiResponse<Release> releaseResponse = discogsApi.release(releaseId);
		if (checkDiscogsApiResponse(releaseResponse, "discogsApi.release()", releaseId)) {
			Release release = releaseResponse.value();
			if (release != null) {
				List<Image> images = release.images();
				if (images != null) {
					images.sort(releaseImageComparator);
				}
			}
			return release;
		} else {
			return null;
		}
	}
	
	private String getRawRelease(String releaseId) {

		DiscogsApiResponse<Release> releaseResponse = discogsApi.release(releaseId);
		if (checkDiscogsApiResponse(releaseResponse, "discogsApi.release()", releaseId)) {
			return releaseResponse.rawResponse();
		} else {
			return null;
		}
	}
	
	private ResultImage getImage(URI imageUri) {
		DiscogsImageResponse imageResponse = discogsApi.image(imageUri);
		if (imageResponse == null) {
			logger.severe("Null response returned by discogsApi.image() on " + Objects.toString(imageUri));
			return new ResultImage(null, ImageStatus.IN_ERROR);
		} else if (imageResponse.statusCode() == 404) {
			logger.severe("discogs image not found " + Objects.toString(imageUri));
			return new ResultImage(null, ImageStatus.NOT_FOUND);
		} else if (imageResponse.statusCode() == DiscogsApi.TOO_MANY_REQUEST_CODE) {
			logger.warning("Too many image request sent to discogs");
			return new ResultImage(null, ImageStatus.TOO_MANY_REQUEST);
		} else if ((imageResponse.statusCode() < 200) || (imageResponse.statusCode() >= 300)) {
			return new ResultImage(null, ImageStatus.IN_ERROR);
		} else {
			return new ResultImage(imageResponse.image(), ImageStatus.OK);
		}
	}
		
	private boolean checkDiscogsApiResponse(DiscogsApiResponse<?> response, String call, String resource) {

		if (response == null) {
			logger.severe("Null response returned by " + call + " " + resource);
			return false;
		} else if (response.statusCode() == 404){
			logger.severe(call + " " + resource + " not found on discogs\n" + response.rawResponse());
			return false;
		} else if (response.statusCode() == DiscogsApi.TOO_MANY_REQUEST_CODE){
			logger.warning("Too many request sent to discogs");
			return false;
		} else if ((response.statusCode() < 200) || (response.statusCode() >= 300)) {
			logger.severe(call + " " + resource + " call error.\n" + response.rawResponse() + "\nStatus code: " + response.statusCode());
			return false;
		} else {
			return true;
		}
	}
	
	public static void clear() {
		discogsInterfaceInstance = null;
	}
	
	public static DiscogsCollectionValue collectionValue() {
		return getInstance().collectionValue;
	}
	
	public static String rawCollectionValue() {
		return getInstance().getRawCollectionValue();
	}
	
	public static UserProfile userProfile() {
		return getInstance().userProfile;
	}
	
	public static String rawUserProfile() {
		return getInstance().getRawUserProfile();
	}
	
	public static Release release(String releaseId) {
		return getInstance().getRelease(releaseId);
	}
	
	public static String rawRelease(String releaseId) {
		return getInstance().getRawRelease(releaseId);
	}
	
	public static ResultImage image(URI imageUri) {
		return getInstance().getImage(imageUri);
	}
}
