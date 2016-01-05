package com.anacleto.parsing.source;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import com.anacleto.base.Constants;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.parsing.ParserException;
import com.anacleto.parsing.UnhandledMimeTypeException;
import com.anacleto.parsing.filetype.FileContentParser;
import com.anacleto.parsing.filetype.FileTypeParser;

public class PGContentProcessor extends FSProcessor{
	
	private void processDir(File currentFile, String parentName, int childCount)
    	throws IOException {

		logger.debug("Processing: " + currentFile.getName());
		
		BookPage page = new BookPage();
		count++;
		
		String name = parentName + "/" + currentFile.getName();
		
		page.setName(name);
		page.setParentName(parentName);
		page.setChildCount(childCount);
		page.setTitle(currentFile.getName());
		page.setBookName(collectionName);
		
		page.setLocation(currentFile.getCanonicalPath());
		page.setContentType(Constants.FileSystemContent);
		
		page.setPath("/" + name);
		
		page.addTextField("indexedAt", String.valueOf(System
		        .currentTimeMillis()));
		
		if (currentFile.isDirectory()) {
		    //directory is added only if it is not present:
		    if (indexMan.getPage(page.getName()) == null) {
		        indexMan.addPageInBatch(page);
		        logger.debug("Directory Entity added to index: "
		                + page.getName());
		    }
		    logger.debug("File Entity added to index: " + page.getName());
		    
		    File child[] = currentFile.listFiles();
		    for (int i = 0; i < child.length; i++) {
		        processDir(child[i], page.getName(), i);
		    }
		
		} else {
		    try {
		        //we do not want to reindex files that are not changed
		        //since last indexing
		        long indexedAt = 0;
		        Document doc = indexMan.getPage(page.getName());
		        if (doc != null)
		            indexedAt = Long.valueOf(doc.get("indexedAt")).longValue();
		        if (indexedAt >= currentFile.lastModified())
		            return;
		
		        FileContentParser ftp = new FileContentParser();
		        ftp.processContentType(currentFile, page);
		
		        searcherUpdateNeeded = true;
		        
		        indexMan.addPageInBatch(page);
		        logger.debug("File Entity added to index: " + page.getName());
		        
		    } catch (ParserException e) {
		        //log this event, but not block index writing
		        logger.warn("Unable to parse file: "
		                + currentFile.getCanonicalPath() + ". Cause:" + e);
		    } catch (Exception e) {
		        //file was cancelled during the process. do not care, go on
		        logger.error("Unable to parse file: "
		                + currentFile.getCanonicalPath()
		                + ". Root cause:" + e );
		    }
		}
	}

	private void filterGutenbergContent(File file, BookPage page) throws FileNotFoundException, ParserException, UnhandledMimeTypeException{
		
		BookPage tmpPage = new BookPage();
		FileContentParser ftp = new FileContentParser();
        ftp.processContentType(file, tmpPage);
		
        StringBuffer buffer = new StringBuffer();
        Iterator it = page.getFields().iterator();
        while (it.hasNext()) {
			Field el = (Field) it.next();
			if (el.name().equals("content")){
				buffer.append(el.stringValue());
			}
			
		}
		
	}
	
}

class PGContentParser {

    //private static String       fieldName             = "";

    //private static StringBuffer fieldValue            = new StringBuffer();

    //private static StringBuffer content               = new StringBuffer();

    private static String pgHeaderStartPattern  = 
    			"The Project Gutenberg EBook of";

    //private static String pgCatalogStartPattern = 
    //			"*****These eBooks Were Prepared By Thousands of Volunteers!";

    private static String contentStartPattern   = 
    			"*** START OF THE PROJECT GUTENBERG EBOOK";

    private static String contentEndPattern     = 
    			"*** END OF THE PROJECT GUTENBERG EBOOK";

    //private static String pgFooterEndPattern    = 
    //			"*END THE SMALL PRINT! FOR PUBLIC DOMAIN EBOOKS";

    private static Pattern catalogPattern        = 
    			Pattern.compile("^([^:]+): (.*?)$");

    private boolean isHeaderStart(String line) {
        return line.startsWith(pgHeaderStartPattern);
    }

    //private boolean isCatalogStart(String line) {
    //    return (line.indexOf(pgCatalogStartPattern) > -1) ? true : false;
    //}

    private boolean isContentStart(String line) {
        return line.startsWith(contentStartPattern);
    }

    private boolean isContentEnd(String line) {
        return line.startsWith(contentEndPattern);
    }

    //private boolean isFooterEnd(String line) {
    //    return (line.indexOf(pgFooterEndPattern) > -1) ? true : false;
    //}

    public void addField(BookPage page, String fieldName, StringBuffer fieldValue) {
        if (!fieldName.equals("")) {
            page.addTextField(fieldName, fieldValue.toString());
        }
        fieldName = "";
        fieldValue.setLength(0);
    }
/*
    public void cleanUpContent(StringBuffer text, BookPage page) {
        String line = new String();
        String filePart = "unused";
        String[] lines = text.toString().split("\\n");
        text.setLength(0);

        for (int i=0; i<lines.length; i++) {
            line = lines[i];
            if (filePart.equals("content") && isHeaderStart(line)) {
                filePart = "unused";
            } else if (filePart.equals("content") && isContentEnd(line)) {
                filePart = "unused";
            } else if (filePart.equals("unused") && isFooterEnd(line)) {
                filePart = "content";
                continue;
            //} else if (filePart.equals("unused") && isCatalogStart(line)) {
            //    filePart = "catalog";
            } else if (filePart.equals("catalog") && isContentStart(line)) {
                addField(page);
                filePart = "content";
                continue;
            }

            if (filePart.equals("catalog")) {
                Matcher catalogMatcher = catalogPattern.matcher(line);
                if (catalogMatcher.find()) {
                    addField(page, catalogMatcher.group(1), catalogMatcher.group(2));
                    //fieldName = catalogMatcher.group(1);
                    //fieldValue.append(catalogMatcher.group(2));
                } else {
                    fieldValue.append(" " + line);
                }
            } else if (filePart.equals("content")) {
                content.append(line + "\n");
            }
        }
    }
    */
}
