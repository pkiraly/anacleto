package com.anacleto.content;

import java.util.Iterator;
import java.util.Stack;

public class PDFBookmarkEntry {
	
	private static String COLON = ":";
	private static String SLASH = "/";

	private String title;
	private Integer pageNumber;
	private String name;
	private Stack path;
	private String fieldName;
	private int siblingCounter;
	private String logicalParent;
	
	private static String[] fieldNames = {
			"title", "name", "path", // "fieldName", 
			"siblingCounter", "logicalParent"
	};

	PDFBookmarkEntry(String title, Integer pageNumber, String name) {
		this.title = title;
		this.pageNumber = pageNumber;
		this.name = name;
	}
	
	public String[] getFieldNames() {
		return fieldNames;
	}

	public String getValue(String key) {
		if(key.equals("title")) {
			return this.title;
		} else if(key.equals("name")) {
			return this.name;
		} else if(key.equals("path")) {
			return printPath();
		} else if(key.equals("fieldName")) {
			return this.fieldName;
		} else if(key.equals("siblingCounter")) {
			return "" + this.siblingCounter;
		} else if(key.equals("logicalParent")) {
			return this.logicalParent;
		}
		return "";
	}

	public String printPath() {
		StringBuffer pathStrBfr = new StringBuffer();
		Iterator it = path.iterator();
		while(it.hasNext()) {
			if(pathStrBfr.length() > 0)
				pathStrBfr.append(SLASH);
			String part = (String) it.next();
			if(part.equals((String)path.lastElement())) {
				pathStrBfr.append(siblingCounter + COLON + part);
			} else {
				pathStrBfr.append(part);
			}
		}
		return pathStrBfr.toString();
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getPageNumber() {
		return pageNumber;
	}
	
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Stack getPath() {
		return path;
	}

	public void setPath(Stack path) {
		this.path = path;
	}

	public int getSiblingCounter() {
		return siblingCounter;
	}

	public void setSiblingCounter(int siblingCounter) {
		this.siblingCounter = siblingCounter;
	}

	public String getLogicalParent() {
		return logicalParent;
	}

	public void setLogicalParent(String logicalParent) {
		this.logicalParent = logicalParent;
	}

}
