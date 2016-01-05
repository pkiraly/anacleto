package org.apache.lucene.search.highlight;

/**
 * Copyright 2002-2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.*;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;

/**
 * {@link Scorer}implementation which scores text fragments by the number of
 * unique query terms found. This class uses the {@link QueryTermExtractor}
 * class to process determine the query terms and their boosts to be used.
 * 
 * @author mark@searcharea.co.uk
 */
//TODO: provide option to boost score of fragments near beginning of document
// based on fragment.getFragNum()
public class QueryScorer implements Scorer {
    TextFragment currentTextFragment = null;

    HashSet uniqueTermsInFragment;

    float totalScore = 0;

    float maxTermWeight = 0;

    private HashMap termsToFind;

    private Collection phraseQueries = new LinkedList();  //kr.n
    
    //tokens within the fragment:
    private Collection tokenColl = new LinkedList();  //kr.n
    
    
    //public TermPositions termPos = new TermPositions();
    
    /**
     * 
     * @param query
     *            a Lucene query (ideally rewritten using query.rewrite before
     *            being passed to this class and the searcher)
     */
    public QueryScorer(Query query) {
        this(QueryTermExtractor.getTerms(query));
        
        //kr.sn
        //extract also terms from phrasequeries:
        analyseQueryForPhrases(query);
//      kr.en
    }

    //  kr.sn
    public void analyseQueryForPhrases(Query query){
   	 if (query instanceof BooleanQuery){
   	 	 BooleanClause[] queryClauses = ((BooleanQuery)query).getClauses();
   	 	 for (int i = 0; i < queryClauses.length; i++) {
				BooleanClause clause = queryClauses[i];
				analyseQueryForPhrases(clause.query);
			}
   	 	
   	 }else if (query instanceof PhraseQuery){
   	     phraseQueries.add(query);
   	 }else {
       	return;
       }
           	
   }
    
    public void addTermPosition(){
    	
    }
//  kr.en

    /**
     * 
     * @param query
     *            a Lucene query (ideally rewritten using query.rewrite before
     *            being passed to this class and the searcher)
     * @param reader
     *            used to compute IDF which can be used to a) score selected
     *            fragments better b) use graded highlights eg set font color
     *            intensity
     * @param fieldName
     *            the field on which Inverse Document Frequency (IDF)
     *            calculations are based
     */
    public QueryScorer(Query query, IndexReader reader, String fieldName) {
        this(QueryTermExtractor.getIdfWeightedTerms(query, reader, fieldName));
    }

    public QueryScorer(WeightedTerm[] weightedTerms) {
        termsToFind = new HashMap();
        for (int i = 0; i < weightedTerms.length; i++) {
            termsToFind.put(weightedTerms[i].term, weightedTerms[i]);
            maxTermWeight = Math.max(maxTermWeight, weightedTerms[i]
                    .getWeight());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.lucene.search.highlight.FragmentScorer#startFragment(org.apache.lucene.search.highlight.TextFragment)
     */
    public void startFragment(TextFragment newFragment) {
        uniqueTermsInFragment = new HashSet();
        currentTextFragment = newFragment;
        totalScore = 0;
        
       
        tokenColl.clear(); //kr.n
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.lucene.search.highlight.FragmentScorer#scoreToken(org.apache.lucene.analysis.Token)
     */
    public float getTokenScore(Token token) {
        String termText = token.termText();

        tokenColl.add(termText);      //kr.n
        
        WeightedTerm queryTerm = (WeightedTerm) termsToFind.get(termText);
        if (queryTerm == null) {
            //not a query term - return
            return 0;
        }
        
        //found a query term - is it unique in this doc?
        if (!uniqueTermsInFragment.contains(termText)) {
            totalScore += queryTerm.getWeight();
            uniqueTermsInFragment.add(termText);
        }
        return queryTerm.getWeight();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.lucene.search.highlight.FragmentScorer#endFragment(org.apache.lucene.search.highlight.TextFragment)
     */
    public float getFragmentScore() {
    	//kr.sn
    	//check if there is any term is present in the list of phraseterms
    	Iterator it = phraseQueries.iterator();
    	while (it.hasNext()) {
    		PhraseQuery query = (PhraseQuery)it.next();
    		
			Term[] phraseTerm = query.getTerms();
			int    slop       = query.getSlop();
			
			int termsPresent = 0;
			int distance     = 0;
			int phraseTermOffset = 0;    //the pos of the first term in the fragment
			for (int i = 0; i < phraseTerm.length; i++) {
				Term term = phraseTerm[i];
				if (uniqueTermsInFragment.contains(term.text())){
					termsPresent++;
					
					//get distance:
					int tokenOffset = 0;
					Iterator iter = tokenColl.iterator();
					while (iter.hasNext()) {
						String token = (String) iter.next();
						
						//stop at first occurance:
						if (token.equals(term.text())){
							if (phraseTermOffset == 0)
								phraseTermOffset = tokenOffset;
							distance = distance + 
							  Math.abs(tokenOffset-phraseTermOffset-i);
						}
						tokenOffset++;
					}
				}
			}
			//the terms of the phrases have to be all present, otherway we cancel the score 
			if (termsPresent != 0 && termsPresent != phraseTerm.length)
				totalScore = 0;
			
			if (termsPresent == phraseTerm.length && distance > slop)
				totalScore = 0;
		}
    	//kr.en
    	return totalScore;     
        
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.lucene.search.highlight.FragmentScorer#allFragmentsProcessed()
     */
    public void allFragmentsProcessed() {
        //this class has no special operations to perform at end of processing
    }

    /**
     * 
     * @return The highest weighted term (useful for passing to
     *         GradientFormatter to set top end of coloring scale.
     */
    public float getMaxTermWeight() {
        return maxTermWeight;
    }
}


