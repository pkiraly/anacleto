package com.anacleto.parsing.filetype;

import java.io.*;

import com.anacleto.hierarchy.BookPage;
import com.anacleto.parsing.ParserException;

/**
 * Plain text file parser 
 * @author robi
 *
 */
public class TextFileParser implements FileTypeParser{

	
	/* (non-Javadoc)
	 * @see com.anacleto.parsing.filetype.FileTypeParser#processStream(java.io.InputStream, com.anacleto.base.BookPage)
	 */
	public void processStream(InputStream in, BookPage page) throws ParserException {
		
		String encoding = page.getEncoding();
		InputStreamReader isr = null;
		
		try{
	        BufferedInputStream is = new BufferedInputStream(in);
	        
	        if (encoding != null)
				try {
					isr = new InputStreamReader(is, encoding);
				} catch (UnsupportedEncodingException e) {
					isr = new InputStreamReader(is);
				}
			else
				isr = new InputStreamReader(is);
	        
	        
	        page.addTextField("content", isr);
	        
		} catch (IOException e){
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
