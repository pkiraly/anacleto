package com.anacleto.index;

import java.io.IOException;
import java.io.Serializable;
import java.text.CollationKey;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

import com.anacleto.base.Configuration;
import com.anacleto.base.Constants;
import com.anacleto.base.Logging;
import com.anacleto.util.MilliSecFormatter;

/**
 * Localized Term List: 
 * Created a sorted termlist. Sorting will be done according the locale 
 * passed when initialized  
 * @author robi
 *
 */
public class LocalizedTermList {
	
	private static org.apache.log4j.Logger logger = Logging.getAdminLogger();
	
	private static final int MAX_TERM_LENGTH = 3; 
	
	private static boolean INITIALIZED = false;
	
	private static TreeMap  fieldMap;
	
	/**
	 * Init the sorted set
	 * @param reader
	 * @param loc
	 * @throws IOException 
	 * @throws IOException
	 */
	synchronized public static void initialize() throws IOException {
		
		String indexDir = Configuration.params.getIndexDir(); 
		Locale loc = Configuration.params.getLocale();
		
		IndexReader reader = IndexReader.open(indexDir);
		Collator primColl;
		if (loc == null)
			primColl = Collator.getInstance();
		else
			primColl = Collator.getInstance(loc);
		
		primColl.setStrength(Collator.PRIMARY);

		LocalizedTermList.INITIALIZED = true;
		
		ShortTerm.initialize(MAX_TERM_LENGTH, primColl);
		
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
				fieldTermSet = new TreeSet();
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
				+ " terms in " + 
				MilliSecFormatter.toString(System.currentTimeMillis()-start) 
				+ " ms.");
	}
	
	/**
	 * Get short terms around the term in input 
	 * @param field - the field containing the pattern 
	 * @param pattern - the pattern to serach for
	 * @throws IOException 
	 */
	synchronized public static Collection getTermsNear(String field, 
			String pattern) throws IOException{
		
		if (!LocalizedTermList.INITIALIZED)
			LocalizedTermList.initialize();
		
		Object[] fieldTermSet = (Object[])fieldMap.get(field);
    	if (fieldTermSet == null) 
    		return null;
    	
    	ShortTerm inTerm = new ShortTerm(pattern);
    	int currPos = Arrays.binarySearch(fieldTermSet, inTerm, 
    								new ShortTermComparator());
    	if (currPos < 0) {
    		currPos = Math.abs(currPos) - 1;
    	}
    	
		return getEqualTerms(fieldTermSet, currPos);
	}

	synchronized public static Collection getTermsAfter(String field, 
			String text) throws IOException {
		
		if (!LocalizedTermList.INITIALIZED)
			LocalizedTermList.initialize();
		
		Object[] fieldTermSet = (Object[])fieldMap.get(field);
    	if (fieldTermSet == null) 
    		return null;
		
    	ShortTerm inTerm = new ShortTerm(text);
		
    	int currPos = Arrays.binarySearch(fieldTermSet, inTerm,
    						new ShortTermComparator());
    	if (currPos < 0)
    		currPos = Math.abs(currPos) - 1;
    	
		//get the first not equal position:
		while (fieldTermSet.length > currPos){
			if (inTerm.comparePrimary((ShortTerm)fieldTermSet[currPos]) != 0)
				break;
			currPos++;
		}
		
		return getEqualTerms(fieldTermSet, currPos);
	}

	/**
	 * Get a list of term patterns before a certain text in alphabetic order
	 * example: for "elf" should return "ele, elé, éle" etc..
	 * @param field
	 * @param text
	 * @return
	 * @throws IOException
	 */
	synchronized public static Collection getTermsBefore(String field, 
			String text) throws IOException{
		
		if (!LocalizedTermList.INITIALIZED)
			LocalizedTermList.initialize();
		
		Object[] fieldTermSet = (Object[])fieldMap.get(field);
    	if (fieldTermSet == null) 
    		return null;
		
    	ShortTerm inTerm = new ShortTerm(text);
		
    	int currPos = Arrays.binarySearch(fieldTermSet, inTerm,
    						new ShortTermComparator());
    	if (currPos < 0)
    		currPos = Math.abs(currPos) - 1;
    	
    	if (currPos <= 0)
			return null;
    	
		//get the first not equal position:
		while (currPos > 0){
			currPos--;
			if (inTerm.comparePrimary((ShortTerm)fieldTermSet[currPos]) != 0)
				break;
		}
		
		return getEqualTerms(fieldTermSet, currPos);
	}

	/**
	 * Get a collection of equal terms in a sorted object array  
	 * @param inTerm
	 * @param co
	 * @param fieldTermSet
	 * @param currPos
	 * @return
	 */
	private static Collection getEqualTerms( Object[] fieldTermSet, 
													 int currPos)
	{
		Collection retColl = new LinkedList();
		if(fieldTermSet.length <= currPos) {
			return retColl;
		}
		ShortTerm inTerm = (ShortTerm)fieldTermSet[currPos];
		
		//get equal terms before the cursor
		while (currPos > 0){
			if (inTerm.comparePrimary((ShortTerm)fieldTermSet[currPos-1]) != 0)
				break;
			currPos--;
		}
		
		//get equal terms after the cursor
		while (fieldTermSet.length > currPos){
			if (inTerm.comparePrimary((ShortTerm)fieldTermSet[currPos]) != 0) {
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
	
	
	//Primary collator, to define equality at the first level:
	//like ele = elé, 
	private static Collator co;
	
	//Secondary collator, to define equality on the second level:
	//like ele <> elé but ele = Ele
	private static Collator co_secondary;
	
	private String termChars;
	
	private CollationKey key_primary;
	private CollationKey key_secondary;
	
	private int    termLength;
	
	static void initialize(int maxTermLength, Collator co){
		MAX_TERM_LENGTH = maxTermLength;
		
		ShortTerm.co = (Collator)co.clone();
		ShortTerm.co.setStrength(Collator.PRIMARY);
		
		ShortTerm.co_secondary = (Collator)co.clone();
		ShortTerm.co_secondary.setStrength(Collator.SECONDARY);
		
	}
	
	ShortTerm(String termText){
		
		termLength = Math.min(termText.length(), MAX_TERM_LENGTH);
		termChars = termText.substring(0, termLength);
		
		key_primary = co.getCollationKey(termChars.toString());
		key_secondary = co_secondary.getCollationKey(termChars.toString());
	}

	/**
	 * @return Returns the key.
	 */
	public CollationKey getKey() {
		return key_primary;
	}

	/**
	 * @return Returns the key.
	 */
	public CollationKey getSecondaryKey() {
		return key_secondary;
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

	/**
	 * Base Comparator, considers the secondary differences:
	 * like ele < elé but ele = Ele
	 * this comparator will be used when elements will be put 
	 * into a set
	 * 
	 * @see java.util.Comparable#compareTo(Object)
	 */
	public int compareTo(Object o) {
		ShortTerm otherTerm = (ShortTerm)o;
		return key_secondary.compareTo(otherTerm.getSecondaryKey());
		
	}

	/**
	 * Primary Comparator, considers the primary differences:
	 * like ele = elé , ele = Ele, but ele < eld
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int comparePrimary(ShortTerm o) {
		return key_primary.compareTo(o.getKey());
	}

	/* (non-Javadoc)
	 * @see java.text.CollationKey#equals(java.lang.Object)
	 */
	public boolean equals(Object target) {
		ShortTerm otherTerm = (ShortTerm)target;
		
		return termChars.equals(otherTerm.getTermChars());
	}

}


class ShortTermComparator implements Comparator{

	public int compare(Object arg0, Object arg1) {
		ShortTerm st1 = (ShortTerm)arg0;
		ShortTerm st2 = (ShortTerm)arg1;
		return st1.comparePrimary(st2);
	}
	
}
