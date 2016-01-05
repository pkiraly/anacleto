package com.anacleto.index;

import org.apache.commons.collections.list.TreeList;

import com.anacleto.base.Configuration;

/**
 * Utility class to compose queries
 * @author rkiraly - Tesuji S.r.l
 *
 */
public class QueryComposer {
	
	public final static int OR  = 0;
	public final static int AND = 1;
	public final static int NOT = 2;
	
	private static int defaultProximityDistance = 10;
	
	private static TreeList escapedChars;
	
	private String query = "";
	
	static {
		escapedChars = new TreeList();
//		+ - && || ! ( ) { } [ ] ^ " ~ * ? : \
		char[]  escChars = new char[]{'+', '-', '!', '(', ')',
				'{', '}', '[', ']', '^', '\"', '~', '*', '?', ':'};
		
		for (int i = 0; i < escChars.length; i++) {
			escapedChars.add(new Character(escChars[i]));
		}
	};

	/**
	 * Add a single term with default field and AND-ing
	 * @param term
	 */
	public void addTerm(String term){
		addTerm(Configuration.getDefaultQueryField(), 
				term,
				AND);
	}
	
	/**
	 * Add a field and a single term and AND-ing
	 * @param field the name of the field
	 * @param term the query term 
	 */
	public void addTerm(String field, String term){
		addTerm(field, 
				term,
				AND);
	}

	/**
	 * Add term and operator with default field
	 * @param text
	 * @param operator
	 */
	public void addTerm(String term, int operator){
		addTerm(Configuration.getDefaultQueryField(), 
				term,
				operator);
	}
	
	/**
	 * Add multiple terms. The query is split to terms by space.
	 * @param field The name of the field
	 * @param text	The input query
	 * @param booleOperator	The boole operator (0,1,2)
	 */
	public void addMultipleTerms(String field, String text, int booleOperator) {
		if(text.indexOf(" ") > -1) {
			String[] terms = text.split(" ");
			for(int i=0, len=terms.length; i<len; i++) {
				addTerm(field, terms[i], booleOperator);
			}
		} else {
			addTerm(field, text, booleOperator);
		}
	}

	/**
	 * add exact phrases from an array. There are two boolean operator: one 
	 * for among the terms, and onother to append to the query 
	 */
	public void addExactPhraseArray(String field, String[] text, int outerOperator, int innerOperator) {
		if(text == null)
			return;
		StringBuffer query = new StringBuffer();
		for(int i=0, len=text.length; i<len; i++) {
			if(text[i].trim().length() > 0) {
				if (i>0) {
					query.append(" " + getOperator(innerOperator) + " ");
				}
				query.append('"' + replaceEscapedChars(text[i]) + '"');
			}
		}
		if(query.toString().length() > 0) {
			appendToQuery(outerOperator, field + ":(" + query.toString() + ')' );
		}
	}

	/**
	 * add a terms from an array. There are two boolean operator: one 
	 * for among the terms, and onother to append to the query 
	 */
	public void addTermsArray(String field, String[] text, int outerOperator, int innerOperator) {
		if(text == null)
			return;
		StringBuffer query = new StringBuffer();
		for(int i=0, len=text.length; i<len; i++) {
			if(text[i].trim().length() > 0) {
				if (i>0) {
					query.append(" " + getOperator(innerOperator) + " ");
				}
				query.append(replaceEscapedChars(text[i]));
			}
		}
		if(query.toString().length() > 0) {
			appendToQuery(outerOperator, field + ":(" + query.toString() + ')' );
		}
	}

	/**
	 * Add a field, term and boolean operator to the query
	 * @param field
	 * @param term
	 * @param booleOperator
	 */
	public void addTerm(String field, String term, int booleOperator){
		
		/*
		if (field == null || field.trim().length() == 0)
				return;
		*/
		if (term == null || term.trim().length() == 0)
			return;
		
		if (field == null || field.trim().length() == 0) {
			appendToQuery(booleOperator, replaceEscapedChars(term));
		} else {
			appendToQuery(booleOperator, field + ":" + replaceEscapedChars(term));
		}
	}

	/**
	 * Add field, term and operator to a prefix query
	 * @param field
	 * @param term
	 * @param booleOperator
	 */
	public void addPrefixQuery(String field, String term, int booleOperator){
		if (field == null || field.trim().length() == 0)
			Configuration.getDefaultQueryField();
		
		if (term == null || term.trim().length() == 0)
			return;
		
		appendToQuery(booleOperator, field + ":" + term);
	}
	
	public void addProximity(String text){
		addProximity(Configuration.getDefaultQueryField(), text, 
				defaultProximityDistance, AND);
	}
	
	public void addProximity(String text, int distance){
		addProximity(Configuration.getDefaultQueryField(), text, distance, AND);
	}

	public void addProximity(String text, int distance, int operator){
		addProximity(Configuration.getDefaultQueryField(), text, distance, AND);
	}

	public void addProximity(String field, String text, int distance, int operator){
		appendToQuery(operator, field + ":" + "\"" + replaceEscapedChars(text) + 
				"\"~" + distance);
	}
	
	/**
	 * Add an exact phrase to the query. If field is empty, then without field notation.
	 * @param field the name of the field
	 * @param text the input query
	 * @param operator	the boolean operator
	 */
	public void addExactPhrase(String field, String text, int operator){
		if (text == null || text.trim().length() == 0)
			return;

		if (field == null || field.trim().length() == 0) {
			appendToQuery(operator, "\"" + replaceEscapedChars(text) + "\"" );
		} else {
			appendToQuery(operator, field + ":" 
					+ "\"" + replaceEscapedChars(text) + "\"" );
		}
	}
	
	private void appendToQuery(int operator, String expression){
		String operatorStr = " " + getOperator(operator) + " ";
		if (operator == QueryComposer.AND || (operator == QueryComposer.OR
				&& query.equals("")))
			operatorStr = " ";
		query = query + operatorStr + expression;
		query = query.trim();
	}
	
	/**
	 * @return Returns the query.
	 */
	public String getQuery() {
		return query;
	}

	private String getOperator(int operator){
		switch (operator) {
		case OR:
			return "OR";
		case NOT:
			return "NOT";
		default:
			return "AND";
		}		
	}
	
	private String replaceEscapedChars(String text){
		
		StringBuffer retBuff = new StringBuffer();
		
		char lastChar = ' ';
		for (int i = 0; i < text.length(); i++){
			char currChar = text.charAt(i);
			
			if (lastChar == '\\'){
				//if the char is already escaped, do nothing
				;
			} else if (escapedChars.contains(new Character(currChar))){
				//insert a backslash:
				retBuff.append('\\');
			} else if ((lastChar == '&' && currChar == '&')
					|| (lastChar == '|' && currChar == '|')){
				//TODO: escape: && and || and \\
				retBuff = new StringBuffer(
						retBuff.substring(retBuff.length()-1) + '\\' + lastChar);
			}
					
			retBuff.append(currChar);	
		}
		return retBuff.toString();
	}
}
