package com.anacleto.query;

//Position of a term within a phrase
public class PhraseTermPos{
	/**
	 * The positions of the term within the text
	 */
	TokenQueue tokenQueue;
	
	/**
	 * The positions of the term within the phrase
	 */
	int phrasePos;
	//String Term;
	
	/**
	 * the actual top position of the phrase in the text
	 */
	int topPosition;
	
	public PhraseTermPos(TokenQueue tokenQueue, int phrasePos) {
		super();
		this.tokenQueue = tokenQueue;
		this.phrasePos = phrasePos;
		//Term = term;
		
		topPosition = getTopPos();
	}
	
	public int getTopPos(){
		return ((TokenPos)tokenQueue.peek()).getPos() - phrasePos;
	}
	
	public int getNextPos(){
		TokenPos token = (TokenPos)tokenQueue.poll();
		int retPos = ((TokenPos)tokenQueue.peek()).getPos() - phrasePos;
		tokenQueue.add(token);
		return retPos;
	}
	
	public TokenPos getTopToken(){
		return (TokenPos)tokenQueue.peek();
	}
	
	public boolean next(){
		if (tokenQueue.size() > 1 ){
			tokenQueue.poll();
			topPosition = getTopPos();
			return true;
		}else{
			return false;
		}
	}
}


