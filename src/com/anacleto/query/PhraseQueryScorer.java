package com.anacleto.query;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.PhrasePrefixQuery;
import org.apache.lucene.search.PhraseQuery;

//Get positions where a Phrasequery scores:
public class PhraseQueryScorer{
	
	TermPositions termPositions;
	PhraseTermQueue termQueue;
	boolean  allTermsFound = true;

	//end is the highest position in of the first positions
	int end = 0;
	int slop = 0;
	
	public PhraseQueryScorer(PhraseQuery query, TermPositions termPositions) {
		
		this.termPositions = termPositions;
		Term[] terms = query.getTerms();
		this.slop     = query.getSlop();
		
		termQueue = new PhraseTermQueue(terms.length);
		
		int tpos[] = query.getPositions();
		for (int i = 0; i < tpos.length; i++) {
			Term term = terms[i];
			TokenQueue tokenQueue = termPositions.getTermPositions(term.text());
			
			//not all the terms of the phrase are found 
			if (tokenQueue == null){
				return;
			}
			
			PhraseTermPos ptp = new PhraseTermPos(tokenQueue, tpos[i]);
			if (ptp.topPosition > end)
                end = ptp.topPosition;
			
			termQueue.put(ptp);
		}
	}
	
	public PhraseQueryScorer(PhrasePrefixQuery query, TermPositions termPositions) {
		this.termPositions = termPositions;
		this.slop     = query.getSlop();
		
		//end is the highest position in of the first positions
		int termCounter = 0;
		int tpos[] = query.getPositions();
		for (int i = 0; i < tpos.length; i++) {
			Term terms[] = query.getTerms(tpos[i]);
			for (int j = 0; j < terms.length; j++) {
				termCounter++;
			}
		}
		
		termQueue = new PhraseTermQueue(termCounter);
		
		for (int i = 0; i < tpos.length; i++) {
			Term terms[] = query.getTerms(tpos[i]);

			//if there is at least one term found for this position
			boolean positionFound = false;			
			
			TokenQueue tokenPosQueue = new TokenQueue(100);
			
			for (int j = 0; j < terms.length; j++) {
				Term term = terms[j];
				TokenQueue tokenQueue = termPositions.getTermPositions(term.text());

				//not all the terms of the phrase are found 
				if (tokenQueue != null){
					positionFound = true;
					while (tokenQueue.size() > 0){
						tokenPosQueue.add(tokenQueue.poll());
					}
				}
			} 
			
			if (!positionFound){
				allTermsFound = false;
				return;
			} else{
				PhraseTermPos ptp = new PhraseTermPos(tokenPosQueue, tpos[i]);
				
				termQueue.put(ptp);
				if (ptp.topPosition > end)
	                end = ptp.topPosition;
			}
		}
	}
	
	public Collection score(int maxScoreNum){
		if (!allTermsFound)
			return null;
		
		Collection foundPositions = new ArrayList();
		
		boolean done = false;
        do {
        	PhraseTermPos pp = (PhraseTermPos) termQueue.pop();
        	if (pp == null)
        		break;
        	
            int start = pp.getTopPos();
            
            int next = ((PhraseTermPos) termQueue.top()).topPosition;
            
            while (pp.tokenQueue.size() > 1 && pp.getNextPos() <= next){
                pp.next();
            	start = pp.topPosition;
            }
            
            int matchLength = end - start;
            if (matchLength <= slop){
            	//foundPositions.add(pos);
            	
            	int minOffset = pp.getTopToken().getStartOffset();
            	int maxOffset = pp.getTopToken().getEndOffset();
            	
            	PhraseTermQueue newtermQueue = new PhraseTermQueue(termQueue.size()+1);
            	//get the min and max offset from the actual positions
            	while (true){
            		PhraseTermPos ppTemp = (PhraseTermPos) termQueue.pop();
            		if (ppTemp == null)
            			break;
            		
            		TokenPos pos = ppTemp.getTopToken();
            		if (minOffset > pos.getStartOffset())
            			minOffset = pos.getStartOffset();
            		
            		if (maxOffset < pos.getEndOffset())
            			maxOffset = pos.getEndOffset();
            		
            		newtermQueue.put(ppTemp);
            	}
            	termQueue = newtermQueue;
            	foundPositions.add(new TokenPos(start, minOffset, maxOffset));

            }
                
            if (!pp.next()) {
                done = true;				  // ran out of a term -- done
            }
            
            if (pp.getTopPos() > end)
                end = pp.getTopPos();
            
            termQueue.put(pp);				  // restore pq
        } while (!done);
        
        return foundPositions;
	}

}

