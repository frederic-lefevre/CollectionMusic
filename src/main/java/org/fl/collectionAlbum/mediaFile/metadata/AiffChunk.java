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

package org.fl.collectionAlbum.mediaFile.metadata;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.fl.collectionAlbum.mediaFile.Utils;

public class AiffChunk {

	public static final int CHUNK_ID_LENGTH = 4;
	public static final int CHUNK_HEADER_LENGTH = 8;
	public static final String FORM_CHUNK_ID = "FORM";
	public static final String COMM_CHUNK_ID = "COMM";
	public static final String ID3_CHUNK_ID = "ID3 ";
	public static final String SOUND_DATA_CHUNK_ID = "SSND";
	
	public static final byte[] FORM_CHUNK_ID_BYTES = FORM_CHUNK_ID.getBytes(StandardCharsets.UTF_8);
	
	private final String chunkId;
	private final long chunkContentLength;
	
	public AiffChunk(ByteBuffer byteBuffer, Path filePath) {
		
		chunkId =  Utils.decodeByteBuffer(byteBuffer, AiffChunk.CHUNK_ID_LENGTH, StandardCharsets.UTF_8);
		chunkContentLength = Utils.get4bytesUnsignedInt(byteBuffer);
	}

	public String getChunkId() {
		return chunkId;
	}

	public long getChunkContentLength() {
		return chunkContentLength;
	}
}
