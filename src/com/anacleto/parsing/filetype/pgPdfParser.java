/*
 * Created on Mar 17, 2005
 *
 */
package com.anacleto.parsing.filetype;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pdfbox.exceptions.CryptographyException;
import org.pdfbox.exceptions.InvalidPasswordException;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentInformation;
import org.pdfbox.util.PDFTextStripper;

import com.anacleto.hierarchy.BookPage;
import com.anacleto.parsing.ParserException;


/**
 * @author robi
 */
public class pgPdfParser implements FileTypeParser {

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
    }

    public void processStream(InputStream in, BookPage page)
            throws ParserException {

        PDDocument pdfDocument = null;
        try {
            pdfDocument = PDDocument.load(in);
            if (pdfDocument.isEncrypted())
                pdfDocument.decrypt("");

            StringWriter writer = new StringWriter();
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.writeText(pdfDocument, writer);
            
            cleanUpContent(writer.getBuffer(), page);

            //String content = writer.getBuffer().toString();
            page.addTextField("content",  content.toString());

            PDDocumentInformation info = pdfDocument.getDocumentInformation();
            page.addTextField("author",   info.getAuthor());
            page.addTextField("keywords", info.getKeywords());
            page.addTextField("title",    info.getTitle());
            page.addTextField("subject",  info.getSubject());
            
        } catch (IOException e) {
            throw new ParserException("Unable to load pdf document", e);
        } catch (CryptographyException e1) {
            throw new ParserException("Unable to decrypt pdf document", e1);
        } catch (InvalidPasswordException e2) {
            throw new ParserException("Missing password for pdf document.", e2);
        } finally {
            try {
                pdfDocument.close();
            } catch (IOException e3) {
                throw new ParserException("Unable to close pdf document", e3);
            }
        }

    }
}
