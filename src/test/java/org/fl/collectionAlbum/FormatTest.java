package org.fl.collectionAlbum;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class FormatTest {

	@Test
	void test() {
		
		Logger logger = Logger.getLogger(FormatTest.class.getName());
		
		String formatStr1 = "{}" ;
		JsonObject jf1 = new JsonParser().parse(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1, logger) ;
		
		assertEquals(0, format1.getPoids()) ;

	}

}
