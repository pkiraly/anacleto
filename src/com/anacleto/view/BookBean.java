/*
 * Created on Apr 20, 2005
 *
 */
package com.anacleto.view;

import com.anacleto.hierarchy.Book;

/**
 * Simple bean to display a book on the admin pages 
 * @author robi
 *
 */
public class BookBean extends Book{

    boolean		selected;
    
    
	/**
	 * @return Returns the selected.
	 */
	public boolean isSelected() {
		return selected;
	}
	/**
	 * @param selected The selected to set.
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
