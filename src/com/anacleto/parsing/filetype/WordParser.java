/*
 * Created on Mar 10, 2005
 *
 */
package com.anacleto.parsing.filetype;

import java.io.InputStream;

import org.textmining.text.extraction.WordExtractor;

import com.anacleto.hierarchy.BookPage;
import com.anacleto.parsing.ParserException;


/**
 * @author robi
 *
 */
public class WordParser implements FileTypeParser{
    
    public void processStream(InputStream in, BookPage page) throws ParserException{
        WordExtractor extractor = new WordExtractor();
        try {
            page.addTextField("content", extractor.extractText(in) );
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }
}
