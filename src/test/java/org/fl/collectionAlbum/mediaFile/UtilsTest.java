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

package org.fl.collectionAlbum.mediaFile;

import static org.assertj.core.api.Assertions.*;
import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

class UtilsTest {

	@Test
	void shouldThrowNPE() {
		assertThatNullPointerException().isThrownBy(() -> Utils.remainingBytesEquals(null, new byte[0]));
	}
	
	@Test
	void shouldThrowNPE2() {
		assertThatNullPointerException().isThrownBy(() -> Utils.remainingBytesEquals(ByteBuffer.allocate(0), null));
	}
	
	@Test
	void shouldBeEqual() {
		assertThat(Utils.remainingBytesEquals(ByteBuffer.allocate(0), new byte[0])).isTrue();
	}
	
	@Test
	void shouldBeEqual2() {
		byte b = 57;
		assertThat(Utils.remainingBytesEquals(ByteBuffer.allocate(1).put(b).position(0), new byte[] {b})).isTrue();
	}
	
	@Test
	void shouldBeEqual3() {
		byte b1 = 23;
		byte b2 = 24;
		byte b3 = 25;
		byte b4 = 26;
		assertThat(Utils.remainingBytesEquals(
				ByteBuffer.allocate(4).put(b1).put(b2).put(b3).put(b4).position(0), 
				new byte[] {b1, b2, b3, b4}))
		.isTrue();
	}
	
	@Test
	void shouldBeNotEqual() {
		byte b = 57;
		assertThat(Utils.remainingBytesEquals(ByteBuffer.allocate(1).put(b).position(0), new byte[] {})).isFalse();
	}
	
	@Test
	void shouldBeNotEqual2() {
		byte b = 57;
		byte b2 = 58;
		assertThat(Utils.remainingBytesEquals(ByteBuffer.allocate(1).put(b).position(0), new byte[] {b2})).isFalse();
	}
	
	@Test
	void shouldNotBeEqual3() {
		byte b1 = 23;
		byte b2 = 24;
		byte b3 = 25;
		byte b4 = 26;
		assertThat(Utils.remainingBytesEquals(
				ByteBuffer.allocate(4).put(b1).put(b2).put(b3).put(b4).position(1), 
				new byte[] {b1, b2, b3, b4}))
		.isFalse();
	}
	
	@Test
	void shouldNotBeEqual4() {
		byte b1 = 23;
		byte b2 = 24;
		byte b3 = 25;
		byte b4 = 26;
		assertThat(Utils.remainingBytesEquals(
				ByteBuffer.allocate(4).put(b1).put(b2).put(b3).position(0), 
				new byte[] {b1, b2, b3, b4}))
		.isFalse();
	}
	
	@Test
	void shouldThrowNPE_NextBytes() {
		assertThatNullPointerException().isThrownBy(() -> Utils.nextBytesEquals(null, new byte[0]));
	}
	
	@Test
	void shouldThrowNPE2_NextBytes() {
		assertThatNullPointerException().isThrownBy(() -> Utils.nextBytesEquals(ByteBuffer.allocate(0), null));
	}
	
	@Test
	void shouldBeEqual_NextBytes() {
		assertThat(Utils.nextBytesEquals(ByteBuffer.allocate(0), new byte[0])).isTrue();
	}
	
	@Test
	void shouldBeEqual2_NextBytes() {
		byte b = 57;
		assertThat(Utils.nextBytesEquals(ByteBuffer.allocate(1).put(b).position(0), new byte[] {b})).isTrue();
	}
	
	@Test
	void shouldBeEqual3_NextBytes() {
		byte b1 = 23;
		byte b2 = 24;
		byte b3 = 25;
		byte b4 = 26;
		assertThat(Utils.nextBytesEquals(
				ByteBuffer.allocate(4).put(b1).put(b2).put(b3).put(b4).position(0), 
				new byte[] {b1, b2, b3, b4}))
		.isTrue();
	}
	
	@Test
	void shouldBeEqual4_NextBytes() {
		byte b = 57;
		assertThat(Utils.nextBytesEquals(ByteBuffer.allocate(1).put(b).position(0), new byte[] {})).isTrue();
	}
	
	@Test
	void shouldBeEqual5_NextBytes() {
		byte b1 = 23;
		byte b2 = 24;
		byte b3 = 25;
		byte b4 = 26;
		byte b5 = 27;
		assertThat(Utils.nextBytesEquals(
				ByteBuffer.allocate(5).put(b1).put(b2).put(b3).put(b4).put(b5).position(0), 
				new byte[] {b1, b2, b3, b4}))
		.isTrue();
	}
	
	@Test
	void shouldBeNotEqual2_NextBytes() {
		byte b = 57;
		byte b2 = 58;
		assertThat(Utils.nextBytesEquals(ByteBuffer.allocate(1).put(b).position(0), new byte[] {b2})).isFalse();
	}
	
	@Test
	void shouldNotBeEqual3_NextBytes() {
		byte b1 = 23;
		byte b2 = 24;
		byte b3 = 25;
		byte b4 = 26;
		assertThat(Utils.nextBytesEquals(
				ByteBuffer.allocate(4).put(b1).put(b2).put(b3).put(b4).position(1), 
				new byte[] {b1, b2, b3, b4}))
		.isFalse();
	}
	
	@Test
	void shouldNotBeEqual4_NextBytes() {
		byte b1 = 23;
		byte b2 = 24;
		byte b3 = 25;
		byte b4 = 26;
		assertThat(Utils.nextBytesEquals(
				ByteBuffer.allocate(4).put(b1).put(b2).put(b3).position(0), 
				new byte[] {b1, b2, b3, b4}))
		.isFalse();
	}
	
	@Test
	void shouldGetUnsignedShort() {
		byte b1 = (byte)0xFF;

		assertThat(b1).isEqualTo((byte)-1);
		
		assertThat(Utils.get2bytesUnsignedInt(ByteBuffer.allocate(2).put(b1).put(b1).position(0)))
		 	.isEqualTo(0xFFFF);
	}
	
	@Test
	void shouldGet3bytesUnsigned0() {
		byte b1 = 0;
		
		assertThat(Utils.get3bytesUnsignedInt(ByteBuffer.allocate(3).put(b1).put(b1).put(b1).position(0))).isZero();
	}
	
	@Test
	void shouldGet3bytesUnsigned1() {
		byte b1 = 0;
		byte b2 = 0;
		byte b3 = 1;
		
		assertThat(Utils.get3bytesUnsignedInt(ByteBuffer.allocate(3).put(b1).put(b2).put(b3).position(0)))
			.isEqualTo(1);
	}
	
	@Test
	void shouldGet3bytesUnsignedFF() {
		byte b1 = (byte)0xFF;

		assertThat(b1).isEqualTo((byte)-1);
		
		assertThat(Utils.get3bytesUnsignedInt(ByteBuffer.allocate(3).put(b1).put(b1).put(b1).position(0)))
		 	.isEqualTo(0xFFFFFF);
	}
}
