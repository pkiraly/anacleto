package com.anacleto.query.highlight;

import java.io.IOException;
import java.io.Reader;

/**
 * A reader implementation, that remembers the last few characters read
 * and the next characters to come
 * @author robi
 *
 */
public class ReaderWithMemory extends Reader {

	private int bufferSize;
	
	private CharBuffer buffer;
	
	private Reader reader;
	
	//position of the cursor to return with the read process
	private int position = 0;
	
	//position of the cursor of the buffer
	private int endPosition = 0;
	
	public ReaderWithMemory(Reader reader, int bufferSize){
		this.reader = reader;
		this.bufferSize = bufferSize;
		
		buffer = new CharBuffer(2 * bufferSize);
	}
	
	/**
	 * Read from the input stream and feed the buffer
	 */
	public int read(char[] cbuf, int off, int len) throws IOException {
		
		//get the number of chars to read from the stream:
		//=chars_missing_from_the_prebuffer + the len in input
		int charsToRead = 2 * bufferSize - buffer.length() + len;
		
		char input[] = new char[charsToRead];
		int charsRead = reader.read(input, 0, charsToRead);
		
		char[] pre = getPre();
		if (len <= pre.length){
			System.arraycopy(pre, 0, cbuf, off, len);
		} else {
			System.arraycopy(pre, 0, cbuf, off, pre.length);
			if (charsRead > 0){
				System.arraycopy(input, 0, cbuf, off + pre.length, 
						len - pre.length);
			};
		}

		if (charsRead > 0){
			buffer.append(input, 0, charsRead);
			endPosition = endPosition + charsRead;
		};
			
		int advance = Math.min(endPosition-position, len);
		
		position = position + advance;
		
		return (advance > 0) ?	advance : -1;
	}

	/**
	 * Read from the input stream and feed the buffer
	 */
	public int read(int len) throws IOException {
		
		//get the number of chars to read from the stream:
		//=chars_missing_from_the_prebuffer + the len in input
		int charsToRead = bufferSize - buffer.length()/2 + len;
		
		char input[] = new char[charsToRead];
		int charsRead = reader.read(input, 0, charsToRead);
		if (charsRead > 0){
			buffer.append(input, 0, charsRead);
			endPosition = endPosition + charsRead;
		};
			
		int advance = Math.min(endPosition-position, len);
		position = position + advance;

		return (advance > 0) ?	advance : -1;
	}

	public void close() throws IOException {
		reader.close();
	}

	/**
	 * @return Returns the postBuffer.
	 */
	public String getPostBuffer() {
		int postLen = position - getStartPosition();
		
		int postLenToReturn = Math.min(postLen, bufferSize); 
		return buffer.toString().substring(postLen-postLenToReturn, postLen);
	}

	/**
	 * @return Returns the preBuffer.
	 */
	public String getPreBuffer() {
		char[] pre = getPre();
		
		int preLenToReturn = Math.min(pre.length, bufferSize);
		return new String(pre).substring(0, preLenToReturn);
	}

	/**
	 * @return Returns the preBuffer.
	 */
	public String getBuffer() {
		return buffer.toString();
	}

	/** 
	 * @return Returns the preBuffer.
	 */
	public char[] getPre() {
		int buffLen = buffer.value.length;
		
		int preStart = buffLen - (endPosition - position);
		int preLen   = endPosition - position;
		
		char[] retC = new char[preLen];
		System.arraycopy(buffer.value, preStart, retC, 0, preLen);
		return retC;
	}
	/**
	 * @return Returns the position.
	 */
	public int getPosition() {
		return position;
	}

	public int getStartPosition() {
		return endPosition - buffer.value.length;
	}
}
/**
 * Fixed sized characterbuffer.
 * @author robi
 *
 */
final class CharBuffer{
	char[] value = new char[0];
	private int maxSize;
	
	
	
	public CharBuffer(int size) {
		maxSize = size;
	}

	public void append(char[] buf, int off, int len){
		int newSize = Math.min(maxSize, value.length + len);
		
		
		if (maxSize <= len){
			//the new array is longer the the buffer, just pick
			//the last chars
			value = new char[newSize];	
			System.arraycopy(buf, off + len - newSize, value, 0, newSize );
		} else {
			//from the old buffer, take the last characters
			//that is the minimum of the size without the new chars
			int lastChars = newSize-len;

			char[] newValue = new char[newSize];	
			System.arraycopy(value, value.length - lastChars, newValue, 0, 
					lastChars);
			System.arraycopy(buf, off, newValue, newSize-len, len );
			value = newValue;
		}
		
	}
	
	public String toString(){
		return new String(value);
	}
	
	public int length(){
		return value.length;
	}
}
