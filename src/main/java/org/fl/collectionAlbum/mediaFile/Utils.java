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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class Utils {

	// Test if the remaining bytes in the ByteBuffer are the same as the byte array
	public static boolean remainingBytesEquals(ByteBuffer bb, byte[] byteArray) {
		
		if (bb.remaining() != byteArray.length) return false;
		
		int index = 0;
		while (index < byteArray.length) {
			if (bb.get() != byteArray[index]) return false;
			index++;
		}
		return true;
	}
	
	// Test if the next bytes in the ByteBuffer are the same as the byte array
	public static boolean nextBytesEquals(ByteBuffer bb, byte[] byteArray) {
		
		if (bb.remaining() < byteArray.length) return false;
		
		int index = 0;
		while (index < byteArray.length) {
			if (bb.get() != byteArray[index]) return false;
			index++;
		}
		return true;
	}
	
	public static int get4bytesUnsignedIntLittleEndian(ByteBuffer bb) {
		return (bb.get() & 0xFF) +
				((bb.get() & 0xFF) << 8) +
				((bb.get() & 0xFF) << 16) +
				((bb.get() & 0xFF) << 24);
	}
	
	public static int get4bytesUnsignedInt(ByteBuffer bb) {
		return ((bb.get() & 0xFF) << 24) +
				((bb.get() & 0xFF) << 16) +
				((bb.get() & 0xFF) << 8) +
				(bb.get() & 0xFF);
	}
	
	public static int get3bytesUnsignedInt(ByteBuffer bb) {
		return ((bb.get() & 0xFF) << 16) +
				((bb.get() & 0xFF) << 8) +
				(bb.get() & 0xFF);
	}
	  
	public static int get2bytesUnsignedInt(ByteBuffer bb) {
		return bb.getShort() & 0xFFFF;
	}

	public static int get1byteUnsignedInt(ByteBuffer bb) {
		return bb.get() & 0xFF;
	}
	
	public static long get10bytesUnsignedLong(ByteBuffer bb) {
		
		byte[] byteArray = new byte[10];
	    bb.get(byteArray);
	    int exponent = ((byteArray[0] << 8) + byteArray[1]) -  16445;
	  
	    int shift = 55;
	    long mantissa = 0;
	    for (int i = 2; i < 9; i++) {
	      mantissa = mantissa + (((long) byteArray[i] & 0XFFL) << shift);
	      shift -= 8;
	    }
	    mantissa = mantissa + (byteArray[9] >>> 1);

	    return (long)(mantissa * Math.pow(2, exponent));
	}
	
	public static ByteBuffer readToDirectByteBuffer(FileChannel sbc, int numberOfBytesToRead) throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(numberOfBytesToRead);		
		sbc.read(byteBuffer);
		return byteBuffer.position(0);
	}
	
	public static String decodeByteBuffer(ByteBuffer bb, int bytesNumber, Charset charSet) {

		ByteBuffer bbSlice = bb.slice(bb.position(), bytesNumber);
		bb.position(bb.position() + bytesNumber);
		return charSet.decode(bbSlice).toString();
	}
}
