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
 * MyEclipse Struts
 * Creation date: 05-16-2005
 * 
 * XDoclet definition:
 * @struts:form name="treeFilterForm"
 */
public class TreeFilterForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables
	Logger log = Logging.getUserEventsLogger();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** query property */
	private String query;

	/** menuDesc property */
	private String menuDesc;

    /** menuDesc property */
    private String[] resDocs;
    

    /** menuDesc property */
    private String highLightStr;

    /** parentStr property */
    private String parentStr;

    /** offset property */
    private int offset;

    /** direction property */
    private int direction;
    
    private String title = "";
    
    private int isSyncronized;
    
    private int maxTocEntryNumber;

    // --------------------------------------------------------- Methods

	public int getMaxTocEntryNumber() {
		return maxTocEntryNumber;
	}

	public void setMaxTocEntryNumber(int maxTocEntryNumber) {
		this.maxTocEntryNumber = maxTocEntryNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
     * @return Returns the direction.
     */
    public int getDirection() {
      return direction;
    }

    /**
     * @param direction The direction to set.
     */
    public void setDirection(int direction) {
      this.direction = direction;
    }

    public boolean getBooleanDirection() {
      if( this.direction == 0 )
      {
        return false;
      } else {
        return true;
      }
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
	 * Returns the query.
	 * @return String
	 */
	public String getQuery() {
		return query;
	}

	/** 
	 * Set the query.
	 * @param query The query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/** 
	 * Returns the menuDesc.
	 * @return String
	 */
	public String getMenuDesc() {
		return menuDesc;
	}

	/** 
	 * Set the menuDesc.
	 * @param menuDesc The menuDesc to set
	 */
	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}

    public String[] getResDocs() {
        return resDocs;
    }

    public void setResDocs(String[] resDocs) {
        this.resDocs = resDocs;
    }

    public String getHighLightStr() {
        return highLightStr;
    }

    public void setHighLightStr(String highLightStr) {
        this.highLightStr = highLightStr;
    }

    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
      return null;
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
      try {
        request.setCharacterEncoding("utf-8");
      } catch (UnsupportedEncodingException e) {}
    }

	public int getIsSyncronized() {
		return isSyncronized;
	}

	public void setIsSyncronized(int isSyncronized) {
		this.isSyncronized = isSyncronized;
	}

	public boolean isSyncronized() {
		if( this.isSyncronized == 0 ) {
			return false;
		} else {
			return true;
		}
	}

}