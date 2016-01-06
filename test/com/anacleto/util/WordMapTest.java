package com.anacleto.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import com.anacleto.content.PDFUtils;
import com.anacleto.index.CustomAnalyzer;

import junit.framework.TestCase;

public class WordMapTest extends TestCase {
	public void testWordMap() throws IOException {
		String file = "G:/naplo/felsohazi_naplo/pages/fn-1861/fn-1861-100.pdf";
		String content = PDFUtils.getPageContent(file, 1);
		List result = customAnalyze(content);
		Iterator it = result.iterator();
		while(it.hasNext()) {
			String t = (String)it.next();
			System.out.println(t);
		}
	}
	
	public List customAnalyze(String text) throws IOException {
		List result = new ArrayList();
		Analyzer analyzer = new CustomAnalyzer(new File("E:/web_projects/naplo/config"));

		TokenStream tokens = analyzer.tokenStream("default", new StringReader(text));
		Token token;
		while((token = tokens.next()) != null) {
			result.add(printTokenToString(token));
		}
		return result;
	}

	public String printTokenToString(Token token) {
		 return token.termText() 
				+ " (" + token.startOffset() + '-' + token.endOffset() + ")"
				+ " " + token.getPositionIncrement() 
				+ ", " + token.type();
	}
}
