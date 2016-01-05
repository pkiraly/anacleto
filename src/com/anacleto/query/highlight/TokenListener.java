package com.anacleto.query.highlight;

import org.apache.lucene.analysis.Token;


public abstract class TokenListener {
	protected boolean active = true;

//	true if query matches the tokens
	protected boolean match = false;
	
	public abstract void addToken(Token token);
	
	protected int firstTermStartPos;
	protected int lastTermEndPos;

	protected String postText;

	/**
	 * @return Returns the match.
	 */
	public boolean isMatch() {
		return match;
	}

	/**
	 * @return Returns the active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @return Returns the firstTermStartPos.
	 */
	public int getFirstTermStartPos() {
		return firstTermStartPos;
	}

	/**
	 * @return Returns the lastTermEndPos.
	 */
	public int getLastTermEndPos() {
		return lastTermEndPos;
	}

	/**
	 * @return Returns the postText.
	 */
	public String getPostText() {
		return postText;
	}

	
	
}
