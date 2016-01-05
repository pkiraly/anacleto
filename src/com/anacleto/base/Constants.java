package com.anacleto.base;

/**
 * Application wide constants
 * @author robi
 */
public class Constants {

	//Sort types
	public static final int SORT_BY_RELEVANCE = 0;
	public static final int SORT_BY_HIERARCHY = 1;

    public static String FileSystemContent       = "FSCONTENT";
    
    public static String WebDavContent           = "WEBDAVCONTENT";
    
    public static String WebServerContent        = "WEBSERVERCONTENT";
    
    /**
     * Nextpage html content: a descriptor file + all the html
     */
    public static String NXT3Content             = "NXT3CONTENT";
    

    /**
     * TEI xml content: an xml file that will be indexed with an XSLT scheme
     */
    public static String TEI2XMLContent          = "TEI2CONTENT";

    /**
     * PDF per page content: parse pdf file and index each pages as a 
     * new Lucene doc
     */
    public static String PdfPerPageContent       = "PDFPERPAGECONTENT";

    /**
     * Bibliographic xml content: an xml file that will be indexed with 
     * an XSLT scheme
     */
    public static String BiblXMLContent          = "BIBLXMLCONTENT";

    /**
     * reserved fields are the ones that make part of the indexing
     * these fields are not part of the indexing statistics, termlist
     */
    public static String[] reservedFields        = {"name", 
    												"parentName" ,
													"childCount",
													"path",
													"book",
													"location",
													"contentType",
													"encoding",
													"firstChildContent",
												    "indexedAt"};

    public static boolean isReservedField(String field){
    	for (int i = 0; i < reservedFields.length; i++) {
			if (field.equals(reservedFields[i]))
				return true;
		}
    	return false;
    }
}
