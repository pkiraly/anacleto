package com.anacleto.content;

import java.io.*;

import com.anacleto.parsing.source.ContentReadException;

public class LocalContentReader {

	private static int BUFFER_SIZE = 1024;
	
    public String readContent(String pageUrl, String encoding) throws IOException {
    	
    	StringBuffer sb = new StringBuffer();
    	InputStreamReader in = null;
    	InputStream file = null;
    	try{
	        pageUrl = pageUrl.replace('\\', '/');
	
	        file = new FileInputStream(pageUrl);
	        BufferedInputStream is = new BufferedInputStream(file);
	        
	        
	        if (encoding != null)
				try {
					in = new InputStreamReader(is, encoding);
				} catch (UnsupportedEncodingException e) {
					in = new InputStreamReader(is);
				}
			else
	        	in = new InputStreamReader(is);
	        
	        char buffer[] = new char[BUFFER_SIZE];
	        int n = 0;
	        while (true) {
	            n = in.read(buffer);
	
	            if (n < 1)
	                break;
	            sb.append(buffer, 0, n);
	        }
    	} finally {
    		if (file != null)
    			file.close();
    		
    	    if (in != null)
    	    	in.close();
    	}
    	
    	return sb.toString();
    }
    
    public String readContent(File inFile, String encoding) throws ContentReadException {
    	
    	StringBuffer sb = new StringBuffer();
    	InputStreamReader in = null;
    	InputStream file = null;
    	try{
	        file = new FileInputStream(inFile);
	        BufferedInputStream is = new BufferedInputStream(file);
	        
	        if (encoding != null)
				try {
					in = new InputStreamReader(is, encoding);
				} catch (UnsupportedEncodingException e) {
					in = new InputStreamReader(is);
				}
			else
	        	in = new InputStreamReader(is);
	        
	        char buffer[] = new char[BUFFER_SIZE];
	        int n = 0;
	        while (true) {
	            n = in.read(buffer);
	
	            if (n < 1)
	                break;
	            sb.append(buffer, 0, n);
	        }
    	} catch (FileNotFoundException e) {
			throw new ContentReadException(e);
		} catch (IOException e) {
			throw new ContentReadException(e);
		} finally {
			if (file != null)
	    		try {
					file.close();
				} catch (IOException e) {
					throw new ContentReadException(e);
				}
				
    	    if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					throw new ContentReadException(e);
				}
    	}
    	
    	return sb.toString();
    }

    public String readContent(InputStream input, String encoding) throws IOException {
    	
    	StringBuffer sb = new StringBuffer();
    	InputStreamReader in = null;
    	
    	try{
	        BufferedInputStream is = new BufferedInputStream(input);
	        
	        if (encoding != null)
				try {
					in = new InputStreamReader(is, encoding);
				} catch (UnsupportedEncodingException e) {
					in = new InputStreamReader(is);
				}
			else
	        	in = new InputStreamReader(is);
	        
	        char buffer[] = new char[BUFFER_SIZE];
	        int n = 0;
	        while (true) {
	            n = in.read(buffer);
	
	            if (n < 1)
	                break;
	            sb.append(buffer, 0, n);
	        }
    	} finally {
    	    in.close();
    	}
    	
    	return sb.toString();
    }
}
