/*
 * Created on Jun 9, 2005
 *
 */
package com.anacleto.query;

import java.util.HashMap;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.highlight.QueryTermExtractor;
import org.apache.lucene.search.highlight.WeightedTerm;

import com.anacleto.base.Configuration;
import com.anacleto.query.highlight.HighlightMethods;

/**
 * @author robi
 */
public class HtmlHighlighter extends HighlightMethods{

	
	//private HashMap termsToFind;

//    private TokenQueue foundPositions = new TokenQueue(100);
    
    private String     originalText;
    private String     pureText;
    
    
    /**
     * 
     * @param query
     *            a Lucene query (ideally rewritten using query.rewrite before
     *            being passed to this class and the searcher)
     */
    public HtmlHighlighter(Query query, String text) {

        //extract terms from the query:
    	WeightedTerm[] weightedTerms = QueryTermExtractor.getTerms(query);
    	termsToFind = new HashMap();
        for (int i = 0; i < weightedTerms.length; i++) {
            termsToFind.put(weightedTerms[i].term, weightedTerms[i]);
        }
        
        this.originalText = text;
    	
    	pureText = removeTags(text, true);
    	
    	getTermPositions(pureText);
    	analyzeQuery(query);
    }

    
    
    
    
    public String highLightText(){
    	StringBuffer retText = new StringBuffer();
    	
    	
    	int lastOffset = 0;
    	int anchor = 0;
    	while (foundPositions.size() > 0) {
    		anchor++;
    		   		
			TokenPos token = (TokenPos) foundPositions.poll();
			if (token.getStartOffset() < lastOffset)
    			token.setStartOffset(lastOffset);
			
			//put anchor to the found element:
			int anchorPos = getAnchorPos(token.getStartOffset());
			if (anchorPos >= lastOffset){
				retText.append(originalText.substring(
						lastOffset, anchorPos));
				retText.append("<a name=\"hit"  + anchor + "\"></a>");
				
				retText.append(originalText.substring(
						anchorPos, token.getStartOffset()));
				
				retText.append("<span  style=\"background: highlight;\">");
				retText.append( originalText.substring(
						token.getStartOffset(), token.getEndOffset()));
				retText.append("</span>");
				lastOffset =  token.getEndOffset();
			} else {
				//already anchored this pos, just put  spans
				retText.append(originalText.substring(
						lastOffset, token.getStartOffset()));
				
				retText.append("<span  style=\"background: highlight;\">");
				retText.append( originalText.substring(
						token.getStartOffset(), token.getEndOffset()));
				retText.append("</span>");
				lastOffset =  token.getEndOffset();
			}
		}
    	retText.append(originalText.substring(lastOffset));
    	return retText.toString();
    	
    }
    
    /**
     * Find the right position for the anchor: if the current position
     * is surraunded by a link, put anchor before if
     * @param beforePos
     * @return
     */
    private int getAnchorPos(int beforePos){
    	int currPos = beforePos ;
    	char currChar;
    	//decide if before the position there is a link:

    	//step back to the first endTag:
    	while (true){
    		currPos--;
    		currChar = originalText.charAt(currPos);
    		if (currChar == '>') 
    			break;
    		
    		//no end tag found:
    		if (currPos <= 0)
    			return beforePos;
    	}
    		
//    	step back to the first starttag:
    	while (true){
    		currPos--;
    		currChar = originalText.charAt(currPos);
    		if (currChar == '<') 
    			break;
    		
    		//no start tag found:
    		if (currPos <= 0)
    			return beforePos;
    	}
    	
    	if (originalText.charAt(currPos +1) == 'a')
    		return currPos--;
    	else 
    		return beforePos;
    }
    

    
    /**
     * Remove html tags from the input text
     * @param text - Original text
     * @param fillWithSpace - if true, length of the orinal text will be 
     * the same
     * @return the transformed text
     */
    private String removeTags(String text, boolean fillWithSpace){
    	
    	StringBuffer buffer = new StringBuffer(text);
    	int state = 0;			//state: how meny open tags are
		int minState = 0;
		int[] stateVec = new int[buffer.length()];
		for (int i = 0; i < stateVec.length; i++) {
            char current = buffer.charAt(i);
			if (current == '<'){
            	state++;
            } else if (current == '>'){
            	stateVec[i] = state;
            	state--;
            	if (state < minState)
            		minState--;
            	continue;
			}
            
            stateVec[i] = state;		            	
	    }
		StringBuffer retBuffer = new StringBuffer();
		for (int i = 0; i < stateVec.length; i++) {
            if  (stateVec[i] == minState)
            	retBuffer.append(buffer.charAt(i));
            else if (fillWithSpace)
            	retBuffer.append(' ');
	    }
		return retBuffer.toString();
    }
	
}



