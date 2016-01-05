package com.anacleto.api;

/**
 * Statistical information bean about an execution of a query
 * @author robi
 *
 */
public class QueryStats {
	
	/**
	 * Number of hits resulting from the query
	 */
	private int numberOfHits;
	
	/**
	 * Query elaboration time in millis
	 */
	private int queryTimeInMillis;

	/**
	 * Number of hits resulting from the query
	 * @return Returns the numberOfHits.
	 */
	public int getNumberOfHits() {
		return numberOfHits;
	}

	/**
	 * @param numberOfHits The numberOfHits to set.
	 */
	public void setNumberOfHits(int numberOfHits) {
		this.numberOfHits = numberOfHits;
	}

	/**
	 * Query elaboration time in millis
	 * @return Returns the queryTimeInMillis.
	 */
	public int getQueryTimeInMillis() {
		return queryTimeInMillis;
	}

	/**
	 * @param queryTimeInMillis The queryTimeInMillis to set.
	 */
	public void setQueryTimeInMillis(int queryTimeInMillis) {
		this.queryTimeInMillis = queryTimeInMillis;
	}

	
	
}
