package com.anacleto.index;

import java.io.IOException;
import java.text.CollationKey;
import java.text.Collator;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FilteredTermEnum;
import org.apache.lucene.search.WildcardTermEnum;

import com.anacleto.base.Configuration;
import com.anacleto.base.Constants;
import com.anacleto.base.Logging;
import com.anacleto.view.TermBean;

/**
 * Get termlist around a certain position
 * @author robi
 *
 */
public class PositionTermList {
	
	/**
	 * the field of which we need the term list
	 */
	private String field;
	
	/**
	 * the queue of terms standing before the searched pattern
	 */
	private ReverseTermQueue termsBeforeQueue;

	/**
	 * the queue of terms standing after the searched pattern
	 */
	private TermQueue termsAfterQueue;
	
	/**
	 * The collator performs locale-sensitive String comparison
	 */
	private Collator collator;
	
	/**
	 * A special representation of pattern, which useful in the comparision
	 * with the terms from the term list
	 */
	private CollationKey patternKey;
	
	public PositionTermList(){
	
	}
	
	public LinkedList getTermsNear(String field, String pattern,
			int numberOfTerms) throws IOException {
	
		if (field == null || field.trim().length() == 0)
			field = Configuration.getDefaultQueryField();
	
		if (Constants.isReservedField(field))
			return null;
	
		this.field = field;
		//this.position = position;
		IndexReader reader = IndexReader.open(Configuration.params
				.getIndexDir());
			
		Locale loc = Configuration.params.getLocale();
		this.collator = Collator.getInstance(loc);
		this.patternKey = collator.getCollationKey(pattern);
		
		int half = (numberOfTerms+1)/2;
		this.termsAfterQueue = new TermQueue(half, collator);
		this.termsBeforeQueue = new ReverseTermQueue(numberOfTerms-half, collator);
		
		if (pattern.length() < 1)
			return termsAfterQueue.getCollection();
		
		try {
			Collection termColl = LocalizedTermList.getTermsNear(field, 
					pattern);
			processTerms(reader, termColl);
			
			String refPattern = pattern;
			//Get terms before the current position:
			while (termsBeforeQueue.size() < termsBeforeQueue.getMaxSize()){
				termColl = LocalizedTermList.getTermsBefore(field, 
						refPattern);
				if ( termColl == null || termColl.size() == 0)
					break;
				refPattern = (String)termColl.iterator().next();
				processTerms(reader, termColl);
			}
			
			//Get terms after the current position:
			refPattern = pattern;
			while (termsAfterQueue.size() < termsAfterQueue.getMaxSize()){
				termColl = LocalizedTermList.getTermsAfter(field, 
						refPattern);
				if ( termColl == null || termColl.size() == 0)
					break;
				
				refPattern = (String)termColl.iterator().next();
				processTerms(reader, termColl);
			}
		} catch (IOException e){	
			reader.close();
		}
		
		LinkedList retColl = new LinkedList();
		retColl.addAll(termsBeforeQueue.getCollection());
		retColl.addAll(termsAfterQueue.getCollection());
		
		while (termsBeforeQueue.size()>0)
			termsBeforeQueue.pop();
		
		while (termsAfterQueue.size()>0)
			termsAfterQueue.pop();
		
		return retColl;
	}
	
	private void processTerms(IndexReader reader, Collection termColl) 
		throws IOException {
		
		if(termColl == null) {
			return;
		}

		Iterator it = termColl.iterator();
		while (it.hasNext()) {
			String termText = (String) it.next();
			Term t = new Term(field, termText);
			if (termText.indexOf("*") == -1){
				CollationKey termKey = collator.getCollationKey(termText);
				TermBean tb = new TermBean(termText, 
						reader.docFreq(t), termKey);
				if (termKey.compareTo(patternKey) >= 0){
					//text is after the posTerm, put it in the aferQueue
					termsAfterQueue.insert(tb);
				} else{
					termsBeforeQueue.insert(tb);
				}
			} else
				getTermsNear(reader, t);
		}
	}

	/**
	 * Get terms near a wildcard term
	 * @param reader Lucene index reader
	 * @param wildcardTerm the wildcard pattern
	 * @throws IOException
	 */
	private void getTermsNear(IndexReader reader, Term wildcardTerm) 
			throws IOException{
		
		//Get terms for the first letter
		FilteredTermEnum tes = new WildcardTermEnum(reader, wildcardTerm);
						
		while (tes.term() != null) {
			String text = tes.term().text();
			
			CollationKey termKey = collator.getCollationKey(text);
			TermBean currTermBean = new TermBean(text, tes.docFreq(), 
					termKey);
			
			if (termKey.compareTo(patternKey) >= 0){
				//text is after the posTerm, put it in the aferQueue
				termsAfterQueue.insert(currTermBean);
			} else{
				termsBeforeQueue.insert(currTermBean);
			}
			
			tes.next();
		}
	
	}	
}
