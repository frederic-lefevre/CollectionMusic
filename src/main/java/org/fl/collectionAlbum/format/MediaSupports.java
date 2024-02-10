/*
 MIT License

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

package org.fl.collectionAlbum.format;

import java.util.Set;

import org.fl.collectionAlbum.JsonMusicProperties;

// Définition des différents supports tels que définis dans la description json des albums
public enum MediaSupports {
	
	CD(		 MediaSupportCategories.CD,  	   JsonMusicProperties.CD,	  Set.of(ContentNature.AUDIO)),
	K7(		 MediaSupportCategories.K7, 	   JsonMusicProperties.K7,	  Set.of(ContentNature.AUDIO)),
	Vinyl33T(MediaSupportCategories.VinylLP, 	   JsonMusicProperties._33T,	  Set.of(ContentNature.AUDIO)),
	Vinyl45TLP(MediaSupportCategories.VinylLP, 	   JsonMusicProperties._45T_LP,	  Set.of(ContentNature.AUDIO)),
	Vinyl45T(MediaSupportCategories.MiniVinyl, 	   JsonMusicProperties._45T, Set.of(ContentNature.AUDIO)),
	MiniCD(	 MediaSupportCategories.MiniCD,  JsonMusicProperties.MINI_CD, Set.of(ContentNature.AUDIO)),
	MiniDVD( MediaSupportCategories.MiniDVD, JsonMusicProperties.MINI_DVD, Set.of(ContentNature.VIDEO)),
	Mini33T( MediaSupportCategories.MiniVinyl, JsonMusicProperties.MINI_33T, Set.of(ContentNature.AUDIO)),
	Maxi45T( MediaSupportCategories.MiniVinyl, JsonMusicProperties.MAXI_45T, Set.of(ContentNature.AUDIO)),
	VHS(	 MediaSupportCategories.VHS,	   JsonMusicProperties.VHS,   Set.of(ContentNature.VIDEO)),
	DVD(	 MediaSupportCategories.DVD, 	   JsonMusicProperties.DVD,   Set.of(ContentNature.VIDEO)),
	BluRay( MediaSupportCategories.BluRay, JsonMusicProperties.BLURAY,   Set.of(ContentNature.VIDEO)),
	BluRayAudio( MediaSupportCategories.BluRay, JsonMusicProperties.BLURAY_AUDIO,   Set.of(ContentNature.AUDIO)),
	BluRayMixed( MediaSupportCategories.BluRay, JsonMusicProperties.BLURAY_MIXED,   Set.of(ContentNature.AUDIO, ContentNature.VIDEO));
	
	private final MediaSupportCategories supportPhysique ;
	private final String jsonPropertyName ;
	private final Set<ContentNature> contentNature;
	
	private MediaSupports(MediaSupportCategories sp, String jp, Set<ContentNature> cn) {
		supportPhysique  = sp ;
		jsonPropertyName = jp ;
		contentNature	 = cn;
	}

	MediaSupportCategories getSupportPhysique() {
		return supportPhysique;
	}

	String getJsonPropertyName() {
		return jsonPropertyName;
	}

	Set<ContentNature> getContentNatures() {
		return contentNature;
	}
}