package com.anacleto.query.highlight;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhrasePrefixQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import com.anacleto.base.Configuration;
import com.anacleto.query.PhraseQueryScorer;
import com.anacleto.query.TermPositions;
import com.anacleto.query.TokenPos;
import com.anacleto.query.TokenQueue;

/**
 * Base Package for all highlight related tasks
 * 
 * It captures scores of a query in a given text 
 * @author Robert
 *
 */
public abstract class HighlightMethods {

	/**
	 * The terms to search for
	 */
	private HashMap termsToFind;

	protected MultiValueTreeMap queriesByWords;

    private TermPositions termPos = new TermPositions();
    
    protected TokenQueue foundPositions = new TokenQueue(100);

    protected Query query;
        
    protected final void analyzeQuery(Query query){
    	if (query instanceof BooleanQuery){
        	BooleanClause[] queryClauses = ((BooleanQuery)query).getClauses();
      	 	for (int i = 0; i < queryClauses.length; i++) {
   				BooleanClause clause = queryClauses[i];
   				if (!clause.prohibited)
   					analyzeQuery(clause.query);
      	 	}
      	
    	}else if (query instanceof TermQuery){
          	TermQuery tq = (TermQuery)query;
          	
			TokenQueue posQueue = (TokenQueue)termPos.getTermPositions(
					tq.getTerm().text());
			if (posQueue != null){
				while (true) {
					TokenPos pos = (TokenPos) posQueue.poll();
					if (pos != null)
						foundPositions.add(pos);
					else
						break;
				}
			}
      	
    	}else if (query instanceof PhraseQuery){
			PhraseQueryScorer scorer = new PhraseQueryScorer(
					(PhraseQuery)query, termPos);
			addCollectionToFoundPositions(scorer.score(100));
			
      	}else if (query instanceof PhrasePrefixQuery){
      		PhraseQueryScorer scorer = new PhraseQueryScorer(
					(PhrasePrefixQuery)query, termPos);
      		addCollectionToFoundPositions(scorer.score(100));
			
      	}else {
          	return;
        }
    }
    
    private void addCollectionToFoundPositions(Collection tokenPosColl){
    	if (tokenPosColl != null){
			Iterator iter = tokenPosColl.iterator();
			while (iter.hasNext()) {
				TokenPos tp = (TokenPos) iter.next();
				foundPositions.add(tp);
			}
		}
    }
    
    /**
     * Tokenize the pure text to get the positions of the relevant terms
     *
     */
    protected final void getTermPositions(String text){
    	
    	Analyzer analyzer = Configuration.getAnalyzer();
    	
    	TokenStream tS= analyzer.tokenStream("content", 
    			new StringReader(text));

    	//first get term positions
    	Token token;
    	try {
    		int counter = 0;
			while ((token = tS.next()) != null) {
				counter++;					
				if (termsToFind.get(token.termText()) != null){
					TokenPos pos = new TokenPos(counter, token.startOffset(), 
							token.endOffset());
					termPos.addTermPosition(token.termText(), pos);
				}
			}
		} catch (IOException e) {
		} finally {
			if (tS != null) {
                try {
                	tS.close();
                } catch (IOException e) {
                }
            }
			
		}
    }
  
    protected void extractQuery(Query query) {
        
    	if (query instanceof BooleanQuery)
    		extractBooleanQuery((BooleanQuery) query);
        
        else if (query instanceof PhraseQuery)
        	extractPhraseQuery((PhraseQuery) query);
        
        else if (query instanceof PhrasePrefixQuery)
        	extractPhrasePrefixQuery((PhrasePrefixQuery) query);
        
        else if (query instanceof TermQuery)
        	extractTermQuery((TermQuery) query);
    }

    private void extractBooleanQuery(BooleanQuery query) {
        BooleanClause[] queryClauses = query.getClauses();
        int i;

        for (i = 0; i < queryClauses.length; i++) {
            if (!queryClauses[i].prohibited)
            	extractQuery(queryClauses[i].query);
        }
    }

    private void extractPhraseQuery(PhraseQuery query) {
        Term[] queryTerms = query.getTerms();
        int i;

        for (i = 0; i < queryTerms.length; i++) {
        	queriesByWords.put(queryTerms[i].text(), query);
        }
    }

    private void extractPhrasePrefixQuery(PhrasePrefixQuery query) {
    	
    	int[] positions = query.getPositions();
    	for (int i = 0; i < positions.length; i++) {
			Term[] queryTerms = query.getTerms(positions[i]);
			for (int j = 0; j < queryTerms.length; j++) {
				queriesByWords.put(queryTerms[j].text(), query);
	        }	
		}
        
    }

    private void extractTermQuery(TermQuery query) {
    	queriesByWords.put(query.getTerm().text(), query);
    }

	public static HighlightFragment scoreTermQuery(ReaderWithMemory memReader, 
			Token token){
	
		HighlightFragment frag = new HighlightFragment();
		
		String preBuffer = memReader.getPreBuffer();
		int endPos = Math.min(preBuffer.length(), 
				token.endOffset() - memReader.getPosition());
		
		frag.matchStartPos = token.startOffset();
		frag.matchEndPos   = token.endOffset();
		frag.leadIn = memReader.getPostBuffer();
		frag.match = preBuffer.substring(0, endPos);
		frag.leadOut = preBuffer.substring(endPos);
	
		return frag;
	}
	
	public static HighlightFragment scoreListener(TokenListener lis, 
			ReaderWithMemory memReader){
		
		int matchLen = lis.getLastTermEndPos()-
		lis.getFirstTermStartPos();
	
		String buffer = memReader.getBuffer();
		int bufferStatrPos = memReader.getStartPosition();
		
		//the position in the current buffer of the start position
		//of the first matching term
		int offsetInBuffer = lis.getFirstTermStartPos()
		- bufferStatrPos;
		
		//highlightFragments
		HighlightFragment frag = new HighlightFragment();
		frag.matchStartPos = lis.firstTermStartPos;
		frag.matchEndPos = lis.lastTermEndPos;
		
		//startpos is within the buffer?
		if (offsetInBuffer >= 0){
			frag.leadIn  = lis.getPostText();
			frag.match   = buffer.substring(offsetInBuffer, 
					offsetInBuffer + matchLen); 
			frag.leadOut = buffer.substring(offsetInBuffer + matchLen);
		} else {
			frag.leadIn  = "";
			frag.match   = buffer.substring(0, offsetInBuffer + matchLen); 
			frag.leadOut = buffer.substring(offsetInBuffer + matchLen)
				+ memReader.getPreBuffer().toString();
		}
		return frag;
	}

}


/**
 * A treemap that has a collection of objects in value
 * When a new object is inserted in the map, the value will be added to
 * the 
 * @author robi
 *
 */
final class MultiValueTreeMap extends TreeMap{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5761458079838799580L;

	/**
	 * @see java.util.TreeMap#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object arg0, Object arg1) {
		Collection queryColl;
		if (containsKey(arg0)){
			queryColl = (Collection)get(arg0);
		} else {
			queryColl = new ArrayList();
		}
		queryColl.add(arg1);
		
		return super.put(arg0, queryColl);
	}
}