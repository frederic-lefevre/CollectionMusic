package org.fl.collectionAlbum;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class FormatTest {

	private final static Logger logger = Logger.getLogger(FormatTest.class.getName());
	
	@Test
	void test1() {
		
		String formatStr1 = "{}" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1, logger) ;

		assertThat(format1.getPoids()).isZero();

	}

	@Test
	void test2() {
		
		String formatStr1 = "{\"cd\": 3 }" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1, logger) ;

		assertThat(format1.getPoids()).isEqualTo(3);

	}
	
	@Test
	void test3() {
		
		String formatStr1 = "{\"cd\": 2 , \"45t\" : 1 }" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1, logger) ;
		
		assertThat(format1.getPoids()).isEqualTo(2.5);
	}
	
	@Test
	void test4() {
		
		String formatStr1 = "{\"33t\": 2 , \"45t\" : 1 }" ;
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1, logger) ;
		
		String formatStr2 = "{\"cd\": 2 , \"45t\" : 1 }" ;
		JsonObject jf2 = JsonParser.parseString(formatStr2).getAsJsonObject();
		Format format2 = new Format(jf2, logger) ;
		
		Format format3 = new Format(null, logger) ;
		assertThat(format3.getPoids()).isZero();
		
		format3.incrementFormat(format1);
		assertThat(format1.getPoids()).isEqualTo(format3.getPoids());
		
		format3.incrementFormat(format2);
		assertThat(format3.getPoids()).isEqualTo(format1.getPoids() + format2.getPoids());
	}
}
