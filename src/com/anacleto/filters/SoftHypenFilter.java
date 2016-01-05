package com.anacleto.filters;

import java.io.IOException;
import java.util.regex.Pattern;

// import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

// import com.anacleto.base.Logging;

/**
 * Filters out the soft hypens ("¬") from the end of the word
 * and join this token with the next token
 * "SoftHypen¬" + "\n" + "Filter" becames "SoftHypenFilter" 
 * @author kiru
 */
public class SoftHypenFilter extends TokenFilter {

	// private static Logger log = Logging.getUserEventsLogger();
	private Token prevToken = null;
	private Token token;
	private static String  regex    = "(¬|­)";
	private static Pattern regexEnd = Pattern.compile(regex + "$");
	// private static Pattern regexIn  = Pattern.compile(regex);
	
	public SoftHypenFilter(TokenStream in) {
		super(in);
	}

	public Token next() throws IOException {
		if ((token = input.next()) == null)
			return null;
		boolean isTokenMerged = false;
		while(regexEnd.matcher(token.termText()).find()) {
			isTokenMerged = true;
			String textPrev;
			String text = token.termText().replaceAll(regex, "");
			int startOffset = -1;
			if(prevToken != null) {
				textPrev = prevToken.termText() + text;
				startOffset = prevToken.startOffset();
			} else {
				textPrev = text;
				startOffset = token.startOffset();
			}
			
			prevToken = new Token(textPrev, startOffset, token.endOffset(), token.type());
			token = input.next();
			if(token == null) {
				token = prevToken;
				prevToken = null;
				isTokenMerged = false;
				break;
			}
		}
		if(token.type().equals("<SOFT_HYPEN>")) {
			token = new Token(
					token.termText().replaceAll(regex, ""), 
					token.startOffset(), 
					token.endOffset(), 
					token.type());
		} else if(regexEnd.matcher(token.termText()).find()) {
			throw new IOException("unhadled hypen: " + token.termText());
		}
		
		if(isTokenMerged == true) {
			token = new Token(
					prevToken.termText() + token.termText(), 
					prevToken.startOffset(), 
					token.endOffset(), 
					token.type());
			prevToken = null;
		}
		return token;
	}
}
