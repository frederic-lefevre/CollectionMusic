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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ID3HeaderUtils {

	private static final String ID3_IDENTIFIER = "ID3";

	private static final byte[] ID3_IDENTIFIER_BYTES = ID3_IDENTIFIER.getBytes(StandardCharsets.ISO_8859_1);

	private static final int MAJOR_VERSION_LENGTH = 1;
	private static final int MINOR_VERSION_LENGTH = 1;
	private static final int FLAG_LENGTH = 1;
	private static final int ID3_HEADER_SIZE_OFFSET = ID3_IDENTIFIER.length() + MAJOR_VERSION_LENGTH + MINOR_VERSION_LENGTH + FLAG_LENGTH;
	private static final int SIZE_LENGTH = 4;
	
	public static final int BEGIN_HEADER_SIZE = ID3_HEADER_SIZE_OFFSET + SIZE_LENGTH;

	public static int getID3v2HeaderLength(ByteBuffer byteBuffer) {

		boolean isID3v2Header = Utils.nextBytesEquals(byteBuffer, ID3_IDENTIFIER_BYTES);
		if (isID3v2Header) {

			// go to size field
			byteBuffer.position(ID3_HEADER_SIZE_OFFSET);
			return decodeID3HeaderLength(byteBuffer) + ID3_HEADER_SIZE_OFFSET + SIZE_LENGTH;
		} else {
			// not a ID3 Header
			return -1;
		}
	}

	private static int decodeID3HeaderLength(ByteBuffer buffer) {
		byte[] byteArray = new byte[SIZE_LENGTH];
		buffer.get(byteArray);
		return (((byteArray[0] & 0xff) << 21) + 
				((byteArray[1] & 0xff) << 14) +
				((byteArray[2] & 0xff) << 7) + 
				((byteArray[3]) & 0xff));
	}
}
