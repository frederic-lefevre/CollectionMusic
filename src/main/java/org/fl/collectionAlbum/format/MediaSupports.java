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

import org.fl.collectionAlbum.JsonMusicProperties;

// Définition des différents supports tels que définis dans la description json des albums
public enum MediaSupports {
	
	CD(		 MediaSupportCategories.CD,  	   JsonMusicProperties.CD,	  ContentNature.AUDIO),
	K7(		 MediaSupportCategories.K7, 	   JsonMusicProperties.K7,	  ContentNature.AUDIO),
	Vinyl33T(MediaSupportCategories.Vinyl33T, 	   JsonMusicProperties._33T,	  ContentNature.AUDIO),
	Vinyl45T(MediaSupportCategories.Vinyl45T, 	   JsonMusicProperties._45T, ContentNature.AUDIO),
	MiniCD(	 MediaSupportCategories.MiniCD,  JsonMusicProperties.MINI_CD, ContentNature.AUDIO),
	MiniDVD( MediaSupportCategories.MiniDVD, JsonMusicProperties.MINI_DVD, ContentNature.VIDEO),
	Mini33T( MediaSupportCategories.Mini33T, JsonMusicProperties.MINI_33T, ContentNature.AUDIO),
	Maxi45T( MediaSupportCategories.Maxi45T, JsonMusicProperties.MAXI_45T, ContentNature.AUDIO),
	VHS(	 MediaSupportCategories.VHS,	   JsonMusicProperties.VHS,   ContentNature.VIDEO),
	DVD(	 MediaSupportCategories.DVD, 	   JsonMusicProperties.DVD,   ContentNature.VIDEO),
	BluRay( MediaSupportCategories.BluRay, JsonMusicProperties.BLURAY,   ContentNature.VIDEO),
	BluRayAudio( MediaSupportCategories.BluRay, JsonMusicProperties.BLURAY_AUDIO,   ContentNature.AUDIO);
	
	private final MediaSupportCategories supportPhysique ;
	private final String jsonPropertyName ;
	private final ContentNature contentNature;
	
	private MediaSupports(MediaSupportCategories sp, String jp, ContentNature cn) {
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

	ContentNature getContentNature() {
		return contentNature;
	}
}