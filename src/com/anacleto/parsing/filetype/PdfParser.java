/*
 * Created on Mar 17, 2005
 *
 */
package com.anacleto.parsing.filetype;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

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
public class PdfParser implements FileTypeParser {
	
	/**
	 * Flag to index metadata of file
	 */
	private boolean indexMetadata = true;

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

            //String content = writer.getBuffer().toString();
            page.addTextField("content",  writer.getBuffer().toString());

            if(indexMetadata) {
            	PDDocumentInformation info = pdfDocument.getDocumentInformation();
            	page.addTextField("author",   info.getAuthor());
            	page.addTextField("keywords", info.getKeywords());
            	page.addTextField("title",    info.getTitle());
            	page.addTextField("subject",  info.getSubject());
            }
            
        } catch (IOException e) {
            throw new ParserException("Unable to load pdf document", e);
        } catch (CryptographyException e1) {
            throw new ParserException("Unable to decrypt pdf document", e1);
        } catch (InvalidPasswordException e2) {
            throw new ParserException("Missing password for pdf document.", e2);
        } catch (Exception e) {
            throw new ParserException("Unable to load pdf document: " + e, e);
        } finally {
            try {
            	if(pdfDocument != null)
            		pdfDocument.close();
            } catch (IOException e3) {
                throw new ParserException("Unable to close pdf document", e3);
            }
        }
    }

	/**
	 * Do index metadata?
	 * @param indexMetadata
	 */
    public void setIndexMetadata(boolean indexMetadata) {
		this.indexMetadata = indexMetadata;
	}
}
