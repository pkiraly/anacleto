package com.anacleto.index;

import java.io.IOException;
import java.text.CollationKey;
import java.text.Collator;
import java.util.LinkedList;
import java.util.Locale;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FilteredTermEnum;
import org.apache.lucene.search.WildcardTermEnum;
import org.apache.lucene.util.PriorityQueue;

import com.anacleto.base.Configuration;
import com.anacleto.base.Constants;
import com.anacleto.view.TermBean;

public class IndexStatistics {

	/**
	 * 
	 * @param pattern -
	 *            Pattern to filter terms. Pattern support wildcards
	 * @param field -
	 *            Lucene field to filter for. In case of empty or null value, no
	 *            field filtering will be applied
	 * @param firstVal 
	 * 			  The filtered list will be displayed starting from this string           
	 * @param numberOfTerms -
	 *            Max number of terms to return
	 * @return A collection of TermBeans
	 * @throws IOException
	 */
	public LinkedList getFilteredTerms(String field, String pattern, 
			String firstVal, int numberOfTerms) throws IOException {

		Locale loc = Configuration.params.getLocale();
		Collator co = Collator.getInstance(loc);
		
		TermQueue termQueue = new TermQueue(numberOfTerms, co);

		if (field == null || field.trim().equals(""))
			return termQueue.getCollection();
		
		IndexReader reader = null;
		try{
			reader = IndexReader.open(Configuration.params.getIndexDir());
					
			getMatchingTerms(reader, field, pattern, firstVal, 
					termQueue, numberOfTerms);
		} catch (IOException e){	
			reader.close();
		}
		return termQueue.getCollection();

	}

	private static void getMatchingTerms(IndexReader reader, String field,
			String pattern, String firstVal, TermQueue termQueue, int numberOfTerms)
			throws IOException {

		if (Constants.isReservedField(field))
			return;
		
		FilteredTermEnum tes = new WildcardTermEnum(reader, new Term(field,
				pattern));
		
		Collator co = Collator.getInstance(Configuration.params.getLocale());
		
		int counter = 0;
		while (tes.term() != null) {
		
			int greaterThanFirst = co.compare(tes.term().text(), firstVal);
			if (greaterThanFirst < 0){
				;
			} else {
				String text = tes.term().text();
				CollationKey termKey = co.getCollationKey(text);
				termQueue.insert(new TermBean(text, tes.docFreq(), termKey));
				counter++;
				if (counter >= numberOfTerms)
					return;
			}
		
			tes.next();
		}
	}
}


