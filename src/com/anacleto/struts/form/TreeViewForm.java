//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Logging;

/**
 * MyEclipse Struts Creation date: 01-21-2005
 * 
 * XDoclet definition:
 * 
 * @struts:form name="treeViewForm"
 */
public class TreeViewForm extends ActionForm {

    // --------------------------------------------------------- Instance
    // Variables

	Logger log = Logging.getUserEventsLogger();
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** treeDesc property */
    private String treeDesc;

    /**
     * Toc name to synchronize with
     */
    private String name;
    
    /**
     * parent name
     */
    private String parentStr;
    
    private int direction;
    
    private String query;

    /** the number of elements in a subelement set */
    private int offset;
    
    /** signs those nodes that has previos siblings */
    private String prevMap;

    /** signs those nodes that has next siblings */
    private String nextMap;
    
    private String title = "";

    private int isSyncronized;
    
    private int maxTocEntryNumber;

    // --------------------------------------------------------- Methods

    public int getIsSyncronized() {
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

        ;
    }

    /**
     * Returns the treeDesc.
     * 
     * @return String
     */
    public String getTreeDesc() {
        return treeDesc;
    }

    /**
     * Set the treeDesc.
     * 
     * @param treeDesc
     *            The treeDesc to set
     */
    public void setTreeDesc(String treeDesc) {
        this.treeDesc = treeDesc;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
     * @return Returns the direction.
     */
    public int getDirection() {
      return direction;
    }

    
    public boolean getBooleanDirection() {
      return ( this.direction == 0 ) ? false : true;
    }

    
    /**
     * @param direction The direction to set.
     */
    public void setDirection(int direction) {
      this.direction = direction;
    }

    /**
     * @return Returns the parentStr.
     */
    public String getParentStr() {
      return ( parentStr != null  && ! parentStr.trim().equals("")  )  
              ? parentStr 
              : "root";
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

	public int getMaxTocEntryNumber() {
		return maxTocEntryNumber;
	}

	public void setMaxTocEntryNumber(int maxTocEntryNumber) {
		this.maxTocEntryNumber = maxTocEntryNumber;
	}

}