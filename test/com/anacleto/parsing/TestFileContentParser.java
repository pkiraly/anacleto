package com.anacleto.parsing;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.lucene.document.Document;

import com.anacleto.hierarchy.BookPage;
import com.anacleto.parsing.filetype.FileContentParser;

import junit.framework.TestCase;

public class TestFileContentParser extends TestCase{
	
	public void testExcelMime(){
		FileContentParser parser = new FileContentParser();
		
		BookPage page = new BookPage();
		try {
			parser.processContentType(new File((getClass().getClassLoader().
					getResource("com/anacleto/parsing/test.xls")).getFile()),
					page);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnhandledMimeTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
