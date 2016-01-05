package com.anacleto.filters;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;

public class SoftHypenAnalyzer extends Analyzer {

	public SoftHypenAnalyzer() {
	}
	
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new SoftHypenFilter(
			new LowerCaseFilter(
				new WhitespaceTokenizer(reader)
			)
		);
	}
}