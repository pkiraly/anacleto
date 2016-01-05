package com.anacleto.parsing.filetype;

import java.io.*;

import com.anacleto.hierarchy.BookPage;
import com.anacleto.parsing.ParserException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Plain text file parser
 * 
 * @author robi
 * 
 */
public class pgTextFileParser implements FileTypeParser {

    private static String       fieldName             = "";

    private static StringBuffer fieldValue            = new StringBuffer();

    private static StringBuffer content               = new StringBuffer();

    static String               pgHeaderStartPattern  = "The Project Gutenberg EBook of";

    static String               pgCatalogStartPattern = "*****These eBooks Were Prepared By Thousands of Volunteers!";

    static String               contentStartPattern   = "*** START OF THE PROJECT GUTENBERG EBOOK";

    static String               contentEndPattern     = "*** END OF THE PROJECT GUTENBERG EBOOK";

    static String               pgFooterEndPattern    = "*END THE SMALL PRINT! FOR PUBLIC DOMAIN EBOOKS";

    static Pattern              catalogPattern        = Pattern
                                                              .compile("^([^:]+): (.*?)$");

    private boolean isHeaderStart(String line) {
        return (line.indexOf(pgHeaderStartPattern) > -1) ? true : false;
    }

    private boolean isCatalogStart(String line) {
        return (line.indexOf(pgCatalogStartPattern) > -1) ? true : false;
    }

    private boolean isContentStart(String line) {
        return (line.indexOf(contentStartPattern) > -1) ? true : false;
    }

    private boolean isContentEnd(String line) {
        return (line.indexOf(contentEndPattern) > -1) ? true : false;
    }

    private boolean isFooterEnd(String line) {
        return (line.indexOf(pgFooterEndPattern) > -1) ? true : false;
    }

    public void addField(BookPage page) {
        if (!fieldName.equals("")) {
            page.addTextField(fieldName, fieldValue.toString());
        }
        fieldName = "";
        fieldValue.setLength(0);
    }
    
    public void cleanUpContent(InputStreamReader isr, BookPage page) {
        try {
            BufferedReader br = new BufferedReader(isr);
            String line = new String();
            String filePart = "unused";
            while ((line = br.readLine()) != null) {
                if (filePart.equals("content") && isHeaderStart(line)) {
                    filePart = "unused";
                } else if (filePart.equals("content") && isContentEnd(line)) {
                    filePart = "unused";
                } else if (filePart.equals("unused") && isFooterEnd(line)) {
                    filePart = "content";
                    continue;
                } else if (filePart.equals("unused") && isCatalogStart(line)) {
                    filePart = "catalog";
                } else if (filePart.equals("catalog") && isContentStart(line)) {
                    addField(page);
                    filePart = "content";
                    continue;
                }

                if (filePart.equals("catalog")) {
                    Matcher catalogMatcher = catalogPattern.matcher(line);
                    if (catalogMatcher.find()) {
                        addField(page);
                        fieldName = catalogMatcher.group(1);
                        fieldValue.append(catalogMatcher.group(2));
                    } else {
                        fieldValue.append(" " + line);
                    }
                } else if (filePart.equals("content")) {
                    content.append(line + "\n");
                }
            }
        } catch (IOException e) {
            // TODO: handle exception
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.anacleto.parsing.filetype.FileTypeParser#processStream(java.io.InputStream,
     *      com.anacleto.base.BookPage)
     */
    public void processStream(InputStream in, BookPage page)
            throws ParserException {
        String encoding = null;
        InputStreamReader isr = null;

        try {
            BufferedInputStream is = new BufferedInputStream(in);

            if (encoding != null)
                try {
                    isr = new InputStreamReader(is, encoding);
                } catch (UnsupportedEncodingException e) {
                    isr = new InputStreamReader(is);
                }
            else
                isr = new InputStreamReader(is);
            
            cleanUpContent(isr, page);

            page.addTextField("content", content.toString());

        } catch (Exception e) {
            ;
        } finally {
            try {
                isr.close();
                in.close();
            } catch (IOException e) {
            }
        }
    }
}
