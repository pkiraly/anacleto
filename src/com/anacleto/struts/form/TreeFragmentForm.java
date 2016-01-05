//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Logging;

/**
 * MyEclipse Struts Creation date: 02-04-2005
 * 
 * XDoclet definition:
 * 
 * @struts:form name="treeFragmentForm"
 */
public class TreeFragmentForm extends ActionForm {

    // --------------------------------------------------------- Instance
    // Variables

	Logger log = Logging.getUserEventsLogger();
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int root;

    private int elem;

    /** the starting element in a subelement set */
    private String start;

    /** the number of elements in a subelement set */
    private int offset;


    /**
     * parent name
     */
    private String parentStr;
    
    /** positive (next siblings) or negative (get previous siblings) */
    private int direction;
    
    private String query;
    
    private String title = "";
    
    private String menuPos;

    /** parent property */
    private String parent;

    private String menuDesc;

    /** signs those nodes that has previos siblings */
    private String prevMap;

    /** signs those nodes that has next siblings */
    private String nextMap;
    
    private int isSyncronized;

    // --------------------------------------------------------- Methods

    public int getIsSyncronized() {
		log.info("get isSyncronized: " + isSyncronized);
		return isSyncronized;
	}

	public void setIsSyncronized(int isSyncronized) {
		log.info("set isSyncronized: " + isSyncronized);
		this.isSyncronized = isSyncronized;
	}

	public boolean isSyncronized() {
		if( this.isSyncronized == 0 ) {
			log.info("isSyncronized: false");
			return false;
		} else {
			log.info("isSyncronized: true");
			return true;
		}
	}
	
	/**
     * Method validate
     * 
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {

        return null;
    }

    /**
     * Method reset
     * 
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {}
    }

    /**
     * @return Returns the menuPos.
     */
    public String getMenuPos() {
        return menuPos;
    }

    /**
     * @param menuPos
     *            The menuPos to set.
     */
    public void setMenuPos(String menuPos) {
        this.menuPos = menuPos;
    }

    /**
     * @return Returns the menuDesc.
     */
    public String getMenuDesc() {
        return menuDesc;
    }

    /**
     * @param menuDesc
     *            The menuDesc to set.
     */
    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    /**
     * Returns the parent.
     * 
     * @return String
     */
    public String getParent() {
        return parent;
    }

    /**
     * Set the parent.
     * 
     * @param parent
     *            The parent to set
     */
    public void setParent(String parent) {
        this.parent = parent;
    }

    /**
     * @return Returns the elem.
     */
    public int getElem() {
        return elem;
    }

    /**
     * @param elem
     *            The elem to set.
     */
    public void setElem(int elem) {
        this.elem = elem;
    }

    /**
     * @return Returns the root.
     */
    public int getRoot() {
        return root;
    }

    /**
     * @param root
     *            The root to set.
     */
    public void setRoot(int root) {
        this.root = root;
    }

    /**
     * @return Returns the offset.
     */
    public int getOffset() {
      return offset;
    }

    /**
     * @param offset The offset to set.
     */
    public void setOffset(int offset) {
      this.offset = offset;
    }

    /**
     * @return Returns the start.
     */
    public String getStart() {
      return start;
    }

    /**
     * @param start The start to set.
     */
    public void setStart(String start) {
      this.start = start;
    }


    /**
     * @return Returns the nextMap.
     */
    public String getNextMap() {
      return nextMap;
    }

    /**
     * @param nextMap The nextMap to set.
     */
    public void setNextMap(String nextMap) {
      this.nextMap = nextMap;
    }

    /**
     * @return Returns the prevMap.
     */
    public String getPrevMap() {
      return prevMap;
    }

    /**
     * @param prevMap The prevMap to set.
     */
    public void setPrevMap(String prevMap) {
      this.prevMap = prevMap;
    }

    /**
     * @return Returns the parentStr.
     */
    public String getParentStr() {
      return parentStr;
    }

    /**
     * @param parentStr The parentStr to set.
     */
    public void setParentStr(String parentStr) {
      this.parentStr = parentStr;
    }

    /**
     * @return Returns the query.
     */
    public String getQuery() {
      return query;
    }

    /**
     * @param query The query to set.
     */
    public void setQuery(String query) {
      this.query = query;
    }

    /**
     * @param direction The direction to set.
     */
    public void setDirection(int direction) {
      this.direction = direction;
    }

    public int getDirection() {
      return direction;
    }

    public boolean getBooleanDirection() {
      return ( this.direction == 0 ) ? false : true;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
    
}