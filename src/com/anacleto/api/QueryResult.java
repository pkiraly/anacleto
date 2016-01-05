package com.anacleto.api;

public class QueryResult {
	
	String title;
	
	String titleLink;
	
	String highlightFragment;

	/**
	 * @return Returns the highlightFragment.
	 */
	public String getHighlightFragment() {
		return highlightFragment;
	}

	/**
	 * @param highlightFragment The highlightFragment to set.
	 */
	public void setHighlightFragment(String highlightFragment) {
		this.highlightFragment = highlightFragment;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the titleLink.
	 */
	public String getTitleLink() {
		return titleLink;
	}

	/**
	 * @param titleLink The titleLink to set.
	 */
	public void setTitleLink(String titleLink) {
		this.titleLink = titleLink;
	}
	
	
	

}
