package com.anacleto.base;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import org.apache.commons.collections.list.TreeList;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.*;

import com.anacleto.hierarchy.BookPage;
import com.anacleto.hierarchy.HierarchicalElement;
import com.anacleto.index.IndexManager;
import com.anacleto.query.highlight.HitlistHighlighter;
import com.anacleto.util.LinkedIntList;


/**
 * Menu related functions
 * 
 * @author robi
 * 
 */
public class MenuHandler {

	Logger log = Logging.getUserEventsLogger();

	/**
	 * The number of child to list at once in the TOC tree 
	 */
	private static int maxTocEntryNumber = 
				Configuration.params.getMaxTocEntryNumber();
	/**
	 * 
	 */
	public MenuHandler() {
	}


	/**
	 * 
	 * @param parentStr the name of parent element, which we need the children of
	 * @param offset	the number first or the last children (which depends on the dirention parameter)
	 * @param direction	true is we need the previous elements, false is we need the following elements
	 * @param filterQuery	query string
	 * @param title	the title of the parent element (need for the 'Logical' navigation)
	 * @param isSyncronized	true if the elements of the parent are syncronized, ie. we need the natural order (not the logical ones) 
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public String getChildElements(String parentStr, int offset, 
			boolean direction, String filterQuery, String title, 
			boolean isSyncronized) 
					throws IOException, ParseException 
	{

		if(filterQuery != null)
			filterQuery = filterQuery.replaceAll("&quot;", "\"");

		log.debug( "parentStr=" + parentStr + "&offset=" + offset
			+ "&direction=" + direction + "&filterQuery=" + filterQuery 
            + "&title=" + title + "&isSyncronized=" + isSyncronized);
		
		HierarchicalElement parentEl = Configuration.getElement(parentStr);
		if (parentEl == null)
			return null;
		
		int startEl = (direction)? offset : offset - maxTocEntryNumber;
		int endEl   = (direction)? offset + maxTocEntryNumber : offset;

//		define the parent element
		TreeElement parent = new TreeElement();
		parent.self = parentEl;
		parent.childStart = startEl;
		
		LinkedList QNames = new LinkedList();
		boolean hasQuery = false;
		
		log.info("start");
		if (filterQuery == null || filterQuery.trim().equals("")){
			//get all childs without queryFilter
			log.info("filterQuery == null");
			Iterator it = null;
			if(isSyncronized == false &&
			   parentEl.getContentType() != null && 
			   parentEl.getContentType().equals("PDFPERPAGECONTENT"))
			{
				/* getLogicalChildElements return the docs and the qualified
				 * names (as name:title) of the proper hits (i.e. the logical
				 * childrens
				 */
				Collection childs = null;
				try {
					log.info("->getLogicalChildElements parentEl: " + parentEl.getClass().getName());
					childs = parentEl.getLogicalChildElements(title);
				log.info("//getLogicalChildElements");
				} catch (Exception e) {
					e.printStackTrace();
				}
				it = childs.iterator();
				/* separate the qualified names and the docs 
				 */
		        while (it.hasNext()) {
		        	Object currEl = it.next();
		        	if(currEl.getClass().getName().equals("java.lang.String")) {
		        		QNames.add(currEl);
		        		it.remove();
		        	}
		        }

				it = childs.iterator();
			} else {
				it = parentEl.getChildElements().iterator();
			}
			int counter = -1;

			LinkedList newQNames = new LinkedList();
	        while (it.hasNext()) {
	            counter++;
	        	TreeElement childTree = new TreeElement();
				childTree.self = (HierarchicalElement)it.next();
				log.info("child#" + counter + ") " + childTree.self.getName());
		    	
	        	if (counter < startEl){
	                continue;
	            }
	        	
	        	if (counter >= endEl ){
	        		parent.lastElementIncluded = false;
	        		break;
	        	}
	            
	        	if(QNames.size() > counter){
	        		newQNames.add(QNames.get(counter));
	        	}
				parent.children.add(childTree);
	        }
	        log.info("QNames.size: " + QNames.size());
			QNames = newQNames;
	        log.info("QNames.size: " + QNames.size());
			
		} else {
	        log.info("get filtered children");
			//get filtered children
			hasQuery = true;
			parent = getTreeFromQuery(parent, filterQuery);
	        
			Iterator it = parent.children.iterator();
			int counter = -1;
			TreeList newChildren = new TreeList();
	        while (it.hasNext()) {
	            counter++;
	        	TreeElement el = (TreeElement)it.next();
				log.info("child#" + counter + ") " + el.self.getName());

	        	if (counter < startEl) {
	                continue;
	            }
	        	if (counter >= endEl ){
	        		parent.lastElementIncluded = false;
	        		break;
	        	}
	            
	        	newChildren.add(el);
	        }
	        parent.children = newChildren;
		}
		
		if(hasQuery == false && isSyncronized == false &&
		   parent.self.getContentType() != null &&
		   parent.self.getContentType().equals("PDFPERPAGECONTENT"))
		{
			return getPDFMenuDescforTreeElement(parent, filterQuery, title, QNames);
		} else {
			return getMenuDescforTreeElement(parent, filterQuery);
		}
	}
	
	private String getPDFMenuDescforTreeElement(TreeElement parentTree, 
			String filterQuery, String title, LinkedList QNames){

		log.info("getPDFMenuDescforTreeElement: " 
				+ parentTree.self.getName() + ", "
				+ title + ", "
				+ QNames);
		StringBuffer retStr = new StringBuffer();
		if (parentTree.self == null)
			return "";
		
		String name = replaceUnwantedChars(parentTree.self.getName());
		
		retStr.append("['" + name + "','" + replaceUnwantedChars(title) + "'");

		// Child elements are present when it has children or
		// tree is filtered and it has one or more occurences
		// but not if it has one occurence and it is a real match
		if (( 
				( parentTree.self.getContentType() != null &&
				  parentTree.self.getContentType().equals("PDFPERPAGECONTENT") && 
				  parentTree.self.hasLogicalChildElements(title)
			     )
			  || 
			   parentTree.self.hasChildElements()
			 )
			 &&
				!(parentTree.occurence == 1 && parentTree.isRealMatch)){
			// log.info("parentTree.children.size: " + parentTree.children.size());

			if (parentTree.children.size() > 0) {
				Iterator it = parentTree.children.iterator();
                boolean isFirst = true;
                retStr.append(", [ ");
                int counter = -1;
				while (it.hasNext()) {
					counter++;
					TreeElement childTree = (TreeElement)it.next();
					String childTitle = "";
					if(QNames != null) {
						if(QNames.size() > counter) {
							String QName = (String) QNames.get(counter);
							childTitle = QName.substring(QName.indexOf(":")+1);
						} else {
							log.info("No QNames: " + counter);
						}
					}
					// log.info(childTree.self.getName() + " " + childTitle);
					String childStr = getPDFMenuDescforTreeElement(childTree,
							filterQuery, childTitle, null);
                    if( isFirst == false ) {
                      retStr.append(",");
                    }
					retStr.append( childStr );
                    isFirst = false;
				}
                retStr.append(" ]");
			} else {
				retStr.append(", ['UL']");
			}
		} else {
			retStr.append(", []");
		}
		// log.info("retStr: " + retStr.toString());

		retStr.append("," + parentTree.childStart);
		if (parentTree.lastElementIncluded)
			retStr.append(",0");
		else
			retStr.append(",1");
		
		if (parentTree.occurence > 0){
			//call highlight
			String parentName = parentTree.self.getName();
			String highlightStr = "";
			
			IndexManager in;
			try {
				HitlistHighlighter h = new HitlistHighlighter(filterQuery);
				in = new IndexManager();
				Document doc = in.findNamedElement(parentName);
				if (doc != null)
					highlightStr = replaceUnwantedChars(h.scoreDoc(doc));
				
			} catch (IOException e) {
				log.error(e);
			} catch (ParseException e) {
				;
			}
			
			retStr.append("," + parentTree.occurence + ",'" + highlightStr + "'");
		}
		retStr.append("]");
		// log.info("finale: " + retStr.toString());
		return retStr.toString();
	}

	private String getMenuDescforTreeElement(TreeElement parentTree, 
			String filterQuery){
		
		//log.info("getMenuDescforTreeElement");
		StringBuffer retStr = new StringBuffer();
		if (parentTree.self == null)
			return "";
		
		String name = replaceUnwantedChars(parentTree.self.getName());
		String title = replaceUnwantedChars(parentTree.self.getTitle());
		if(title == null || title.equals("")) {
			log.info("title is null or empty");
			// title = ((BookPage) parentTree.self).getFieldValue("title");
			title = ((BookPage) parentTree.self).getPathFromDocRoot();
			log.info("get title field value: " + title);
		}
		
		log.info("['" + name + "','" + title + "'");
		retStr.append("['" + name + "','" + title + "'");

		//Child elements are present when it has children or
		//tree is filtered and it has one or more occurences
		//but not if it has one occurence and it is a real match

		if (parentTree.self.hasChildElements()&&
				!(parentTree.occurence == 1 && parentTree.isRealMatch)){
			
			if (parentTree.children.size() > 0) {
				Iterator it = parentTree.children.iterator();
                boolean isFirst = true;
                retStr.append(", [ ");
				while (it.hasNext()) {
					TreeElement childTree = (TreeElement)it.next();
					String childStr = getMenuDescforTreeElement(childTree,
							filterQuery);
                    if( isFirst == false ) {
                      retStr.append(",");
                    }
					retStr.append( childStr );
                    isFirst = false;
				}
                retStr.append(" ]");
			} else {
				retStr.append(", ['UL']");
			}
		} else
			retStr.append(", []");
		
		retStr.append("," + parentTree.childStart);
		if (parentTree.lastElementIncluded)
			retStr.append(",0");
		else
			retStr.append(",1");
		
		if (parentTree.occurence > 0){
			//call highlight
			String parentName = parentTree.self.getName();
			String highlightStr = "";
			
			IndexManager in;
			try {
				HitlistHighlighter h = new HitlistHighlighter(filterQuery);
				in = new IndexManager();
				Document doc = in.findNamedElement(parentName);
				if (doc != null)
					highlightStr = replaceUnwantedChars(h.scoreDoc(doc));
				
			} catch (IOException e) {
				log.error(e);
			} catch (ParseException e) {
				log.error(e);
			}
			
			retStr.append("," + parentTree.occurence + ",'" + highlightStr + "'");
		}
		retStr.append("]");
		
		return retStr.toString();
	}
	
	
	/**
	 * Get child elements and their counter that have relevant documents
	 * under
	 * @param parentEl
	 * @param query
	 * @return a map with the found Element name in key, and its counter in value
	 * @throws IOException
	 * @throws ParseException
	 */
	private TreeElement getTreeFromQuery(TreeElement parent, String query ) 
				throws IOException, ParseException {
		log.info("query: " + query);
		
        if( query == null || query.trim().length() == 0 )
            return null;
        
        //Get document tree from the cache
        GlobalCache cache = new GlobalCache(GlobalCache.FilteredDocTreeCache);
        
        TreeElement cachedTree = (TreeElement)cache.retrieve(query);
        if (cachedTree == null){
        	//cache empty, build tree from query
        	cachedTree = buildTreeFromQuery(query);
        	cache.store(query, cachedTree);
        } 
        
        TreeElement matchedNode = findNodeInTree(cachedTree, 
        		parent.self.getName());
		if (matchedNode != null){
			TreeElement retNode = (TreeElement)matchedNode.clone();
			
			Iterator it = matchedNode.children.iterator();
			while (it.hasNext()) {
				TreeElement child = (TreeElement) it.next();
				TreeElement newChild = (TreeElement)child.clone();
				retNode.children.add(newChild);
			}
			return retNode;
		}
        
        return null;
	}
	
	private TreeElement findNodeInTree(TreeElement parent, String name) {
		if(parent == null) {
			log.info("parent is null in tree");
			return null;
		}
		
		if (parent.self.getName().equals(name))
			return parent;

		Iterator it = parent.children.iterator();
		while (it.hasNext()) {
			TreeElement child = (TreeElement) it.next();
			TreeElement matchedNode = findNodeInTree(child, name);
			if (matchedNode != null){
				return matchedNode;
			}
		}
		return null;
	}
	
	private TreeElement buildTreeFromQuery(String query) 
		throws IOException, ParseException 
	{
		log.info("query: " + query);
		BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
        IndexManager im = new IndexManager();
        IndexReader reader = IndexReader.open(Configuration.params
				.getIndexDir());
        
        NotOrderedHitCollector hc = new NotOrderedHitCollector();
		Searcher searcher = im.getSearcher();
		searcher.search(im.getQuery(query), hc);
		
		TreeElement retTree = new TreeElement();
        
		for (int i = 0; i < hc.length(); i++) {
			int docNum = hc.get(i);
			
			Document doc = im.getFastDocument(reader, docNum);
			if (doc == null)
				continue;
			
			BookPage page = new BookPage(doc);
			mergeDocIntoTree(retTree, page); 
 		}
		reader.close();
		
		return retTree;
	}
	
	private void mergeDocIntoTree(TreeElement tree, HierarchicalElement page) 
			throws IOException {
		
		HierarchicalElement el = page;
		
		//get origins, as a list
		LinkedList originList = new LinkedList();
		while (el != null){
			originList.add(0, el);
			el = el.getParentElement();
		}
		
		TreeElement currTree = tree;
		//merge origins in the tree:
		Iterator it = originList.iterator();
		
		//first element is the root
		el = (HierarchicalElement) it.next();
		if (tree.self == null){
			tree.self = el;
		}
		tree.occurence++;
		
		while (it.hasNext()) {
			el = (HierarchicalElement) it.next();
			
			//look if there is already a child.
			boolean found = false;
			int insertPos = 0;
			int currPos = 0;
			boolean hasInsertPos = false;
			Iterator childIt = currTree.children.iterator();
			while (childIt.hasNext()) {
				currPos++;
				
				//find it among children
				TreeElement childEl = (TreeElement) childIt.next();
				if (childEl.self.getName().equals(el.getName())){
					found = true;
					childEl.occurence++;
					currTree = childEl;
				}
				
				//get the position to insert
				if (childEl.self.getChildCount() < el.getChildCount()) {
					insertPos = currPos;
					hasInsertPos = true;
				}
			}
			
			if (!found){
				//currTree.children.add
				TreeElement newEl = new TreeElement();
				newEl.self = el;
				newEl.occurence++;
				
				if(currTree.self.getName().equals("root") || hasInsertPos == true) {
					currTree.children.add(insertPos, newEl);
				} else {
					currTree.children.add(newEl);
				}
				currTree = newEl;
			}
		}
		currTree.isRealMatch = true;
		
	}
	
	
	/**
	 * called when the user synchronizes the actual page with the menu, we have
	 * to extract all the subitems from the root to this element
	 * 
	 * 
	 * @param el
	 * @return
	 * @throws IOException
	 */
	public String syncToElement(HierarchicalElement el, String name, int offset)
			throws IOException {

		TreeElement treeEl = new TreeElement();
		treeEl.self = el;
        treeEl.childStart = 0;
        treeEl.lastElementIncluded = true;

		while (treeEl.self != null && treeEl.self.getParentElement() != null) {

			TreeElement parentEl = new TreeElement();

			parentEl.self = treeEl.self.getParentElement();
            
			parentEl.children = new TreeList(parentEl.self.getChildElements());
            parentEl.childStart = 0; //
            parentEl.lastElementIncluded = true;

            if (parentEl.children.size() > offset) {
				int halfSize = (offset / 2);
				TreeList children = new TreeList();
				LinkedList puffer = new LinkedList();
				boolean pre = true;
				String selfname = treeEl.self.getName();
                

				Iterator iter = parentEl.children.iterator();
                int counter = -1;
				while (iter.hasNext()) {
                    counter++;
					HierarchicalElement curr = (HierarchicalElement) 
							iter.next();

                    if (curr.getName().equals(selfname)) {
						children.addAll(puffer);
						children.add(curr);
						puffer.clear();
						pre = false;
                        parentEl.childStart = counter - children.size();

                    } else {
						if (puffer.size() < halfSize) {
							puffer.add(curr);
						} else {
							if (pre) {
								puffer.remove(0);
								puffer.add(curr);
							} else {
                                parentEl.lastElementIncluded = false;
                                break;
							}
						}
					}
                    
				}
				children.addAll(puffer);
				puffer.clear();

				parentEl.children = children;

			}

			parentEl.treeChild = treeEl;

			treeEl = parentEl;
		}
        
		return getTreeMenuDescForSync(treeEl);
	}

    /**
     * Get menu descriptor from a tree element: the direct path from the 'root'
     * to the named node: root (entry point) -> treeChild -> ... -> treeChild ->
     * named node which hasn't treeChild and childs.size() == 0 but may has
     * child element (outside the current tree -> see hasChildElements() )
     * 
     * @param el
     * @return
     */
    private String getTreeMenuDescForSync(TreeElement el) {

        StringBuffer menuDesc = new StringBuffer();
        String title = replaceUnwantedChars(el.self.getTitle());
        String name = replaceUnwantedChars(el.self.getName());
        
        menuDesc.append("['" + name + "','" + title + "'");
        if (el.children != null && el.children.size() > 0) {
            
            
            menuDesc.append(", [");
            Iterator iter = el.children.iterator();
            boolean isFirst = true;
            while (iter.hasNext()) {
                HierarchicalElement childEl = (HierarchicalElement) iter.next();

                // String menuPos;
                title = replaceUnwantedChars(childEl.getTitle());
                name = replaceUnwantedChars(childEl.getName());
                
                if( ! isFirst )
                    menuDesc.append(", ");
                

                boolean isNotDirectChild = false;
                if (childEl.hasChildElements() ) {
                    if (el.treeChild != null
                        && childEl.getName().equals( el.treeChild.self.getName())
                        && el.treeChild.children != null
                    ) {
                       menuDesc.append( getTreeMenuDescForSync(el.treeChild) );
                    } else {
                        
                        menuDesc.append("['" + name + "','" + title + "'");
                        menuDesc.append(", ['UL']");
                        menuDesc.append("," + el.childStart);
                        menuDesc.append("," + ( ( el.lastElementIncluded ) ? 0 : 1 ) );
                        isNotDirectChild = true;
                    }
                } else {
                    menuDesc.append("['" + name + "','" + title + "'");
                    menuDesc.append(",[],0,0");
                    isNotDirectChild = true;
                }
                if( isNotDirectChild) {
                    menuDesc.append("]");
                }
                isFirst = false;
            }

            menuDesc.append("]");
            
            // el = el.treeChild;
        } else {
            // this is true for the nameToSync only
            if (el.self.hasChildElements()) {
                menuDesc.append(", ['UL']");
            }
        }
        menuDesc.append("," + el.childStart);
        menuDesc.append("," + ( ( el.lastElementIncluded ) ? 0 : 1 ) );
        menuDesc.append("],");

        String menuDescResult = menuDesc.toString();
        // delete the last "," char
        if (menuDescResult.length() > 0)
            menuDescResult = menuDescResult.substring(0, menuDescResult
                    .length() - 1);

        return menuDescResult;
    }

 
	private String replaceUnwantedChars(String in) {
		if (in == null)
			return "";
		
		String out = in.replaceAll("\"", " ");
		out = out.replaceAll("'", " ");
		out = out.replaceAll("\n", " ");
		out = out.replaceAll("\r", " ");
		return out;
	}
}

final class TreeElement implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8330456081756207555L;

	public TreeElement treeChild;

	public TreeList children = new TreeList();

	public HierarchicalElement self;

	public HierarchicalElement parent;

	/**
	 * Counter values, to know if childs are only
	 * a segment of all childs 
	 */
	public int childStart;
	public int childCounter;
	
	/**
	 * Counter of the branch, used to count documents
	 * under this branch, matching a query 
	 * filtered
	 */
	public int occurence; 
	
	/**
	 * True if the childlist contains the last element
	 */
	public boolean lastElementIncluded = true;
	
	/**
	 * True if this element has a match against the query
	 * if filtered
	 */
	public boolean isRealMatch = false;
	
	public Object clone(){
		TreeElement retNode = new TreeElement();
		retNode.self = self;
		retNode.childStart = childStart;
		retNode.childCounter = childCounter;
		retNode.occurence = occurence; 
		retNode.lastElementIncluded  = lastElementIncluded;
		retNode.isRealMatch = isRealMatch;
		
		return retNode;
	}
	
	public int getChildCounter() {
		return this.childCounter;
	}
}

/**
 * Used when matching child elements against query results
 * Stores the counter of the occurences under a child node,
 * and if the given child matches (isRealMatch = true) tha query or
 * olny its children (isRealMatch = false)  
 * @author robi
 *
 */
final class ChildMatchAttributes{
	public int 	   occurence = 0;
	public boolean isRealMatch = false;
}

final class NotOrderedHitCollector extends HitCollector{
	
	LinkedIntList resList = new LinkedIntList();
	public void collect(int doc, float score) {
		resList.add(doc);
	}	
	
	public int length(){ 
		return resList.length();
	}
	
	public int get(int n){
		return resList.get(n);
	}
}


