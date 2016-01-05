package com.anacleto.parsing;

import java.io.IOException;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.activation.MimetypesFileTypeMap;

import junit.framework.TestCase;

import com.anacleto.base.BookException;
import com.anacleto.hierarchy.Book;
import com.anacleto.parsing.source.WebServerProcessor;

public class TestWebServerProcessor extends TestCase{
	
	public void testCrawling(){
		/*
		Book b = new Book();
		b.setLocation("http://www.repubblica.it");
		
		WebServerProcessor proc = new WebServerProcessor();
		try {
			proc.indexBook(b);
		} catch (BookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		System.out.println("A::"+ new MimetypesFileTypeMap().getContentType("al.html"));
		
		MimeType mimeType;
		try {
			mimeType = new MimeType("application/x-msexcel");
			System.out.println(mimeType.match(new MimeType("application/excel")));
			
		} catch (MimeTypeParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
				
		String mime = new MimetypesFileTypeMap().getContentType("al.html");
		String mime2 = new MimetypesFileTypeMap().getContentType("al.pdf");
		System.out.println(mime2);
		try {
			MimeType b = new MimeType("text/html; charset=utf-8");
			System.out.println("B::"+b.getBaseType());
			System.out.println("A=B?" +b.match(mime));
		} catch (MimeTypeParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
