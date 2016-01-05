/*
 * Created on Mar 17, 2005
 *
 */
package com.anacleto.parsing.filetype;

import java.io.InputStream;

import com.anacleto.hierarchy.BookPage;
import com.anacleto.parsing.ParserException;

/**
 * @author robi
 * 
 */
public interface FileTypeParser {

    public void processStream(InputStream in, BookPage page)
            throws ParserException;
}
