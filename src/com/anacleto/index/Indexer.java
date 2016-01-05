/*
 * Created on 25-gen-2005
 *
 */
package com.anacleto.index;

import com.anacleto.hierarchy.BookPage;

/**
 * @author robert
 * 
 */
public interface Indexer {

    public void getDocumentStructure(BookPage page, String fileName)
            throws IndexException;

}