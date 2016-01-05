package com.anacleto.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;

import com.anacleto.base.Configuration;
import com.anacleto.base.Logging;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.hierarchy.HierarchicalElement;
import com.anacleto.hierarchy.Shelf;
import com.anacleto.util.WildCardMatch;

/**
 * PathQeuryExtractor class extracts path expressions in a query
 * Path expressions can contain shelf information that is not in the 
 * index.
 * A path expression is /shelf1/shelf1_2/* means that all books will be 
 * searched under the shelf1_2
 * 
 * Regular expressions are supported
 * 
 * @author robi
 *
 */
public class PathQueryExtractor {
	
    private static  Logger log = Logging.getUserEventsLogger();

	private Collection foundElements = new ArrayList();
	
	public PathQueryExtractor(){
		
	}
	
	public void resetQuery(){
		foundElements.clear();
	}
	
	
	public Query extractPath(String pathExpr) throws IOException {
		// log.info("pathExpr: " + pathExpr);
		/*
		pathExpr = pathExpr.replaceAll("_cl_", "\\:")
							.replaceAll("_sp_", " ");
		*/
		// log.info("pathExpr: " + pathExpr);

		foundElements.clear();
		
		while (pathExpr.startsWith("/")) {
			pathExpr = pathExpr.substring(1);
		}
		// log.info("afterWhile: " + pathExpr);
		
		String[] elements = pathExpr.split("/");
		HierarchicalElement parentEl = Configuration.getElement(elements[0]);
		HierarchicalElement child = null;
		Iterator it = parentEl.getChildElements().iterator();
		while(it.hasNext()) {
			child = (HierarchicalElement) it.next();
			break;
		}
		
		if(parentEl != null) { 
			if(parentEl.getContentType().equals("PDFPERPAGECONTENT")
				|| child.getContentType().equals("PDFPERPAGECONTENT") 
				) 
			{
				StringBuffer newPath = new StringBuffer();
				for(int i=0,len=elements.length; i<len; i++) {
					newPath.append("/");
					if(elements[i].indexOf(":") == -1) {
						newPath.append(elements[i].replaceAll("\\*$", ""));
					} else {
						String[] component = elements[i].split(":", 2);
						newPath.append(component[0] + ":");
						newPath.append(component[1].replaceAll("_", " ")
								.replaceAll("\\*$", ""));
					}
				}
				pathExpr = newPath.toString();
				// log.info("pathExpr: " + pathExpr);
				return new PrefixQuery(new Term("path", pathExpr));
			}
		}
		
		extractElement(elements);
				
		it = foundElements.iterator();
		BooleanQuery query = new BooleanQuery();
		while (it.hasNext()) {
			Query q = (Query)it.next();
			query.add(q, false, false);
		}
		return query;
	}
	
	/**
	 * Match the actual element against the childs of the parent. 
	 * Put the elements to the foundElements list
	 * @param elements
	 * @return
	 * @throws IOException 
	 */
	private void extractElement(String childArray[]) 
			throws IOException {
	
		if (childArray.length == 0) {
			return;
		}
		
		String parent = childArray[0];
		HierarchicalElement parentEl = Configuration.getElement(parent);
		if (parentEl == null) {
			return;
		}
		
		if (parentEl instanceof Shelf) {
			String newElements[] = null;
			if (childArray.length == 1){
				newElements = new String[1];
				newElements[0] = "*";
			} else {
				newElements = new String[childArray.length-1];
				// copy childArray to newElements (except parentEl)
				System.arraycopy(childArray, 1, newElements, 0, 
						childArray.length-1);
			}
			Collection matchedChilds = matchChilds(parent, newElements[0]);
			Iterator it = matchedChilds.iterator();
			while (it.hasNext()) {
				HierarchicalElement element = (HierarchicalElement) it.next();
				newElements[0] = element.getName();
				extractElement(newElements);
			}
			
		} else if (parentEl instanceof Book){
			Book book = (Book) parentEl;
			boolean isPdfPerPageContent = false;
			if(book.getContentType().equals("PLUGINCONTENT")){
				if(childArray[1] != null && !childArray[1].equals("*")) {
					String pageName = childArray[1];
					int k;
					if((k = childArray[1].indexOf(":")) > 0) {
						pageName = childArray[1].substring(0, k);
					}
					
					HierarchicalElement firstChild = Configuration.getElement(pageName);
					if(firstChild != null &&
						firstChild.getContentType().equals("PDFPERPAGECONTENT"))
					{
						isPdfPerPageContent = true;
					}
				}
			}
			
			if(isPdfPerPageContent == true) {
				StringBuffer tmpPath = new StringBuffer();
				for(int z=0, len=childArray.length; z<len; z++) {
					//if(z > 0)
					tmpPath.append("/");
					tmpPath.append(childArray[z]);
				}
				String path = tmpPath.toString();
				path = path.replaceAll("\\*$", "");
				Query query = new PrefixQuery(new Term("path", path));
				foundElements.add(query);
				
			} else {
				Query query = new TermQuery(new Term("book", book.getName()));
				if (childArray.length == 1){
					//no further details are present,
					//create a query for all pages of the book
				} else {
					if (childArray.length == 2 && childArray[1].equals("*")){
						//
					} else{
						StringBuffer pathExpr = new StringBuffer();
						//the new childarray will be the copy of the old one
						for (int i = 0; i < childArray.length; i++) {
							pathExpr.append( "/" + childArray[i]);
						}
					
						//TODO: it may be better to create prefix query
						if (pathExpr.indexOf("*") >= 0 ||
							pathExpr.indexOf("?") >= 0){
							query = new WildcardQuery(new Term("path", pathExpr.toString()));
						} else {
							query = new TermQuery(new Term("path", pathExpr.toString()));
						}
					}
				}
				foundElements.add(query);
			}
		} else if (parentEl instanceof BookPage){
			BookPage book = (BookPage) parentEl;
			boolean isPdfPerPageContent = false;
			if(book.getContentType().equals("PLUGINCONTENT")){
				if(childArray[1] != null && !childArray[1].equals("*")) {
					String pageName = childArray[1];
					int k;
					if((k = childArray[1].indexOf(":")) > 0) {
						pageName = childArray[1].substring(0, k);
					}
					
					HierarchicalElement firstChild = Configuration.getElement(pageName);
					if(firstChild != null &&
						firstChild.getContentType().equals("PDFPERPAGECONTENT"))
					{
						isPdfPerPageContent = true;
					}
				}
			}
			
			if(isPdfPerPageContent == true) {
				StringBuffer tmpPath = new StringBuffer();
				for(int z=0, len=childArray.length; z<len; z++) {
					//if(z > 0)
					tmpPath.append("/");
					tmpPath.append(childArray[z]);
				}
				String path = tmpPath.toString();
				path = path.replaceAll("\\*$", "");
				Query query = new PrefixQuery(new Term("path", path));
				foundElements.add(query);
				
			} else {
				Query query = new TermQuery(new Term("book", book.getName()));
				if (childArray.length == 1){
					//no further details are present,
					//create a query for all pages of the book
				} else {
					if (childArray.length == 2 && childArray[1].equals("*")){
						//
					} else{
						StringBuffer pathExpr = new StringBuffer();
						//the new childarray will be the copy of the old one
						for (int i = 0; i < childArray.length; i++) {
							pathExpr.append( "/" + childArray[i]);
						}
					
						//TODO: it may be better to create prefix query
						if (pathExpr.indexOf("*") >= 0 ||
							pathExpr.indexOf("?") >= 0){
							query = new WildcardQuery(new Term("path", pathExpr.toString()));
						} else {
							query = new TermQuery(new Term("path", pathExpr.toString()));
						}
					}
				}
				foundElements.add(query);
			}
		}
	}
	
	/**
	 * Get all children of a parent, whose names match against a pattern 
	 * @param parentName The parent element's name
	 * @param childPattern The name pattern
	 * @return
	 * @throws IOException
	 */
	private Collection matchChilds(String parentName, String childPattern) 
				throws IOException{
		Collection retColl = new ArrayList();
		
		HierarchicalElement parent = Configuration.getElement(parentName);
	    Collection childColl = parent.getChildElements();
		Iterator it = childColl.iterator();
		while (it.hasNext()) {
			HierarchicalElement currEl = (HierarchicalElement) it.next();
			if (WildCardMatch.match(currEl.getName(),childPattern)){
				retColl.add(currEl);
			}
		}
		return retColl;
	}
}
