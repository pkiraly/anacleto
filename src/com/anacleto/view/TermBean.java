package com.anacleto.view;

import java.text.CollationKey;

/**
 * TermBean class to define term related data
 * 
 * @author robi
 */
public class TermBean {

    private String term;
    private int    freq;
    private CollationKey key;
    
    /**
     *  
     */
    public TermBean(String term, int freq, CollationKey key){
        this.setTerm(term);
        this.setFreq(freq);
        this.setKey(key); 
    }

    
    public String getTerm() {
        return term;
    }
    
    public void setTerm(String term) {
        this.term = term;
    }
    
    public int getFreq() {
        return freq;
    }
    
    public void setFreq(int freq) {
        this.freq = freq;
    }

	/**
	 * @return Returns the key.
	 */
	public CollationKey getKey() {
		return key;
	}

	/**
	 * @param key The key to set.
	 */
	public void setKey(CollationKey key) {
		this.key = key;
	}
    
    
}