package com.anacleto.index;

import java.io.IOException;
import java.io.Serializable;
import java.text.CollationKey;
import java.text.Collator;
import java.util.*;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

import com.anacleto.base.Constants;
import com.anacleto.base.Logging;

/**
 * Localized Term List: 
 * Created a sorted termlist. Sorting will be done according the locale 
 * passed when initialized  
 * @author robi
 *
 */
public class LocalizedTermList{
	
	private static org.apache.log4j.Logger logger = Logging.getAdminLogger();
	
	private static final int MAX_TERM_LENGTH = 3; 
	
	private static Collator primColl;
	
	private static TreeMap  fieldMap;
	
	/**
	 * Init the sorted set
	 * @param reader
	 * @param loc
	 * @throws IOException 
	 * @throws IOException
	 */
	synchronized public static void initialize(String indexDir, Locale loc) 
			throws IOException {
		
		IndexReader reader = IndexReader.open(indexDir);
		
		Collator co = null;
		if (loc == null)
			co = Collator.getInstance();
		else
			co = Collator.getInstance(loc);
		
		primColl = Collator.getInstance(loc);
		primColl.setStrength(Collator.PRIMARY);

		ShortTerm.initialize(MAX_TERM_LENGTH, co);
		
		long start = System.currentTimeMillis();
		
		fieldMap = new TreeMap();
		
		TermEnum terms = reader.terms();
		int counter = 0;
		while (terms.next()) {
			Term currTerm = terms.term();

			if (Constants.isReservedField(currTerm.field()))
        		continue;

			SortedSet fieldTermSet = (SortedSet)fieldMap.get(currTerm.field());
        	if (fieldTermSet == null) {
				fieldTermSet = new TreeSet();//new TermOrderComparator());
				fieldMap.put(currTerm.field(), fieldTermSet);
			}
        	
        	ShortTerm st = new ShortTerm(currTerm.text());
        	fieldTermSet.add(st);
        	
        	counter++;
        }
		
		Iterator it = fieldMap.keySet().iterator();
		while (it.hasNext()) {
			String currField = (String)it.next();
			
			SortedSet fieldTermSet = (SortedSet) fieldMap.get(currField);
			fieldMap.put(currField, fieldTermSet.toArray());
		}
				
		logger.info("Localized termlist initialized with " + counter 
				+ " terms in " + (System.currentTimeMillis()-start) + "ms.");
	}
	
	/**
	 * Get short terms around the term in input 
	 * @param field - the field containing the pattern 
	 * @param pattern - the pattern to serach for
	 */
	synchronized public static Collection getTermsNear(String field, 
			String pattern){
		
		Object[] fieldTermSet = (Object[])fieldMap.get(field);
    	if (fieldTermSet == null) 
    		return null;
    	
    	ShortTerm inTerm = new ShortTerm(pattern);
    	int currPos = Arrays.binarySearch(fieldTermSet, inTerm);
    	if (currPos < 0) {
    		currPos = Math.abs(currPos) - 1;
    	}
    	
		return getEqualTerms(primColl, fieldTermSet, currPos);
	}

	synchronized public static Collection getTermsAfter(String field, 
			String text){
		
		Object[] fieldTermSet = (Object[])fieldMap.get(field);
    	if (fieldTermSet == null) 
    		return null;
		
    	ShortTerm inTerm = new ShortTerm(text);
		
    	int currPos = Arrays.binarySearch(fieldTermSet, inTerm);
    	if (currPos < 0)
    		currPos = Math.abs(currPos);
    	
		//get the first not equal position:
		while (true){
			if (fieldTermSet.length <= currPos
				|| inTerm.compareTo(primColl, fieldTermSet[currPos]) != 0)
				break;
			currPos++;
		}
		
		return getEqualTerms(primColl, fieldTermSet, currPos);
	}

	synchronized public static Collection getTermsBefore(String field, 
			String text){
		
		Object[] fieldTermSet = (Object[])fieldMap.get(field);
    	if (fieldTermSet == null) 
    		return null;
		
    	ShortTerm inTerm = new ShortTerm(text);
		
    	int currPos = Arrays.binarySearch(fieldTermSet, inTerm);
    	if (currPos < 0)
    		currPos = Math.abs(currPos) - 1;
    	
		//get the first not equal position:
		while (true){
			if (currPos <= 0)
				return null;
			
			currPos--;
			if (inTerm.compareTo(primColl, fieldTermSet[currPos]) != 0)
				break;
		}
		
		return getEqualTerms(primColl, fieldTermSet, currPos);
	}

	/**
	 * Get a collection of equal terms in a sorted object array  
	 * @param inTerm
	 * @param co
	 * @param fieldTermSet
	 * @param currPos
	 * @return
	 */
	private static Collection getEqualTerms(
			Collator co,
			Object[] fieldTermSet, 
			int currPos)
	{
		Collection retColl = new LinkedList();
		if(fieldTermSet.length <= currPos) {
			return retColl;
		}
		ShortTerm inTerm = (ShortTerm)fieldTermSet[currPos];
		
		//get equal terms before the cursor
		while (true){
			if (currPos <= 0)
				break;
			
			if (inTerm.compareTo(co, fieldTermSet[currPos-1]) != 0)
				break;
			currPos--;
		}
		
		//get equal terms after the cursor
		while (true){
			if(fieldTermSet.length <= currPos) {
				break;
			}
			if (inTerm.compareTo(co, fieldTermSet[currPos]) != 0) {
				break;
			} else {
				ShortTerm st = (ShortTerm)fieldTermSet[currPos];
				String term = st.getTermChars().toString();
				if (st.getTermLength() >= MAX_TERM_LENGTH)
					term = term + "*";
				
				retColl.add(term);
			}
			currPos++;
		}
		
		return retColl;
	}
	
}

/**
 * Short term memorizes the first n characters of a term, and 
 * its collation key for sorting
 * @author robi
 *
 */
class ShortTerm implements Comparable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6549119625714339701L;
	
	private static int MAX_TERM_LENGTH = 0; 
	private static Collator co;
	
	private String termChars;
	private CollationKey key;
	private int    termLength;
	
	static void initialize(int maxTermLength, Collator co){
		MAX_TERM_LENGTH = maxTermLength;
		ShortTerm.co = co;
	}
	
	ShortTerm(String termText){
		
		//termChars = new Character[MAX_TERM_LENGTH];
		termLength = Math.min(termText.length(), MAX_TERM_LENGTH);
		/*
		try{
			for (int i = 0; i < MAX_TERM_LENGTH; i++) {
				termChars[i] = new Character(termText.charAt(i));
			}
		}catch (IndexOutOfBoundsException e){
		}
		*/
		termChars = termText.substring(0, termLength);
		
		key = co.getCollationKey(termChars.toString());
	}

	/**
	 * @return Returns the key.
	 */
	public CollationKey getKey() {
		return key;
	}

	/**
	 * @return Returns the termChars.
	 */
	public String getTermChars() {
		return termChars;
	}
	
	/**
	 * @return Returns the termLength.
	 */
	public int getTermLength() {
		return termLength;
	}

	public int compareTo(Object o) {
		ShortTerm otherTerm = (ShortTerm)o;
		
		return key.compareTo(otherTerm.getKey());
	}

	public int compareTo(Collator co, Object o) {
		String currTermStr = termChars.toString();
		ShortTerm otherTerm = (ShortTerm)o;
		String otherTermStr = (otherTerm.getTermChars()).toString();
		return co.compare(currTermStr, otherTermStr);
	}

	/* (non-Javadoc)
	 * @see java.text.CollationKey#equals(java.lang.Object)
	 */
	public boolean equals(Object target) {
		ShortTerm otherTerm = (ShortTerm)target;
		
		return termChars == otherTerm.getTermChars();
	}


}
