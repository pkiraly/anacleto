package com.anacleto.query;

/**
 * Tokenized text position: pos is the word counter, startOffset and endOffset
 *  are the absolute positions of the start end the end of the token 
 * @author robi
 *
 */
public final class TokenPos{
	private int pos;
	private int startOffset;
	private int endOffset;
	
	public TokenPos(int pos, int startOffset, int endOffset){
		this.pos = pos;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}

	public int getEndOffset() {
		return endOffset;
	}

	public int getPos() {
		return pos;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public void setEndOffset(int endOffset) {
		this.endOffset = endOffset;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}
	
	
	
}
